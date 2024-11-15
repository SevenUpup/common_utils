
[![](https://jitpack.io/v/SevenUpup/common_utils.svg)](https://jitpack.io/#SevenUpup/common_utils)
![GitHub Release](https://img.shields.io/github/v/release/SevenUpup/common_utils)
![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/SevenUpup/common_utils/total)
<!-- ![GitHub Downloads (all assets, latest release)](https://img.shields.io/github/downloads/SevenUpup/common_utils/latest/total) -->

# common_utils
Android common utils  
Android asm plugin was underway...目前已支持部分功能

## 安装

添加远程仓库根据创建项目的 Android Studio 版本有所不同。

### Android Studio Arctic Fox以下版本
在项目根目录的 `build.gradle` 添加仓库：

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

### Android Studio Arctic Fox以上版本
在项目根目录的 `settings.gradle` 添加仓库：

```kotlin
dependencyResolutionManagement {
    repositories {
        // ...
        maven(url = "https://jitpack.io")
    }
}
```

然后在 module 的 `build.gradle` 添加依赖框架：

```groovy
dependencies {
    //常用工具类
    implementation 'com.github.SevenUpup.common_utils:base_util:tag'
    //一些常用UI
    implementation 'com.github.SevenUpup.common_utils:base_ui:tag'
}
```

项目根目录中 `gradle.properties` 添加：

```
android.enableJetifier=true
android.useAndroidX=true
```

## ASM Plugin 支持以下功能

### ViewClickPluginParameter
通过配置替换原生 click 事件：一般用于防抖。

### ReplaceClassPluginParameter
全局替换类继承关系：例如将继承 `androidx.appcompat.app.AppCompatActivity` 改为基类 `Activity`。

### HookClassParameter
修改类中(静态)常/变量值：支持基本数据类型，可用于打包时一些动态配置的参数。如果需要修改类中常/变量，当修饰符为 `final int/float/double/long = 0` 时，此时修改值会失效，因为插件中需要对 class 中 `<init>` 方法进行 hook，当值为 0 时，字节码默认不会在 `<init>` 方法对其进行操作？`final + number` 类型时最好不要将初始值赋值为 0。

### 替换指定方法
提供替换指定方法的功能（需引入注解库 `fido_annotation` 通过 `AsmMethodReplace` 注解实现）。开关控制：root 下 [build.gradle](build.gradle) 添加 `ext.fido_asm_replace_method_enable = true`，debug 时可以置为 false，以免影响编译效率。通过以下方式添加含有注解的全类名（object 静态方法记得添加 `@JvmStatic` 哦）：

1. root 下的 .gradle 添加 `ext.fido_asm_replace_method_class = ["xxx"]`
2. 通过 `ReplaceMethodPluginParameters`：

```groovy
ReplaceMethodPluginParameters {
    enable = true
    replaceClassName = []
}
```

目前可实现在用户未同意隐私权限前，三方库或现有代码调用获取‘硬件信息’时，替换为，通过‘注解’的方法，具体使用可参考 `DeviceInfoAc/AsmHookActivity`。

```groovy
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

//如需使用替换指定类插件，请添加下面的依赖
implementation 'com.github.SevenUpup.common_utils:fido_annotation:v0.3.60'

//然后就可以愉快的使用啦，模块下.gradle最外层
HookClassParameter {
    //className中“*”会替换成它前面不为“*”的值，及[A,A,A,B,B,B,C,C,C,C]，避免写那么长的类名
    className = ["xxx.A", "*", "*", "xxx.B", "*", "*", "xxx.C", "*", "*", "*", "*", "*"]
    parameterName = ["STATCI_VAL_DOUBLE", "STATCI_VAL_LONG", "INT_VAL", "INT_PROT", "INT_PUB"]
    parameterNewValue = ["6666", 16.0, 555L, 777, true]
}
```

## Common Plugin

```groovy
plugins {
    id 'com.fido.plugin.asm'
}

fido {
    printDependencies = true //print project dependencies 并输出到桌面
    permissionsToRemove = ['android.permission.XXX', 'android.permission.XXX'] // delete permission 并将结果输出到桌面
}
```

## 开发中功能

```
emm...关于插件功能，目前正在新增并完善，欢迎提出你的想法(因为提了我也不一定理)
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
See the License for the specific language governing permissions and limitations under the License.
```
