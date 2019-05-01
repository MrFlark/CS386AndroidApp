package passtheaux.operationunthinkable;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import cz.msebera.android.httpclient.Header;
import passtheaux.operationunthinkable.RequestModels.GetSongListRequest;
import passtheaux.operationunthinkable.ResponseModels.CreateSessionResponse;
import passtheaux.operationunthinkable.ResponseModels.GetSongListResponse;
import passtheaux.operationunthinkable.ResponseModels.Song;

public class NowPlayingActivity extends AppCompatActivity {

    class DownloadFileTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int lengthOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), lengthOfFile);

                File f = new File(getFilesDir().toString() + "/download_test.mp3");
                f.createNewFile();

                OutputStream output = new FileOutputStream(getFilesDir().toString() + "/download_test.mp3");

                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            //play song
            try {
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(getFilesDir() + "/download_test.mp3");
                mediaPlayer.prepare();
                mediaPlayer.start();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = ((TextView)findViewById(R.id.title));
                        tv.setText(tv.getText() + "(playing...)");
                    }
                });
            } catch (Exception e) {

            }
        }
    }

    private static MediaPlayer mediaPlayer = null;
    private static boolean playing = false;
    private static int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((Button)findViewById(R.id.queueSongButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NowPlayingActivity.this, QueueSongActivity.class));
            }
        });

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

        findViewById(R.id.refreshButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSongList();
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

                if(!response.songs.isEmpty() && !playing)
                {
                    new NowPlayingActivity.DownloadFileTask().execute("http://josephsirna.org:81/dev/Data/GetSong?SongId=" + response.songs.get(0).id);
                }

                for(Song s : response.songs)
                {
                    String url = s.location.URL;
                    //todo predownload other songs
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("test", "test");
            }
        });
    }
}
