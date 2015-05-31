package com.example.matthew.homework4;
import android.app.Application;
import com.parse.Parse;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "NL6gmiUyU2NMpUVcNYljVJtWX8qeBt2pFbEVVqEu", "FnvpibKUq6pTEh89SytxXVOKSSZWg0TCFQsuzMr6");

    }
}