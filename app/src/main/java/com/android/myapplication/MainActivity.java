package com.android.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String REQUEST_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&page=2&api_key=FxWTIx6Zl4irZmQkbEhXRTOP3p3SZtgoFTnhBhrk";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewAsyncTask asyncTask = new NewAsyncTask();
                asyncTask.execute(REQUEST_URL);
            }
        });
    }

    private void updateDisplay(String s) {
        textView.setText(s);
    }

    private class NewAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            if (urls.length == 0 || urls[0] == null) {
                return null;
            }
            String result = Utils.fetchMarsData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                return;
            }
            updateDisplay(s);
        }
    }
}