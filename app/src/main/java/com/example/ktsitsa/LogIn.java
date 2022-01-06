package com.example.ktsitsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.time.Instant;

public class LogIn extends AppCompatActivity {


    private EditText TxtUser, TxtPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        initdata();
    }

    private void initdata() {
        TxtUser= (EditText)findViewById(R.id.TxtEmailAdd);
        TxtPassword= (EditText)findViewById(R.id.TxtPasswordAdd);
        //Gets UserName and Password
        String User =TxtUser.getText().toString();
        String Password =TxtPassword.getText().toString();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if the user is allredy in (dident sign out)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("Admin");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String admins = snapshot.getValue().toString();
                    if(admins.equals("1")) {
                        Toast.makeText(LogIn.this, "login as Admin!", Toast.LENGTH_LONG).show();
                        Intent Aintent = new Intent(LogIn.this, MainActivity.class);
                        Aintent.putExtra("isAdmin", true);
                        startActivity(Aintent);

                    }else{
                        Intent NAintent = new Intent(LogIn.this, MainActivity.class);
                        NAintent.putExtra("isAdmin", false);
                        startActivity(NAintent);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
    }



    public void sign_up_click(View view) {
        // takes the text from the login lines and convert them to string
        EditText email = (EditText) findViewById(R.id.TxtEmailAdd);
        EditText Password = (EditText) findViewById(R.id.TxtPasswordAdd);
        String StrPassword = Password.getText().toString();
        String StrEmail = email.getText().toString();
        //checks login conditions
        if(!StrEmail.equals("") && !StrPassword.equals("") &&  StrPassword.length() >= 6 && StrEmail.contains("@") &&  StrEmail.contains(".")) {


            mAuth.createUserWithEmailAndPassword(StrEmail, StrPassword).addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //moves to the main page
                        Intent NAintent = new Intent(LogIn.this, MainActivity.class);
                        NAintent.putExtra("isAdmin", false);
                        startActivity(NAintent);

                        //Set user authorization
                        String uid = mAuth.getCurrentUser().getUid();
                        database = FirebaseDatabase.getInstance().getReference("users").child(uid).child("Admin");
                        database.setValue("0");

                    } else {
                        Toast.makeText(LogIn.this, "לא ניתן להירשם עם פרטים אלה אנא בדוק שם משתמש וסיסמה תקינים!", Toast.LENGTH_LONG).show();// הודעה
                    }
                }
            });
        }
        else if(!StrEmail.contains("@") ||  !StrEmail.contains(".")){
            Toast.makeText(this, "שם המשתמש צריך להיות אימייל תקין!", Toast.LENGTH_SHORT).show();
        }
        else if(StrPassword.length() < 6){
            Toast.makeText(this, "הסיסמה צריכה להיות באורך 6 תווים לפחות!", Toast.LENGTH_SHORT).show();
        }

    }


    public void sign_in_click(View view) {
        EditText email = (EditText) findViewById(R.id.TxtEmailAdd);
        String StrEmail = email.getText().toString();
        EditText Password = (EditText) findViewById(R.id.TxtPasswordAdd);
        String StrPassword = Password.getText().toString();
        if(!StrEmail.equals("") && !StrPassword.equals("")) {


            mAuth.signInWithEmailAndPassword(StrEmail, StrPassword).addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {

                //Login and checks if the user is admin and contiue to main page
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid()).child("Admin");
                        database.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String admins = snapshot.getValue().toString();
                                if (admins.equals("1")) {
                                    Toast.makeText(LogIn.this, "login as Admin!", Toast.LENGTH_LONG).show();
                                    Intent Aintent = new Intent(LogIn.this, MainActivity.class);
                                    Aintent.putExtra("isAdmin", true);
                                    startActivity(Aintent);

                                } else {
                                    Intent NAintent = new Intent(LogIn.this, MainActivity.class);
                                    NAintent.putExtra("isAdmin", false);
                                    startActivity(NAintent);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    } else {
                        Toast.makeText(LogIn.this, "לא ניתן להתחבר עם פרטים אלה!", Toast.LENGTH_LONG).show();// הודעה
                    }
                }
            });
        }
    }

}