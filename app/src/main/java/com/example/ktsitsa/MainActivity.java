package com.example.ktsitsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button Search_B, Recommended_B, Upload_B, Settings_B;
    private Boolean IsAdmin;

    //Sets the main menu page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initbutton();
        Buttons_Click();


    }
    //Each button nevigates to a diffrent screen
    private void Buttons_Click() {
        Search_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Search_Options.class);
                intent.putExtra("isAdmin",IsAdmin);
                startActivity(intent);
            }
        });
        Recommended_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Recommended.class);
                intent.putExtra("isAdmin",IsAdmin);
                startActivity(intent);
            }
        });
        Upload_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Ingredients_CheckBox.class);
                intent.putExtra("isAdmin",IsAdmin);
                startActivity(intent);
            }
        });
        Settings_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("isAdmin",IsAdmin);
                startActivity(intent);
            }
        });
    }

    private void initbutton() {
        IsAdmin = getIntent().getExtras().getBoolean("isAdmin");
        Search_B = findViewById(R.id.buttonrecipes);
        Recommended_B = findViewById(R.id.buttonrec);
        Upload_B = findViewById(R.id.buttonADD);
        Settings_B = findViewById(R.id.buttonsettings);
    }
}