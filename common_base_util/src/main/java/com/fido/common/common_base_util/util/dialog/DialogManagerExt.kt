package com.fido.common.common_base_util.util.dialog

import android.app.Dialog
import android.os.Build
import android.view.View
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.util.dialog.DialogManager.Config

/**
@author FiDo
@description: 管理 Dialog 弹出顺序,弹窗的链式调用
@date :2023/10/30 9:47
 */

@PublishedApi
internal val dialogManager:DialogManager
    get() = DialogManager.getInstance()

/**
 * 暂不支持 DialogFragment
 * @param dialog 可以是任意实现了 show & dismiss & setOnDismissListener方法的 系统或自定义Dialog
 * @param prioroty 值越大优先级越高
 * @param isShowCheck DialogManager 此“概念”弹窗可否展示 如我们有些业务需要首页处于哪个Tab时才可展示此弹窗 可以在这里自行实现判断条件
*/
fun creatDialogManagerConfig(dialog:Any,prioroty:Int = 0,isShowCheck:Boolean = true): Config<Any> = Config.Builder<Any>()
    .`as`(object :DialogManager.BaseType<Any>(dialog){

        override fun init(config: DialogManager.Config<Any>) {
            when(val type = config.baseType.type){
                is Dialog->{
                    type.setOnDismissListener {
                        config.dispatch()
                    }
                }
                /*is DialogFragment -> {
                    type.view?.post {
                        type.dialog?.setOnDismissListener {
                            config.dispatch()
                        }
                    }
                }*/
                else -> {
                    if (dialog is View){
                        dialog.post {
                            doInit(config)
                        }
                    }else{
                        doInit(config)
                    }
                }
            }
        }

        private fun doInit(config: Config<Any>) {
            var mReflectDialog:Dialog?=null
            try {
                var dialogField = config.baseType.type.javaClass.declaredFields.find {
                    Dialog::class.java.isAssignableFrom(it.type)
                }
                if (dialogField == null){
                    dialogField = config.baseType.type.javaClass.fields.find {
                        Dialog::class.java.isAssignableFrom(it.type)
                    }
                }

                // 有定义的 Dialog变量并且反射获取了值
                mReflectDialog = dialogField?.run {
                    isAccessible = true
                    get(config.baseType.type) as Dialog?
                }

                // 反射失败 或 没有获取到 Dialog变量
                if (mReflectDialog == null) {
                    var dialogMethod = config.baseType.type.javaClass.declaredMethods.find {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Dialog::class.java.isAssignableFrom(it.returnType) && it.parameterCount == 0
                        } else {
                            Dialog::class.java.isAssignableFrom(it.returnType)
                        }
                    }
                    if (dialogMethod == null){
                        dialogMethod = config.baseType.type.javaClass.methods.find {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Dialog::class.java.isAssignableFrom(it.returnType) && it.parameterCount == 0
                            } else {
                                Dialog::class.java.isAssignableFrom(it.returnType)
                            }
                        }
                    }
                    dialogMethod?.run {
                        isAccessible = true
                        mReflectDialog = invoke(config.baseType.type) as Dialog?
                    }
                }
                mReflectDialog?.let {
                    it.setOnDismissListener {
                        config.dispatch()
                    }
                }
            }catch (e:Exception){
                loge(e.message.toString())
                if (mReflectDialog == null) {
                    throw RuntimeException("please make sure your custom dialog has least one Dialog Field or return Dialog method")
                }
            }
        }

        override fun show(config: DialogManager.Config<Any>) {
            when (val type = config.baseType.type) {
                is Dialog -> {
                    type.show()
                }
                /*is DialogFragment -> {
                    if (type.dialog == null) {
                        type.show((topActivity as AppCompatActivity).supportFragmentManager, type.javaClass.simpleName)
                    } else {
                        type.dialog?.show()
                    }
                }*/
                else -> {
                    try {
                        config.baseType.type.javaClass.getMethod("show").run {
                            isAccessible = true
                            invoke(config.baseType.type)
                        }
                    }catch (e:Exception){
                        throw RuntimeException("如果是自定义的Dialog请确保实现${config.baseType.type}的show()方法")
                    }
                }
            }
        }

        override fun dismiss(config: DialogManager.Config<Any>) {
            when(val type = config.baseType.type){
                is Dialog -> {
                    type.dismiss()
                }
                /*is DialogFragment -> {
                    type.dismiss()
                }*/
                else ->{
                    try {
                        config.baseType.type.javaClass.getMethod("dismiss").run {
                            isAccessible = true
                            invoke(config.baseType.type)
                        }
                    }catch (e:Exception){
                        throw RuntimeException("如果是自定义的Dialog,请确保实现${config.baseType.type}了dismiss()方法")
                    }
                }
            }
        }
    })
    .priority(prioroty)
    .onShowCheckListener {
        isShowCheck
    }
    .build()

fun addToDialogManager(config: Config<Any>): DialogManager = dialogManager.apply {
    add(config)
}

fun addToDialogManager(vararg configs: Config<Any>?) = dialogManager.apply {
    configs.forEach {
        if (it!=null) addToDialogManager(it)
    }
}

fun removeFromDialigManager(config: Config<Any>) = dialogManager.apply {
    remove(config)
}

inline fun <reified D:Dialog>showDialogManager() = dialogManager.show<D>()