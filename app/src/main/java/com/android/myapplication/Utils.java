package com.android.myapplication;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class Utils {

    static String fetchMarsData(String s) {
        URL url = createUrl(s);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Utils", "Error making http request", e);
        }
        String result = extractFeatureFromData(jsonResponse);
        return result;
    }

    private static URL createUrl(String s) {
        URL url = null;
        try {
            url = new URL(s);
        } catch (MalformedURLException e) {
            Log.e("Utils", "Url creation error", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Utils", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Utils", "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String extractFeatureFromData(String s) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(s)) {
            return null;
        }

        try {
            JSONObject baseJsonResponse = new JSONObject(s);
            JSONArray featureArray = baseJsonResponse.getJSONArray("photos");
            if (featureArray.length() > 0) {
                JSONObject firstFeature = featureArray.getJSONObject(0);
                String id = firstFeature.getString("id");
                return id;
            }

            // If there are results in the features array

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return null;
    }
}