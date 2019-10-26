package com.example.notificationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FingerprintActivity extends AppCompatActivity {
    private static final String TAG = FingerprintActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fingerprint);

        //create a thread pool with a single thread
        Executor newExecutor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

        //start listening for authentication events//
        final BiometricPrompt myBiometricPrompt = new BiometricPrompt(activity,newExecutor,
                new BiometricPrompt.AuthenticationCallback(){

                    public void onAuthenticationError(int errorCode, CharSequence errString){

                super.onAuthenticationError(errorCode, errString);
                if(errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON){

                }else{
                    Log.d(TAG, "An unrecoverable error has occured");
                }

            }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Log.d(TAG, "Fingerprint recognized successfully");

                        //TODO

                        onSuccess();

                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Log.d(TAG, "Fingerprint not recoganized");
                    }
                });

        final BiometricPrompt.PromptInfo promptInfo = new
                BiometricPrompt.PromptInfo.Builder()

                //adding text to the actual instance
                .setTitle("Finger Print Login")
                .setSubtitle("Please touch your finger")
                .setDescription("Secured LogIn")
                .setNegativeButtonText("Cancel")
                //build the dialog
                .build();

        //assign an onClicklistener to the app's "authenticate button"

        findViewById(R.id.fingerprint_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBiometricPrompt.authenticate(promptInfo);
            }
        });


    }

    public void onSuccess(){
        this.startActivity(new Intent(this, LocationTracker.class));
    }
}
