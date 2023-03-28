package com.fido.common.common_base_ui.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘工具类
 */
public class KeyboardUtils {

	/**
	 * 为给定的编辑器开启软键盘
	 * @param context 
	 * @param editText 给定的编辑器
	 */
	public static void openSoftKeyboard(Context context, EditText editText){
		editText.requestFocus();
		 InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
	        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
	                InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * 关闭软键盘
	  */
	public static void closeSoftKeyboard(View view){
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(),0);
		}
	}


	/**
	 * 动态隐藏软键盘--√yyjobs
	 */
	public static void hideSoftInput(Activity activity) {
		View view = activity.getCurrentFocus();
		if (view == null) view = new View(activity);
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (imm == null) return;
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/*
	 * 显示键盘
	 * */
	public static void showKeyboard(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
	}


	/**
	 * 切换软键盘状态 如果是弹出就隐藏
	 *
	 * @param activity
	 */
	public static void toggleSoftkeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}


	/**
	 * 判断当前输入法是否打开
	 *
	 * @param activity
	 * @return
	 */
	public static boolean getInputOpen(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.isActive();//isOpen若返回true，则表示输入法打开
	}


	/**
	 * 显示软键盘
	 *
	 * @param activity
	 */
	public static void showSoftkeyboard(Activity activity) {
		try {
			InputMethodManager inputManager =
					(InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.showSoftInput(activity.getCurrentFocus(), 0);
		} catch (NullPointerException e) {

		}
	}

	/**
	 * 隐藏软键盘
	 *
	 * @param activity
	 */
	public static void hideSoftkeyboard(Activity activity) {
		try {
			((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (NullPointerException e) {
		}
	}

	// =======================  软键盘监听 begin ↓ =========================

	/**
	 * 监听输入键盘的谈起和收起
	 *
	 * @param activity
	 * @param onSoftkeyboardChangeListener
	 */
	public static void setOnSoftkeyboardChangeListener(Activity activity, final OnSoftKeyBoardChangeListener onSoftkeyboardChangeListener) {

		final View decorView = activity.getWindow().getDecorView();
		decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			//当键盘弹出隐藏的时候会 调用此方法。
			@Override
			public void onGlobalLayout() {
				final Rect rect = new Rect();
				decorView.getWindowVisibleDisplayFrame(rect);
				final int screenHeight = decorView.getRootView().getHeight();
				final int heightDifference = screenHeight - rect.bottom;
				boolean visible = heightDifference > screenHeight / 3;
				onSoftkeyboardChangeListener.onSoftKeyBoardChange(visible, heightDifference);
			}
		});
	}


	/**
	 * 监听软键盘的弹起和收起的回调
	 */
	public interface OnSoftKeyBoardChangeListener {

		/**
		 * 第一个参数是否显示
		 * 第二个参数是软键盘高度
		 *
		 * @param isShow
		 * @param keyboadHeight
		 */
		void onSoftKeyBoardChange(boolean isShow, int keyboadHeight);

	}

}
