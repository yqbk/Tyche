package pl.edu.agh.tyche;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.*;

// RestClient.java
public class RestClient{

    public RestClient() throws IOException {
//        URL urlToRequest = new URL("http://176.115.10.86:9000/oauth/token");
//        HttpURLConnection urlConnection = (HttpURLConnection) urlToRequest.openConnection();
//
//        try {
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
////            urlConnection.setRequestProperty("Set-Cookie", sessionCookie);
//            urlConnection.setDoOutput(true);
//            urlConnection.setDoInput(true);
//
//
//            urlConnection.setRequestProperty("username", "admin");
//            urlConnection.setRequestProperty("password", "Admin123!@#");
//            urlConnection.setRequestProperty("grant_type", "password");
//            urlConnection.connect();
//
//            if (urlConnection.getResponseCode() == 200) {
//                System.out.println("\n\n\n\n\n success tadads sasdasdas as\n\n\n\n\n");
//            } else {
//                System.out.println(" \n\n\n\n\n nasz failure \n\n\n\n\n");
//            }
//
//
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//        }
    }

        public static String getJSON(String url, String json, int timeout, String method) {
        HttpURLConnection connection = null;
        try {

            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod(method);

            //set the sending type and receiving type to json
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            if (json != null) {
                //set the content length of the body
                connection.setRequestProperty("Content-length", json.getBytes().length + "");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                //send the json as body of the request
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }

            //Connect to the server
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
                    return sb.toString();
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
        return null;
    }

    }




     //   urlConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");

//        urlConnection.setDoOutput(true);
//        urlConnection.setRequestMethod("POST");
//        urlConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
//        urlConnection.connect();
