package com.example.ktsitsa;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class AddRecipes extends AppCompatActivity {



    private String recipeName, recipeIngredients, recipeDescription;
    private FirebaseDatabase db;
    private DatabaseReference dbr;
    private ActivityResultLauncher<String> mGetImage;
    private StorageReference STR;
    private ProgressDialog progressDialog;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private String Uid,IngListString ;
    private Boolean IsAdmin;
    private TextView IngString;
    private ArrayList<Ingredients> IngList;

    //Sets the the screen of adding recepie
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initdata();
        SelectImage();

    }

    private void SelectImage() {
        // select image from phone.
        mGetImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                ((ImageView)findViewById(R.id.imageView6)).setImageURI(result);
                imageUri = result;

            }
        });
    }

    private void initdata() {
        //Gets info from FireBase
        mAuth = FirebaseAuth.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        //Gets info from previus page
        IsAdmin = getIntent().getExtras().getBoolean("isAdmin");
        IngList = getIntent().getExtras().getParcelableArrayList("IngList");
        IngListString = IngList.toString();

        // get ingredients and set them as text.
        IngString = findViewById(R.id.ingrid_text);
        IngString.setText(IngListString.substring(1,IngListString.length()-1));
        IngString.setOnClickListener(v -> onBackPressed());

    }
    //Retunrs to home page
    public void HomeBtnClick(View view) {
        Intent intent = new Intent(AddRecipes.this, MainActivity.class);
        intent.putExtra("isAdmin",IsAdmin);
        startActivity(intent);

    }
    //Add recepie to FireBase
    public void add(View view) {
        //init data
        recipeName = ((EditText) findViewById(R.id.TxtTitle)).getText().toString();
        recipeIngredients =  ingToString(IngListString);
        recipeDescription = ((EditText) findViewById(R.id.txtInstructions)).getText().toString();
        //Sets name for image file by date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        //Checks all fields arent empty before uploading recepie to FireBase
        if(!recipeName.isEmpty() && !recipeIngredients.isEmpty() && !recipeDescription.isEmpty() && (imageUri != null) &&(Uid != null)){

            upimage(fileName);
            db = FirebaseDatabase.getInstance();
            dbr = db.getReference("recipes");
            //Upload recepie with link (File name) to FireBase storage
            DatabaseReference pushrecipes = dbr.push();
            Recipes r = new Recipes(recipeName,recipeDescription,recipeIngredients,"images/" + fileName + ".jpeg", pushrecipes.getKey(), Uid);

            pushrecipes.setValue(r).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(AddRecipes.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                    //After adding recepie returns to main page
                    Intent intent = new Intent(AddRecipes.this, MainActivity.class);
                    intent.putExtra("isAdmin",IsAdmin);
                    startActivity(intent);
                }
            });


        }
        else{
            Toast.makeText(this, "missing values, please fill all the fields and upload image ", Toast.LENGTH_SHORT).show();
        }

    }
    //Upload image to FireBase storage
    private void upimage(String fileName) {

        progressDialog = new ProgressDialog(AddRecipes.this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();



        STR = FirebaseStorage.getInstance().getReference("images/" + fileName + ".jpeg");
        STR.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        ((ImageView)findViewById(R.id.imageView6)).setImageURI(null);
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(AddRecipes.this,"העלאה נכשלה",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Select image from phone
    public void addimage(View view) {

        mGetImage.launch("image/*");

    }
    //Overide arrary to string ingredients
    public String ingToString(String ingredientsString){
        String ans = "";
        String[] splitString = ingredientsString.substring(1,ingredientsString.length() -1).split(",");
        for(String s: splitString){
            ans += s + "\n";
        }

        return ans;
    }




}