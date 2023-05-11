package com.fido.common.common_utils.widgets

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fido.common.common_base_util.ext.margin
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.util.BannerUtils

/**
@author FiDo
@description:
@date :2023/3/31 19:07
 */
class ImageAdapter(imageUrls: List<String>) : BannerAdapter<String, ImageAdapter.ImageHolder>(imageUrls) {


    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        val imageView = ImageView(parent!!.context)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        //通过裁剪实现圆角
        BannerUtils.setBannerRound(imageView, 20f)
        return ImageHolder(imageView)
    }

    override fun onBindView(holder: ImageHolder, data: String, position: Int, size: Int) {
        Glide.with(holder.itemView)
            .load(data)
            .into(holder.imageView)
    }


    class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view as ImageView
    }

}

class ImageAdapter2(imageUrls: List<String>,val context:Context) : BannerAdapter<String, ImageAdapter2.ImageHolder2>(imageUrls) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder2 {
//        val imageView = CircleImageView(parent!!.context,Color.TRANSPARENT)
        val imageView = ImageView(parent!!.context)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.layoutParams = params
        imageView.setBackgroundColor(Color.TRANSPARENT)
//        val view = LayoutInflater.from(parent!!.getContext()).inflate(R.layout.layout_banner_item, parent, false)
        return ImageHolder2(imageView)
    }

    override fun onBindView(holder: ImageHolder2, data: String, position: Int, size: Int) {
        Glide.with(holder.itemView)
            .applyDefaultRequestOptions(RequestOptions.circleCropTransform())
            .load(data)
            .into(holder.imageView)
    }

    class ImageHolder2(val view: View) : RecyclerView.ViewHolder(view) {

        var imageView: ImageView = view as ImageView
            get() {
                field.post {
                    field.margin(-5.dp,0-5.dp,0)
                }
                return field
            }
//        val imageView: ImageView
//            get() = view.findViewById(R.id.circleView)
    }

}