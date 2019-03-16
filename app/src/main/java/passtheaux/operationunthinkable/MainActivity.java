package passtheaux.operationunthinkable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import passtheaux.operationunthinkable.RequestModels.CreateSessionRequest;
import passtheaux.operationunthinkable.RequestModels.Request;
import passtheaux.operationunthinkable.ResponseModels.CreateSessionResponse;
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

    private void SendPostRequest(final Request req, final HttpCallback callback) {

        RequestParams params = new RequestParams(req.Serialize());

        client.post(req.url, params, new BaseJsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                callback.callback(req.ResponseModel.getClass().cast(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                ///TODO
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return new Gson().fromJson(rawJsonData, req.ResponseModel.getClass());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateSessionRequest req = new CreateSessionRequest();
        req.displayName = "display name";
        req.name = "name";

        SendPostRequest(req, new HttpCallback() {
            @Override
            public void callback(Response r) {

                final CreateSessionResponse response = CreateSessionResponse.class.cast(r);

                //joined session
                runOnUiThread(new Runnable() {
                    public void run() {
                        //Update UI elements on the UI thread
                        ((TextView) findViewById(R.id.title)).setText("[TEST] ClientId=" + response.ClientId.toString());
                    }
                });
            }
        });

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
            }
        });

    }


}
