package com.kevin_miller.csit551.assignment3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;

public class BrowsingHistoryActivity extends AppCompatActivity {
    MyApplication app;
    TextView tvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = (MyApplication) getApplication();
        tvHistory = (TextView) findViewById(R.id.bh_tvHistory);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        try {
            FileInputStream fin = openFileInput(app.getUsername());
            File file = new File(BrowsingHistoryActivity.this.getFilesDir(), app.getUsername());

            int c;
            String temp = "";

            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            tvHistory.setText(temp);

        } catch (Exception e) {
        }

    }

}
