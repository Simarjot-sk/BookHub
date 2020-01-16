package com.simarjot.bookwala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.makeramen.roundedimageview.RoundedImageView;
import com.simarjot.bookwala.model.Book;
import com.simarjot.bookwala.ui.ImageSliderAdapter;

import java.util.List;

public class SeeDetailsActivity extends AppCompatActivity {
    public static final String BOOK_EXTRA = "book_extra";
    private Book book;
    private int currentImageIndex = 0;
    private List<String> allImageUris;
    //widgets
    private MapView mapView;
    private ViewPager mViewPager;
    private TextView mAuthorTV;
    private TextView mSubjectTV;
    private TextView mDescTV;
    private TextView mTitleTV;
    private TextView mPriceTV;
    private Button mNextBtn;
    private Button mPrevBtn;
    private ImageButton mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_details);
        initViews();

        String json = getIntent().getStringExtra(BOOK_EXTRA);
        book = Book.parse(json);
        setValues();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(googleMap -> {
                    if (book.getPostedIn() != null) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(book.getPostedIn().getLatitude(), book.getPostedIn().getLongitude())));
                        googleMap.setMinZoomPreference(15);
                    }
                }
        );

        mViewPager = findViewById(R.id.image_slider);
        allImageUris = book.getImageDownloadUris();
        allImageUris.add(0, book.getCoverDownloadUri());
        mViewPager.setAdapter(new ImageSliderAdapter(this, allImageUris));

        if(allImageUris.size()==1){
            mPrevBtn.setVisibility(View.INVISIBLE);
            mNextBtn.setVisibility(View.INVISIBLE);
        }

        mNextBtn.setOnClickListener(v -> {
            currentImageIndex++;
            mViewPager.setCurrentItem(currentImageIndex);
            //updateButtonVisibility();
        });

        mPrevBtn.setOnClickListener(v -> {
            currentImageIndex--;
            mViewPager.setCurrentItem(currentImageIndex);
            //updateButtonVisibility();
        });

        mBackBtn.setOnClickListener(v -> finish());
    }

    private void updateButtonVisibility(){
        //checking if currentImageIndex represents the last item
        if(currentImageIndex == allImageUris.size()-1){
            mNextBtn.setVisibility(View.INVISIBLE);
        }else{
            mNextBtn.setVisibility(View.VISIBLE);
        }

        //if currentImageIndex represents the first image
        if(currentImageIndex == 0){
            mPrevBtn.setVisibility(View.INVISIBLE);
        }else{
            mPrevBtn.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        mapView = findViewById(R.id.mapView);
        mDescTV = findViewById(R.id.desc_tv);
        mAuthorTV = findViewById(R.id.author_tv);
        mPriceTV = findViewById(R.id.price_tv);
        mSubjectTV = findViewById(R.id.subject_tv);
        mTitleTV = findViewById(R.id.title_tv);
        mPrevBtn = findViewById(R.id.prev_btn);
        mNextBtn = findViewById(R.id.next_btn);
        mBackBtn = findViewById(R.id.back_button);
    }

    private void setValues() {
        mAuthorTV.setText(book.getAuthor());
        mTitleTV.setText(book.getTitle());
        mPriceTV.setText(book.priceWithCurrency());
        mSubjectTV.setText(book.getSubject());
        mDescTV.setText(book.getDescription());
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
