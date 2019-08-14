package com.example.cameracoordinator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cameracoordinator.util.CreateReferalCode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button GetReferalCode, StartCamera;
    private TextView ReferalCode;
    private EditText SideName, EnteredReferalCode;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference referal_code = firebaseDatabase.getReference().child("referalCodes");
    private String referalCode, sideName, enteredReferalCode;
    private ImageButton GoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetReferalCode = findViewById(R.id.get_referal_code);
        ReferalCode = findViewById(R.id.referal_code_tv);
        StartCamera = findViewById(R.id.start_camera_button);
        SideName = findViewById(R.id.side_et);
        EnteredReferalCode = findViewById(R.id.entered_ref_code);
        GoButton = findViewById(R.id.go_button);

        GetReferalCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                referalCode = CreateReferalCode.getReferalCode();
                Toast.makeText(getApplicationContext(),referalCode,Toast.LENGTH_SHORT).show();
                ReferalCode.setText(referalCode);
            }
        });

        GoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sideName = SideName.getText().toString().trim();

                if (sideName != null && referalCode != null) {
                    Intent intent = new Intent(getBaseContext(), CameraActivity.class);
                    intent.putExtra("SideName", sideName);
                    intent.putExtra("ReferalCode", referalCode);
                    startActivity(intent);

                } else {

                    if ((sideName == null)) {
                        SideName.setError("Enter Side Name");
                    } else {
                        ReferalCode.setError("Enter Referal Code");
                    }
                }
            }
        });


        StartCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sideName = SideName.getText().toString().trim();
                enteredReferalCode = EnteredReferalCode.getText().toString().trim();

                if (sideName != null && enteredReferalCode != null) {
                    Intent intent = new Intent(getBaseContext(), CameraActivity.class);
                    intent.putExtra("SideName", sideName);
                    intent.putExtra("ReferalCode", enteredReferalCode);
                    startActivity(intent);
                } else {

                    if ((sideName == null)) {
                        SideName.setError("Enter Side Name");
                    } else {
                        EnteredReferalCode.setError("Enter Referal Code");
                    }
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
