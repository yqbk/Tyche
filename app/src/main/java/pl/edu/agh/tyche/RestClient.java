package pl.edu.agh.tyche;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.*;
import org.json.JSONObject;

// RestClient.java
public class RestClient{

    private static String token = "";

    public RestClient() throws IOException {

    }

    public static boolean getToken(String url, JSONObject json, int timeout, String method) {
    HttpURLConnection connection = null;
    try {

        URL u = new URL(url);
        connection = (HttpURLConnection) u.openConnection();
        connection.setRequestMethod(method);

        connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        String body =
                "username=" + URLEncoder.encode ("admin") +
                "&password=" + URLEncoder.encode ("Admin123!@#") +
                "&grant_type=" + URLEncoder.encode ("password");


        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(body.getBytes("UTF-8"));
        outputStream.close();
        connection.connect();

//            InputStream error = connection.getErrorStream();

        int status = connection.getResponseCode();
        Log.i("HTTP Client", "HTTP status code : " + status);
        switch (status) {
            case 200:
            case 201:
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                bufferedReader.close();
                Log.i("HTTP Client", "Received String : " + sb.toString());
                //return received string
                JSONObject token_json = new JSONObject(sb.toString());
                token = (String) token_json.get("access_token");
                return true;
        }

    } catch (MalformedURLException ex) {
        Log.e("HTTP Client", "Error in http connection" + ex.toString());
    } catch (IOException ex) {
        Log.e("HTTP Client", "Error in http connection" + ex.toString());
    } catch (Exception ex) {
        Log.e("HTTP Client", "Error in http connection" + ex.toString());
    } finally {
        if (connection != null) {
            try {
                connection.disconnect();
            } catch (Exception ex) {
                Log.e("HTTP Client", "Error in http connection" + ex.toString());
            }
        }
    }
    return false;
    }

    public static String getData(String url, int timeout, String data) {
        HttpURLConnection connection = null;
        try {
            URL u = new URL(url + data);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Authorization", "bearer " + token);

            connection.connect();

            int status = connection.getResponseCode();
            Log.i("HTTP Client", "HTTP status code : " + status);
            switch (status) {
                case 200:
                case 201:
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    bufferedReader.close();
                    Log.i("HTTP Client", "Received String : " + sb.toString());
                    //return received string
                    String response = sb.toString();
                    return response;
            }

        } catch (MalformedURLException ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } catch (IOException ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } catch (Exception ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Log.e("HTTP Client", "Error in http connection" + ex.toString());
                }
            }
        }
        return "";
    }
}
