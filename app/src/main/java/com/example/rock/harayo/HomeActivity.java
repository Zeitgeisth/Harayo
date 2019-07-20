package com.example.rock.harayo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    Button lost, found;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lost = findViewById(R.id.LOST);
        found = findViewById(R.id.FOUND);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.LOST){

        }else if(view.getId() == R.id.FOUND){

        }
    }
}
