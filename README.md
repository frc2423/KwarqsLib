# KwarqsLib

## Install
The supported way of introducing TitanUtil to a project is via Gradle. Gradle 
is the current build system for WPILib robot projects.

To install, add the following to the root build.gradle
```
allprojects {
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

and, add the dependency (Make sure it's the latest release)
```
dependencies {
    implementation 'com.github.frc2423:KwarqsLib:2021.0.0' // Replace tag with the latest release if needed
}
```
