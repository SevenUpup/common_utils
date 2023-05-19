package com.fido.common.common_base_util.ext

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import kotlin.math.roundToInt

/**
@author FiDo
@description: textview 相关扩展
@date :2023/5/16 10:35
 */
// ============================ TextView ===============================

fun TextView.setDrawable(
    @DrawableRes leftDrawable: Int = 0,
    @DrawableRes topDrawable: Int = 0,
    @DrawableRes rightDrawable: Int = 0,
    @DrawableRes bottomDrawable: Int = 0
) {
    setCompoundDrawablesWithIntrinsicBounds(leftDrawable,topDrawable,rightDrawable,bottomDrawable)
}

fun TextView.setDrawable(
    left: Drawable?=null,
    top: Drawable?=null,
    right: Drawable?=null,
    bottom: Drawable?=null,
){
    setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
}

inline val TextView.textString:String get() = text.toString()

fun TextView.isTextEmpty():Boolean = textString.isEmpty()

fun TextView.isTextNotEmpty():Boolean = textString.isNotEmpty()

inline var TextView.isPasswordVisible: Boolean
    get() = transformationMethod != PasswordTransformationMethod.getInstance()
    set(value) {
        transformationMethod = if (value) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
    }

inline var TextView.isFakeBoldText: Boolean
    get() = paint.isFakeBoldText
    set(value) {
        paint.isFakeBoldText = value
    }

var TextView.font: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR)
    get() = noGetter()
    set(@FontRes value) {
        typeface = ResourcesCompat.getFont(context,value)
    }

var TextView.typefaceFromAssets: String
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR)
    get() = noGetter()
    set(value) {
        typeface = Typeface.createFromAsset(context.assets, value)
    }

fun TextView.addUnderline() {
    paint.flags = Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.transparentHighlightColor() {
    highlightColor = Color.TRANSPARENT
}

fun TextView.enableWhenOtherTextNoEmpty(vararg textViews:TextView){
    enableWhenOtherTextChange(*textViews){all { it.isTextNotEmpty() }}
}

inline fun TextView.enableWhenOtherTextChange(
    vararg textViews:TextView,
    crossinline block:Array<out TextView>.()->Boolean,
){
    isEnabled = block(textViews)
    textViews.forEach {
        it.doAfterTextChanged {
            isEnabled = block(textViews)
        }
    }
}

fun TextView.startCountDown(
    lifecycleOwner: LifecycleOwner,
    sencondInFuture:Int = 60,
    onTick:TextView.(sencondUntilFinished:Int)->Unit,
    onFinish:TextView.()->Unit,
) = object : CountDownTimer(sencondInFuture*1000L,1000) {
    override fun onTick(millisUntilFinished: Long) {
        isEnabled = false
        this@startCountDown.onTick((millisUntilFinished/1000f).roundToInt())
    }

    override fun onFinish() {
        isEnabled = true
        this@startCountDown.onFinish()
    }
}.also { countDownTimer ->
    countDownTimer.start()
    lifecycleOwner.doOnLifeCycle(onDestory = {
        countDownTimer.cancel()
    })
}
