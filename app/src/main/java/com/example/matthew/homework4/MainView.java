package com.example.matthew.homework4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class MainView extends ActionBarActivity {

    ArrayList<String> mPushesToShow = new ArrayList<>();
    ListView lvToShow;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        //firstTime();
        checkLogin();
        getSupportActionBar().setTitle("Pushes");
        getSupportActionBar().setIcon(R.drawable.app_icon_small);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Push");
        query.whereEqualTo("destination",ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> pushList, ParseException e) {
                    if (e == null) {
                        for (int i = 0; i < pushList.size(); i++) {
                            ParseUser.getCurrentUser().add("pushList", pushList.get(i).getString("pushString"));
                            pushList.get(i).deleteInBackground();
                            ParseUser.getCurrentUser().saveInBackground();
                        }
                    } else {
                        return;
                    }
                }});

            List pushListToShow = ParseUser.getCurrentUser().getList("pushList");
            if (pushListToShow == null) {
                Toast.makeText(getApplicationContext(), "You do not have any tasks. :D", Toast.LENGTH_LONG).show();

            }
            else {
                for (int i = 0; i < pushListToShow.size(); i++) {
                    if (pushListToShow.get(i).toString().equals("[]")) {
                        mPushesToShow.remove(i);
                    } else {
                        mPushesToShow.add(pushListToShow.get(i).toString());
                    }
                }
            }



                lvToShow = (ListView)findViewById(R.id.mainListView);
            ArrayAdapter<String> adapter;
            adapter = new PushAdapter(this, R.layout.list_pushes, mPushesToShow);
            lvToShow.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent (this, OnNoLogin.class);
            this.startActivity(intent);
            return true;
        }

        else if (id == R.id.action_add) {
            Intent intent = new Intent (this, AddActivity.class);
            this.startActivity(intent);
            return true;
        }

        else if (id == R.id.action_friends) {
            Intent intent = new Intent (this, FriendsActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public  void  firstTime(){
//        //checks if this is the first time a user has used the app
//        sharedTime = getSharedPreferences(preferences_name1,0);
//        if (sharedTime.getBoolean("firstTime",true))
//        {
//            isFirstTime = false;
//            sharedTime.edit().putBoolean("firstTime",false).apply();
//        }
//    }

    public  void  checkLogin(){
        //checks if user is logged in
        if (ParseUser.getCurrentUser()==null)
        {
            Intent intent = new Intent (this, OnNoLogin.class);
            this.startActivity(intent);

        }

        else if (ParseUser.getCurrentUser().isNew()){
            ParseUser.getCurrentUser().getList("pushList");
        }

    }

    private class PushAdapter extends ArrayAdapter<String> {
        //creating variables
        private int mResource;
        private ArrayList<String> mPushes;

        public PushAdapter(Context context, int resource, ArrayList<String> pushStrings) {
            super(context, resource, pushStrings);
            mResource = resource;
            mPushes = pushStrings;
        }

        //display subject data in every row of listView
        @Override
        public View getView(final int position, View row, ViewGroup parent) {
            if (row == null) {
                row = getLayoutInflater().inflate(mResource, parent, false);
            }
            //get the homework to be displayed in row
            final String currentPush = mPushes.get(position);
            //display data from homework into row
            TextView friendTextView = (TextView) row.findViewById(R.id.pushStyleTextView);
            friendTextView.setText(currentPush);

            Button minusButton = (Button) row.findViewById(R.id.minusButton);
            minusButton.setBackgroundResource(R.drawable.minus_button);
            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPushes.remove(position);
                    ParseUser.getCurrentUser().remove("pushList");
                    ParseUser.getCurrentUser().put("pushList", mPushes);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Intent intent = new Intent(MainView.this, MainView.class);
                            MainView.this.startActivity(intent);
                        }
                    });
                }
            });

            return row;
        }
}
}

