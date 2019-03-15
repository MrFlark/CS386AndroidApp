package passtheaux.operationunthinkable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Variables used for Web Script
    String youtubeURL;
    // Variables used within Android Studio display
    TextView title;
    TextView instructions;
    EditText youtubeLink;
    Button queueButton;




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
            }
        });

    }



}
