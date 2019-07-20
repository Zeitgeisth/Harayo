package com.example.rock.harayo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    Button lost, found;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lost = findViewById(R.id.LostItem);
        found = findViewById(R.id.FoundItem);
        lost.setOnClickListener(this);
        found.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.LostItem){
            Intent intent = new Intent(getApplicationContext(), LostFoundActivity.class);
            startActivity(intent);

        }else if(view.getId() == R.id.FoundItem){
            SendJSON sendJSON = new SendJSON(this);
            sendJSON.getJSON(new SendJSON.VolleyCallback() {
                @Override
                public void onSuccess(HashMap<String, String> result) {
                    Log.i("myLog", result.toString());
                }
            });
        }
    }
}
