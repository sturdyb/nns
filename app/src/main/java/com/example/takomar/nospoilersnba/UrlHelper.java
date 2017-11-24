package com.example.takomar.nospoilersnba;

import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by takomar on 06/02/17.
 */

public class UrlHelper {
    static public StringBuffer retrieveJSONBuffer(String urlLink) throws IOException {

        StringBuffer buffer = new StringBuffer();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        Log.v("SpoilDbg", urlLink);
        try {

            URL url = new URL(urlLink);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Referer", "http://stats.nba.com/scores/");
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                return buffer;
            }

        } catch (SocketTimeoutException e) {
            Log.e("SpoilErr", "Timeout " + urlLink, e);

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

