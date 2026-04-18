package com.tripsphere.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * Manages per-destination user photos.
 * Stores absolute file paths (strings) so Coil can load them via File directly,
 * which is always reliable for files inside the app's internal storage.
 */
@HiltViewModel
class DestinationPhotoViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val prefs by lazy {
        context.getSharedPreferences("dest_photos_v2", Context.MODE_PRIVATE)
    }

    // Absolute file paths, newest first
    private val _photos = MutableStateFlow<List<String>>(emptyList())
    val photos: StateFlow<List<String>> = _photos.asStateFlow()

    fun loadPhotos(destinationId: Int) {
        val stored = prefs.getStringSet(prefKey(destinationId), emptySet()) ?: emptySet()
        // Keep only paths whose files still exist, sort newest first
        _photos.value = stored
            .filter { File(it).exists() }
            .sortedByDescending { File(it).lastModified() }
    }

    /** Called after camera captures — the File is already written. */
    fun addPhotoFile(destinationId: Int, file: File) {
        if (!file.exists() || file.length() == 0L) return
        val updated = listOf(file.absolutePath) + _photos.value
        _photos.value = updated
        persistAll(destinationId, updated)
    }

    /**
     * Called after gallery pick — copies the content URI to internal storage
     * so we have a permanent, readable file path.
     */
    fun addPhotoFromUri(destinationId: Int, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dir = photoDir(destinationId)
                val dest = File(dir, "gallery_${System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    dest.outputStream().use { out -> input.copyTo(out) }
                }
                if (dest.exists() && dest.length() > 0) {
                    val updated = listOf(dest.absolutePath) + _photos.value
                    _photos.value = updated
                    persistAll(destinationId, updated)
                }
            } catch (_: Exception) { }
        }
    }

    fun deletePhoto(destinationId: Int, path: String) {
        runCatching { File(path).delete() }
        val updated = _photos.value.filter { it != path }
        _photos.value = updated
        persistAll(destinationId, updated)
    }

    /** Creates a fresh JPEG file in the app's private directory for camera output. */
    fun createImageFile(destinationId: Int): File {
        val dir = photoDir(destinationId)
        return File.createTempFile("IMG_${System.currentTimeMillis()}_", ".jpg", dir)
    }

    private fun photoDir(destinationId: Int) =
        File(context.filesDir, "dest_photos_$destinationId").apply { mkdirs() }

    private fun persistAll(destinationId: Int, paths: List<String>) {
        prefs.edit()
            .putStringSet(prefKey(destinationId), paths.toSet())
            .apply()
    }

    private fun prefKey(destinationId: Int) = "dest_$destinationId"
}
