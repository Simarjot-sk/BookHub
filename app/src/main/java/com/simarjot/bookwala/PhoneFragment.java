package com.simarjot.bookwala;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.hbb20.CountryCodePicker;
import com.simarjot.bookwala.databinding.FragmentEnterPhoneNumberBinding;

public class PhoneFragment extends Fragment {
    public static final int OTP_ACTIVITY = 451;
    public static String MOBILE_EXTRA = "mobile_extra";
    public static String MOBILE_EXTRA_FORMATTED = "mobile_formatted";
    public static String TAG = "nerd";
    FragmentEnterPhoneNumberBinding binding;
    private boolean isValid = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enter_phone_number, container, false);

        binding.tickMark.setVisibility(View.GONE);
        binding.ccp.registerCarrierNumberEditText(binding.mobileEditText);
        binding.ccp.setPhoneNumberValidityChangeListener(isValidNumber -> {
            isValid = isValidNumber;
            if (isValid) {
                binding.tickMark.setVisibility(View.VISIBLE);
            } else {
                binding.tickMark.setVisibility(View.GONE);
            }
        });

        binding.submitBtn.setOnClickListener(v -> {
            if (isValid) {
                String mobileNo = binding.ccp.getFullNumberWithPlus().trim();
                String formattedMobileNo = binding.ccp.getFormattedFullNumber().trim();
                PhoneFragmentDirections.ActionEnterPhoneNumberFragmentToOtpFragment action =
                        PhoneFragmentDirections.actionEnterPhoneNumberFragmentToOtpFragment(mobileNo, formattedMobileNo);
                Navigation.findNavController(v).navigate(action);
            } else {
                Toast.makeText(getContext(), "Please Enter a Valid Mobile Number", Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }
}
