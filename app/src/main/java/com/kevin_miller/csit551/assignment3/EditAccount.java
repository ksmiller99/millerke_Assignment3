package com.kevin_miller.csit551.assignment3;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MyApplication app;
    private DatabaseHelper myDB;

    //pop-up datepicker lifted from
    //http://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
    //http://stackoverflow.com/questions/11790573/onclick-event-is-not-triggering-android
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateListener;
    Boolean firstTime = true;

    EditText etUsername;
    EditText etFullName;
    EditText etDob;
    EditText etEmail;
    EditText etPassword1;
    EditText etPassword2;
    Spinner spnMajor;

    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = (MyApplication) getApplication();
        myDB = new DatabaseHelper(this);

        etUsername = (EditText) findViewById(R.id.ea_etUsername);
        etFullName = (EditText) findViewById(R.id.ea_etFullName);
        etDob = (EditText) findViewById(R.id.ea_etDOB);
        etEmail = (EditText) findViewById(R.id.ea_tvEmail);
        etPassword1 = (EditText) findViewById(R.id.ea_etPassword1);
        etPassword2 = (EditText) findViewById(R.id.ea_etPassword2);
        spnMajor = (Spinner) findViewById(R.id.ea_spnMajor);

        btnSave = (Button) findViewById(R.id.ea_btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean error = false;

                //if no major selected, set error flag and background to red
                if (spnMajor.getSelectedItem().toString().equals("Select your major")) {
                    error = true;
                    spnMajor.setBackgroundColor(0xFFFF0000);
                    AlertDialog alertDialog = new AlertDialog.Builder(EditAccount.this).create();
                    alertDialog.setTitle("Major Selection Error");
                    alertDialog.setMessage("You must select a major.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    spnMajor.setBackgroundColor(0x00000000);
                }

                //passwords must match, be longer than 6, contain leter and number, else set error flag and background to red
                //regex lifted from http://stackoverflow.com/questions/19605150/regex-for-password-must-be-contain-at-least-8-characters-least-1-number-and-bot
                String password1 = etPassword1.getText().toString();
                if ((!etPassword1.getText().toString().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")) ||
                        (!etPassword1.getText().toString().equals(etPassword2.getText().toString()))) {

                    error = true;
                    etPassword1.setBackgroundColor(0xFFFF0000);
                    etPassword2.setBackgroundColor(0xFFFF0000);
                    AlertDialog alertDialog = new AlertDialog.Builder(EditAccount.this).create();
                    alertDialog.setTitle("Password Error");
                    alertDialog.setMessage("Passwords must match, contain at least 6 characters including at least one number and one letter.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

                //check email address
                String em = etEmail.getText().toString();
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(em).matches() &&
                        em.endsWith("@montclair.edu")) {
                    etEmail.setBackgroundColor(0x00000000);
                } else {
                    error = true;
                    etEmail.setBackgroundColor(0xFFFF0000);
                    AlertDialog alertDialog = new AlertDialog.Builder(EditAccount.this).create();
                    alertDialog.setTitle("EMail Error");
                    alertDialog.setMessage("Only valid MSU email addresses are allowed.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

                //check if username is blank
                String username = etUsername.getText().toString();
                if (username.isEmpty()) {
                    error = true;
                    etUsername.setBackgroundColor(0xFFFF0000);
                    AlertDialog alertDialog = new AlertDialog.Builder(EditAccount.this).create();
                    alertDialog.setTitle("Username Blank");
                    alertDialog.setMessage("You must enter a new username");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

                //check if Full Name is blank
                String fullname = etFullName.getText().toString();
                if (fullname.isEmpty()) {
                    error = true;
                    etFullName.setBackgroundColor(0xFFFF0000);
                    AlertDialog alertDialog = new AlertDialog.Builder(EditAccount.this).create();
                    alertDialog.setTitle("Full Name Blank");
                    alertDialog.setMessage("You must enter your full name");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    etFullName.setBackgroundColor(0x00000000);
                }

                //check if DOB is blank
                String dob = etDob.getText().toString();
                if (dob.isEmpty()) {
                    error = true;
                    etDob.setBackgroundColor(0xFFFF0000);
                    AlertDialog alertDialog = new AlertDialog.Builder(EditAccount.this).create();
                    alertDialog.setTitle("Username Blank");
                    alertDialog.setMessage("You must enter a new username");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    etDob.setBackgroundColor(0x00000000);
                }

                if (!error) {

                    boolean isUpdated = myDB.updateData(
                            String.valueOf(app.getPrimaryKey()),
                            etUsername.getText().toString(),
                            etFullName.getText().toString(),
                            etDob.getText().toString(),
                            spnMajor.getSelectedItem().toString(),
                            em,
                            etPassword1.getText().toString()
                    );
                    if (isUpdated) {
                        Toast.makeText(getApplicationContext(), "Data updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditAccount.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Data NOT updated", Toast.LENGTH_SHORT).show();
                    }
                }

            }


        });

        //put spinner index on current major
        Resources res = getResources();
        String[] majors = res.getStringArray(R.array.major_array);
        int index = 0;
        for (int i = 0; i < majors.length; i++) {
            if (majors[i].equals(app.getMajor())) {
                index = i;
                break;
            }
        }
        spnMajor.setSelection(index);

        //get currented DOB details
        DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH);
        try {
            Date date = format.parse(app.getDob());
            int year = date.getYear();
            int month = date.getMonth();
            int day = date.getDate();
            if (year + 2000 > Calendar.getInstance().get(Calendar.YEAR))
                year += 1900;
            else
                year += 2000;
            myCalendar.set(year, month, day);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //TODO for possible future use
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //call datepicker when DOB field is clicked
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditAccount.this, dateListener,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //date listener for popup date picker
        dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        //ensure username is not blank
        etUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!v.getText().toString().isEmpty()) {
                    v.setBackgroundColor(0x00000000);
                    return false;
                } else {
                    v.setBackgroundColor(0xFFFF0000);
                    AlertDialog alertDialog = new AlertDialog.Builder(EditAccount.this).create();
                    alertDialog.setTitle("Username Error");
                    alertDialog.setMessage("Username cannot be blank.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                    return true;
                }
            }
        });

        //validate password1 is 6+ characters, has at least one letter and at least one number and set background to red if not
        etPassword1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (etPassword1.getText().toString().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")) {
                    etPassword1.setBackgroundColor(0x00000000);

                } else {
                    etPassword1.setBackgroundColor(0xFFFF0000);
                    AlertDialog alertDialog = new AlertDialog.Builder(EditAccount.this).create();
                    alertDialog.setTitle("Password Error");
                    alertDialog.setMessage("Password must be at least 6 characters long and contain at least one letter and at least one number.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                return false;
            }
        });

        //validate passwords match when exiting password2. Set background to red and
        //if they don't match
        etPassword2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (etPassword1.getText().toString().equals(etPassword2.getText().toString())) {
                    etPassword2.setBackgroundColor(0x00000000);
                    etPassword1.setBackgroundColor(0x00000000);
                } else {
                    etPassword1.setBackgroundColor(0xFFFF0000);
                    etPassword2.setBackgroundColor(0xFFFF0000);
                    AlertDialog alertDialog = new AlertDialog.Builder(EditAccount.this).create();
                    alertDialog.setTitle("Password Error");
                    alertDialog.setMessage("Passwords do not match.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                return false;
            }
        });

        spnMajor.setOnItemSelectedListener(this);


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


        etUsername.setText(app.getUsername());
        etFullName.setText(app.getFullname());
        etDob.setText(app.getDob());
        etEmail.setText(app.getEmail());
        //etMajor.setText(app.getMajor());
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

        account.setVisible(false);

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

                AlertDialog alertDialog = new AlertDialog.Builder(EditAccount.this).create();
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
                Intent intent = new Intent(EditAccount.this, EditAccount.class);
                startActivity(intent);
                break;

            case R.id.action_history:
                Intent bhIntent = new Intent(EditAccount.this, BrowsingHistoryActivity.class);
                startActivity(bhIntent);
                break;

            case R.id.action_notes:
                Toast.makeText(getApplicationContext(), "Notes is not implemented yet", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_home:
                Intent hIntent = new Intent(EditAccount.this, LandingScreen.class);
                startActivity(hIntent);
                break;


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //update the DOB field after user selects a date
    //spinner ensures correct format
    private void updateLabel() {

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        EditText et = (EditText) findViewById(R.id.ea_etDOB);
        et.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //to prevent firing during build
        if (!firstTime) {
            if (parent.getItemAtPosition(position).toString().equals("Select your major")) {
                view.setBackgroundColor(0xFFFF0000);
            } else {
                view.setBackgroundColor(0x00000000);
            }
        } else {
            firstTime = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
