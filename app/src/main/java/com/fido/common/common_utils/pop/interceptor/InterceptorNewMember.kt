package com.fido.common.common_utils.pop.interceptor

import com.fido.common.common_base_ui.util.showNormalConfirmDialog
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_utils.pop.base_interceptor.BaseInterceptorImp
import com.fido.common.common_utils.pop.base_interceptor.InterceptorChain

/**
 * @author: FiDo
 * @date: 2024/6/5
 * @des:  新用户拦截操作
 */
class InterceptorNewMember(private val bean: InterceptorBean,val loginBlock:(()->Unit)?=null): BaseInterceptorImp() {

    override fun interceptor(chain: InterceptorChain) {
        super.interceptor(chain)

        if (bean.isNewMember) {
            chain.activity?.showNormalConfirmDialog(content = "新用户，先去登陆，是否立即登陆？", confirmText = "立即登陆", onConfirmBlock = loginBlock, cancelText = "我就不，告辞", onCancelBlock = {

            })?.show()
        }else{
            chain.process()
        }
    }

    //已经登陆完成-放行操作
    fun resetNewMember(){
        //执行下一个chain
        mChain?.process()
    }
}

class InterceptorInfoFill(val bean: InterceptorBean):BaseInterceptorImp(){
    override fun interceptor(chain: InterceptorChain) {
        super.interceptor(chain)
        if (!bean.isFillInfo) {
            //未完善用户信息
            chain.activity?.showNormalConfirmDialog(content = "为完善用户信息，是否立即去完善？", confirmText = "立即完善",cancelText = "我就不，告辞", onConfirmBlock = {
                toast("完善成功")
                bean.isFillInfo = true
                chain.process()
            })?.show()
        }else{
            chain.process()
        }
    }
}

// 是否完善人脸信息
class InterceptorUserFace(val bean: InterceptorBean):BaseInterceptorImp(){
    override fun interceptor(chain: InterceptorChain) {
        super.interceptor(chain)
        if (bean.isNeedFace) {
            chain.activity?.showNormalConfirmDialog(content = "您的人脸信息未收集,是否立即收集？", confirmText = "立即收集", cancelText = "我就不，告辞",onConfirmBlock = {
                bean.isNeedFace = false
                chain.process()
            })?.show()
        }else{
            chain.process()
        }
    }
}