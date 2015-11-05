package project2a.cloverauthenticator;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;


public class MainActivity extends AppCompatActivity {

    private SpassFingerprint mSpassFingerprint;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mContext = this;
        Spass mSpass = new Spass();
        try {
            mSpass.initialize(MainActivity.this);
        } catch (SsdkUnsupportedException e) {
            // Error handling
        } catch (UnsupportedOperationException e){
            // Error handling
        }
        boolean isFeatureEnabled = mSpass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);

        if(isFeatureEnabled){
            mSpassFingerprint = new SpassFingerprint(MainActivity.this);
        } else {
//            Log.d("Main Activity", "Fingerprint Service is not supported in the device.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private SpassFingerprint.IdentifyListener listener =
        new SpassFingerprint.IdentifyListener() {
            @Override
            public void onFinished(int eventStatus) {
                // It is called when fingerprint identification is finished.
                if (eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS) {
                    // Identify operation succeeded with fingerprint

                } else if (eventStatus == SpassFingerprint.
                        STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS) {
                    // Identify operation succeeded with alternative password
                } else {
                    // Identify operation failed with given eventStatus.
                    // STATUS_TIMEOUT_FAILED
                    // STATUS_USER_CANCELLED
                    // STATUS_AUTHENTIFICATION_FAILED
                    // STATUS_QUALITY_FAILED
                }
            }

            @Override
            public void onReady() {
                // It is called when fingerprint identification is ready after
                // startIdentify() is called.
            }

            @Override
            public void onStarted() {
                // It is called when the user touches the fingerprint sensor after
                // startIdentify() is called.
            }
        };
}

