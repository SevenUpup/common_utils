package com.fido.common.surface

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.media.PlaybackParams
import android.net.Uri
import android.os.Build
import android.view.SurfaceHolder
import com.fido.common.base.BaseVBActivity
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcSurfaceBinding
import java.io.IOException

/**
@author FiDo
@description:
@date :2023/4/6 16:29
 */
class SurfaceAc : BaseVBActivity<AcSurfaceBinding>(), SurfaceHolder.Callback {

    lateinit var mediaPlayer: MediaPlayer

    override fun getLayoutId(): Int {
        return R.layout.ac_surface
    }

    override fun initView() {
        val playUrl = "https://oss-p56.xpccdn.com/prod/footage/preview/xpc/YK07SWbmmCa1Qfp.mp4"
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(this, Uri.parse(playUrl))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        binding.surfaceView.holder.addCallback(this)

    }

    override fun initData() {
//        binding.included.mIv.setImageResource(R.drawable.ic_zelda)
    }

    override fun initEvent() {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mediaPlayer.setDisplay(holder)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener(OnPreparedListener { mp ->
            mp.start()
            // 6.0及以后支持倍速，测试
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mp.playbackParams = PlaybackParams().setSpeed(0.5f)
            }
        })

        mediaPlayer.setOnCompletionListener(OnCompletionListener {
            toast("onCompletion")
        })
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }
}