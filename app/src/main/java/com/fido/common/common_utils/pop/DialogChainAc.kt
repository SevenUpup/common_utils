package com.fido.common.common_utils.pop

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.addItemChildLongClick
import com.fido.common.common_base_ui.ext.bindAdapter
import com.fido.common.common_base_ui.ext.bindData
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_ui.util.showNormalConfirmDialog
import com.fido.common.common_base_ui.util.showNormalListDialog
import com.fido.common.common_base_ui.util.sp
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.rectangleCornerBg
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.util.dialog.*
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcDialogChainBinding
import com.fido.common.common_utils.rv.MyItemTouchHelperCallBack


/**
@author FiDo
@description:
@date :2023/10/30 9:28
 */
class DialogChainAc :AppCompatActivity(){

    val binding:AcDialogChainBinding by binding()

    private val mList = mutableListOf(
    "kotlin,java,flutter",
    "九阳神功,乾坤大挪移,易经经",
    "曼城,多特蒙德,阿森纳,皇马",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.bt3.apply {
            rectangleCornerBg(R.color.colorBgBtnGray,12f)
            throttleClick {
                /*val list = mutableListOf<DialogFragment>()
                for (i in 0..2) {
                    val dialogFragment = MyDialogFragment()
                    list.add(dialogFragment)
                }
                val dialogConfig = list.map {
                    creatDialogManagerConfig(it)
                }.toMutableList()
                addToDialogManager(configs = dialogConfig.toTypedArray()).show<Dialog>()*/
                var mDialogFragment:DialogFragment?=null

                mDialogFragment = MyDialogFragment {
                    try {
                        val dialogField = DialogFragment::class.java.getDeclaredField("mDialog")
                        val field = DialogFragment::class.java.declaredFields.find {
                            Dialog::class.java.isAssignableFrom(it.type)
                        }
                        val dialog1 = dialogField.run {
                            isAccessible = true
                            get(mDialogFragment) as? Dialog
                        }
                        val dialog2 = field?.run {
                            isAccessible = true
                            get(mDialogFragment) as? Dialog
                        }
                        loge("dialogField=${dialogField} field=$field  dialog1 =${dialog1} dialog2=${dialog2}")

                    }catch (e:Exception){
                        loge(e.message.toString())
                    }
                }
                mDialogFragment.show(supportFragmentManager,"FiDo")
            }

        }

        binding.bt2.throttleClick {
            DialogManager.getInstance().clear()
            val list = mutableListOf("A","B","C")
            var dialogConfigs = mutableListOf<DialogManager.Config<Any>>()
            dialogConfigs = list.mapIndexed { index, it ->
                showNormalConfirmDialog(title = "i am 第${(index+1)} 个弹窗", content = it, isAutoShow = false, onCancleBlock = {
                    if (dialogConfigs.isNotEmpty()) {
                        toast("index =${index}")
                    }
                }){
                    if (dialogConfigs.isNotEmpty() && index == 0) {
                        removeFromDialigManager(dialogConfigs[1])
                    }
                }
            }.mapIndexed { index, basePopupView ->
                creatDialogManagerConfig(basePopupView,list.size - index)
            }.toMutableList()
            loge("dialogConfigs = ${dialogConfigs}")
            addToDialogManager(configs = dialogConfigs.toTypedArray()).show<Dialog>()
        }

        binding.bt.throttleClick {
            DialogManager.getInstance().clear()
            val data = binding.mRv.bindAdapter.items
            toast(data.toString())
            val m = data as? List<String>
            val configList = mutableListOf<DialogManager.Config<Any>>()
            m?.mapIndexed { index, it ->
                showNormalListDialog(it.split(","),title = "i am 第${(index+1)} 个弹窗", isAutoShow = false){position, text -> toast(text) }
            }?.forEachIndexed { index, basePopupView ->
                try {
                    basePopupView?.let {
//                        configList.add(creatDialogManagerConfig(basePopupView, prioroty = m.size - index))
                        configList.add(creatDialogManagerConfig(basePopupView, prioroty = index ))
                    }
                }catch (e:Exception){
                    loge("error = ${e.message}")
                }
            }
            addToDialogManager(configs = configList.toTypedArray()).show<Dialog>()
        }

        binding.mRv.vertical()
            .bindData(
                mList,
                com.fido.common.common_utils.R.layout.item_rv_text
            ){holder, position, item ->
                holder.setGone(com.fido.common.common_utils.R.id.bt_down,true)
                holder.setGone(com.fido.common.common_utils.R.id.bt_up,true)
                holder.getView<TextView>(com.fido.common.common_utils.R.id.mTitle).apply {
                    text = item
                    textSize = 13.sp.toFloat()
                }
            }
            .addItemChildLongClick<String>(com.fido.common.common_utils.R.id.mTitle){ adapter, view, position ->
                toast(adapter.items[position])
            }

        ItemTouchHelper(MyItemTouchHelperCallBack(binding.mRv.bindAdapter).apply {
            setCanDrag(true)
        }).attachToRecyclerView(
            binding.mRv
        )
    }

}

class MyDialogFragment(private val initBlock:(()->Unit)?=null):DialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.layout_header_view, null)
            builder.setView(view)
                .setPositiveButton("Sign in",
                DialogInterface.OnClickListener { dialog, id -> }).setNegativeButton("Cancel", null)
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        initBlock?.invoke()
    }

}