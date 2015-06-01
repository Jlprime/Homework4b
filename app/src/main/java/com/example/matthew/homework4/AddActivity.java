package com.example.matthew.homework4;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AddActivity extends ActionBarActivity {

    Button pushButton;
    EditText mPushName;
    EditText mPushToDo;
    EditText mDateText;
    EditText mTimeText;
    String dateTime;
    String name;
    String toDo;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel1();
        }

    };

    TimePickerDialog.OnTimeSetListener time1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateTimeLabel1();
        }
    };

    private void updateDateLabel1() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDateText.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateTimeLabel1() {

        String myFormat = "K:mm a"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        mTimeText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setIcon(R.drawable.add_icon);
        getSupportActionBar().setTitle("Push Someone!");

        final ParseQuery<ParseUser> check = ParseUser.getQuery();

        mPushName = (EditText) findViewById(R.id.pushName);
        mPushToDo = (EditText) findViewById(R.id.pushToDo);
        mDateText = (EditText) findViewById(R.id.pushDate);
        mTimeText = (EditText) findViewById(R.id.pushTime);

        mDateText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDateText.setInputType(InputType.TYPE_NULL);
                mDateText.onTouchEvent(event);
                return true;
            }
        });
        //Then generating the DatePicker when pressed.
        mDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mTimeText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTimeText.setInputType(InputType.TYPE_NULL); // disable soft input
                mTimeText.onTouchEvent(event); // call native handler
                return true; // consume touch even
            }
        });
        //Then generating the TimePicker when pressed.
        mTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddActivity.this, time1, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        pushButton = (Button) findViewById(R.id.pushButton);
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mPushName.getText().toString();
                toDo = mPushToDo.getText().toString();
                dateTime = mDateText.getText().toString() + " " + mTimeText.getText().toString();
                if (name.equals("") | toDo.equals("") | mDateText.getText().toString().equals("") | mTimeText.getText().toString().equals("")){
                    alertMessage("Please fill in the empty fields.");
                    //checks for empty fields
                }

                else{
                final List friendsList = ParseUser.getCurrentUser().getList("friendsList");
                check.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> usersList, ParseException e) {
                        if (e == null) {
                            for (int i = 0; i < usersList.size(); i++) {
                                for (int k = 0; k < friendsList.size(); k++) {
                                if (usersList.get(i).getUsername().equals(name)&& usersList.get(i).getUsername().equals(friendsList.get(k))) {
                                    String push = "Hey! " + ParseUser.getCurrentUser().getUsername() + " has just pushed you to do " + toDo + " by " + dateTime + "! Don't forget!";
                                    ParseObject pushed = new ParseObject("Push");
                                    ParseACL groupACL = new ParseACL();
                                    pushed.put("pushString", push);
                                    pushed.put("destination", usersList.get(i));
                                    ArrayList<ParseUser> userList = new ArrayList<>();
                                    userList.add(ParseUser.getCurrentUser());
                                    userList.add(usersList.get(i));
                                    for (ParseUser user : userList) {
                                        groupACL.setReadAccess(user, true);
                                        groupACL.setWriteAccess(user, true);
                                    }

                                    pushed.setACL(groupACL);
                                    pushed.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Intent intent = new Intent(AddActivity.this, MainView.class);
                                            AddActivity.this.startActivity(intent);


                                        }
                                    });
                                    break;

                                } else if (i == usersList.size()) {
                                    mPushName.setText("");
                                    Toast.makeText(getApplicationContext(), "This user is not your friend.", Toast.LENGTH_LONG).show();
                                    break;
                                }

                            }
                        }
                        } else {
                            Toast.makeText(getApplicationContext(), "This user does not exist.", Toast.LENGTH_LONG).show();
                        }
                    }


                });}}

                //TODO Try to put the pushes into an array to put into the destination ParseUser.
            });
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_back) {

            Intent intent = new Intent (this, MainView.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void alertMessage(String Message)
    {
        Toast.makeText(AddActivity.this, Message, Toast.LENGTH_SHORT).show();
    }
}
