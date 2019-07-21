package com.example.rock.harayo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LostFoundActivity extends AppCompatActivity{

    // class variables
    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();
    EditText productName, Description;
    Button selectImages, submit,mapBtn;
    final int ImageCode = 100;
    TextView tvResults;
    RecyclerView imagesList;
    SendJSON sendJSON;
    ArrayList<String> imagePaths = new ArrayList<>();
    String lat="", lng="";


    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);



        selectImages = findViewById(R.id.SelectImages);
        productName = findViewById(R.id.NameProduct);
        Description = findViewById(R.id.Description);

        submit = findViewById(R.id.Submit);

        mapBtn = findViewById(R.id.mapBtn);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        imagesList = (RecyclerView) findViewById(R.id.imagesList);
        imagesList.setLayoutManager(layoutManager);
        ImagesAdapter adapter = new ImagesAdapter(new ArrayList<String>(), getApplicationContext());
        imagesList.setAdapter(adapter);
        selectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start multiple photos selector
                Intent intent = new Intent(LostFoundActivity.this, ImagesSelectorActivity.class);
                // max number of images to be selected
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
                // min size of image which will be shown; to filter tiny images (mainly icons)
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                // show camera or not
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                // pass current selected images as the initial value
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                // start the selector
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult((new Intent(getApplicationContext(), MapsActivity.class)),SECOND_ACTIVITY_REQUEST_CODE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String proName = productName.getText().toString();
                String descrip = Description.getText().toString();
                String latitude = "27.700769";
                String longitude = "85.300140";
                String category = "Lost";

            sendJSON = new SendJSON(getApplicationContext(), proName, longitude+","+latitude, descrip, category, imagePaths);
            sendJSON.sendRequest();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;
                for (String result : mResults) {
                    imagePaths.add(result);

                }
                ImagesAdapter adapter = new ImagesAdapter(imagePaths, getApplicationContext());
                imagesList.swapAdapter(adapter, false);
//                tvResults.setText(sb.toString());
            }
        }

        // Check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // Get String data from Intent

                lat = data.getStringExtra("lat");
                lng = data.getStringExtra("long");
                // Set text view with string
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
