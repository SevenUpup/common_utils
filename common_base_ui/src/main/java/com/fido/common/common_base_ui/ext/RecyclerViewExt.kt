package com.fido.common.common_base_ui.ext

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.viewholder.DataBindingHolder
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.fido.common.common_base_ui.base.entity.BaseMuiltEntity

/**
 * 设置分割线
 * @param color 分割线的颜色，默认是#DEDEDE
 * @param size 分割线的大小，默认是1px
 * @param isReplace 是否覆盖之前的ItemDecoration，默认是false
 */
fun RecyclerView.divider(
    color: Int = Color.parseColor("#DEDEDE"),
    size: Int = 1,
    isReplace: Boolean = false
): RecyclerView {
    val decoration = DividerItemDecoration(context, orientation)
    decoration.setDrawable(GradientDrawable().apply {
        setColor(color)
        shape = GradientDrawable.RECTANGLE
        setSize(size, size)
    })
    if (isReplace && itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
    addItemDecoration(decoration)
    return this
}

/**
 * 设置垂直的Manager,默认不是瀑布流
 */
fun RecyclerView.vertical(
    spanCount: Int = 0,
    isStaggered: Boolean = false
): RecyclerView {
    layoutManager =
        if (spanCount > 0) GridLayoutManager(context, spanCount) else LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    }
    removeFromMapWhenDetch()
    return this
}

/**
 * 设置水平的Manager,默认不是瀑布流
 */
fun RecyclerView.horizontal(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = if (spanCount > 0) GridLayoutManager(
        context,
        spanCount,
        GridLayoutManager.HORIZONTAL,
        false
    ) else LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL)
    }
    removeFromMapWhenDetch()
    return this
}


inline val RecyclerView.orientation
    get() = if (layoutManager == null) -1 else layoutManager.run {
        when (this) {
            is LinearLayoutManager -> orientation
            is GridLayoutManager -> orientation
            is StaggeredGridLayoutManager -> orientation
            else -> -1
        }
    }

// =================================== Adapter 基于 BRVAH 的使用 ===================================

/**
 * 获取adapter数据
 */
internal inline val RecyclerView.itemsData
    get() = contentAdapter?.items

inline val RecyclerView.bindAdapter
    get() = adapter as? BaseQuickAdapter<*,*>?: throw NullPointerException("RecyclerView Without BindAdapter")

//多布局Holder bind 方法
internal var muiltHolderBindFun:((holder: RecyclerView.ViewHolder, position: Int, item: Any?) -> Unit)? = null

// 如果添加了 Header/Footer 用于存储 ConcatAdapter , 获取 ConcatAdapter 里面的 contentAdapter
internal val qucikAdapterHelperMap = HashMap<Int, QuickAdapterHelper>()
internal inline val RecyclerView.contentAdapter
    get() = qucikAdapterHelperMap[adapter.hashCode()]?.contentAdapter?:(adapter as? BaseQuickAdapter<*, *>)

/**
 * 用于存储当前 Rv 多布局时 xml是否使用binding 的方式
 */
internal val muiltItemUseBindingMap = hashMapOf<Int,Boolean>()
internal var RecyclerView.itemLayoutUseBinding:Boolean
    get() = muiltItemUseBindingMap[id]?:false
    set(value){
        muiltItemUseBindingMap[id] = value
    }

internal fun RecyclerView.removeFromMapWhenDetch(){
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(view: View) {}
        override fun onViewDetachedFromWindow(view: View) {
            qucikAdapterHelperMap.remove(adapter.hashCode())
            muiltItemUseBindingMap.remove(id)
            removeOnAttachStateChangeListener(this)
        }
    })
}

/**
 * 绑定Rv数据 单一类型
 */
fun <T> RecyclerView.bindData(
    data: List<T> = emptyList(),
    layoutResId: Int,
    bindFun: ((holder: QuickViewHolder, position: Int, item: T?) -> Unit)? = null
): RecyclerView {
    adapter = object : BaseQuickAdapter<T, QuickViewHolder>(data) {
        override fun onCreateViewHolder(
            context: Context,
            parent: ViewGroup,
            viewType: Int
        ): QuickViewHolder {
            return QuickViewHolder(layoutResId, parent)
        }

        override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: T?) {
            bindFun?.invoke(holder, position, item)
        }
    }
    return this
}

/**
 * 设置 adapter 多布局 Item 样式 需要在设置数据前调用
 * 目前暂时需要一次性传入所有itemType 和 itemLayoutRes
 * 改方法需要将 xml 设置为 DataBinding
 * @param bindFun    用于onBind 方法回调
 */
fun <T:BaseMuiltEntity> RecyclerView.bindMuiltVBData(
    data: List<T>,
    bindFun: ((holder: DataBindingHolder<ViewDataBinding>, position: Int, item: T?) -> Unit)? = null
): RecyclerView {
    itemLayoutUseBinding = true
    muiltHolderBindFun = bindFun as? ((RecyclerView.ViewHolder, Int, Any?) -> Unit)?
    adapter = object : BaseMultiItemAdapter<T>(data) {}.apply {
        if (data.isNotEmpty()) {
            data.forEach {
                addItemType(it.itemType,
                    object :
                        BaseMultiItemAdapter.OnMultiItemAdapterListener<T, DataBindingHolder<ViewDataBinding>> {
                        override fun onCreate(
                            context: Context,
                            parent: ViewGroup,
                            viewType: Int
                        ): DataBindingHolder<ViewDataBinding> {
                            return DataBindingHolder(it.itemLayoutRes, parent)
                        }

                        override fun onBind(
                            holder: DataBindingHolder<ViewDataBinding>,
                            position: Int,
                            item: T?
                        ) {
                            muiltHolderBindFun?.invoke(holder, position, item)
                        }
                    })
            }
        }
        onItemViewType { position, list ->
            list[position].itemType
        }
    }
    return this
}

/**
 * 设置 adapter 多布局 Item 样式 需要在设置数据前调用
 * 目前暂时需要一次性传入所有itemType 和 itemLayoutRes
 * @param bindFun    用于onBind 方法回调
 */
fun <T : BaseMuiltEntity> RecyclerView.bindMuiltData(
    data: List<T>,
    bindFun: ((holder: QuickViewHolder, position: Int, item: T?) -> Unit)? = null
): RecyclerView {
    itemLayoutUseBinding = false
    muiltHolderBindFun = bindFun as? ((RecyclerView.ViewHolder, Int, Any?) -> Unit)?
    adapter = object : BaseMultiItemAdapter<T>(data) {}.apply {
        if (data.isNotEmpty()) {
            data.forEach {
                addItemType(it.itemType,
                    object : BaseMultiItemAdapter.OnMultiItemAdapterListener<T, QuickViewHolder> {
                        override fun onCreate(
                            context: Context,
                            parent: ViewGroup,
                            viewType: Int
                        ): QuickViewHolder {
                            return QuickViewHolder(it.itemLayoutRes, parent)
                        }

                        override fun onBind(holder: QuickViewHolder, position: Int, item: T?) {
                            bindFun?.invoke(holder, position, item)
                        }
                    })
            }
        }
        onItemViewType { position, list ->
            list[position].itemType
        }
    }
    return this
}

/**
 * 首次设置数据
 */
fun <T> RecyclerView.submitList(
    data: List<T>,
) :RecyclerView{
    // 可能添加了 header/footer 用 qucikAdapterHelperMap 判断
    (contentAdapter as? BaseQuickAdapter<T, *>)?.apply {
        if (this is BaseMultiItemAdapter) {
            addItemsType(data)
        }
        submitList(data)
    }
    return this
}

/**
 * 添加数据
 */
fun <T> RecyclerView.addAll(
    data: List<T>,
    position: Int = -1,
) {
    (contentAdapter as? BaseQuickAdapter<T,*>)?.apply {
        if (this is BaseMultiItemAdapter) {
            addItemsType(data)
        }
        if (position < 0) {
            addAll(data)
        } else {
            addAll(position,data)
        }
    }
}

/**
 * 添加单条数据，可以指定位置
 */
fun <T> RecyclerView.addData(
    data:T,
    position: Int = -1,
):RecyclerView{
    (contentAdapter as? BaseQuickAdapter<T,*>)?.apply {
        addAll(mutableListOf(data),position)
    }
    return this
}

/**
 * @param helper    val helper = QuickAdapterHelper.Builder(adapter as BaseQuickAdapter<*, *>).build()
 * @param index
 * @param data      创建 headerAdapter的数据源
 * @param layoutRes 创建 headerAdapter的 xml id
 * @param bindFun   headerAdapter onBind 处理
 */
fun <T> RecyclerView.addHeader(
    data: List<T>,
    layoutRes: Int,
    bindFun: ((holder: QuickViewHolder, position: Int, item: T?) -> Unit)? = null
): RecyclerView {
    var helper: QuickAdapterHelper? = qucikAdapterHelperMap[adapter.hashCode()]
    if (helper == null) {
        helper = QuickAdapterHelper.Builder(adapter as BaseQuickAdapter<*, *>).build()
        adapter = helper.adapter
        qucikAdapterHelperMap[adapter.hashCode()] = helper
    }
    helper.apply {
        val quickAdapter = object : BaseQuickAdapter<T, QuickViewHolder>(data) {
            override fun onCreateViewHolder(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): QuickViewHolder {
                return QuickViewHolder(layoutRes, parent)
            }

            override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: T?) {
                bindFun?.invoke(holder, position, item)
            }
        }
        addBeforeAdapter(quickAdapter)
    }
    return this
}

/**
 * @param isBefore = false  在 contentAdapter 之后添加 Adapter
 * @param data      创建 footerAdapter的数据源
 * @param layoutRes 创建 footerAdapter的 xml id
 * @param bindFun   footerAdapter onBind 处理
 */
fun <T> RecyclerView.addFooter(
    data: List<T>,
    layoutRes: Int,
    bindFun: ((holder: QuickViewHolder, position: Int, item: T?) -> Unit)? = null
): RecyclerView {
    var helper: QuickAdapterHelper? = qucikAdapterHelperMap[adapter.hashCode()]
    if (helper == null) {
        helper = QuickAdapterHelper.Builder(adapter as BaseQuickAdapter<*, *>).build()
        adapter = helper.adapter
        qucikAdapterHelperMap[adapter.hashCode()] = helper
    }
    helper.apply {
        val quickAdapter = object : BaseQuickAdapter<T, QuickViewHolder>(data) {
            override fun onCreateViewHolder(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): QuickViewHolder {
                return QuickViewHolder(layoutRes, parent)
            }

            override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: T?) {
                bindFun?.invoke(holder, position, item)
            }
        }
        addAfterAdapter(quickAdapter)
    }
    adapter = helper.adapter
    return this
}


/**
 * adapter item click
 */
fun <T> RecyclerView.itemClick(block: (adapter: BaseQuickAdapter<T, *>, view: View, position: Int) -> Unit): RecyclerView {
    adapter?.apply {
        (contentAdapter as BaseQuickAdapter<T,*>).setOnItemClickListener { adapter, view, position ->
            block(adapter, view, position)
        }
    }
    return this
}

/**
 * adapter item child click
 */
fun <T> RecyclerView.addItemChildClick(
    viewId: Int,
    block: (adapter: BaseQuickAdapter<T, *>, view: View, position: Int) -> Unit
): RecyclerView {
    adapter?.apply {
        (contentAdapter as BaseQuickAdapter<T, *>).addOnItemChildClickListener(viewId) { adapter, view, position ->
            block(adapter, view, position)
        }
    }
    return this
}

/**
 * item child long click
 */
fun <T> RecyclerView.addItemChildLongClick(
    viewId: Int,
    block: (adapter: BaseQuickAdapter<T, *>, view: View, position: Int) -> Unit
): RecyclerView {
    adapter?.apply {
        if (this is BaseQuickAdapter<*, *>) {
            (adapter as BaseQuickAdapter<T, *>).addOnItemChildLongClickListener(viewId) { adapter, view, position ->
                block(adapter, view, position)
                true
            }
        }
    }
    return this
}

/**
 * 清理 mAdapter 前面的所有 Adapter
 */
fun RecyclerView.clearBeforeAdapters(): RecyclerView {
    qucikAdapterHelperMap[adapter.hashCode()]?.apply {
        clearBeforeAdapters()
        if (beforeAdapterList.isEmpty() && afterAdapterList.isEmpty()) {
            qucikAdapterHelperMap.remove(adapter.hashCode())
        }
    }
    return this
}

/**
 * 清理 mAdapter 后面的所有 Adapter
 */
fun RecyclerView.clearAfterAdapters(): RecyclerView {
    qucikAdapterHelperMap[adapter.hashCode()]?.apply {
        clearAfterAdapters()
        if (beforeAdapterList.isEmpty() && afterAdapterList.isEmpty()) {
            qucikAdapterHelperMap.remove(adapter.hashCode())
        }
    }
    return this
}

fun RecyclerView.clearBeforeAndAfterAdapters():RecyclerView{
    qucikAdapterHelperMap[adapter.hashCode()]?.apply {
        clearBeforeAdapters()
        clearAfterAdapters()
        qucikAdapterHelperMap.remove(adapter.hashCode())
    }
    return this
}

internal fun <T> BaseMultiItemAdapter<T>.addItemsType(
    data: List<T>,
){
    /*// 方式一 : 用反射获取 BaseMultiItemAdapter 的 typeViewHolders ( SparseArray 存放 items 的 itemtype和 OnMultiItemAdapterListener)
    try {
        val mClazz = BaseMultiItemAdapter::class.java
        val typeViewHolders = mClazz.getDeclaredField("typeViewHolders").run {
            isAccessible = true
            get(this@addItemsType)
        } as? SparseArray<BaseMultiItemAdapter.OnMultiItemAdapterListener<T,RecyclerView.ViewHolder>>
    }catch (e:Exception){
        e.printStackTrace()
    }*/
    // 方式二 : adapter data 遍历判断
    if (data.isNotEmpty()) {
        // A.subtract(B) => A中去掉和B相同的元素
        val subtractList = (data as? List<BaseMuiltEntity>)?.map { it.itemType }?.subtract((recyclerView.itemsData as List<BaseMuiltEntity>).map { it.itemType })
        if (!subtractList.isNullOrEmpty()) {
            subtractList.forEach { itemType ->
                val entity = data.find { it.itemType == itemType }
                entity?.let {
                    if (it.itemLayoutRes > 0) {
                        addItemType(it, muiltHolderBindFun)
                    }
                }
            }
        }
    }
}

/**
 *  用于 BaseMultiItemAdapter addItemType 方法
 *  @param itemUseDatabinding xml是否使用了 databinding , 创建的Holder不同
 */
internal fun <T>BaseMultiItemAdapter<T>.addItemType(
    baseMuiltEntity: BaseMuiltEntity,
    vbBindFun: ((holder: RecyclerView.ViewHolder, position: Int, item: T?) -> Unit)? = null
){
    if (recyclerView.itemLayoutUseBinding) {
        addItemType(baseMuiltEntity.itemType,object :BaseMultiItemAdapter.OnMultiItemAdapterListener<T,DataBindingHolder<ViewDataBinding>>{
            override fun onBind(holder: DataBindingHolder<ViewDataBinding>, position: Int, item: T?) {
                vbBindFun?.invoke(holder, position, item)
            }
            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): DataBindingHolder<ViewDataBinding> {
                return DataBindingHolder(baseMuiltEntity.itemLayoutRes,parent)
            }
        })
    } else {
        addItemType(baseMuiltEntity.itemType,object :BaseMultiItemAdapter.OnMultiItemAdapterListener<T,QuickViewHolder>{
            override fun onBind(holder: QuickViewHolder, position: Int, item: T?) {
                vbBindFun?.invoke(holder,position,item)
            }
            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): QuickViewHolder {
                return QuickViewHolder(baseMuiltEntity.itemLayoutRes,parent)
            }
        })
    }
}