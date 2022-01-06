package com.example.ktsitsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Search_Options extends AppCompatActivity {

    private Button name,ing;
    private boolean IsAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_options);


        initbutton();

        //Each button nevigates to a diffrent screen
        ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_Options.this, Search_by_Ingredient.class);
                intent.putExtra("isAdmin",IsAdmin);
                startActivity(intent);
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_Options.this, SearchRecipes.class);
                intent.putExtra("isAdmin",IsAdmin);
                startActivity(intent);
            }
        });
    }
    private void initbutton() {
        IsAdmin = getIntent().getExtras().getBoolean("isAdmin");
        name = findViewById(R.id.buttonSearchName);
        ing = findViewById(R.id.buttonSearchIng);

    }
}