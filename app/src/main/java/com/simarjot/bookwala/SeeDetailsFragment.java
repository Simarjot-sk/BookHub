package com.simarjot.bookwala;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.simarjot.bookwala.helpers.Helper;
import com.simarjot.bookwala.model.Book;
import com.simarjot.bookwala.model.UserRecieved;
import com.simarjot.bookwala.ui.AllChatsFragment;
import com.simarjot.bookwala.ui.ImageSliderAdapter;

import java.util.List;
import java.util.Objects;

public class SeeDetailsFragment extends Fragment {
    private Book book;
    private int currentImageIndex = 0;
    private List<String> allImageUris;
    //widgets
    private MapView mapView;
    private ViewPager mViewPager;
    private TextView mAuthorTV, mSubjectTV, mDescTV, mTitleTV, mPriceTV, mSellerNameTV;
    private Button mNextBtn, mPrevBtn, mChatBtn;
    private ImageButton mBackBtn;
    private ImageView mSellerPhotoIV;
    private View mRootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = inflater.inflate(R.layout.activity_see_details, container, false);
        initViews();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(googleMap -> {
            if (book.getPostedIn() != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(book.getPostedIn().getLatitude(), book.getPostedIn().getLongitude())));
                googleMap.setMinZoomPreference(10);
            }
        });
        SeeDetailsFragmentArgs args = SeeDetailsFragmentArgs.fromBundle(Objects.requireNonNull(getArguments()));
        String json = args.getBookJson();
        book = Book.parse(json);

        getSellerInfo();
        setValues();
        configureViewPager();

        mChatBtn.setOnClickListener(v -> {
            SeeDetailsFragmentDirections.ActionSeeDetailsFragmentToMessagingActivity action =
                    SeeDetailsFragmentDirections.actionSeeDetailsFragmentToMessagingActivity(book.getUserUUID());
            Navigation.findNavController(v).navigate(action);
        });
        return mRootView;
    }

    private void configureViewPager() {
        allImageUris = book.getImageDownloadUris();
        allImageUris.add(0, book.getCoverDownloadUri());
        mViewPager.setAdapter(new ImageSliderAdapter(getActivity(), allImageUris));

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
    }

    private void updateButtonVisibility() {
        //checking if currentImageIndex represents the last item
        if (currentImageIndex == allImageUris.size() - 1) {
            mNextBtn.setVisibility(View.INVISIBLE);
        } else {
            mNextBtn.setVisibility(View.VISIBLE);
        }

        //if currentImageIndex represents the first image
        if (currentImageIndex == 0) {
            mPrevBtn.setVisibility(View.INVISIBLE);
        } else {
            mPrevBtn.setVisibility(View.VISIBLE);
        }
    }

    private void getSellerInfo() {
        Log.d(Helper.TAG, "called get seller info");
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
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

    private void initViews() {
        mapView = mRootView.findViewById(R.id.mapView);
        mDescTV = mRootView.findViewById(R.id.desc_tv);
        mAuthorTV = mRootView.findViewById(R.id.author_tv);
        mPriceTV = mRootView.findViewById(R.id.price_tv);
        mSubjectTV = mRootView.findViewById(R.id.subject_tv);
        mTitleTV = mRootView.findViewById(R.id.title_tv);
        mPrevBtn = mRootView.findViewById(R.id.prev_btn);
        mNextBtn = mRootView.findViewById(R.id.next_btn);
        mBackBtn = mRootView.findViewById(R.id.back_button);
        mViewPager = mRootView.findViewById(R.id.image_slider);
        mSellerNameTV = mRootView.findViewById(R.id.seller_name_tv);
        mSellerPhotoIV = mRootView.findViewById(R.id.seller_image_view);
        mChatBtn = mRootView.findViewById(R.id.chat_btn);
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
