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

public class SearchRecipes extends AppCompatActivity {

    private RecyclerView RecRV ;
    private DatabaseReference database;
    private Boolean IsAdmin;
    private ArrayList<Recipes> RecList;
    private ArrayList<String> RecListkey;
    private imagRV_adapter ada;
    private SearchView searchView;
    private ArrayList<Ingredients> IngList;
    private String IngListString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);

        initData();
        SetAdapter(RecList);
        GetDataFromFirebase();
        search();

    }

    private void GetDataFromFirebase() {

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Recipes r = dataSnapshot.getValue(Recipes.class);
                    if ((r.isApproved()) && !RecListkey.contains(r.getKey())) {
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


        IsAdmin = getIntent().getExtras().getBoolean("isAdmin");
        database = FirebaseDatabase.getInstance().getReference("recipes");
        RecRV = findViewById(R.id.recRVSearchRecipes);
        RecRV.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.rspSearchViewSearchRecipes);

        RecList  = new ArrayList<>();
        RecListkey  = new ArrayList<>();

    }
    public void HomeBtnClickSearchRecipes(View view) {
        Intent intent = new Intent(SearchRecipes.this, MainActivity.class);
        intent.putExtra("isAdmin",IsAdmin);
        startActivity(intent);
    }
    private void SetAdapter(ArrayList<Recipes> arrList) {
        ada = new imagRV_adapter(arrList,SearchRecipes.this, IsAdmin);
        RecRV.setAdapter(ada);
    }
    // search recipes with serch view
    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Recipes> SearchRes = new ArrayList<>();
                for (Recipes rec: RecList){
                    if(rec.getRecipeName().toLowerCase().contains(newText.toLowerCase())){
                        SearchRes.add(rec);
                    }
                }
                SetAdapter(SearchRes);
                ada.notifyDataSetChanged();
                return false;
            }
        });

    }
}