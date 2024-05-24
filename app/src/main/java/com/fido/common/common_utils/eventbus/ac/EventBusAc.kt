package com.fido.common.common_utils.eventbus.ac

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.showNormalListDialog
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.runMain
import com.fido.common.common_base_util.util.dialog.DialogManager
import com.fido.common.databinding.AcHEventbusBinding
import com.fido.common.common_utils.eventbus.HEventBus
import com.fido.common.common_utils.eventbus.Subscribe
import com.fido.common.common_utils.eventbus.ThreadMode
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.impl.CenterListPopupView

/**
@author FiDo
@description:
@date :2023/10/27 11:29
 */
class EventBusAc:AppCompatActivity() {

    val binding:AcHEventbusBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HEventBus.getDefault().register(this)


        val dialog1 = showNormalListDialog(mutableListOf("1","2","3"),isAutoShow = false){position: Int, text: String ->
            toast(text)
        }

        val dialog2 = showNormalListDialog(mutableListOf("kotlin","java","python"), isAutoShow = false){position, text ->
            toast(text)
        }

        val dialog3 = showNormalListDialog(mutableListOf("hello","world","!"),isAutoShow = false){position, text ->
            toast(text)
        }

        val build1 = creatDialogManagerConfig(dialog1)
        val build2 = creatDialogManagerConfig(dialog2)
        val build3 = creatDialogManagerConfig(dialog3)

        DialogManager.getInstance().add(build1)
        DialogManager.getInstance().add(build2)
        DialogManager.getInstance().add(build3)
        DialogManager.getInstance().enableDebug(true)
        binding.btSend.throttleClick {
            HEventBus.getDefault().post(HEventBusTestBean().apply {
                text = "i am from EventBusAc"
            })

            DialogManager.getInstance().show<BasePopupView>()
        }
    }

    private fun creatDialogManagerConfig(dialog1: BasePopupView?):DialogManager.Config<BasePopupView> {
        return DialogManager.Config.Builder<BasePopupView>()
            .`as`(object : DialogManager.BaseType<BasePopupView>(dialog1) {
                override fun init(config: DialogManager.Config<BasePopupView>?) {
                    val pop = (config?.baseType?.type as?  CenterListPopupView)
                    pop?.post {
                        Log.e("FiDo", "init: pop=$pop pop dialog=${pop.dialog}" )
                        pop.dialog?.setOnDismissListener {
                            config.dispatch()
                        }
                    }
                }

                override fun show(config: DialogManager.Config<BasePopupView>?) {
                    /*这是DialogManager执行到展示“概念”弹窗的逻辑，需外部实现相应的show逻辑*/
                    config?.baseType?.type?.show()
                }

                override fun dismiss(config: DialogManager.Config<BasePopupView>?) {
                    /*这是DialogManager执行到隐藏“概念”弹窗的逻辑，需外部实现相应的dismiss逻辑*/
                    config?.getBaseType()?.getType()?.dismiss()
                }
            })
            .priority(0)
            .onShowCheckListener {
                /*这是告诉DialogManager此“概念”弹窗可否展示*/
                /*如我们有些业务需要首页处于哪个Tab时才可展示此弹窗
                 *可以在这里自行实现判断条件
                */
                true
            }
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        HEventBus.getDefault().unRegister(this)
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun showMain(e :EventBus2Bean){
        runMain {
            Toast.makeText(this,e.str,Toast.LENGTH_LONG).show()
        }
    }

}

class EventBus2Bean(val str:String)