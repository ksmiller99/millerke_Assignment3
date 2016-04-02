package com.kevin_miller.csit551.assignment3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences userDetails = getApplicationContext().getSharedPreferences(app.getUsername(), MODE_PRIVATE);
                String history = userDetails.getString("history", "");
                AlertDialog alertDialog = new AlertDialog.Builder(BrowsingHistoryActivity.this).create();
                alertDialog.setTitle("History");
                alertDialog.setMessage("I originally did the history via files by mistake. This is the history via Shared Preferences:\n\n" + history);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem account = menu.findItem(R.id.action_account);
        MenuItem history = menu.findItem(R.id.action_history);
        MenuItem notes = menu.findItem(R.id.action_notes);
        MenuItem home = menu.findItem(R.id.action_home);

        history.setEnabled(false);
        history.getIcon().setAlpha(100);

        if (app.getUsername().isEmpty()) {
            // disabled
            account.setEnabled(false);
            account.getIcon().setAlpha(100);
            notes.setEnabled(false);
            notes.getIcon().setAlpha(100);
            home.setEnabled(false);
            home.getIcon().setAlpha(100);
        } else {
            //enabled
            account.setEnabled(true);
            account.getIcon().setAlpha(255);
            notes.setEnabled(true);
            notes.getIcon().setAlpha(255);
            home.setEnabled(true);
            home.getIcon().setAlpha(255);
        }


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_help:

                AlertDialog alertDialog = new AlertDialog.Builder(BrowsingHistoryActivity.this).create();
                alertDialog.setTitle("Help");
                alertDialog.setMessage("Use the menu bar to select an activity.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;

            case R.id.action_account:
                Intent intent = new Intent(BrowsingHistoryActivity.this, EditAccount.class);
                startActivity(intent);
                break;

            case R.id.action_home:
                Intent hIntent = new Intent(BrowsingHistoryActivity.this, LandingScreen.class);
                startActivity(hIntent);
                break;

            case R.id.action_notes:
                Intent nIntent = new Intent(BrowsingHistoryActivity.this, NotesActivity.class);
                startActivity(nIntent);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!app.getUsername().toString().isEmpty()) {
            //log activity
            String data = DateFormat.getDateTimeInstance().format(new Date()) + " " + this.getClass().getSimpleName() + "\n";

            SharedPreferences sharedpreferences = getSharedPreferences(app.getUsername(), Context.MODE_APPEND);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("history", sharedpreferences.getString("history", "") + data);
            editor.commit();

            FileOutputStream fOut = null;
            try {
                fOut = openFileOutput(app.getUsername(), MODE_APPEND);
                fOut.write(data.getBytes());
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
