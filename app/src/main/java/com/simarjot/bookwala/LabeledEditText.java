package com.simarjot.bookwala;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class LabeledEditText extends LinearLayout {
    private boolean hasErros;
    private String mHint;
    private int mLines;
    private int mDefaultBackground;
    private int mErrorBackground;

    //widgets
    private EditText mMainET;
    private TextView mLabelTV;
    private LinearLayout mLinearLayout;

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
        typedArray.recycle();
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.labeled_edit_text, this);

        mDefaultBackground = R.drawable.rounded_edit_text_white;
        mErrorBackground = R.drawable.background_error;

        mMainET = this.findViewById(R.id.main_et);
        mLabelTV = this.findViewById(R.id.label_tv);
        mLinearLayout = this.findViewById(R.id.parent);

        mMainET.setHint(mHint);
        mMainET.setLines(mLines);
        mMainET.setMaxLines(mLines);

        mLabelTV.setText(mHint);
        mLabelTV.setVisibility(View.INVISIBLE);
    }

    private void addTextChangedListener(){
        mMainET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    mLinearLayout.setBackgroundResource(mErrorBackground);
                    mLabelTV.setVisibility(View.INVISIBLE);
                    hasErros = true;
                }else{
                    mLinearLayout.setBackgroundResource(mDefaultBackground);
                    mLabelTV.setVisibility(View.VISIBLE);
                    hasErros = true;
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

    public boolean hasErrors(){
        return hasErros;
    }

    public String getText(){
        return mMainET.getText().toString();
    }
}
