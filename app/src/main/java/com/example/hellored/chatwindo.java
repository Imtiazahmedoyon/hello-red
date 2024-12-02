package com.example.hellored;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatwindo extends AppCompatActivity {
    String reciverimg, reciverUid, reciverName, SenderUID;
    CircleImageView profile;
    TextView reciverNName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String senderImg;
    public static String reciverIImg;
    CardView sendbtn;
    EditText textmsg;
    RecyclerView messageAdpter;
    ArrayList<msgModelclass> messagesArrayList;
    messagesAdpter mmessagesAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwin);
        Objects.requireNonNull(getSupportActionBar()).hide();

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        messagesArrayList = new ArrayList<>();
        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);
        reciverNName = findViewById(R.id.recivername);
        profile = findViewById(R.id.profileimgg);
        messageAdpter = findViewById(R.id.msgadpter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdpter.setLayoutManager(linearLayoutManager);

        mmessagesAdpter = new messagesAdpter(chatwindo.this, messagesArrayList);
        messageAdpter.setAdapter(mmessagesAdpter);

        Picasso.get().load(reciverimg).into(profile);
        reciverNName.setText(reciverName);

        SenderUID = firebaseAuth.getUid();
        senderImg = FirebaseDatabase.getInstance().getReference().child("user").child(SenderUID).child("profilepic").toString();
        reciverIImg = reciverimg;

        sendbtn.setOnClickListener(view -> {
            String message = textmsg.getText().toString();
            if (message.isEmpty()) {
                Toast.makeText(chatwindo.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                return;
            }
            textmsg.setText("");
            Date date = new Date();
            msgModelclass messagess = new msgModelclass(message, SenderUID, date.getTime());

            DatabaseReference chatReference = database.getReference().child("chats").child(SenderUID + reciverUid).child("messages");
            chatReference.push().setValue(messagess);
        });
    }
}
