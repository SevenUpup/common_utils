package com.fido.common.common_base_ui.ext.textview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.fido.common.common_base_ui.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * @author: HuTao
 * @date: 2026/5/19
 * @des:
 */
class PrinterTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    // 打字速度（毫秒/字符）
    private var typingSpeed = 80L

    // 光标闪烁速度（毫秒）
    private var cursorBlinkSpeed = 500L

    // 是否显示光标
    private var showCursor = true

    // 是否正在打字
    private var isTyping = false

    // 当前显示的文本
    private var currentText = ""

    // 完整文本
    private var fullText = ""

    // 光标位置
    private var cursorPosition = 0

    // 光标是否可见
    private var cursorVisible = true

    // 协程任务
    private var typingJob: Job? = null
    private var cursorJob: Job? = null

    // 光标绘制相关
    private val cursorPaint = Paint().apply {
        color = currentTextColor
        style = Paint.Style.FILL
        strokeWidth = 4f
    }

    init {
        // 从XML属性读取配置
        context.obtainStyledAttributes(attrs, R.styleable.PrinterTextView).apply {
            typingSpeed = getInt(R.styleable.PrinterTextView_typingSpeed, 80).toLong()
            cursorBlinkSpeed = getInt(R.styleable.PrinterTextView_cursorBlinkSpeed, 500).toLong()
            showCursor = getBoolean(R.styleable.PrinterTextView_showPrintTextCursor, true)

            // 如果设置了初始文本，立即开始打字
            val text = getString(R.styleable.PrinterTextView_typewriterText)
            if (!text.isNullOrEmpty()) {
                setTextWithAnimation(text)
            }

            recycle()
        }
    }

    /**
     * 设置打字机文本并开始动画
     */
    fun setTextWithAnimation(text: String) {
        cancelJobs()
        fullText = text
        currentText = ""
        cursorPosition = 0
        isTyping = true

        // 开始打字效果
        typingJob = getLifecycleScope().launch {
            flow {
                fullText.forEachIndexed { index, char ->
                    delay(typingSpeed)
                    currentText = fullText.substring(0, index + 1)
                    cursorPosition = currentText.length
                    emit(currentText)
                }
            }.collect {
                setText(it)
                // 请求重绘以更新光标
                invalidate()
            }
            // 打字完成后停止光标闪烁
            isTyping = false
            if (showCursor) {
                cursorJob?.cancel()
                setText(fullText) // 确保最终文本不包含光标
            }
        }

        // 开始光标闪烁效果
        if (showCursor) {
            cursorJob = getLifecycleScope().launch {
                while (isActive && isTyping) {
                    cursorVisible = !cursorVisible
                    invalidate() // 请求重绘
                    delay(cursorBlinkSpeed)
                }
            }
        }
    }

    /**
     * 暂停打字效果
     */
    fun pauseAnimation() {
        typingJob?.cancel()
        cursorJob?.cancel()
        isTyping = false
    }

    /**
     * 继续打字效果
     */
    fun resumeAnimation() {
        if (currentText.length < fullText.length) {
            setTextWithAnimation(fullText)
        }
    }

    /**
     * 重置打字效果
     */
    fun resetAnimation() {
        cancelJobs()
        currentText = ""
        fullText = ""
        cursorPosition = 0
        text = ""
    }

    /**
     * 设置打字速度
     */
    fun setTypingSpeed(speed: Long) {
        typingSpeed = speed
    }

    /**
     * 设置光标闪烁速度
     */
    fun setCursorBlinkSpeed(speed: Long) {
        cursorBlinkSpeed = speed
    }

    /**
     * 是否显示光标
     */
    fun setShowCursor(show: Boolean) {
        showCursor = show
        if (!show) {
            cursorJob?.cancel()
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制光标
        if (showCursor && cursorVisible && isTyping && layout != null) {
            val cursorLine = layout.getLineForOffset(cursorPosition)
            val cursorX = layout.getPrimaryHorizontal(cursorPosition)
            val baseline = layout.getLineBaseline(cursorLine).toFloat()

            // 用 Paint 的 FontMetrics 计算文字的实际上下边界（不含行间距）
            val fm = paint.fontMetrics
            val cursorTop = baseline + fm.ascent
            val cursorBottom = baseline + fm.descent

            cursorPaint.color = currentTextColor
            cursorPaint.strokeWidth = 2f
            cursorPaint.style = Paint.Style.STROKE

            canvas.drawLine(cursorX, cursorTop, cursorX, cursorBottom, cursorPaint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelJobs()
    }

    private fun cancelJobs() {
        typingJob?.cancel()
        cursorJob?.cancel()
        isTyping = false
    }

    private fun getLifecycleScope(): CoroutineScope {
        return try {
            // 尝试获取LifecycleOwner的scope
            (context as? LifecycleOwner)?.lifecycleScope ?: CoroutineScope(Dispatchers.Main)
        } catch (e: Exception) {
            CoroutineScope(Dispatchers.Main)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.currentText = currentText
        savedState.fullText = fullText
        savedState.cursorPosition = cursorPosition
        savedState.isTyping = isTyping
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            currentText = state.currentText
            fullText = state.fullText
            cursorPosition = state.cursorPosition
            isTyping = state.isTyping
            text = currentText

            // 如果之前正在打字，恢复动画
            if (isTyping && currentText.length < fullText.length) {
                setTextWithAnimation(fullText)
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private class SavedState : BaseSavedState {
        var currentText: String = ""
        var fullText: String = ""
        var cursorPosition: Int = 0
        var isTyping: Boolean = false

        constructor(superState: Parcelable?) : super(superState)

        private constructor(parcel: Parcel) : super(parcel) {
            currentText = parcel.readString() ?: ""
            fullText = parcel.readString() ?: ""
            cursorPosition = parcel.readInt()
            isTyping = parcel.readInt() == 1
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(currentText)
            out.writeString(fullText)
            out.writeInt(cursorPosition)
            out.writeInt(if (isTyping) 1 else 0)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel) = SavedState(source)
                override fun newArray(size: Int) = arrayOfNulls<SavedState?>(size)
            }
        }
    }

}