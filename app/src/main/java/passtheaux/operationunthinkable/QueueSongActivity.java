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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import passtheaux.operationunthinkable.RequestModels.QueueSongRequest;
import passtheaux.operationunthinkable.ResponseModels.CreateSessionResponse;
import passtheaux.operationunthinkable.ResponseModels.YouTubeItem;
import passtheaux.operationunthinkable.ResponseModels.YouTubeSearchResult;

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

                final AsyncHttpClient client = new AsyncHttpClient();
                client.get(url, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseJson = new String(responseBody);
                        Log.d("TESTING 123", responseJson);

                        LinearLayout resultsView = ((LinearLayout)findViewById(R.id.results_view));
                        resultsView.removeAllViews();

                        YouTubeSearchResult result = new Gson().fromJson(responseJson, YouTubeSearchResult.class);

                        int id_counter = 0;
                        for(YouTubeItem item : result.items){

                            String title = item.snippet.title;
                            final String videoId = item.id.videoId;

                            Button b = new Button(QueueSongActivity.this);
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    QueueSongRequest req2 = new QueueSongRequest();
                                    req2.clientId = Constants.ClientId;
                                    req2.songUrl = "https://youtube.com/watch?v=" + videoId;
                                    req2.source = "YouTube";

                                    client.post(req2.url, new RequestParams(req2.Serialize()), new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                        }
                                    });
                                }
                            });
                            b.setText(item.snippet.title);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);

                            id_counter++;
                            b.setId(id_counter);
                            resultsView.addView(b, params);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("TESTING 123", "failed to post:" + new String(responseBody));
                    }
                });

            }
        });
    }

}
