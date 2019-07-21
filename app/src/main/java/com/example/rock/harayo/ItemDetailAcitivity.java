package com.example.rock.harayo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ItemDetailAcitivity extends AppCompatActivity {
    String name, description, image, category, location;
    TextView nameTxt ,desTxt, catTxt, locTxt;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail_acitivity);

        nameTxt = findViewById(R.id.detail_name);
        desTxt = findViewById(R.id.detail_description);
        catTxt = findViewById(R.id.detail_category);
        locTxt = findViewById(R.id.detail_location);
        imgView = findViewById(R.id.detail_image);

        name = this.getIntent().getStringExtra("name");
        description = this.getIntent().getStringExtra("description");
        image = this.getIntent().getStringExtra("image");
        location = this.getIntent().getStringExtra("location");
        category = this.getIntent().getStringExtra("category");


        nameTxt.setText(name);
        desTxt.setText(description);
        locTxt.setText(location);
        catTxt.setText(category);
        Glide.with(this).load(image).into(imgView);
    }
}
