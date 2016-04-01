package com.kevin_miller.csit551.assignment3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {

    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        myDb = new DatabaseHelper(ForgotPasswordActivity.this);

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

}
