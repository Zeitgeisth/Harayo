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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SendJSON {

    public static final String URL = Constants.BASE_URL+"add_lost_item";
    RequestQueue queue;
    public String name, location, description, category, status,user;
    public ArrayList<String> imagePaths;
    public Context context;
    HashMap<String, String> JsonData = new HashMap<>();
    final ArrayList<HashMap> list = new ArrayList<>();

    public ArrayList<HashMap> getList(){
        return list;
    }

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

    void getJSON(final Context context, final VolleyCallback callback){
        final String url = Constants.BASE_URL+"get_lost_items";


        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++){
                                JSONObject object = response.getJSONObject(i);
                                String name  = object.getString("name");
                                String location = object.getString("location");
                                String description = object.getString("description");
                                String category = object.getString("catagory");
                                String images = object.getString("image");
                                String user = object.getString("user");
                                String status = object.getString("status");
                                JsonData = new HashMap<>();
                                JsonData.put("name", name);
                                JsonData.put("location", location);
                                JsonData.put("description", description);
                                JsonData.put("category", category);
                                JsonData.put("images", images);
                                JsonData.put("user", user);
                                JsonData.put("status", status);

                                list.add(JsonData);
                            }
                            callback.onSuccess(true);


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
        void onSuccess(Boolean success);
    }




    public void showProgressDialog(){

    }

    public void hideProgressDialog(){

    }
}
