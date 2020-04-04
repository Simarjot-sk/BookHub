package com.simarjot.bookwala;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.GeoPoint;
import com.simarjot.bookwala.databinding.FragmentDetails2Binding;
import com.simarjot.bookwala.helpers.Helper;
import com.simarjot.bookwala.model.Book;
import com.simarjot.bookwala.model.BookSharedPrefs;
import com.simarjot.bookwala.model.Server;

import java.util.Arrays;
import java.util.List;

public class FragmentDetails2 extends Fragment {
    private static final int LOCATION_REQUEST = 114;
    private FragmentDetails2Binding mBinding;
    private Context mContext;

    private Location currentLocation;
    /////////////


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details2, container, false);
        mContext = getActivity();

        mBinding.progressBar.setVisibility(View.GONE);

        List<String> currencySymbols = Arrays.asList("\u20B9", "US$", "PKR", "AU$", "CA$", "BDT", "NPR");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, currencySymbols);
        mBinding.currencySelectorSpinner.setAdapter(spinnerAdapter);

        mBinding.doneBtn.setOnClickListener(v -> {
            mBinding.doneBtn.setClickable(false);
            Log.d(Helper.TAG, "done button clicked");
            String desc = mBinding.descriptionEt.getText().toString();
            String price = mBinding.priceEt.getText().toString();
            String currency = (String) mBinding.currencySelectorSpinner.getSelectedItem();

            getCurrentLocation();

            if(!desc.isEmpty() && !price.isEmpty() && currency!=null ){
                SharedPreferences.Editor editor= mContext.getSharedPreferences(BookSharedPrefs.SHARED_PREFS, Context.MODE_PRIVATE).edit();
                editor.putString(BookSharedPrefs.DESCRIPTION, desc);
                editor.putString(BookSharedPrefs.PRICE, price);
                editor.putString(BookSharedPrefs.CURRENCY, currency);
                editor.apply();

                Book book = BookSharedPrefs.getBook(mContext);
                book.setPostedIn(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
                Server server = new Server(book);
                mBinding.progressBar.setVisibility(View.VISIBLE);

                server.uploadBookWithImages(BookSharedPrefs.getImageUris(mContext),
                        BookSharedPrefs.getCoverImageUri(mContext),
                        book.getBookUUID())
                        .setUploadFinishedListener(new Server.OnUploadFinishedListener() {
                            @Override
                            public void onUploadFinish() {
                                mBinding.progressBar.setVisibility(View.GONE);
                                mBinding.doneBtn.setClickable(true);
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onUploadFailed() {
                                mBinding.progressBar.setVisibility(View.GONE);
                                mBinding.doneBtn.setClickable(true);
                            }
                        });
            }else{
                Toast.makeText(mContext, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
        return mBinding.getRoot();
    }

    private void getCurrentLocation(){
        if(getLocationPermission()){
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, false);
            currentLocation = locationManager.getLastKnownLocation(bestProvider);
        }
    }

    private boolean getLocationPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if(mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_REQUEST){
            for(int i=0;i<permissions.length;i++){
                if(permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        getCurrentLocation();
                    }else{
                        Toast.makeText(mContext, "Cannot perform operation without location Permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
