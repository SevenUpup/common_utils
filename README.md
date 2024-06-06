[![](https://jitpack.io/v/SevenUpup/common_utils.svg)](https://jitpack.io/#SevenUpup/common_utils)
![GitHub Release](https://img.shields.io/github/v/release/SevenUpup/common_utils)
![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/SevenUpup/common_utils/total)
<!-- ![GitHub Downloads (all assets, latest release)](https://img.shields.io/github/downloads/SevenUpup/common_utils/latest/total) -->

# common_utils
Android common utils  
Android asm plugin was underway...目前已支持部分功能

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

### ASM Plugin支持以下功能

#### ViewClickPluginParameter       
##### 通过配置替换原生click事件：一般用于防抖  

#### ReplaceClassPluginParameter    
##### 全局替换类继承关系：例如将继承androidx.appcompat.app.AppCompatActivity改为基类Activity  

#### HookClassParameter             
##### 修改类中(静态)常/变量值：支持基本数据类型，可用于打包时一些动态配置的参数 
如果需要修改类中常/变量 当修饰符为 final int/float/double/long = 0 时，此时修改值会失效  
因为插件中需要对class中`<init>`方法进行hook，当值为0时，字节码默认不会在`<init>`方法对其进行操作？  
final + number类型 时最好不要将初始值赋值为0
#### ReplaceMethodPluginParameters  
##### 提供替换任意类方法的功能，但需配合注解库fido_annotation一起使用
需提供含有注解的类名，通过以下两种方式添加
1.root的.gradle下添加 ext.fido_asm_replace_method_class = ["xxx"]        
2.通过ReplaceMethodPluginParameters{ replaceClassName=[] } 且root下[build.gradle](build.gradle)添加 ext.fido_asm_replace_method_enable = true
目前可实现在用户未同意隐私权限前，三方库或现有代码调用获取‘硬件信息’时，替换为，通过‘注解’的方法，具体使用可参考DeviceInfoAc/AsmHookActivity

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
    //如需使用替换指定类插件，请添加下面的依赖
    implementation 'com.github.SevenUpup.common_utils:fido_annotation:v0.3.60'
    //然后就可以愉快的使用啦，模块下.gradle最外层  
    HookClassParameter {  //className中“*”会替换成它前面不为“*”的值，及[A,A,A,B,B,B,C,C,C,C]，避免写那么长的类名
         className = ["xxx.A","*","*","xxx.B","*","*","xxx.C","*","*","*","*","*"]
         parameterName = ["STATCI_VAL_DOUBLE","STATCI_VAL_LONG","INT_VAL","INT_PROT","INT_PUB"]
         parameterNewValue = ["6666",16.0,555L,777,true]
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
