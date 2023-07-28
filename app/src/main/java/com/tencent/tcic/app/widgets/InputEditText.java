package com.tencent.tcic.app.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import com.tencent.tcic.app.R;

/**
 * 功能描述：数据框
 *
 * @author eric
 */
public class InputEditText extends LinearLayout {

    private AppCompatTextView titleView;
    private AppCompatEditText inputView;
    private AppCompatTextView errorView;

    public InputEditText(Context context) {
        this(context, null);
    }

    public InputEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 文字输入框构造函数
     *
     * @param context 上下文
     * @param attrs attrs值
     * @param defStyleAttr style值
     */
    public InputEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.widget_input_text_layout, this, true);
        titleView = findViewById(R.id.title);
        inputView = findViewById(R.id.text);
        errorView = findViewById(R.id.error);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputEditText);

        String title = typedArray.getString(R.styleable.InputEditText_title);
        titleView.setText(title);

        String text = typedArray.getString(R.styleable.InputEditText_text);
        inputView.setText(text);

        int inputType = typedArray.getInt(R.styleable.InputEditText_android_inputType, -1);
        if (inputType != -1) {
            inputView.setInputType(inputType);
        }
        int maxLength = typedArray.getInt(R.styleable.InputEditText_android_maxLength, -1);
        if (maxLength != -1) {
            inputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        String hint = typedArray.getString(R.styleable.InputEditText_hint);
        inputView.setHint(hint);

        int drawableRightId = typedArray
                .getResourceId(R.styleable.InputEditText_android_drawableRight, -1);
        if (drawableRightId != -1) {
            Drawable drawableRight = ContextCompat.getDrawable(context, drawableRightId);
            inputView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawableRight,
                    null);
        }

        int drawableLeftId = typedArray
                .getResourceId(R.styleable.InputEditText_android_drawableLeft, -1);
        if (drawableLeftId != -1) {
            Drawable drawableLeft = ContextCompat.getDrawable(context, drawableLeftId);
            inputView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableLeft, null, null,
                    null);
        }

        boolean editable = typedArray.getBoolean(R.styleable.InputEditText_android_editable, true);
        if (!editable) {
            inputView.setInputType(InputType.TYPE_NULL);
        }
        typedArray.recycle();
    }

    public void setEnabled(boolean enabled) {
        inputView.setEnabled(enabled);
    }

    public Editable getText() {
        return inputView.getText();
    }

    public void setText(@StringRes int resId) {
        inputView.setText(resId);
    }

    public void setText(String text) {
        inputView.setText(text);
    }

    @Override
    public void setBackgroundResource(int resid) {
        inputView.setBackgroundResource(resid);
    }

    public void setTitle(@StringRes int resId) {
        titleView.setText(resId);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setHint(@StringRes int resId) {
        inputView.setHint(resId);
    }

    public void setHint(String text) {
        inputView.setText(text);
    }

    public void setError(@StringRes int resId) {
        errorView.setText(resId);
    }

    public void setError(String error) {
        if (TextUtils.isEmpty(error)) {
            errorView.setVisibility(INVISIBLE);
        } else {
            errorView.setVisibility(VISIBLE);
        }
        errorView.setText(error);
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        inputView.setOnEditorActionListener(listener);
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        inputView.setOnClickListener(listener);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        inputView.addTextChangedListener(watcher);
    }
}
