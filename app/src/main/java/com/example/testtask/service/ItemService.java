package com.example.testtask.service;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.example.testtask.domain.GithubItem;
import com.example.testtask.repos.ItemRepos;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ItemService {

    private static final String LOG_TAG = " [ITEM SERVICE] ";


    @NonNull
    private final ItemRepos itemRepos;

    public void getFromGithub(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL githubEndpoint = new URL("https://api.github.com/repositories?since=364");
                    HttpsURLConnection myConnection =
                            (HttpsURLConnection) githubEndpoint.openConnection();
                    myConnection.setRequestProperty("Accept",
                            "application/vnd.github.v3+json");
                    Log.i(LOG_TAG, "CONNECTION SUCCESS");

                    if (myConnection.getResponseCode() == 200) {
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");

                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        Log.i(LOG_TAG, "GET JSON");

                        jsonReader.beginObject();
                        Log.i(LOG_TAG, "JSON READING...");

                        while (jsonReader.hasNext()) {
                            String key = jsonReader.nextName();
                            if (key.equals("id")) {
                                String value = jsonReader.nextString();
                                Log.i(LOG_TAG, "REPOSITORY ID : " + value);
                                break;
                            } else {
                                jsonReader.skipValue();
                            }
                        }
                        jsonReader.close();
                        myConnection.disconnect();
                    } else {
                        Log.i(LOG_TAG, "RESPONSE CODE: " + myConnection.getResponseCode());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Log.i(LOG_TAG, "Inserting ..");
        if (itemRepos.findAll().size() < 30){
            itemRepos.save(new GithubItem("url","name","img"));
        }
    }

    public List<GithubItem> findAll() {
        Log.i(LOG_TAG, "Searching ..");
        return itemRepos.findAll();
    }
}
