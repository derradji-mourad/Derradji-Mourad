package com.example.projetdememoire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import AppTracker.DataBaseHelperAll;
import AppTracker.UsageState;



public class MainActivity extends AppCompatActivity {
    ImageButton button;
    ImageButton button2;
    ImageButton button3;
    DataBaseHelperAll db ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    button = findViewById(R.id.Button);
    button2 = findViewById(R.id.Button2);
    button3=findViewById(R.id.Button3);


    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,Notification.class);
            startActivity(intent);
        }
    });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UsageState.class));

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, DisplaytackedApp.class));
            }
        });

    }

}