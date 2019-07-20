package com.example.rock.harayo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
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

    public static final String URL = "http://10.16.1.167:5000/add_lost_item";
    RequestQueue queue;
    public String name, location, description, category, status,user;
    public ArrayList<String> imagePaths;
    public Context context;
    HashMap<String, String> JsonData = new HashMap<>();

    public SendJSON(Context context){

        this.context = context;
        queue = Volley.newRequestQueue(this.context);

    }
    public SendJSON(Context context, String name, String location, String description, String category, ArrayList<String> imagePaths) {

        this.name = name;
        this.location = location;
        this.description = description;
        this.category = category;
        this.imagePaths = imagePaths;
        this.status = "1";
        this.user = "1";
        this.context = context;
        queue = Volley.newRequestQueue(this.context);
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
            String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
            encodedImages.add(encodedImage);
            break;
        }
        postParam.put("images", encodedImages.get(0).toString());
        postParam.put("user", "1");
        postParam.put("status", this.status);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URL
                , new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                        Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);
    }

    void getJSON(final VolleyCallback callback){
        final String url = "http://10.16.1.167:5000/get_lost_items";


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name  = response.getString("name");
                            String location = response.getString("location");
                            String description = response.getString("description");
                            String category = response.getString("category");
                            String images = response.getString("images");
                            String user = response.getString("user");
                            String status = response.getString("status");
                            JsonData = new HashMap<>();
                            JsonData.put("name", name);
                            JsonData.put("location", location);
                            JsonData.put("description", description);
                            JsonData.put("category", category);
                            JsonData.put("images", images);
                            JsonData.put("user", user);
                            JsonData.put("status", status);
                            callback.onSuccess(JsonData);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error+"");
                    }
                }
        );


        queue.add(getRequest);


    }

    public interface VolleyCallback{
        void onSuccess(HashMap<String,String> result);
    }




    public void showProgressDialog(){

    }

    public void hideProgressDialog(){

    }
}
