package com.bkav.demochat;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestToServer {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private Activity mActivity;

    public RequestToServer(Activity activity) {
        this.mActivity = activity;
    }

    public void post(String url, String s, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tiennvh", "abc");
            jsonObject.put("abc", "nbc");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(JSON, gson.toJson(jsonObject));
        Log.d("TienNAb", "post: "+gson.toJson(jsonObject));
        Request request = new Request.Builder()
                .url(url)
                .method("POST", requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

}
