package com.fido.common.common_base_ui.ext.image_select

import android.content.Context
import com.luck.picture.lib.engine.UriToFileTransformEngine
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.utils.SandboxTransformUtils

/**
 * 自定义沙盒文件处理
 */
internal class MeSandboxFileEngine private constructor(): UriToFileTransformEngine {

    companion object{
        private var instance:MeSandboxFileEngine?=null

        fun createSandboxFileEngine():MeSandboxFileEngine?{
            if (null == instance) {
                synchronized(MeSandboxFileEngine::class.java) {
                    if (null == instance) {
                        instance = MeSandboxFileEngine()
                    }
                }
            }
            return instance
        }
    }

    override fun onUriToFileAsyncTransform(
        context: Context,
        srcPath: String,
        mineType: String,
        call: OnKeyValueResultCallbackListener?
    ) {
        call?.onCallback(
            srcPath,
            SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType)
        )
    }
}