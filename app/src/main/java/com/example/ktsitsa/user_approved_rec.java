package com.example.ktsitsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class user_approved_rec extends AppCompatActivity {


    private RecyclerView RecRV ;
    private DatabaseReference database;
    private Boolean IsAdmin;
    private FirebaseAuth mAuth;
    private String Uid;
    private ArrayList<Recipes> RecList;
    private ArrayList<String> RecListkey;
    private imagRV_adapter ada;


    public void HomeBtnClick(View view) {
        Intent intent = new Intent(user_approved_rec.this, MainActivity.class);
        intent.putExtra("isAdmin",IsAdmin);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_approved_rec);

        initData();
        setadapter();
        getDataFromFireBase();

    }

    private void setadapter() {
        ada = new imagRV_adapter(RecList,user_approved_rec.this, IsAdmin);
        RecRV.setAdapter(ada);
    }

    private void getDataFromFireBase() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Recipes r = dataSnapshot.getValue(Recipes.class);

                    if ((r.isApproved() && r.getUid().equals(Uid)) && !RecListkey.contains(r.getKey())) {
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

    private void initData() {
        mAuth = FirebaseAuth.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        IsAdmin = getIntent().getExtras().getBoolean("isAdmin");

        database = FirebaseDatabase.getInstance().getReference("recipes");
        RecRV = findViewById(R.id.recRVapproved);
        RecRV.setLayoutManager(new LinearLayoutManager(this));

        RecList  = new ArrayList<>();
        RecListkey  = new ArrayList<>();
    }
}