package com.example.takomar.nospoilersnba;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by takomar on 06/02/17.
 */

public class UrlHelper {
    static final SimpleDateFormat dateFormatUrl = new SimpleDateFormat("MM/dd/yyyy");
    static public StringBuffer retrieveJSONBuffer(String urlLink) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

   //     Log.v("SpoilDbg", urlLink);
        try {
            URL url = new URL(urlLink);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Referer", "http://stats.nba.com/scores/");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                return buffer;
            }

        } catch (IOException e) {
            Log.e("SpoilErr", "damn too slow " + urlLink, e);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException ioe) {
                    Log.e("SpoilErr", "Error closing stream", ioe);
                }
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("SpoilErr", "Error closing stream", e);
                }
            }
        }
        return new StringBuffer();
    }
}

