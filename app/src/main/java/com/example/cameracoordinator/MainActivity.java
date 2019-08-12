package com.example.cameracoordinator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cameracoordinator.util.CreateReferalCode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button StartSync, StartCamera;
    private TextView ReferalCode;
    private EditText SideName;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference referal_code = firebaseDatabase.getReference().child("referalCodes");
    private String referalCode,sideName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartSync = findViewById(R.id.start_syncying);
        ReferalCode = findViewById(R.id.referal_code);
        StartCamera = findViewById(R.id.start_camera);
        SideName = findViewById(R.id.side_et);

        StartSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                referalCode = CreateReferalCode.getReferalCode();
                ReferalCode.setText(referalCode);
            }
        });


        StartCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sideName = SideName.getText().toString().trim();
                if(sideName != null){
                Intent intent = new Intent(getBaseContext(),CameraActivity.class);
                intent.putExtra("SideName",sideName);
                startActivity(intent);
            }else {
                    SideName.setError("Enter Side Name");
                }
            }
        });


    }
}
