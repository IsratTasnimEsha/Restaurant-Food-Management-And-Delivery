package com.example.foodforyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String str_phone;
    TextView h_rname, h_phone, h_rating, h_review;
    ImageView h_rphoto, bht_menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    View nView;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent=getIntent();
        str_phone=intent.getStringExtra("Phone");

        drawerLayout=findViewById(R.id.h_drawer);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this, drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        actionBarDrawerToggle.syncState();

        navigationView=findViewById(R.id.h_navigation);
        nView=navigationView.getHeaderView(0);

        h_rname=nView.findViewById(R.id.h_rname);
        h_phone=nView.findViewById(R.id.h_rphone);
        h_rphoto=nView.findViewById(R.id.h_rphoto);
        bht_menu=findViewById(R.id.bht_menu);
        h_rating=nView.findViewById(R.id.h_rating);
        h_review=nView.findViewById(R.id.h_review);

        bht_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        databaseReference.child("users").child(str_phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str_rname= (String) snapshot.child("Restaurant_Name").getValue();
                h_rname.setText(str_rname);
                String str_phone= (String) snapshot.child("Restaurant_Contact_Number").getValue();
                h_phone.setText(str_phone);
                String str_rating = (String) snapshot.child("Rating").getValue();

                char ch_rating[] = {str_rating.charAt(0), str_rating.charAt(1), str_rating.charAt(2)};
                String st_rating = new String(ch_rating);
                h_rating.setText(st_rating);
                
                String str_review = (String) snapshot.child("Review").getValue();
                h_review.setText(str_review + " Reviews");

                String str_rphoto= (String) snapshot.child("Restaurant_Photo").getValue();
                if(!str_rphoto.isEmpty()) {
                    Picasso.get().load(str_rphoto).into(h_rphoto);
                }
                else {
                    h_rphoto.setBackgroundResource(R.drawable.ic_baseline_add_a_photo_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.m_category)
        {
            Intent intent2=new Intent(this, FoodCategoryActivity.class);
            intent2.putExtra("Phone", str_phone);

            startActivity(intent2);
        }
        if(item.getItemId()==R.id.m_notification)
        {
            Intent intent2 = new Intent(this, NotificationActivity.class);
            intent2.putExtra("Phone", str_phone);

            startActivity(intent2);
        }
        if(item.getItemId()==R.id.m_update)
        {
            Intent intent2=new Intent(this, UpdateProfileActivity.class);
            intent2.putExtra("Phone", str_phone);

            startActivity(intent2);
        }
        if(item.getItemId()==R.id.m_change)
        {
            Intent intent2=new Intent(this, ChangePasswordActivity.class);
            intent2.putExtra("Phone", str_phone);

            startActivity(intent2);
        }
        if(item.getItemId()==R.id.m_delete)
        {
            databaseReference.child("users").child(str_phone)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild("Food_Orders")) {
                                if (snapshot.child("Food_Orders").getValue().toString().equals("")) {
                                    databaseReference.child("users").child(str_phone).removeValue();
                                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                                }
                                else {
                                    Toast.makeText(HomeActivity.this, "You Can't Delete Your Account " +
                                            "When You Have Promised To Delivery Food", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                                databaseReference.child(str_phone).removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        if(item.getItemId()==R.id.m_logout)
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return true;
    }

    public void BurgerPage(View view) {
        Intent intent2=new Intent(this, BurgerActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void SandwichPage(View view) {
        Intent intent2=new Intent(this, SandwichActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void PastaPage(View view) {
        Intent intent2=new Intent(this, PastaActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void PizzaPage(View view) {
        Intent intent2=new Intent(this, PizzaActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void FriesPage(View view) {
        Intent intent2=new Intent(this, FriesActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void RicePage(View view) {
        Intent intent2=new Intent(this, RiceActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void MeatPage(View view) {
        Intent intent2=new Intent(this, MeatActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void VegPage(View view) {
        Intent intent2=new Intent(this, VegActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void SaladPage(View view) {
        Intent intent2=new Intent(this, SaladActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void SoupPage(View view) {
        Intent intent2=new Intent(this, SoupActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void DrinksPage(View view) {
        Intent intent2=new Intent(this, DrinksActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void DessertPage(View view) {
        Intent intent2=new Intent(this, DessertActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void FastFoodComboPage(View view) {
        Intent intent2=new Intent(this, FastFoodComboActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void RiceComboPage(View view) {
        Intent intent2=new Intent(this, RiceComboActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void SpecialPage(View view) {
        Intent intent2=new Intent(this, SpecialActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void OthersPage(View view) {
        Intent intent2=new Intent(this, OthersActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }

    public void OffersPage(View view) {
        Intent intent2=new Intent(this, OffersActivity.class);
        intent2.putExtra("Phone", str_phone);

        startActivity(intent2);
    }
}