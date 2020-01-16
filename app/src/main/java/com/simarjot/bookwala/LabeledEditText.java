package com.simarjot.bookwala;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.simarjot.bookwala.helpers.Helper;

public class LabeledEditText extends LinearLayout {
    private static int DEFAULT_BACKGROUND;
    private static int ERROR_BACKGROUND;
    private String mHint;
    private boolean hasErrors;
    private int mLines;
    private int mDrawableLeftId;
    private int mInputType;
    //widgets
    private EditText mMainET;
    private TextView mLabelTV;
    private ConstraintLayout mLinearLayout;
    private ImageView mDrawableLeft;

    public LabeledEditText(Context context) {
        super(context);
        init(context);
    }

    public LabeledEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttributes(context, attrs);
        init(context);
    }

    public LabeledEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributes(context, attrs);
        init(context);
    }

    private void getAttributes(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabeledEditText);
        mHint = typedArray.getString(R.styleable.LabeledEditText_hint);

        mLines = typedArray.getInt(R.styleable.LabeledEditText_lines, 1);
        mInputType = typedArray.getInt(R.styleable.LabeledEditText_android_inputType, EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES);
        mInputType--;
        mDrawableLeftId = typedArray.getResourceId(R.styleable.LabeledEditText_drawableLeft, -1);
        typedArray.recycle();
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.labeled_edit_text, this);
        view.setOnClickListener(v -> requestFocus());
        DEFAULT_BACKGROUND = R.drawable.rounded_edit_text_grey;
        ERROR_BACKGROUND = R.drawable.background_error;

        mMainET = this.findViewById(R.id.main_et);
        mLabelTV = this.findViewById(R.id.label_tv);
        mLinearLayout = this.findViewById(R.id.parent);
        mDrawableLeft = this.findViewById(R.id.drawable_left);

        mMainET.setHint(mHint);
        mMainET.setLines(mLines);
        mMainET.setMaxLines(mLines);
        mMainET.setInputType(mInputType);
        if (mDrawableLeftId==-1){
            mDrawableLeft.setVisibility(View.GONE);
        }else {
            mDrawableLeft.setImageResource(mDrawableLeftId);
        }
        mLabelTV.setText(mHint);
        mLabelTV.setVisibility(View.INVISIBLE);

        Log.d(Helper.TAG, mInputType+  ", " + EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    private void addTextChangedListener(){
        mMainET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    mLinearLayout.setBackgroundResource(ERROR_BACKGROUND);
                    mLabelTV.setVisibility(View.INVISIBLE);
                    hasErrors = true;
                }else{
                    mLinearLayout.setBackgroundResource(DEFAULT_BACKGROUND);
                    mLabelTV.setVisibility(View.VISIBLE);
                    hasErrors = false;
                }
                switch (mInputType){
                    case InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS:
                        if (Helper.isValidEmail(s)){
                            mLinearLayout.setBackgroundResource(DEFAULT_BACKGROUND);
                            hasErrors = false;
                        }else {
                            mLinearLayout.setBackgroundResource(ERROR_BACKGROUND);
                            hasErrors = true;
                        }
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addTextChangedListener();
    }

    public void setHint(String hint){
        mHint = hint;
        mMainET.setHint(mHint);
        mLabelTV.setText(mHint);
    }

    public boolean hasErrors(){
        return hasErrors;
    }

    public String getText(){
        return mMainET.getText().toString();
    }
}
