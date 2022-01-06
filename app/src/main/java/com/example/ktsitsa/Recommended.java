package com.example.ktsitsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class Recommended extends AppCompatActivity {

    private RecyclerView RecRV ;
    private DatabaseReference database;
    private Boolean IsAdmin;
    private ArrayList<Recipes> RecList;
    private ArrayList<String> RecListkey;
    private imagRV_adapter ada;
    private SearchView searchView;
    private ArrayList<Ingredients> IngList;
    private String IngListString;


    //Returns to main menu screen
    public void HomeBtnClick(View view) {
        Intent intent = new Intent(Recommended.this, MainActivity.class);
        intent.putExtra("isAdmin",IsAdmin);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultssrearchbying);

        initData();

        SetAdapter(RecList);

        GetDataFromFirebase();




    }



    private void GetDataFromFirebase() {
        database.addValueEventListener(new ValueEventListener() {
            //checks if data has been changed on firebase and update if it did
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Recipes r = dataSnapshot.getValue(Recipes.class);
                    if ((r.isApproved()) && !RecListkey.contains(r.getKey()) && r.isRecommended()) {
                        RecList.add(r);
                        RecListkey.add(r.getKey());
                    }
                }

                ada.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SetAdapter(ArrayList<Recipes> arrList) {
        ada = new imagRV_adapter(arrList, Recommended.this, IsAdmin);
        RecRV.setAdapter(ada);
    }

    private void initData() {
        IsAdmin = getIntent().getExtras().getBoolean("isAdmin");
        IngList = getIntent().getExtras().getParcelableArrayList("IngList");
        if(IngList!=null){
            String tempString = IngList.toString();
            IngListString = tempString.substring(1, tempString.length()-1);
        }

        database = FirebaseDatabase.getInstance().getReference("recipes");
        RecRV = findViewById(R.id.recRV);
        RecRV.setLayoutManager(new LinearLayoutManager(this));
        RecList  = new ArrayList<>();
        RecListkey  = new ArrayList<>();

    }
}