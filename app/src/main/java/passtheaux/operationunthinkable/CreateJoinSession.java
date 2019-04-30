package passtheaux.operationunthinkable;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.plattysoft.leonids.ParticleSystem;

public class CreateJoinSession extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_session);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ParticleSystem(CreateJoinSession.this, 5000, R.drawable.confetti, 2000)
                        .setSpeedRange(0.2f, 0.7f)
                        .oneShot(fab, 500);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToJoin = new Intent(CreateJoinSession.this, JoinSession.class);

                startActivity(goToJoin);
            }
        });
    }

    public void goToQueue(View view)
    {
        startActivity(new Intent(this, CreateSessionActivity.class));
    }
}