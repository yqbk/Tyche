package pl.edu.agh.tyche;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String data = "";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;
    private ImageView egzamin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        SERVER API
        setData("/api/accounts/users");


        final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Bitmap exam = imageBitmap;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                exam.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();

                sendData(Base64.encodeToString(imageBytes, Base64.DEFAULT));

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Info");
                alertDialog.setMessage("Egzamin przesłany do oceny");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                ImageView imageView = (ImageView) findViewById(R.id.imageView2);
                imageView.setImageResource(R.drawable.logo);

                alertDialog.show();
            }
        });

        final Button buttonCancel = (Button) findViewById(R.id.button);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Anulowanie przesyłania egzaminu");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                ImageView imageView = (ImageView) findViewById(R.id.imageView2);
                imageView.setImageResource(R.drawable.logo);

                alertDialog.show();
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public Bitmap getScreenshotBmp() {


        FileOutputStream fileOutputStream = null;

        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        String uniqueID = UUID.randomUUID().toString();

        File file = new File(path, uniqueID + ".jpg");
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 30, fileOutputStream);

        try {
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBitmap;
    }

    private void setData(final String path)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    RestClient client = new RestClient();
                    int timeout = 100;
                    String url = "http://176.115.10.86:9000";
                    String test = client.getData(url, timeout, path);
                    data = test;

                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendData(final String data)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    RestClient client = new RestClient();
                    int timeout = 100;
                    String url = "http://176.115.10.86:9000/api/exam/img";
                        client.sendData(url, timeout, data);

                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_qr) {
            // Handle the camera action

        } else if (id == R.id.nav_scan) {

            dispatchTakePictureIntent();

        } else if (id == R.id.nav_logout) {
            Intent myIntent = new Intent(this, TycheLogin.class);
            startActivityForResult(myIntent, 0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data!=null) {
            Bundle extras = data.getExtras();

            if (extras.keySet().contains("data") ){
                imageBitmap = (Bitmap) extras.get("data");
                ImageView imageView = (ImageView) findViewById(R.id.imageView2);
                imageView.setImageBitmap(imageBitmap);
            } else
            {
                System.out.print("\n\n\n\n don't have data \n\n\n\n");
            }
        }
    }

}
