package project2a.cloverauthenticator;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity {
// Uncomment this if you want to get the regid for other devices
//public class MainActivity extends AppCompatActivity implements OnClickListener {

    static final String TAG = "MAIN";
    static final String EDGEREGID= "APA91bHqOHYn8Jjl2G2g2bpZFOfmkS_E334JyJ5W0-oHdDastaupYtdt0GYAbOQD5NvtPt04xTMHUB-R7P21BbsLeKcPqov6z9a7t8amxEggpFeHia7nrb0pEY9TTybu2KoBu0jSy1rI";
    static String PROJECT_NUMBER = "501746165158";


    private SpassFingerprint mSpassFingerprint;
    private Context mContext;
    private WebSocketClient mWebSocketClient;

    protected TextView statusText;

//    GoogleCloudMessaging gcm;
//    String regid;
//    Button btRegId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = (TextView) findViewById(R.id.display_text);
        statusText.setText("Authenticating...");

//        btRegId = (Button) findViewById(R.id.btnGetRegId);
//        btRegId.setOnClickListener(this);

        mContext = this;
        Spass mSpass = new Spass();
        try {
            mSpass.initialize(mContext);
        } catch (SsdkUnsupportedException e) {
            Log.d(TAG, "SSDK Unsupported Ex: " + e.getMessage());
            // Error handling
        } catch (UnsupportedOperationException e){
            Log.d(TAG, "Unsupported Operation: " + e.getMessage());
            // Error handling
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e);
        }
        boolean isFeatureEnabled = mSpass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);


        Log.d(TAG, "=============ATTEMPTING TO CONNECT TO WEBSOCKET");
        connectWebSocket();

        if(isFeatureEnabled){
            mSpassFingerprint = new SpassFingerprint(mContext);
            mSpassFingerprint.setCanceledOnTouchOutside(true);
            mSpassFingerprint.startIdentifyWithDialog(mContext, listener, true);
        } else {
            statusText.setText("This phone doesn't support fingerprinting");
            mWebSocketClient.send("This phone does not support fingerprinting");
            Log.i(TAG, "Fingerprint Service is not supported in the device.");
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
                if (eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS ||
                        eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS) {
                    // Identify operation succeeded with fingerprint or alternative password
                    statusText.setText("Success");
                    mWebSocketClient.send("Success");
                } else {
                    statusText.setText("FAILED");
                    mWebSocketClient.send("Error");
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
                Log.d(TAG, "ON READY");
            }

            @Override
            public void onStarted() {
                // It is called when the user touches the fingerprint sensor after
                // startIdentify() is called.
                Log.d(TAG, "ON STARTED");
            }
        };



    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://10.0.0.210:8080");
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
                        statusText.setText(statusText.getText() + "\n" + message);
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

    /* This is used to get the registration id for other devices. For now we have hard coded the
        registration id for the Edge demo device we are using.
     */
//    private void getRegId() {
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                String msg = "";
//                try {
//                    if (gcm == null) {
//                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
//                    }
//                    regid = gcm.register(PROJECT_NUMBER);
//                    msg = "======================Device registered, registrtaion ID=" + regid;
//                    Log.i("GCM", msg);
//
//                } catch (IOException e) {
//                    msg = "Error: " + e.getMessage();
//                }
//                return msg;
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
//                statusText.setText("regid: " + regid);
//
//            }
//        }.execute(null, null, null);
//    }
//
//    @Override
//    public void onClick(View v) {
//        getRegId();
//    }

}

