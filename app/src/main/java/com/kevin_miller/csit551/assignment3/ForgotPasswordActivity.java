package com.kevin_miller.csit551.assignment3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;

public class ForgotPasswordActivity extends AppCompatActivity {

    MyApplication app;
    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        myDb = new DatabaseHelper(ForgotPasswordActivity.this);
        app = (MyApplication) getApplication();

        final Button btnChkRec = (Button) findViewById(R.id.fp_chkRec_btn);
        final EditText etUsername = (EditText) findViewById(R.id.fp_etForgotUsername);

        btnChkRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsername.getText().toString();
                if (!username.isEmpty()){
                    etUsername.setBackgroundColor(0x00000000);

                    checkRecord(v, username);

//                    Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
//                    startActivity(intent);
                } else {
                    etUsername.setBackgroundColor(0xFFFF0000);
                    AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                    alertDialog.setTitle("Username Error");
                    alertDialog.setMessage("You must input a username");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

    }

    public void checkRecord(View v, String username){
        Cursor cursor = myDb.getRecForUsername(username);
        if (cursor.getCount() == 0)
            Snackbar.make(v,"No records exist",Snackbar.LENGTH_LONG ).show();
        else {
            Snackbar.make(v, "Record exists", Snackbar.LENGTH_LONG).show();
            cursor.moveToFirst();
            //TODO check for multiple records and handle
            Intent intent = new Intent(this,ShowPasswordActivity.class);
            intent.putExtra("password",cursor.getString(6));
            startActivity(intent);
        }

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

        if (app.getUsername().isEmpty()) {
            // disabled
            account.setEnabled(false);
            account.getIcon().setAlpha(100);
            history.setEnabled(false);
            history.getIcon().setAlpha(100);
            notes.setEnabled(false);
            notes.getIcon().setAlpha(100);
            home.setEnabled(false);
            home.getIcon().setAlpha(100);
        } else {
            //enabled
            account.setEnabled(true);
            account.getIcon().setAlpha(255);
            history.setEnabled(true);
            history.getIcon().setAlpha(255);
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

                AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
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
                Intent intent = new Intent(ForgotPasswordActivity.this, EditAccount.class);
                startActivity(intent);
                break;

            case R.id.action_history:
                Intent bhIntent = new Intent(ForgotPasswordActivity.this, BrowsingHistoryActivity.class);
                startActivity(bhIntent);
                break;

            case R.id.action_notes:
                Toast.makeText(getApplicationContext(), "Notes is not implemented yet", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_home:
                Intent hIntent = new Intent(ForgotPasswordActivity.this, LandingScreen.class);
                startActivity(hIntent);
                break;


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
