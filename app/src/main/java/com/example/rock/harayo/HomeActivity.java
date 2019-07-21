package com.example.rock.harayo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    Button lost;
    //    Button found;
    RecyclerView itemsView;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        itemsView = findViewById(R.id.Items);
        lost = findViewById(R.id.LostItem);
//        found = findViewById(R.id.FoundItem);
        lost.setOnClickListener(this);
//        found.setOnClickListener(this);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        itemsView.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(new ArrayList<Item>(), getApplicationContext());
        itemsView.setAdapter(adapter);
        getItems();

        Intent serviceIntent = new Intent(getApplicationContext(), ourService.class);
        startService(serviceIntent);
    }

    public void getItems() {
        final SendJSON sendJSON = new SendJSON(this);
        SendJSON.VolleyCallback callback = new SendJSON.VolleyCallback() {
            @Override
            public void onSuccess(Boolean success) {
                if (success) {
                    ArrayList<HashMap> lists = sendJSON.getList();
                    ArrayList<Item> items = new ArrayList<>();
                    for (HashMap map : lists) {
                        Item item = new Item();
                        item.name = map.get("name").toString();
                        item.call = map.get("location").toString();
                        item.status = map.get("status").toString();
                        item.imageUrl = map.get("images").toString();
//                        item.category = map.get("catagory").toString();
                        item.location = map.get("location").toString();
                        items.add(item);
                        Log.i("myLog", item.name);
                    }
                    Log.i("myLog", String.valueOf(items.size()));
                    Log.i("myLog", items.toString());
                    Toast.makeText(getApplicationContext(), String.valueOf(items.size()), Toast.LENGTH_LONG).show();


                    adapter = new ItemAdapter(items, getApplicationContext());
                    itemsView.swapAdapter(adapter, false);

                }
            }
        };
        sendJSON.getJSON(getApplicationContext(), callback);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.LostItem) {
            Intent intent = new Intent(getApplicationContext(), LostFoundActivity.class);
            startActivity(intent);

        }
//        else if (view.getId() == R.id.FoundItem) {

//        }
    }
}
