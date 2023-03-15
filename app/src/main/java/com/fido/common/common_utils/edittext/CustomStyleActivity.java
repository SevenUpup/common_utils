package com.fido.common.common_utils.edittext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fido.common.common_base_ui.widget.edittext.CodeEditText;
import com.fido.common.common_utils.R;
import com.fido.common.common_utils.edittext.style.CustomTextDrawer;
import com.fido.common.common_utils.edittext.style.CustomeBlockDrawer;


/**
 * Created by lwj on 2019/1/17.
 * lwjfork@gmail.com
 */
public class CustomStyleActivity extends Activity {


    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CustomStyleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_style);
        final CodeEditText et_test = (CodeEditText) findViewById(R.id.et_test);

        et_test.setOnTextChangedListener(new CodeEditText.OnTextChangedListener() {
            @Override
            public void onCodeChanged(CharSequence changeText) {
                Log.e(" onCodeChanged Text ", changeText + "");
            }

            @Override
            public void onInputCompleted(CharSequence text) {
                Log.e("onInputCompleted Text ", text + "");
            }
        });
        findViewById(R.id.btn_chang_block).setOnClickListener(v -> et_test.setBlockShape(new CustomeBlockDrawer()));
        findViewById(R.id.btn_chang_text).setOnClickListener(v -> et_test.setCodeInputType(new CustomTextDrawer()));
    }
}
