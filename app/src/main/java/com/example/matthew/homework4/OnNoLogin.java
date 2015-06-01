package com.example.matthew.homework4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;


public class OnNoLogin extends ActionBarActivity {

    Button loginButton;
    EditText mUsernameField;
    EditText mPasswordField;
    String usernameInput;
    String passwordInput;
    View mTextEntryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_action_bar);
        getSupportActionBar().hide();
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();

            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    public void Dialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        mTextEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.app_icon_small)
                .setTitle("Login to Taskworker")
                .setView(mTextEntryView)

                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        positiveButton();
                    }
                })
                .setNeutralButton("Sign Up", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(OnNoLogin.this, signUpActivity.class);
                        OnNoLogin.this.startActivity(intent);

                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }
    public void alertMessage(String Message)
    {
        Toast.makeText(OnNoLogin.this, Message, Toast.LENGTH_SHORT).show();
    }

    public void positiveButton()
    {
        mUsernameField = (EditText) mTextEntryView.findViewById(R.id.usernameEditText);
        mPasswordField = (EditText) mTextEntryView.findViewById(R.id.passwordEditText2);
        usernameInput = mUsernameField.getText().toString();
        passwordInput = mPasswordField.getText().toString();
        if (usernameInput.equals("") | passwordInput.equals("")){
            alertMessage("Please fill in the empty fields.");
            //checks for empty fields
        }
        else {
            try {
                ParseUser.logIn(usernameInput, passwordInput);
            } catch (ParseException e) {
                alertMessage("Login failed. Please try again.");
            }
            MainView.isLogin = true;
            Intent intent = new Intent(this, MainView.class);
            this.startActivity(intent);

        }
    }
}