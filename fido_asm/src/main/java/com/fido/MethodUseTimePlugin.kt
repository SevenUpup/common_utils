package com.fido

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import org.gradle.api.Plugin;
import org.gradle.api.Project

/**
 * @author: FiDo
 * @date: 2024/4/3
 * @des:
 */
class MethodUseTimePlugin: Plugin<Project>{
    override fun apply(target: Project) {
        // 判断是否有 com.android.application 插件来判断该 module 是否是一个 Android App 的 moudle，我们只处理 Android APP
        val appPlugin = try {
            target.plugins.getPlugin("com.android.application")
        }catch (e:Throwable){
            null
        }
        if (appPlugin != null) {
            Log.d(msg = "find android app plugin")
            // 通过 project.extensions.getByType(AndroidComponentsExtension::class.java) 来拿到 Android 的 Extension
            val androidExt = target.extensions.getByType(AndroidComponentsExtension::class.java)
            //通过他的 onVariants() 方法来遍历所有的变体信息，然后通过他的 instrumentation 对象来处理插桩的参数
            androidExt.onVariants { variant: Variant ->
                Log.d(msg = variant.name)
                // 通过方法 variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES) 选择方法 Frame 的计算方式和插桩时 MaxStack 的计算方式，我们选择直接复制原来的方法中的这两个值
                variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
                //通过方法 variant.instrumentation.transformClassesWith(AndroidActivityClassVisitorFactory::class.java, InstrumentationScope.ALL) {} 来注册插桩，第一个参数就是我们定义的插桩实现，
                // 他必须是抽象的对象，第二个参数是插桩的范围，可以选择只插桩应用字节码，也可以选择也包含库的字节码，我们选择的是都插桩。
                variant.instrumentation.transformClassesWith(AndroidActivityClassVisitorFactory::class.java,InstrumentationScope.ALL){}
            }
        }
    }
}