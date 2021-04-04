package com.example.analyticsassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsActivity extends AppCompatActivity {
    List<Product> products = new ArrayList<>();
    FirebaseAnalytics firebaseAnalytics;
    long startTime;
    FirebaseAuth auth;
    CollectionReference timerRef;
    CollectionReference usersRef;
    FirebaseFirestore firestore;
    String currentUserId;
    DocumentReference userDocumentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firestore = FirebaseFirestore.getInstance();
        timerRef = firestore.collection("timeTracker");
        usersRef = firestore.collection("users");
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
            currentUserId = auth.getCurrentUser().getUid();
        createUserScreenTimeRecorder("ProductsActivity");
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(category);

        LogProductsViewed(category);
        userDocumentRef = usersRef.document("1");
        if (category.equals("electronics")) {

            updateCategoryMap("electronics");
            Product p1 = new Product();
            p1.setId(1);
            p1.setName("CPU");
            p1.setDescription("Intel CPU");
            p1.setPrice(1200);
            p1.setAmount(10);

            Product p2 = new Product();
            p2.setId(2);
            p2.setName("Headphone");
            p2.setDescription("SeaSonic Headphone");
            p2.setPrice(200);
            p2.setAmount(25);

            Product p3 = new Product();
            p3.setId(3);
            p3.setName("Mouse");
            p3.setDescription("Razer Mouse");
            p3.setPrice(150);
            p3.setAmount(43);

            Product p4 = new Product();
            p4.setId(1);
            p4.setName("Keyboard");
            p4.setDescription("Logitech Keyboard");
            p4.setPrice(250);
            p4.setAmount(10);

            products = Arrays.asList(p1, p2, p3, p4);
        } else if (category.equals("clothing")) {
            updateCategoryMap("clothing");
            Product p1 = new Product();
            p1.setId(1);
            p1.setName("Shirt");
            p1.setDescription("Red Shirt");
            p1.setPrice(100);
            p1.setAmount(50);

            Product p2 = new Product();
            p2.setId(2);
            p2.setName("Socks");
            p2.setDescription("Blue Socks");
            p2.setPrice(20);
            p2.setAmount(120);

            Product p3 = new Product();
            p3.setId(3);
            p3.setName("Pants");
            p3.setDescription("White Pants");
            p3.setPrice(120);
            p3.setAmount(66);

            Product p4 = new Product();
            p4.setId(1);
            p4.setName("Suit");
            p4.setDescription("Black Suit");
            p4.setPrice(340);
            p4.setAmount(8);
            products = Arrays.asList(p1, p2, p3, p4);
        } else if (category.equals("makeup")) {

            updateCategoryMap("makeup");
            Product p1 = new Product();
            p1.setId(1);
            p1.setName("Lipstick");
            p1.setDescription("Red Lipstick");
            p1.setPrice(100);
            p1.setAmount(50);

            Product p2 = new Product();
            p2.setId(2);
            p2.setName("Eyeliner");
            p2.setDescription("grey Eyeliner");
            p2.setPrice(20);
            p2.setAmount(120);

            Product p3 = new Product();
            p3.setId(3);
            p3.setName("Brush");
            p3.setDescription("Medium Brush");
            p3.setPrice(120);
            p3.setAmount(66);

            Product p4 = new Product();
            p4.setId(1);
            p4.setName("Powder");
            p4.setDescription("Pink Face Powder");
            p4.setPrice(340);
            p4.setAmount(8);
            products = Arrays.asList(p1, p2, p3, p4);


        } else if (category.equals("smartphone")) {
            updateCategoryMap("smartphone");
            Product p1 = new Product();
            p1.setId(1);
            p1.setName("Samsung");
            p1.setDescription("Samsung S20");
            p1.setPrice(2500);
            p1.setAmount(25);

            Product p2 = new Product();
            p2.setId(2);
            p2.setName("One Plus");
            p2.setDescription("One Plus Phone");
            p2.setPrice(1700);
            p2.setAmount(32);

            Product p3 = new Product();
            p3.setId(3);
            p3.setName("Samsung");
            p3.setDescription("Samsung Fold");
            p3.setPrice(2700);
            p3.setAmount(14);

            Product p4 = new Product();
            p4.setId(1);
            p4.setName("Iphone");
            p4.setDescription("Iphone XS");
            p4.setPrice(3000);
            p4.setAmount(9);
            products = Arrays.asList(p1, p2, p3, p4);

        }

        ProductsAdapter adapter = new ProductsAdapter(products);
        RecyclerView rv = findViewById(R.id.productsRv);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setAdapter(adapter);

    }


    @Override
    protected void onStart() {
        startTime = System.currentTimeMillis() / 1000;
        super.onStart();
    }

    @Override
    protected void onStop() {
        incrementUserScreenTime(System.currentTimeMillis() / 1000 - startTime, "ProductsActivity");
        super.onStop();
    }


    private void LogProductsViewed(String category) {
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        firebaseAnalytics.logEvent("screenVisited", bundle);
    }


    public void createUserScreenTimeRecorder(final String activityName) {

        timerRef.document(currentUserId + "-" + activityName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.getResult().exists()) {
                    Map map = new HashMap();
                    map.put("TotalScreenTimeInSeconds", 0);
                    map.put("Screen", activityName);
                    map.put("TotalScreenTimeInMinutes", 0);
                    timerRef.document(currentUserId + "-" + activityName).set(map);
                }
            }
        });
    }


    public void incrementUserScreenTime(final long totalTime, String activityName) {

        String documentId = currentUserId + "-" + activityName;
        final DocumentReference dr = timerRef.document(documentId);
        dr.update("TotalScreenTimeInSeconds", FieldValue.increment(totalTime));
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                dr.update("TotalScreenTimeInMinutes", documentSnapshot.getLong("TotalScreenTimeInSeconds") / 60);
            }
        });

        final DocumentReference dr2 = usersRef.document(currentUserId);

        dr2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                dr2.update("totalTimeSpentInApp", FieldValue.increment(totalTime));
            }
        });

    }

    public void updateCategoryMap(final String category) {
        userDocumentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map map = (Map) documentSnapshot.get("categories");
                long catCount = ((long) (map.get(category)));

                if (catCount + 1 > 5) {
                    firebaseAnalytics.setUserProperty("interest", category);
                }
                map.put(category, catCount + 1);
                userDocumentRef.update("categories", map);
            }
        });

    }


}
