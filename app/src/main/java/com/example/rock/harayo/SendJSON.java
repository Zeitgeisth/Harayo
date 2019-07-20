package com.example.rock.harayo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 7/20/2019.
 */

public class SendJSON {

    public static final String URL = "10.16.1.167:5000/add_lost_item";
    RequestQueue queue;
    public String name, location, description, category, status,user;
    public ArrayList<String> imagePaths;
    public SendJSON(Context context, String name, String location, String description, String category, ArrayList<String> imagePaths, String status, String user) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.category = category;
        this.imagePaths = imagePaths;
        this.status = status;
        this.user = user;
        queue = Volley.newRequestQueue(context);
    }

    public void sendRequest(){
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("name", name);
        postParam.put("location", location);
        postParam.put("description", description);
        postParam.put("category", category);
        ArrayList<String> encodedImages = new ArrayList<>();
        for (String imagePath: imagePaths) {
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            encodedImages.add(encodedImage);
        }
        postParam.put("images", encodedImages.toString());
        postParam.put("user", "1");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URL
                , new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        jsonObjReq.setTag("test");
        // Adding request to request queue
        queue.add(jsonObjReq);
    }

    public void showProgressDialog(){

    }

    public void hideProgressDialog(){

    }
}
