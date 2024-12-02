package com.example.hellored;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdpter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView imglogout;
    ImageView cumbut, setbut;

    // ActivityResultLauncher for camera intent
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Log.d("CameraIntent", "Image captured successfully");
                    // Handle the image result here if needed
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Initialize Firebase and UI components
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        cumbut = findViewById(R.id.camBut);
        setbut = findViewById(R.id.settingBut);
        imglogout = findViewById(R.id.logoutimg);
        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);

        // RecyclerView setup
        usersArrayList = new ArrayList<>();
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdpter(MainActivity.this, usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);

        // Firebase Realtime Database reference
        DatabaseReference reference = database.getReference().child("user");

        // Fetch users from the database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear(); // Avoid duplicate entries
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    if (users != null) {
                        usersArrayList.add(users);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
            }
        });

        // Logout functionality
        imglogout.setOnClickListener(v -> {
            Dialog dialog = new Dialog(MainActivity.this, R.style.dialoge);
            try {
                dialog.setContentView(R.layout.dialog_layout);
                Button no, yes;
                yes = dialog.findViewById(R.id.yesbnt);
                no = dialog.findViewById(R.id.nobnt);

                yes.setOnClickListener(view -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, login.class);
                    startActivity(intent);
                    finish();
                });

                no.setOnClickListener(view -> dialog.dismiss());
                dialog.show();
            } catch (Exception e) {
                Log.e("DialogError", "Error showing logout dialog: " + e.getMessage());
            }
        });

        // Camera button functionality
        cumbut.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(intent);
        });

        // Settings button functionality
        setbut.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, setting.class);
            startActivity(intent);
        });

        // Check if user is authenticated
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
            finish();
        }
    }
}
