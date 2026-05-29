package com.fido.pc_coroutine_retrofit.data

/**
 * @author: HuTao
 * @date: 2026/5/22
 * @des:
 */
data class WanAndroidHomeData(

    val datas: MutableList<HomeItemBean>,
    val curPage: Int,
    val size: Int,
    val total: Int

)