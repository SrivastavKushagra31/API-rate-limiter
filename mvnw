#!/bin/sh
# Maven Wrapper startup script for Unix
MAVEN_OPTS="-Xmx512m"
exec "$(dirname "$0")/mvnw" "$@"