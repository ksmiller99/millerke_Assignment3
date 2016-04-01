package com.kevin_miller.csit551.assignment3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myDB;

    MyApplication app;

    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    Button btnForgotPassword;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDB = new DatabaseHelper(this);
        app = (MyApplication) getApplication();

        etUsername = (EditText) findViewById(R.id.ma_etUserName);
        etPassword = (EditText) findViewById(R.id.ma_etPassword);

        btnLogin = (Button) findViewById(R.id.ma_btnLogIn);
        btnRegister = (Button) findViewById(R.id.ma_btnRegister);
        btnForgotPassword = (Button) findViewById(R.id.ma_btnForgotPasword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()) {
                    Cursor cursor = myDB.login(username, password);
                    if ((cursor != null) && (cursor.getCount() != 0)) {
                        cursor.moveToFirst();
                        app.setUsername(cursor.getString(1));
                        app.setFullname(cursor.getString(2));
                        app.setDob(cursor.getString(3));
                        app.setMajor(cursor.getString(4));
                        app.setEmail(cursor.getString(5));
                        Intent intent = new Intent(MainActivity.this, LandingScreen.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Login error. Check inputs and try again.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "You must input a username and password to log in.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationPage.class);
                startActivity(intent);
            }
        });

//        //go to landing page after inputting username and password without validating
//        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                Intent intent = new Intent(MainActivity.this, LandingScreen.class);
//                startActivity(intent);
//                return false;
//            }
//        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

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

//        MyApplication app = (MyApplication) getApplication();

        if (app.getUsername().isEmpty()){
            // disabled
            account.setEnabled(false); account.getIcon().setAlpha(100);
            history.setEnabled(false); history.getIcon().setAlpha(100);
            notes.setEnabled(false); notes.getIcon().setAlpha(100);
        }else{
            //enabled
            account.setEnabled(true); account.getIcon().setAlpha(255);
            history.setEnabled(true); history.getIcon().setAlpha(255);
            notes.setEnabled(true); notes.getIcon().setAlpha(255);
        }


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id==R.id.action_help){
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Help");
            alertDialog.setMessage("You must log in before you can use this app.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

}
