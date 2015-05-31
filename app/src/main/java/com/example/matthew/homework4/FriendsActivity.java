package com.example.matthew.homework4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends ActionBarActivity {

    View mTextEntryView;
    String friendUsername;
    EditText mFriendUsernameField;
    ListView lvToShow;
    ArrayList<String> mFriendsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        getSupportActionBar().setIcon(R.drawable.friend_icon);
        getSupportActionBar().setTitle("Friends");

        List friendsListToShow = ParseUser.getCurrentUser().getList("friendsList");
        if (friendsListToShow == null) {
            Toast.makeText(getApplicationContext(), "You do not have any friends. D:", Toast.LENGTH_LONG).show();

        }
        else {
            for (int i = 0; i < friendsListToShow.size(); i++) {
                if(friendsListToShow.get(i).toString().equals("[]")){
                    mFriendsArray.remove(i);
                }
                else {
                    mFriendsArray.add(friendsListToShow.get(i).toString());
                }
            }
        }


        lvToShow = (ListView)findViewById(R.id.friendsListView);
        ArrayAdapter<String> adapter;
        adapter = new FriendsAdapter(this, R.layout.list_friends, mFriendsArray);
        lvToShow.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Dialog();
            return true;
        } else if (id == R.id.action_back) {
            Intent intent = new Intent(this, MainView.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Dialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        mTextEntryView = factory.inflate(R.layout.addfriend_dialog_text_entry, null);

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.app_icon_small)
                .setTitle("Add Friend")
                .setMessage("Input is case sensitive")
                .setView(mTextEntryView)

                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        positiveButton();
                    }
                })

                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }

    public void positiveButton() {
        mFriendUsernameField = (EditText) mTextEntryView.findViewById(R.id.friendUsernameEditText);
        friendUsername = mFriendUsernameField.getText().toString();
        ParseQuery<ParseUser> check = ParseUser.getQuery();
        check.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friendsList, ParseException e) {
                if (e == null){
                    for (int i = 0; i < friendsList.size(); i++) {
                        if(friendsList.get(i).getUsername().equals(friendUsername)){
                            ParseUser.getCurrentUser().addUnique("friendsList", friendUsername);
                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null) {
                                        Intent intent = new Intent(FriendsActivity.this, FriendsActivity.class);
                                        FriendsActivity.this.startActivity(intent);
                                    }
                                }
                            });
                            break;
                            }

                        else if (i == friendsList.size())
                        {
                            Toast.makeText(getApplicationContext(), "This user does not exist.", Toast.LENGTH_LONG).show();
                            break;
                        }

                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "This user does not exist.", Toast.LENGTH_LONG).show();
                    }
            }



        });}


        private class FriendsAdapter extends ArrayAdapter<String> {
        //creating variables
        private int mResource;
        private ArrayList<String> mFriends;

        public FriendsAdapter (Context context, int resource, ArrayList<String> parseUsers) {
            super(context, resource, parseUsers);
            mResource = resource;
            mFriends = parseUsers;
        }

        //display subject data in every row of listView
        @Override
        public View getView(final int position, View row, ViewGroup parent) {
            if (row == null) {
                row = getLayoutInflater().inflate(mResource, parent, false);
            }
            //get the homework to be displayed in row
            final String currentFriend = mFriends.get(position);
            //display data from homework into row
            TextView friendTextView = (TextView) row.findViewById(R.id.friendsStyleTextView);
            friendTextView.setText(currentFriend);
            final TextView friendPushedTextView = (TextView) row.findViewById(R.id.pushedXTimes);
            ParseQuery<ParseUser> check = ParseUser.getQuery();
            check.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> friendsList, ParseException e) {
                    if (e == null) {
                        for (int i = 0; i < friendsList.size(); i++) {
                            if (friendsList.get(i).getUsername().equals(currentFriend)) {
                                Integer j = friendsList.get(i).getList("pushList").size();
                                friendPushedTextView.setText("Pushed " + j + " times in total");
                                break;
                            }

                        else if (i == friendsList.size()) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            break;
                        }

                    }

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }



            });
            //TODO Try to put the above strings into an array (I think) to put into the destination ParseUser.
            Button minusButton = (Button) row.findViewById(R.id.minusButton);
            minusButton.setBackgroundResource(R.drawable.minus_button);
            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFriends.remove(position);
                    ParseUser.getCurrentUser().remove("friendsList");
                    ParseUser.getCurrentUser().put("friendsList", mFriends);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Intent intent = new Intent(FriendsActivity.this, FriendsActivity.class);
                            FriendsActivity.this.startActivity(intent);
                        }
                    });
                }
            });

            return row;
        }
    }
}
