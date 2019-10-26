package com.example.notificationtest;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExampleFragment extends Fragment {
    private static final int YES = 0;
    private static final int NO = 1;


    public ExampleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =
                inflater.inflate(R.layout.fragment_example,container,false);
        final RadioGroup radioGroup = rootView.findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);

                TextView textView = rootView.findViewById(R.id.fragment_header);

                switch (index){
                    case YES:
                        textView.setText(R.string.yes_message);
                        break;

                    case NO:
                        textView.setText(R.string.no_message);
                        break;

                    default://no choice made
                        //do nothing
                        break;
                }
            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_example, container, false);
    }

    public static ExampleFragment newInstance(){
        return  new ExampleFragment();
    }

}
