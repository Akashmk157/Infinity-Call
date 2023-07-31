package com.example.infinitycall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class DashboardActivity extends AppCompatActivity {

    EditText secretCodeBox;
    Button joinBtn, shareBtn;
    View buttonLogout;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        auth = FirebaseAuth.getInstance();

        secretCodeBox = findViewById(R.id.codeBox);
        joinBtn = findViewById(R.id.joinBtn);
        shareBtn = findViewById(R.id.shareBtn);
        buttonLogout = findViewById(R.id.buttonLogOut);

        URL serverURL;

        try {
            serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions
                    = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL)
                    .setFeatureFlag("welcomepage.enabled", false)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!secretCodeBox.getText().toString().isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Secret Code");
                    intent.putExtra(Intent.EXTRA_TEXT,"Infinity Secret Code: "+ secretCodeBox.getText().toString());
                    startActivity(Intent.createChooser(intent,"share via"));
                }
                else{
                    secretCodeBox.setError("Enter Secret Code");
                    Toast.makeText(DashboardActivity.this, "Please set Secret Code..", Toast.LENGTH_SHORT).show();
                }

            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!secretCodeBox.getText().toString().isEmpty()){
                    JitsiMeetConferenceOptions options
                            = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(secretCodeBox.getText().toString())
                            .build();
                    JitsiMeetActivity.launch(DashboardActivity.this, options);
                }
                else{
                    secretCodeBox.setError("Enter Secret Code");
                    Toast.makeText(DashboardActivity.this, "Please set Secret Code..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
            }
        });
    }
}