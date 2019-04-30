package passtheaux.operationunthinkable;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import passtheaux.operationunthinkable.RequestModels.GetSongListRequest;
import passtheaux.operationunthinkable.ResponseModels.CreateSessionResponse;
import passtheaux.operationunthinkable.ResponseModels.GetSongListResponse;
import passtheaux.operationunthinkable.ResponseModels.Song;

public class NowPlayingActivity extends AppCompatActivity {

    private static MediaPlayer mediaPlayer = null;
    private static boolean playing = false;
    private static int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((Button)findViewById(R.id.pauseButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ((Button)findViewById(R.id.pauseButton)).setText(playing ? "play" : "pause");

                        if(playing) {
                            currentPosition = mediaPlayer.getCurrentPosition();
                            mediaPlayer.pause();
                        }else{
                            if(currentPosition != -1) {
                                mediaPlayer.seekTo(currentPosition);
                            }
                            mediaPlayer.start();
                        }

                        playing = !playing;
                    }
                });
            }
        });
    }

    private void refreshSongList(){
        final AsyncHttpClient client = new AsyncHttpClient();
        GetSongListRequest req = new GetSongListRequest();
        req.sessionId = Constants.SessionId;

        client.post(req.url, new RequestParams(req.Serialize()), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseJson = new String(responseBody);

                Log.v("TAG", responseJson);

                final GetSongListResponse response = new Gson().fromJson(responseJson, GetSongListResponse.class);
                for(Song s : response.Songs)
                {
                    String url = s.location.URL;
                    //todo download song
                }

                Log.v("TAG", "callback 2");

                startActivity(new Intent(NowPlayingActivity.this, NowPlayingActivity.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
