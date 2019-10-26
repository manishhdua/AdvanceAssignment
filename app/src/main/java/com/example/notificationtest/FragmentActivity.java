package com.example.notificationtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FragmentActivity extends AppCompatActivity {
    private Button mButton;
    private boolean isFragmentDisplayed = false;

    final static String STATE_FRAGEMENT = "state_of_fragment";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mButton = findViewById(R.id.open_button);
        if(savedInstanceState!=null){
            isFragmentDisplayed = savedInstanceState.getBoolean(STATE_FRAGEMENT);
            if(isFragmentDisplayed){
                mButton.setText(R.string.close);
            }
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFragmentDisplayed){
                    displayFragment();
                }
                else{
                    closeFragment();
                }
            }
        });
    }

    public void displayFragment() {
        ExampleFragment exampleFragment
                = ExampleFragment.newInstance();


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction
                . add(R.id.fragment_container, exampleFragment)
                .addToBackStack(null)
                .commit();

        mButton.setText(R.string.close);

        isFragmentDisplayed = true;
    }

    public  void closeFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ExampleFragment exampleFragment = (ExampleFragment) fragmentManager.findFragmentById(R.id.fragment_container);

        if(exampleFragment != null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(exampleFragment).commit();


        }
        mButton.setText(R.string.open);

        isFragmentDisplayed =false;


    }


    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(STATE_FRAGEMENT,isFragmentDisplayed);

        super.onSaveInstanceState(savedInstanceState);
    }


}
