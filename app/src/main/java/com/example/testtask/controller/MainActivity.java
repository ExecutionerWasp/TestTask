package com.example.testtask.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testtask.R;
import com.example.testtask.domain.GithubItem;
import com.example.testtask.repos.ItemRepos;
import com.example.testtask.service.ItemService;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = " [APPLICATION] ";
    private final ItemService ITEM_SERVICE = new ItemService(new ItemRepos(this));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG_TAG, "BUTTON CLICKED");
                onSearch();
            }
        });
    }

    private void onSearch(){
                EditText searchField = findViewById(R.id.name);
                String value = searchField.getText().toString();

                GithubItem obj = ITEM_SERVICE.findByName(value);
                LinearLayout items = findViewById(R.id.list);

                LinearLayout item = findViewById(MainActivity.CONTEXT_IGNORE_SECURITY);

                ImageView img = findViewById(MainActivity.CONTEXT_IGNORE_SECURITY);
                img.setImageURI(Uri.parse(obj.getUrl()));

                TextView name = findViewById(MainActivity.CONTEXT_IGNORE_SECURITY);
                name.setText(obj.getName());

                item.addView(img);
                item.addView(name);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItem();
                    }
                });

                items.addView(item);
    }

    private void onItem() {
        LinearLayout item = findViewById(R.id.item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG_TAG, "ITEM CLICKED");
                LinearLayout item = findViewById(R.id.item);
                TextView name = (TextView) item.getChildAt(1);
                openInBrowser(ITEM_SERVICE.findByName(String.valueOf(name.getText())));

            }
        });
    }

    private void openInBrowser(GithubItem item){
        Log.i(LOG_TAG, "OPENING IN BROWSER...");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
        startActivity(browserIntent);
    }

}
