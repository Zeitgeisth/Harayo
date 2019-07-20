package com.example.rock.harayo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    Button lost, found;
    RecyclerView items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        items = findViewById(R.id.Items);
        lost = findViewById(R.id.LostItem);
        found = findViewById(R.id.FoundItem);
        lost.setOnClickListener(this);
        found.setOnClickListener(this);

        Intent serviceIntent = new Intent(getApplicationContext(), ourService.class);
        startService(serviceIntent);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.LostItem){
            Intent intent = new Intent(getApplicationContext(), LostFoundActivity.class);
            startActivity(intent);

        }else if(view.getId() == R.id.FoundItem){
            final SendJSON sendJSON = new SendJSON(this);
            SendJSON.VolleyCallback callback = new SendJSON.VolleyCallback() {
                @Override
                public void onSuccess(Boolean success) {
                    if(success){
                        Toast.makeText(getApplicationContext(), "true", Toast.LENGTH_LONG).show();
                        ArrayList<HashMap> lists = sendJSON.getList();
                        Toast.makeText(getApplicationContext(), lists.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            };
            sendJSON.getJSON(getApplicationContext(), callback);
        }
    }
}
