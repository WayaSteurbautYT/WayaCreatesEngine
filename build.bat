@rem
@echo off
echo Building WayaCreates Engine...
if exist "gradlew.bat" (
    gradlew.bat build
) else if exist "gradlew" (
    gradlew build
) else if exist "gradle" (
    gradle build
) else (
    echo No Gradle wrapper found. Please run 'gradle wrapper' first.
)
pause
