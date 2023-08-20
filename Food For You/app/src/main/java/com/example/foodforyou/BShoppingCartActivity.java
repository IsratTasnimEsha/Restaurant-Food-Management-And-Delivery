package com.example.foodforyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BShoppingCartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<BUserClass> arrayList;
    BCartAdapterClass bCartAdapterClass;
    String str_bphone;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME="myPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bshopping_cart);

        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        str_bphone=sharedPreferences.getString("BPhone", null);

        recyclerView=findViewById(R.id.bsc_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList= new ArrayList<>();
        bCartAdapterClass= new BCartAdapterClass(this, arrayList);
        recyclerView.setAdapter(bCartAdapterClass);

        databaseReference.child("customers").child(str_bphone).child("Food_Cart")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                            BUserClass bUserClass=dataSnapshot.getValue(BUserClass.class);
                            arrayList.add(bUserClass);
                        }
                        bCartAdapterClass.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}