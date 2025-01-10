package com.fido.common.common_utils.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: FiDo
 * @date: 2025/1/10
 * @des:
 */

fun<T> RecyclerView.bindData(
    list: List<T>,
    layoutRes: Int,
    onBind:(holder: CommonVH,pos:Int,item:T)-> Unit
){
    val mAdapter = CommonRvAdapter<T>(list,layoutRes, onBind = onBind)
    this.adapter = mAdapter

}

internal class CommonRvAdapter<T>(val list: List<T>,val layoutRes: Int,val onBind:((holder: CommonVH,pos:Int,item:T)-> Unit)?=null): RecyclerView.Adapter<CommonVH>(){

    val data
        get() = list

    fun clearData(){
        list.toMutableList().clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommonVH {
        return CommonVH(LayoutInflater.from(parent.context).inflate(layoutRes,parent,false))
    }

    override fun onBindViewHolder(
        holder: CommonVH,
        position: Int
    ) {
        onBind?.invoke(holder,position,list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class CommonVH(itemView: View): RecyclerView.ViewHolder(itemView)

fun CommonVH.setText(id: Int, text: String){
    this.itemView.findViewById<TextView>(id).text = text
}

fun CommonVH.setGone(id: Int,isGone: Boolean){
    this.itemView.findViewById<View>(id).isGone = isGone
}