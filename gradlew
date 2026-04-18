#!/usr/bin/env sh
##############################################################################
# Gradle wrapper start up script for UN*X
##############################################################################
set -e

# Attempt to detect JAVA_HOME if not set
if [ -z "$JAVA_HOME" ] ; then
  JAVA_HOME=$(/usr/libexec/java_home 2>/dev/null || echo "")
fi

APP_HOME=$( cd "${0%/*}" && pwd )
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

exec "$JAVA_HOME/bin/java" \
    -classpath "$CLASSPATH" \
    org.gradle.wrapper.GradleWrapperMain \
    "$@"
