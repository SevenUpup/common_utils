package com.fido.common.common_utils.rv

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.common_base_ui.base.entity.BaseMuiltEntity
import com.fido.common.common_base_ui.ext.*
import com.fido.common.common_base_util.BaseUtils.getColorCompat
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcRvBinding
import com.fido.common.common_utils.databinding.ItemRvImgBinding
import com.fido.common.common_utils.databinding.ItemRvTextBinding

class RvAc:AppCompatActivity() {

    lateinit var mBinding:AcRvBinding
    private lateinit var mRv: RecyclerView
    private lateinit var mRv2: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.ac_rv)

        initView()
    }

    private fun initView() {
        mRv = findViewById(R.id.mRv)
        mRv2 = findViewById(R.id.mRv2)

        val mTestData = mutableListOf<MEntity>()
        for (i in 0 until 10) {
            val d = MEntity().apply {
                mType = if (i % 2 == 0) 0 else 1
                content = (i+5).toString()
            }
            mTestData.add(d)
        }

        mRv.vertical()
            .setMuiltVBItems(mutableListOf<MEntity>().apply {
                add(MEntity().apply {
                    itemType = 0
                    itemLayoutRes = R.layout.item_rv_text
                })
                add(MEntity().apply {
                    itemType = 1
                    itemLayoutRes = R.layout.item_rv_img
                })
                add(MEntity().apply {
                    itemType = 2
                    itemLayoutRes = R.layout.item_rv_text_img
                })
            }) { holder, position, item ->
                when(holder.binding){
                    is ItemRvTextBinding ->{
                        (holder.binding as ItemRvTextBinding).data = "xml data bind${item?.content}"
                    }
                    is ItemRvImgBinding -> {
                        (holder.binding as? ItemRvImgBinding)?.mIv?.setImageResource(R.mipmap.flower)
                    }
                }
            }
            .addHeader(mutableListOf("1"), R.layout.layout_header_view) { holder, position, item ->
                holder.setText(R.id.tv_header_title, "rv header1")
                holder.itemView.setOnClickListener {
                    mRv.addFooter(mutableListOf("222"), R.layout.layout_header_view)
                }
            }
            .divider(size = 5)
            .itemClick<MEntity> { adapter, view, position ->
                Toast.makeText(this, "pos:$position", Toast.LENGTH_SHORT).show()
            }
            .addItemChildClick<MEntity>(R.id.mIv) { adapter, view, position ->
                Toast.makeText(this, "click img:$position 变为横向", Toast.LENGTH_SHORT).show()
                mRv.horizontal()
                    .divider(size = 0, isReplace = true)
            }
            .submitList(mTestData)

        mRv2.vertical()
            .divider(color = getColorCompat(R.color.purple_500), size = 1, isReplace = true)
            .bindData(
//                mutableListOf("1", "2", "3", "呵呵我嘿嘿"),
                mutableListOf<String>(),
                R.layout.item_rv_text
            ) { holder, position, item ->
                holder.setText(R.id.mTitle, "$position-->${item}")
            }
            .addHeader(mutableListOf("1"), R.layout.layout_header_view) { holder, position, item ->
                holder.setText(R.id.tv_header_title, "rv2 header1")
                holder.itemView.setOnClickListener {
                    mRv2.addHeader(mutableListOf("1"), R.layout.layout_header_view)
                }
            }
            .addHeader(mutableListOf("1"), R.layout.layout_header_view) { holder, position, item ->
                holder.setText(R.id.tv_header_title, "rv2 header2")
                holder.itemView.setOnClickListener {
                    mRv2.clearBeforeAdapters()
                }
            }
            .addFooter(
                mutableListOf("444", "555", "666"),
                R.layout.layout_header_view
            ) { holder, position, item ->
                holder.setText(R.id.tv_header_title, "rv2 footer1")
            }
            .itemClick<String> { adapter, view, position ->
                Toast.makeText(this, "pos${position}", Toast.LENGTH_SHORT).show()
            }

        Handler().postDelayed({
            mRv.addAll(listOf(MEntity().apply {
                mType = 2
            }))
            val list = ArrayList<String>()
            list.add("66666666")
            mRv2.addAll(list)
        }, 4000)

    }


}

class MEntity : BaseMuiltEntity() {

    var content = ""
    var mType: Int = 0
        get() = field
        set(value) {
            itemType = value
            field = value
        }

}