package com.example.analyticsassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    long startTime;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    CollectionReference timerRef;
    CollectionReference usersRef;
    String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firestore  = FirebaseFirestore.getInstance();
        timerRef = firestore.collection("timeTracker");
        usersRef = firestore.collection("users");
         auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Map map = new HashMap();
                        map.put("totalTimeSpentInApp",0);
                        Map catMap = new HashMap();
                        catMap.put("electronics",0);
                        catMap.put("clothing",0);
                        catMap.put("makeup",0);
                        catMap.put("smartphone",0);
                        map.put("categories",catMap);
                        usersRef.document(auth.getCurrentUser().getUid()).set(map);
                        createUserScreenTimeRecorder("MainActivity");
                    }
                }
            });
        }else{
            createUserScreenTimeRecorder("MainActivity");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RelativeLayout electronicsLayout = findViewById(R.id.electronicsLayout);
        RelativeLayout clothingLayout = findViewById(R.id.clothingLayout);
        RelativeLayout makeupLayout = findViewById(R.id.makeupLayout);
        RelativeLayout smartphoneLayout = findViewById(R.id.smartphoneLayout);

        electronicsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ProductsActivity.class).putExtra("category","electronics"));
            }
        });


        clothingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ProductsActivity.class).putExtra("category","clothing"));

            }
        });

        makeupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ProductsActivity.class).putExtra("category","makeup"));

            }
        });
        smartphoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ProductsActivity.class).putExtra("category","smartphone"));

            }
        });

    }


    @Override
    protected void onStart() {
        startTime = System.currentTimeMillis()/1000;
        super.onStart();
    }

    @Override
    protected void onStop() {
        incrementUserScreenTime(System.currentTimeMillis()/1000 - startTime,"MainActivity");
        super.onStop();
    }


public void createUserScreenTimeRecorder(final String activityName){
    currentUserId = auth.getCurrentUser().getUid();
    timerRef.document(currentUserId+"-"+activityName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if(!task.getResult().exists()){
                Map map = new HashMap();
                map.put("TotalScreenTimeInSeconds",0);
                map.put("Screen",activityName);
                map.put("TotalScreenTimeInMinutes",0);
                timerRef.document(currentUserId+"-"+activityName).set(map);
            }
        }
    });
}


public void incrementUserScreenTime(final long totalTime, String activityName){

    String documentId= currentUserId+"-"+activityName;
    final DocumentReference dr  = timerRef.document(documentId);
    dr.update("TotalScreenTimeInSeconds",FieldValue.increment(totalTime));
    dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            dr.update("TotalScreenTimeInMinutes",documentSnapshot.getLong("TotalScreenTimeInSeconds")/60);
        }
    });
    final DocumentReference dr2  = usersRef.document(currentUserId);
    dr2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            dr2.update("totalTimeSpentInApp",FieldValue.increment(totalTime));
        }
    });
}
}
