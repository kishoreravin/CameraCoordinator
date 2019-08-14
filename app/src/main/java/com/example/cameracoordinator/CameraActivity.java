package com.example.cameracoordinator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class CameraActivity extends AppCompatActivity {

    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    private CameraKitView cameraKitView;
    private String sideName, referalCode;
    private TextView SizeTextView;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAxtqQRtc:APA91bG77vKYy-e15slE2x6yMHZIKkml8XMLnC3ecHmHQlOTUKRPS4CzMVg8m7gFsLJmK4yDUzgFKS2a8TA2hkfyydT5o1ob9g_OYMKMdOGfCt1DBwgVirwkyaHRlThCEs3_xO_YoGno";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE = "Camera Clicked";
    String NOTIFICATION_MESSAGE = "Image Saved";
    String TOPIC;
    String token;

//    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//    DatabaseReference clickReference;


    int height, width = 250;

    public void setSize(int height, int width) {
        this.height = height;
        this.width = width;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean rec = intent.getBooleanExtra("received", true);
            token = intent.getStringExtra("token");
            if (rec) {
                captureImage();
                Toast.makeText(getApplicationContext(), "BroadcastReceived", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        cameraKitView = findViewById(R.id.camera);
        SizeTextView = findViewById(R.id.size_tv);

        Bundle bundle = getIntent().getExtras();
        sideName = bundle.getString("SideName");
        referalCode = bundle.getString("ReferalCode");


        FirebaseMessaging.getInstance().subscribeToTopic(referalCode)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "connected";
                        if (!task.isSuccessful()) {
                            msg = "Connection failed";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });

        //start camera if permission has been granted by user
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }


    }

    private void startCamera() {

        findViewById(R.id.imgCapture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendNotification();
            }

        });

    }

    public void captureImage() {
        final File savedPhoto = new File(Environment.getExternalStorageDirectory() + "/" + "ImageData", sideName + "_" + System.currentTimeMillis() + ".png");

        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
                try {
                    FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                    outputStream.write(capturedImage);
                    outputStream.close();
                    Toast.makeText(getApplicationContext(), "Image save to Imagedata directory", Toast.LENGTH_SHORT).show();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted() {

        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void sendNotification() {

        SendNotificationModel sendNotificationModel = new SendNotificationModel("Image Click Request", "Camera Coordinator", referalCode);
        RequestNotification requestNotificaton = new RequestNotification();
        requestNotificaton.setSendNotificationModel(sendNotificationModel);
        //token is id , whom you want to send notification ,
        requestNotificaton.setToken(token);

        ApiInterface apiService = BaseApiHelper.getClient().create(ApiInterface.class);
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(requestNotificaton);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("FCM", response.body().toString());
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.cameracoordinator");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraKitView.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

}


