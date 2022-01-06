package com.example.ktsitsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private Button b1,b2,adding,approving, send_notify_bnt;
    private Boolean IsAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initbutton();
        // If the current user is Admin show the Admin Buttons
        Admin_Buttons();

        ClickListeners();

    }

    private void Admin_Buttons() {
        if(IsAdmin){
            adding.setVisibility(View.VISIBLE);
            approving.setVisibility(View.VISIBLE);
            send_notify_bnt.setVisibility(View.VISIBLE);
        }
        else {
            b2.setVisibility(View.VISIBLE);
        }
    }

    private void ClickListeners() {
        adding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AddIngredient.class);
                intent.putExtra("isAdmin",IsAdmin);
                startActivity(intent);
            }
        });

        approving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, Waiting_For_Approve.class);
                intent.putExtra("isAdmin",IsAdmin);
                startActivity(intent);
            }
        });

//
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, user_approved_rec.class);
                intent.putExtra("isAdmin",IsAdmin);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, user_not_approved_rec.class);
                intent.putExtra("isAdmin",IsAdmin);
                startActivity(intent);
            }
        });
        send_notify_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, send_notification.class);
                intent.putExtra("isAdmin",IsAdmin);
                startActivity(intent);
            }
        });

    }

    public void HomeBtnClick(View view) {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.putExtra("isAdmin",IsAdmin);
        startActivity(intent);

    }

    public void LogOut(View view) {
         FirebaseAuth.getInstance().signOut();
         startActivity(new Intent(this, LogIn.class));
         finish();

    }

    private void initbutton() {
        b1 = findViewById(R.id.approvedrec);
        b2 = findViewById(R.id.notapprovedrec);
        adding = findViewById(R.id.Btnadding1);
        approving = findViewById(R.id.BtnAdminNotApp);
        send_notify_bnt = findViewById(R.id.sendnot);
        IsAdmin = getIntent().getExtras().getBoolean("isAdmin");

    }
}