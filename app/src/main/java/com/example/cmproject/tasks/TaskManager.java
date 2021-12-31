package com.example.cmproject.tasks;



import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.cmproject.objects.Pharmacy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.security.auth.callback.Callback;


public class TaskManager {
    private Context context;

    public interface Callback {
        void onComplete(ArrayList<Pharmacy> pharmacies);
        //void onComplete(ArrayList<Note> notes);
        //void onComplete(Note note);
    }


    public TaskManager(Context cont) {
        this.context = cont;
    }

    final Executor executor = Executors.newSingleThreadExecutor();
    final Handler handler = new Handler(Looper.getMainLooper());


    public void executeGetAPIInfo(Callback callback) {
        String apiKey = "AIzaSyD_bG6vfjNnb881ADhRI9ge3cgYgo4YEUA";
        String testURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword=farmacia&location=40.19432221874864,-8.398034673512614&radius=1500&key=AIzaSyD_bG6vfjNnb881ADhRI9ge3cgYgo4YEUA";
        executor.execute(() -> {
            ArrayList<Pharmacy> pharmacies = new ArrayList<>();
            try {
                URL url = new URL(testURL);

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = in.readLine()) != null)
                {
                    stringBuffer.append(line);
                }


                JSONObject jsonObj = new JSONObject(stringBuffer.toString());
                JSONArray jsonResponse = jsonObj.getJSONArray("results");
                for(int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject c = jsonResponse.getJSONObject(i);
                    String state;
                    try {
                        JSONObject opening_hours = c.getJSONObject("opening_hours");
                        state = opening_hours.getString("open_now");
                        if (state.equals("true"))
                            state = "ABERTO";
                        else
                            state = "FECHADO";
                    } catch (JSONException e) {
                        state = "SEM INFO";
                    }
                    String name = c.getString("name");
                    String rawAddress = c.getString("vicinity");
                    String address;
                    if (rawAddress.length() != 7)
                        address = rawAddress.substring(0, rawAddress.length() - 9);
                    else
                        address = rawAddress;


                    /*
                    System.out.println("FARMÁCIA: "+name);
                    System.out.println("ESTADO: "+state);
                    System.out.println("MORADA: "+address);
                    */
                    pharmacies.add(new Pharmacy(name, state, address));
                }

                in.close();

            }
            catch (MalformedURLException e) {
                System.out.println("Malformed URL: " + e.getMessage());
            }
            catch (IOException e) {
                System.out.println("I/O Error: " + e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            handler.post(() -> {
                callback.onComplete(pharmacies);
            });


        });
    }
}
