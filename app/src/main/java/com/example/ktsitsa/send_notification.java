package com.example.ktsitsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class send_notification extends AppCompatActivity {


    private Button notify_button, custom_notify_btn;
    private DatabaseReference database;
    private ArrayList<String> RecList;
    private String RandRec, RandTitle, RandMess, Stitle, Smessage;
    private EditText title, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        initData();
        getDatafromFirebase();
        send();



    }

    private String randomData() {
        String pubRandomsen;
        ArrayList<String> Randomsen= new ArrayList<>();
        Randomsen.add("טעמתם כבר את ה-");
        Randomsen.add("אתם חייבים לטעום את ה-");
        Randomsen.add("עוד לא נרגענו מה");
        Randomsen.add("אל תשארו רעבים כנסו לטעום את ה-");
        Randomsen.add("היום זה יום של ");
        int index2 = (int) (Math.random() * Randomsen.size());
        pubRandomsen = Randomsen.get(index2);
        return pubRandomsen;

    }

    private void getDatafromFirebase() {
        database.addValueEventListener(new ValueEventListener() {
            //checks if data has been changed on firebase and update if it did
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipes r = dataSnapshot.getValue(Recipes.class);
                    if ((r.isApproved()) && r.isRecommended()) {
                        RecList.add(r.getRecipeName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void send() {
        notify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RecList.size()>0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(send_notification.this);
                    builder.setTitle(" שליחת התראה אוטומטית            ");
                    builder.setCancelable(false);
                    builder.setMessage("שלחיחת התראה אוטומטית לא תתיחס לשדות המותאמים אישית ותשלח מתכון מומלץ רנדומלי!").setPositiveButton("שלח!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int index = (int) (Math.random() * RecList.size());
                            RandRec = RecList.get(index);
                            FcmNotificationsSender fsend = new FcmNotificationsSender("/topics/all", "המלצת היום בקציצה!", randomData() + RandRec +"\nעכשיו במומלצים", getApplicationContext(), send_notification.this);
                            fsend.SendNotifications();
                            Toast.makeText(send_notification.this, "פורסם!", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();


                }else{
                    Toast.makeText(send_notification.this, "אין מתכונים מומלצים!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        custom_notify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Stitle = title.getText().toString();
                Smessage = message.getText().toString();

                if(!Stitle.isEmpty() && !Smessage.isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(send_notification.this);
                    builder.setTitle(" שליחת התראה                ");
                    builder.setCancelable(false);
                    builder.setMessage("האם לשלוח התראה זו לכלל המשתמשים?").setPositiveButton("שלח!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FcmNotificationsSender fsend = new FcmNotificationsSender("/topics/all", Stitle, Smessage, getApplicationContext(), send_notification.this);
                            fsend.SendNotifications();
                            title.setText("");
                            message.setText("");
                            Toast.makeText(send_notification.this, "פורסם!", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();


                }else{
                    Toast.makeText(send_notification.this, "מלא את השדות כותרת ותוכן ההתראה!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initData() {
        RecList = new ArrayList<>();
        notify_button = findViewById(R.id.send_notify_btn);
        custom_notify_btn = findViewById(R.id.send_notify_btn_Custom);
        database = FirebaseDatabase.getInstance().getReference("recipes");
        title = findViewById(R.id.TxtTitlenotify);
        message = findViewById(R.id.txtmessnotify);

    }


    //Returns to main menu screen
    public void HomeBtnClick(View view) {
        Intent intent = new Intent(send_notification.this, MainActivity.class);
        startActivity(intent);
    }
}