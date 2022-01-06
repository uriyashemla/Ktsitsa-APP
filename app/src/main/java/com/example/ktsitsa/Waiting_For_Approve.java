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

public class Waiting_For_Approve extends AppCompatActivity {


    private RecyclerView RecRV ;
    private DatabaseReference database;
    private Boolean IsAdmin;
    private FirebaseAuth mAuth;
    private ArrayList<Recipes> RecList;
    private ArrayList<String> RecListkey;
    private imagRV_adapter ada;

    public void HomeBtnClick(View view) {
        Intent intent = new Intent(Waiting_For_Approve.this, MainActivity.class);
        intent.putExtra("isAdmin",IsAdmin);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_not_approved_rec);

        initData();
        Setadapter();
        GetdataFromFirebase();

    }

    private void GetdataFromFirebase() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Recipes r = dataSnapshot.getValue(Recipes.class);

                    if (!r.isApproved()  && !RecListkey.contains(r.getKey())) {
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
        IsAdmin = getIntent().getExtras().getBoolean("isAdmin");

        database = FirebaseDatabase.getInstance().getReference("recipes");
        RecRV = findViewById(R.id.recRVnotapproved);

        RecRV.setLayoutManager(new LinearLayoutManager(this));

        RecList  = new ArrayList<>();
        RecListkey  = new ArrayList<>();
    }

    private void Setadapter() {
        ada = new imagRV_adapter(RecList,Waiting_For_Approve.this, IsAdmin);
        RecRV.setAdapter(ada);
    }
}