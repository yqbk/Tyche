package pl.edu.agh.tyche;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.*;

// RestClient.java
public class RestClient{

    public RestClient() throws IOException
    {
        URL urlToRequest = new URL("http://176.115.10.86:9000/");
        HttpURLConnection urlConnection = (HttpURLConnection) urlToRequest.openConnection();

        try{
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            urlConnection.connect();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }




     //   urlConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");

//        urlConnection.setDoOutput(true);
//        urlConnection.setRequestMethod("POST");
//        urlConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
//        urlConnection.connect();

}