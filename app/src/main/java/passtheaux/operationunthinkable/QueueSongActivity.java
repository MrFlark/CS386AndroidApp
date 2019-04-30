package passtheaux.operationunthinkable;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import passtheaux.operationunthinkable.ResponseModels.CreateSessionResponse;

public class QueueSongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_song);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = ((EditText)findViewById(R.id.searchBar)).getText().toString();

                String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="+query+"&type=video&key=" + Constants.GOOGLE_API_KEY;

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(url, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseJson = new String(responseBody);
                        Log.d("TESTING 123", responseJson);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("TESTING 123", "failed to post");
                    }
                });

            }
        });
    }

}
