package passtheaux.operationunthinkable;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import passtheaux.operationunthinkable.R;
import passtheaux.operationunthinkable.RequestModels.CreateSessionRequest;
import passtheaux.operationunthinkable.RequestModels.QueueSongRequest;
import passtheaux.operationunthinkable.ResponseModels.CreateSessionResponse;
import passtheaux.operationunthinkable.ResponseModels.QueueSongResponse;

public class CreateSessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final AsyncHttpClient client = new AsyncHttpClient();

        ((Button)findViewById(R.id.createButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText usernameField = (EditText)findViewById(R.id.usernameField);
                EditText passwordField = (EditText)findViewById(R.id.passwordField);
                EditText displayNameField = (EditText)findViewById(R.id.displayNameField);

                final String username = usernameField.getText().toString();
                final String password = passwordField.getText().toString();
                final String displayName = displayNameField.getText().toString();

                if(username == null || username == "")
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(CreateSessionActivity.this, "Please enter a value for username", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }

                if(displayName == null || displayName == "")
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(CreateSessionActivity.this, "Please enter a value for display name", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }


                client.post(req.url, new RequestParams(req.Serialize()), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        String responseJson = new String(responseBody);

                        Log.v("TAG", responseJson);

                        final CreateSessionResponse response = new Gson().fromJson(responseJson, CreateSessionResponse.class);

                        Log.v("TAG", "callback 2");

                        //joined session, now queue song


                        CreateSessionRequest req = new CreateSessionRequest();
                        req.displayName = displayName;
                        req.name = username;
                        req.password = password;
                        client.post(req.url, new RequestParams(req.Serialize()), new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                String responseJson = new String(responseBody);

                                Log.v("TAG", responseJson);

                                final CreateSessionResponse response = new Gson().fromJson(responseJson, CreateSessionResponse.class);

                                Log.v("TAG", "callback 2");

                                startActivity(new Intent(CreateSessionActivity.this, NowPlayingActivity.class));
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });


            }
        });
    }
}