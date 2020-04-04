package com.simarjot.bookwala;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.simarjot.bookwala.databinding.FragmentOtpBinding;
import com.simarjot.bookwala.helpers.Helper;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;

public class OtpFragment extends Fragment {
    private String mobileNo;
    private String formattedMobileNo;
    private FirebaseAuth mAuth;
    private String verificationCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String otp;
    private FragmentOtpBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp, container, false);

        OtpFragmentArgs args = OtpFragmentArgs.fromBundle(getArguments());
        mobileNo = args.getMobileNo();
        formattedMobileNo = args.getMobileNoFormatted();

        binding.otpView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            @Override
            public void onOTPComplete(String otp) {
                OtpFragment.this.otp = otp;
            }
        });


        startFirebaseLogin();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNo,           // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback mBinding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


        binding.submitBtn.setOnClickListener(v -> {
            if (otp == null || otp.length() < 6) {
                Toast.makeText(getContext(), "please enter the OTP", Toast.LENGTH_SHORT).show();
            } else {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                Log.d("nerd", credential.getSmsCode());
                signInWithPhone(credential);
            }
        });

        return binding.getRoot();
    }


    private void startFirebaseLogin() {

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getContext(), "verification failed", Toast.LENGTH_SHORT).show();
                Log.d(Helper.TAG, "verification failed", e);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                binding.mobileTv.setText("OTP has been sent to you on " + formattedMobileNo);
                Toast.makeText(getContext(), "Code sent", Toast.LENGTH_SHORT).show();
                Log.d(Helper.TAG, verificationCode);
            }
        };
    }

    private void signInWithPhone(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    Activity homeActivity = getActivity();
                    assert homeActivity != null;
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host);

                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "otp is correct", Toast.LENGTH_SHORT).show();
                        boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                        if (isNewUser) {
                            navController.navigate(OtpFragmentDirections.actionOtpFragmentToRegistrationFragment());
                        }else{
                            navController.navigate(OtpFragmentDirections.actionOtpFragmentToDiscoverMenu());
                        }
                    } else {
                        Toast.makeText(getContext(), "Incorrect OTP", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Log.d(Helper.TAG, "verification failed", e));
    }
}
