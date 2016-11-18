package pl.edu.agh.tyche;

import android.os.AsyncTask;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by yqbk on 11/18/16.
 */

public class Api {

    URL githubEndpoint = new URL("https://api.github.com/");

    // Create connection
    HttpsURLConnection myConnection = (HttpsURLConnection) githubEndpoint.openConnection();

    AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
            // All your networking logic
            // should be here
        }
    });
}
