package com.mapplinks.calculator;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mapplinks.X.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {

    Button submit;
    Intent intent = getIntent();

    EditText name,email,feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        submit=(Button) findViewById(R.id.submit_feedback);

        name=(EditText)findViewById(R.id.name_add);
        email=(EditText)findViewById(R.id.email_add);
        feedback=(EditText)findViewById(R.id.feedback);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String fName=name.getText().toString();
                 String fEmail=email.getText().toString();
                 String fFeedback=feedback.getText().toString();
                if(isOnline()){
                    new SendEmail(getApplicationContext()).execute(fName,fEmail,fFeedback);

                    Toast.makeText(FeedbackActivity.this, "Thankyou!", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    Toast.makeText(FeedbackActivity.this,"The internet isn't seem to be working!",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

    }


    public boolean isOnline(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    public class SendEmail extends AsyncTask<String, Void, String> {
        private Context context;

        SendEmail(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://docs.google.com/a/mapplinks.com/forms/d/1I8Bzj9g6882YuMiiz7M9MBrXHoqIEusHNxzqupWkvws/formResponse");

            //if (context != null) {
                try {
                    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                    pairs.add(new BasicNameValuePair("entry.2051927270", params[0]));
                    pairs.add(new BasicNameValuePair("entry.1437188323", params[1]));
                    pairs.add(new BasicNameValuePair("entry.1106319041", params[2]));

                    httppost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    JSONObject json_object = new JSONObject(result);


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }
                return null;
            //}
           // return null;
        }

        protected void onPostExecute(String msgs) {
            super.onPostExecute(msgs);
            if (msgs != null && context != null)
                Toast.makeText(FeedbackActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculator, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            shareTextUrl();
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);


        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Percentor");
        share.putExtra(Intent.EXTRA_TEXT, "I am using @Percentor to improve my #sales efficiency via @Mapplinks. Find it here: https://goo.gl/DIuFZI");

        startActivity(Intent.createChooser(share, "Spread the Word!"));


    }
}
