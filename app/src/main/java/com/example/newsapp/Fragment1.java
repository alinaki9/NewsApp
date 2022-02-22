package com.example.newsapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


public class Fragment1 extends Fragment implements View.OnClickListener{

    private TextView headline;
    private TextView body_view;
    private ImageView image;
    private View rootView;
    BroadcastReceiver updateUIReciver;

    public Fragment1() {
        super(R.layout.fragment_1);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mSharedPreference= getActivity().getSharedPreferences("YO", Context.MODE_PRIVATE);
        String listen1 = mSharedPreference.getString("title", "");

        IntentFilter filter = new IntentFilter();

        filter.addAction("com.hello.action");

        updateUIReciver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //UI update here
                SharedPreferences mSharedPreference= getContext().getSharedPreferences("YO", Context.MODE_PRIVATE);

                String title = mSharedPreference.getString("title", "");
                String body = mSharedPreference.getString("body", "");
                String image_url = mSharedPreference.getString("image-url", "");

                headline = getView().findViewById(R.id.headline1);
                headline.setText(title);

                body_view = getView().findViewById(R.id.title1);
                body_view.setText(body);

                image = getView().findViewById(R.id.imageView);

                Picasso.with(getContext())
                        .load(image_url)
                        .into(image);


            }
        };
        getContext().registerReceiver(updateUIReciver,filter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_1, container, false);
        headline = rootView.findViewById(R.id.headline1);
        body_view = rootView.findViewById(R.id.title1);
        image = rootView.findViewById(R.id.imageView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onClick(View view) {


    }


}