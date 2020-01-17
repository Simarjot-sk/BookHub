package com.simarjot.bookwala;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.gson.Gson;
import com.simarjot.bookwala.helpers.Helper;
import com.simarjot.bookwala.model.Book;
import com.simarjot.bookwala.model.UserRecieved;
import com.simarjot.bookwala.ui.ImageSliderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
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
    private ImageView mSellerPhotoIV;
    private TextView mSellerNameTV;

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
        getSellerInfo();
    }

    private void getSellerInfo() {
        Log.d(Helper.TAG, "called get seller info");
        RequestQueue queue = Volley.newRequestQueue(this);
        Uri uri = Uri.parse("https://us-central1-bookwala-86dc9.cloudfunctions.net");
        String uriString = uri.buildUpon()
                .appendPath("user")
                .appendQueryParameter("uid", book.getUserUUID()).build().toString();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
//                response -> {
//                    try {
//                        Glide.with(this).load(response.getString("photoUrl")).into(mSellerPhotoIV);
//                        mSellerNameTV.setText(response.getString("displayName"));
//                    } catch (JSONException e) {
//                        Log.d(Helper.TAG, "error while parsing json", e);
//                    }
//                },
//                error -> Log.d(Helper.TAG, error.getMessage()));
        Log.d(Helper.TAG, uriString);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uriString,
                response -> {
                    Gson gson = new Gson();
                    UserRecieved seller = gson.fromJson(response, UserRecieved.class);
                    Glide.with(this).load(seller.getPhotoUrl()).into(mSellerPhotoIV);
                        mSellerNameTV.setText(seller.getDisplayName());
                },
                error -> Log.d(Helper.TAG, error.getMessage(), error));
        queue.add(stringRequest);
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
        mSellerNameTV = findViewById(R.id.seller_name_tv);
        mSellerPhotoIV = findViewById(R.id.seller_image_view);
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
