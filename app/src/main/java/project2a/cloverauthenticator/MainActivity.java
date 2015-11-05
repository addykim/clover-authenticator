package project2a.cloverauthenticator;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity {

    private SpassFingerprint mSpassFingerprint;
    private Context mContext;
    private TextView displayText;
    private WebSocketClient mWebSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayText = (TextView) findViewById(R.id.display_text);
        displayText.setText("Hello world!");


        mContext = this;
        Spass mSpass = new Spass();
        try {
            mSpass.initialize(mContext);
        } catch (SsdkUnsupportedException e) {
            Log.d("MAIN", "Exception: " + e.getMessage());
            // Error handling
        } catch (UnsupportedOperationException e){
            Log.d("MAIN", "Exception: " + e.getMessage());
            // Error handling
        }
        boolean isFeatureEnabled = mSpass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);

        if(isFeatureEnabled){
            mSpassFingerprint = new SpassFingerprint(mContext);
            mSpassFingerprint.setCanceledOnTouchOutside(true);
            mSpassFingerprint.startIdentifyWithDialog(mContext, listener, true);
//            boolean mHasRegisteredFinger = mSpassFingerprint.hasRegisteredFinger();
        } else {
            Log.d("Main Activity", "Fingerprint Service is not supported in the device.");
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
                    displayText.setText("Success");
                } else if (eventStatus == SpassFingerprint.
                        STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS) {
                    // Identify operation succeeded with alternative password
                    displayText.setText("Success");
                } else if (eventStatus == SpassFingerprint.STATUS_USER_CANCELLED_BY_TOUCH_OUTSIDE) {
                    displayText.setText("Touched outside");
                } else if (eventStatus == SpassFingerprint.STATUS_USER_CANCELLED) {
                    displayText.setText("User canceled");
                } else {
                    displayText.setText("FAILED");
                    // Identify operation failed with given eventStatus.
                    // STATUS_TIMEOUT_FAILED
                    // STATUS_AUTHENTIFICATION_FAILED
                    // STATUS_QUALITY_FAILED
                }
            }

            @Override
            public void onReady() {
                // It is called when fingerprint identification is ready after
                // startIdentify() is called.
                Log.d("MAIN", "ON READY");
            }

            @Override
            public void onStarted() {
                // It is called when the user touches the fingerprint sensor after
                // startIdentify() is called.
                Log.d("MAIN", "ON STARTED");

            }
        };


    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://websockethost:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayText.setText(displayText.getText() + "\n" + message);
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
}

