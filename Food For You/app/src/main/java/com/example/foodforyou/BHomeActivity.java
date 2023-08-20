package com.example.foodforyou;

import static java.lang.Integer.MAX_VALUE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView bh_uname, bh_phone;
    String st_bphone;
    ImageView bht_menu;
    RecyclerView recyclerView;
    ArrayList<RestaurantClass>arrayList;
    BAdapterClass adapterClass;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    View nView;
    SearchView searchView;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME="myPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bhome);

        Intent intent1=getIntent();
        st_bphone=intent1.getStringExtra("BPhone");
        bht_menu=findViewById(R.id.bht_menu);

        bht_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        searchView=findViewById(R.id.bht_search);

        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("BPhone", st_bphone);
        editor.apply();

        recyclerView=findViewById(R.id.bh_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        drawerLayout=findViewById(R.id.bh_drawer);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this, drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        actionBarDrawerToggle.syncState();

        navigationView=findViewById(R.id.bh_navigation);
        nView=navigationView.getHeaderView(0);

        bh_uname=nView.findViewById(R.id.bh_uname);
        bh_phone=nView.findViewById(R.id.bh_phone);

        databaseReference.child("customers").child(st_bphone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str_uname= (String) snapshot.child("User_Name").getValue();
                bh_uname.setText(str_uname);
                String str_phone= (String) snapshot.child("Phone").getValue();
                bh_phone.setText(str_phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        arrayList= new ArrayList<>();
        adapterClass= new BAdapterClass(this, arrayList);
        recyclerView.setAdapter(adapterClass);

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    RestaurantClass restaurantClass=dataSnapshot.getValue(RestaurantClass.class);
                    arrayList.add(restaurantClass);
                }
                adapterClass.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filtered(s);
                return true;
            }
        });
    }

    private void filtered(String s) {
        ArrayList<RestaurantClass>filteredList=new ArrayList<>();
        for(RestaurantClass item:arrayList) {
            if(item.getRestaurant_Name().toLowerCase().contains(s.toLowerCase())) {
                filteredList.add(item);
            }
            else if(item.getRestaurant_Address().toLowerCase().contains(s.toLowerCase())) {
                filteredList.add(item);
            }
            else {
                Toast.makeText(this, "No Data Found.", Toast.LENGTH_SHORT).show();
            }
        }
        adapterClass.filterList(filteredList);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.bh_update) {
            Intent intent=new Intent(this, BUpdateProfileActivity.class);
            intent.putExtra("BPhone", st_bphone);
            startActivity(intent);
        }

        if(item.getItemId()==R.id.bh_order) {
            Intent intent=new Intent(this, BOrderedFoodsActivity.class);
            intent.putExtra("BPhone", st_bphone);
            startActivity(intent);
        }

        if(item.getItemId()==R.id.bh_cart) {
            Intent intent=new Intent(this, BShoppingCartActivity.class);
            intent.putExtra("BPhone", st_bphone);
            startActivity(intent);
        }

        if(item.getItemId()==R.id.bh_delete)
        {
            databaseReference.child("customers").child(st_bphone)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild("Food_Orders")) {
                                if (snapshot.child("Food_Orders").getValue().toString().isEmpty()) {
                                    databaseReference.child("customers").child(st_bphone).removeValue();
                                    startActivity(new Intent(BHomeActivity.this, MainActivity.class));
                                }
                                else {
                                    Toast.makeText(BHomeActivity.this, "You Can't Delete Your Account " +
                                            "When You Are Waiting For Food", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                startActivity(new Intent(BHomeActivity.this, MainActivity.class));
                                databaseReference.child(st_bphone).removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

        if(item.getItemId()==R.id.bh_logout) {
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return true;
    }
}