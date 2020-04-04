package com.simarjot.bookwala;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.simarjot.bookwala.databinding.FragmentDetailsBinding;
import com.simarjot.bookwala.model.BookSharedPrefs;
import com.simarjot.bookwala.model.Category;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class DetailsFragment extends Fragment {
    private SharedPreferences mSharedPrefs;
    private Category mCategory;
    private int mLevel = 1;
    private FragmentDetailsBinding mBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        mSharedPrefs = getActivity().getSharedPreferences(BookSharedPrefs.SHARED_PREFS, Context.MODE_PRIVATE);
        mCategory = Category.valueOf( mSharedPrefs.getString(BookSharedPrefs.CATEGORY,null) );

        switch(mCategory){
            case SCHOOL:
                mBinding.levelTypeTv.setText(R.string.class_msg);
                mBinding.levelSeekBar.setMax(12);
                mBinding.levelSeekBar.setTickCount(12);
                mBinding.categoryTv.setText(R.string.school);
                break;
            case COLLEGE:
                mBinding.levelTypeTv.setText(R.string.semester_msg);
                mBinding.levelSeekBar.setMax(8);
                mBinding.levelSeekBar.setTickCount(8);
                mBinding.categoryTv.setText(R.string.college);
                break;
            case COMPETITIVE:
                mBinding.levelSeekBar.setVisibility(View.GONE);
                mBinding.levelTypeTv.setVisibility(View.GONE);
                mBinding.subjectTv.setVisibility(View.GONE);
                mBinding.subjectTv.setHint("Exam Name");
                mBinding.categoryTv.setText(R.string.competitive);
                break;
            case OTHERS:
                mBinding.levelSeekBar.setVisibility(View.GONE);
                mBinding.levelTypeTv.setVisibility(View.GONE);
                mBinding.subjectTv.setVisibility(View.GONE);
                mBinding.subjectTv.setHint("Genre");
                mBinding.categoryTv.setText(R.string.others);
                break;
        }

        mBinding.levelSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                mLevel = seekBar.getProgress();

                if(mLevel==1){
                    mBinding.subjectTv.setText(mLevel + "st");
                }else if(mLevel==2){
                    mBinding.subjectTv.setText(mLevel + "nd");
                }else if(mLevel==3){
                    mBinding.subjectTv.setText(mLevel + "rd");
                }else if(mLevel>3 && mLevel<13){
                    mBinding.subjectTv.setText(mLevel + "th");
                }
            }
        });

        mBinding.doneBtn.setOnClickListener(v ->{
            String author = mBinding.authorEt.getText();
            String subject = mBinding.subjectEt.getText();
            String title = mBinding.titleEt.getText();
            if(author!=null && subject!=null && title!=null ){
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(BookSharedPrefs.SHARED_PREFS, Context.MODE_PRIVATE).edit();
                editor.putString(BookSharedPrefs.SUBJECT, subject);
                editor.putInt(BookSharedPrefs.LEVEL, mLevel);
                editor.putString(BookSharedPrefs.AUTHOR, author);
                editor.putString(BookSharedPrefs.TITLE, title);
                editor.apply();

                Navigation.findNavController(v).navigate(DetailsFragmentDirections.actionDetailsFragmentToFragmentDetails2());
            }else {
                Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences prefs = getContext().getSharedPreferences(BookSharedPrefs.SHARED_PREFS, Context.MODE_PRIVATE);
        String coverImageUrl = prefs.getString(BookSharedPrefs.COVER_IMAGE_URI, null);
        mBinding.coverImage.setImageURI(Uri.parse(coverImageUrl));

        return mBinding.getRoot();
    }
}
