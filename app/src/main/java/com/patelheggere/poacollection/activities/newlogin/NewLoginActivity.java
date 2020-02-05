package com.patelheggere.poacollection.activities.newlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.patelheggere.poacollection.R;
import com.patelheggere.poacollection.activities.MapLocationActivity;
import com.patelheggere.poacollection.activities.PhoneAuthActivity;

import java.util.concurrent.TimeUnit;

public class NewLoginActivity extends AppCompatActivity {
    private static final String TAG = "NewLoginActivity";

    private Button sendOTP, resendOTP, verifyBtn;
    private TextInputEditText textInputEditTextName, inputEditTextPhone, inputEditTextOTP;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId, phoneNumber, otp;

    private TextInputLayout name, phone, otpfld;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        initViews();
        initData();
        initListeners();
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent =new Intent(NewLoginActivity.this, MapLocationActivity.class);
            intent.putExtra("Name", currentUser.getDisplayName().toString());
            intent.putExtra("mobile",currentUser.getPhoneNumber().substring(3, currentUser.getPhoneNumber().length()));
            intent.putExtra("uid",currentUser.getUid().toString() );
            startActivity(intent);
            finish();
        }
    }

    private void initListeners() {
        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhoneNumberVerification("+91" + inputEditTextPhone.getText().toString());
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(phoneNumber, mResendToken);
            }
        });
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = inputEditTextOTP.getText().toString();
                verifyPhoneNumberWithCode(mVerificationId, otp);
            }
        });
    }

    private void initViews() {
        textInputEditTextName = findViewById(R.id.et_name_login);
        inputEditTextOTP = findViewById(R.id.et_pwd_login);
        inputEditTextPhone = findViewById(R.id.et_phone_login);
        sendOTP = findViewById(R.id.btn_send_otp);
        resendOTP = findViewById(R.id.btn_resend);
        verifyBtn = findViewById(R.id.btn_verify_otp);
        name = findViewById(R.id.til_name_login);
        phone = findViewById(R.id.til_phone_login);
        otpfld = findViewById(R.id.til_pwd_login);
    }
    private void initData(){
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    inputEditTextPhone.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                   /* Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();*/
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
                otpfld.setVisibility(View.VISIBLE);
                name.setVisibility(View.GONE);
                sendOTP.setVisibility(View.GONE);
                phone.setVisibility(View.GONE);
                verifyBtn.setVisibility(View.VISIBLE);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser currentUser = task.getResult().getUser();
                            UserProfileChangeRequest update = new UserProfileChangeRequest.Builder().setDisplayName(textInputEditTextName.getText().toString()).build();
                            currentUser.updateProfile(update);
                            Intent intent =new Intent(NewLoginActivity.this, MapLocationActivity.class);
                            intent.putExtra("Name", textInputEditTextName.getText().toString());
                            intent.putExtra("mobile",currentUser.getPhoneNumber().substring(3, currentUser.getPhoneNumber().length()));
                            intent.putExtra("uid",currentUser.getUid().toString() );
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(NewLoginActivity.this, "Invalid Code", Toast.LENGTH_LONG).show();
                               // mVerificationField.setError("Invalid code.");
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = inputEditTextPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length()!=10) {
            inputEditTextPhone.setError("Invalid phone number.");
            return false;
        }
        return true;
    }
}
