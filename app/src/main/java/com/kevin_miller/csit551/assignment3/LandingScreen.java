package com.kevin_miller.csit551.assignment3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;

public class LandingScreen extends AppCompatActivity {

    MyApplication app;

    TextView tvUsername;
    TextView tvFullName;
    TextView tvDob;
    TextView tvEmail;
    TextView tvMajor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = (MyApplication) getApplication();

        tvUsername = (TextView) findViewById(R.id.ls_tvUsername);
        tvFullName = (TextView) findViewById(R.id.ls_tvFullName);
        tvDob = (TextView) findViewById(R.id.ls_tvDOB);
        tvEmail = (TextView) findViewById(R.id.ls_tvEmail);
        tvMajor = (TextView) findViewById(R.id.ls_tvMajor);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!app.getUsername().toString().isEmpty()) {
            //log activity
            String data = DateFormat.getDateTimeInstance().format(new Date()) + " " + this.getClass().getSimpleName() + "\n";
            ;

            FileOutputStream fOut = null;
            try {
                fOut = openFileOutput(app.getUsername(), MODE_APPEND);
                fOut.write(data.getBytes());
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        tvUsername.setText(app.getUsername());
        tvFullName.setText(app.getFullname());
        tvDob.setText(app.getDob());
        tvEmail.setText(app.getEmail());
        tvMajor.setText(app.getMajor());
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

        home.setEnabled(false);
        home.getIcon().setAlpha(100);

        if (app.getUsername().isEmpty()) {
            // disabled
            account.setEnabled(false);
            account.getIcon().setAlpha(100);
            history.setEnabled(false);
            history.getIcon().setAlpha(100);
            notes.setEnabled(false);
            notes.getIcon().setAlpha(100);
        } else {
            //enabled
            account.setEnabled(true);
            account.getIcon().setAlpha(255);
            history.setEnabled(true);
            history.getIcon().setAlpha(255);
            notes.setEnabled(true);
            notes.getIcon().setAlpha(255);
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

                AlertDialog alertDialog = new AlertDialog.Builder(LandingScreen.this).create();
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
                Intent intent = new Intent(LandingScreen.this, EditAccount.class);
                startActivity(intent);
                break;

            case R.id.action_history:
                Intent bhIntent = new Intent(LandingScreen.this, BrowsingHistoryActivity.class);
                startActivity(bhIntent);
                break;

            case R.id.action_notes:
                Toast.makeText(getApplicationContext(), "Notes is not implemented yet", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
