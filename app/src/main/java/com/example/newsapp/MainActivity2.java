package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.FileInputStream;

public class MainActivity2 extends AppCompatActivity {

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);


        String[] files = getBaseContext().fileList();
        int filesSize = files.length;
        int most_recent_number;

        if (filesSize == 1){

            most_recent_number = 0;
        }
        else{
            most_recent_number = filesSize/3 ;
        }


        text1.setText(GetFromInternal("title_" + Integer.toString(most_recent_number-1)));
        text2.setText(GetFromInternal("title_" + Integer.toString(most_recent_number-2)));
        text3.setText(GetFromInternal("title_" + Integer.toString(most_recent_number-3)));
        text4.setText(GetFromInternal("title_" + Integer.toString(most_recent_number-4)));
        text5.setText(GetFromInternal("title_" + Integer.toString(most_recent_number-5)));




    }

    public String GetFromInternal(String filename){

        String temp= "";
        try {

            FileInputStream fin = openFileInput(filename);
            int c;

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }

        }
        catch(Exception e){
        }

        return temp;

    }
}