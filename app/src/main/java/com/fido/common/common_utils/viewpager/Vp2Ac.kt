package com.fido.common.common_utils.viewpager

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.CommonVp2FragmentStateAdapter
import com.fido.common.common_base_util.ext.addFragment
import com.fido.common.common_base_util.ext.addFragment2Front
import com.fido.common.common_base_util.ext.bindFragment
import com.fido.common.common_base_util.ext.bindView
import com.fido.common.common_base_util.ext.clearFragments
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.removeFragment
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcVp2Binding

/**
 * @author: FiDo
 * @date: 2024/3/8
 * @des:
 */
class Vp2Ac:AppCompatActivity() {

    val binding:AcVp2Binding by binding()

    private val mFragments = mutableListOf<Fragment>()
    private var index = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        for (i in 0..5) {
            mFragments.add(TestFragment.newInstance("i am Fragment $i "))
        }
        binding.apply {

            btAdd.click {
                mFragments.add(0,TestFragment.newInstance("动态添加的Fragment${index}"))
//                mVp2.adapter?.notifyDataSetChanged()
                mVp2.setCurrentItem(mVp2.currentItem+1,false)
                index++
            }
            btDeleteFirst.click {
                mFragments.removeFirst()
                mVp2.adapter?.notifyItemRemoved(0)
                mVp2.setCurrentItem(mVp2.currentItem,false)
            }
            btDeleteLast.click {
                mFragments.removeLast()
                mVp2.adapter?.notifyItemRemoved(mFragments.size-1)
                mVp2.setCurrentItem(mFragments.size - 1,false)
            }
            clear.click {
                mFragments.clear()
                mVp2.adapter?.notifyDataSetChanged()
            }
            mVp2.bindFragment(supportFragmentManager,this@Vp2Ac.lifecycle,mFragments,5)

            initCustomVp2Adapter(this)
        }
    }

    private val mFragments2 = listOf<Fragment>()
    private var fIndex = 0
    private var lIndex = 0
    private fun initCustomVp2Adapter(binding: AcVp2Binding) {
        binding.apply {
            vp2.bindFragment(supportFragmentManager,this@Vp2Ac.lifecycle,mFragments2,5)
            btAddFrontCustom.click {
                vp2.addFragment2Front(TestFragment.newInstance("最左边添加的Framgent${lIndex}"))
                lIndex++
            }
            btAddCustom.click {
                vp2.addFragment(TestFragment.newInstance("我是动态添加的Framgent${fIndex}"))
                fIndex++
            }
            btAddMiddleCustom.click {
                if (vp2.adapter is CommonVp2FragmentStateAdapter) {
                    val fSize = (vp2.adapter as CommonVp2FragmentStateAdapter).fragments.size
                    if ( fSize < 2) {
                        toast("当前Fragment长度为${fSize}")
                        return@click
                    }
                }
                vp2.addFragment(TestFragment.newInstance("下标为2的位置插入一个Fragment"), position = 2)
            }
            btDeleteCustom.click {
                vp2.removeFragment(vp2.currentItem)
            }
            btDeleteAllCustom.click {
                vp2.clearFragments()
            }

            binding.vp2.bindView(5,R.layout.item_rv_img_match){ position ->
                findViewById<ImageView>(R.id.mIv).apply {
                    setImageResource(R.drawable.bg)
                    click {
                        toast(position.toString())
                    }
                }
            }

        }

    }

}