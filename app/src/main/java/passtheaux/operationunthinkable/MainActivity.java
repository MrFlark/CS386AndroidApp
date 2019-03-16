package passtheaux.operationunthinkable;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import passtheaux.operationunthinkable.RequestModels.CreateSessionRequest;
import passtheaux.operationunthinkable.RequestModels.QueueSongRequest;
import passtheaux.operationunthinkable.RequestModels.Request;
import passtheaux.operationunthinkable.ResponseModels.CreateSessionResponse;
import passtheaux.operationunthinkable.ResponseModels.QueueSongResponse;
import passtheaux.operationunthinkable.ResponseModels.Response;

public class MainActivity extends AppCompatActivity {

    // Variables used for Web Script
    String youtubeURL;
    // Variables used within Android Studio display
    TextView title;
    TextView instructions;
    EditText youtubeLink;
    Button queueButton;

    AsyncHttpClient client = new AsyncHttpClient();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This will save the ID of youtubeLink to be manipulated later
        youtubeLink = (EditText) findViewById(R.id.youtubeLink);
        // This saves the button ID to be used for OnClick listener
        queueButton = (Button) findViewById(R.id.queueButton);
        // Use the built in 'setOnClickListener' to determine what happens
        // when the Queue Button is clicked.

        //This stores the input youtubeLink to youtubeURL
        queueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Must convert text to string or it will not be clean
                youtubeURL = youtubeLink.getText().toString();

                Log.v("TAG", "button clicked");

                CreateSessionRequest req = new CreateSessionRequest();
                req.displayName = "display name";
                req.name = "name";
                client.post(req.url, new RequestParams(req.Serialize()), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        String responseJson = new String(responseBody);

                        Log.v("TAG", responseJson);

                        final CreateSessionResponse response = new Gson().fromJson(responseJson, CreateSessionResponse.class);

                        Log.v("TAG", "callback 2");

                        //joined session, now queue song


                        QueueSongRequest req2 = new QueueSongRequest();
                        req2.clientId = response.clientId;
                        req2.source = "YouTube";
                        req2.songUrl = youtubeURL;

                        client.post(req2.url, new RequestParams(req2.Serialize()), new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                String responseJson = new String(responseBody);

                                Log.v("TAG", responseJson);

                                final QueueSongResponse response = new Gson().fromJson(responseJson, QueueSongResponse.class);

                                Log.v("TAG", "callback 2");

                                String songId = response.songs.get(0).id.toString();

                                //get mp3
                                new DownloadFileTask().execute("http://josephsirna.org:81/dev/Data/GetSong?SongId=" + songId);
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