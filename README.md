<div align="center"><img src="https://i.ibb.co/zRcvmKb/Chocolate.png" width="400"></div>
<br>

<div align="center">
<a href="https://github.com/lucasperretta/chocolate"><img src="https://img.shields.io/static/v1?label=stable&message=BETA&color=informational" alt="Version"></a>
<a href="https://github.com/lucasperretta/chocolate"><img src="https://img.shields.io/static/v1?label=license&message=MIT&color=green" alt="License"></a>
<a href="https://github.com/lucasperretta/chocolate"><img 
src="https://img.shields.io/badge/language-Java 8-yellow" alt="Language"></a>
</div>

### Prerequisites
Android Chocolate works on Android 5.0+ (API 22) and requires Java 8+.

If you don't have Java 8 installed, add this line in your android definition inside the module level build.gradle file:
``` groovy
android {
    // ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    // ...
}
```

### Instalation
In your project level build.gradle file, add this line inside your dependencies declaration:
``` groovy
implementation 'com.google.code.gson:gson:2.8.6'
```
