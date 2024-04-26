[![](https://jitpack.io/v/SevenUpup/common_utils.svg)](https://jitpack.io/#SevenUpup/common_utils)
# common_utils
Android常用工具类

## 安装

添加远程仓库根据创建项目的 Android Studio 版本有所不同

Android Studio Arctic Fox以下创建的项目 在项目根目录的 build.gradle 添加仓库

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

Android Studio Arctic Fox以上创建的项目 在项目根目录的 settings.gradle 添加仓库

```kotlin
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

然后在 module 的 build.gradle 添加依赖框架

```groovy
dependencies {
    //常用工具类
    implementation 'com.github.SevenUpup.common_utils:base_util:tag'
    //一些常用UI
    implementation 'com.github.SevenUpup.common_utils:base_ui:tag'
}
```

项目根目录中 gradle.properties 添加

```
android.enableJetifier=true
android.useAndroidX=true
```
ASM Plugin (提供：通过配置替换原生click事件，替换继承类，替换类中(静态)常/变量值)  
Tips:如果需要替换类中常/变量 当修饰符为 final int/float/double/long = 0 时，此时修改值会失效  
     因为插件中需要对class中`<init>`方法进行hook，当值为0时，字节码默认不会在`<init>`方法对其进行操作？  
     final + number类型 时最好不要将初始值赋值为0
```
    //项目根目录.gradle
    buildscript {
         repositories {
             mavenCentral()
         }
         //远程插件依赖
         dependencies {
             classpath 'com.github.SevenUpup.common_utils:fido_asm:tag'
         }
    }
    //然后 模块下.gradle
    plugins {
        //在需要的 的 module 中引用插件：
        id 'com.fido.plugin.asm'
    }
```

## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
