package com.example.foodforyou;

import static com.example.foodforyou.BHomeActivity.SHARED_PREF_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class BRestaurantActivity extends AppCompatActivity {
    String st_phone, st_bphone, st_blocation;
    BottomNavigationView bottom1, bottom2, bottom3, bottom4;
    BAboutRestaurantFragment bAboutRestaurantFragment=new BAboutRestaurantFragment();
    BBurgerFragment bBurgerFragment = new BBurgerFragment();
    BSandwichFragment bSandwichFragment=new BSandwichFragment();
    BPastaFragment bPastaFragment=new BPastaFragment();
    BPizzaFragment bPizzaFragment=new BPizzaFragment();
    BFriesFragment bFriesFragment=new BFriesFragment();
    BRiceFragment bRiceFragment= new BRiceFragment();
    BMeatFragment bMeatFragment= new BMeatFragment();
    BVegFragment bVegFragment=new BVegFragment();
    BSaladFragment bSaladFragment=new BSaladFragment();
    BSoupFragment bSoupFragment=new BSoupFragment();
    BDrinksFragment bDrinksFragment=new BDrinksFragment();
    BDessertFragment bDessertFragment=new BDessertFragment();
    BFastFoodComboFragment bFastFoodComboFragment=new BFastFoodComboFragment();
    BRiceComboFragment bRiceComboFragment=new BRiceComboFragment();
    BOthersFragment bOthersFragment=new BOthersFragment();
    BSpecialFragment bSpecialFragment=new BSpecialFragment();
    BOffersFragment bOffersFragment=new BOffersFragment();

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME="myPref";

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brestaurant);

        Intent intent1=getIntent();
        st_phone=intent1.getStringExtra("Phone");

        bottom1=findViewById(R.id.bres_bottom1);
        bottom2=findViewById(R.id.bres_bottom2);
        bottom3=findViewById(R.id.bres_bottom3);
        bottom4=findViewById(R.id.bres_bottom4);

        getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bAboutRestaurantFragment).commit();

        bottom1.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.f_burger) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bBurgerFragment).commit();
                }
                if(item.getItemId()==R.id.f_sandwich) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bSandwichFragment).commit();
                }
                if(item.getItemId()==R.id.f_pasta) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bPastaFragment).commit();
                }
                if(item.getItemId()==R.id.f_pizza) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bPizzaFragment).commit();
                }
                if(item.getItemId()==R.id.f_fries) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bFriesFragment).commit();
                }
                return true;
            }
        });

        bottom2.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.f_rice) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bRiceFragment).commit();
                }
                if(item.getItemId()==R.id.f_meat) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bMeatFragment).commit();
                }
                if(item.getItemId()==R.id.f_veg) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bVegFragment).commit();
                }
                if(item.getItemId()==R.id.f_salad) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bSaladFragment).commit();
                }
                if(item.getItemId()==R.id.f_soup) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bSoupFragment).commit();
                }
                return true;
            }
        });

        bottom3.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.f_drinks) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bDrinksFragment).commit();
                }
                if(item.getItemId()==R.id.f_dessert) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bDessertFragment).commit();
                }
                if(item.getItemId()==R.id.f_ffcombo) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bFastFoodComboFragment).commit();
                }
                if(item.getItemId()==R.id.f_rcombo) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bRiceComboFragment).commit();
                }
                if(item.getItemId()==R.id.f_others) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bOthersFragment).commit();
                }
                return true;
            }
        });

        bottom4.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.f_about) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bAboutRestaurantFragment).commit();
                }
                if(item.getItemId()==R.id.f_map) {
                    databaseReference.child("users").child(st_phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String str_address= (String) snapshot.child("Restaurant_Address").getValue();

                            sharedPreferences=getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                            st_bphone=sharedPreferences.getString("BPhone", null);

                            databaseReference.child("customers").child(st_bphone).child("Location")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            st_blocation=snapshot.getValue().toString();

                                            try {
                                                Uri uri=Uri.parse("https://www.google.co.in/maps/dir/" + st_blocation + "/" + str_address);
                                                Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                                                intent.setPackage("com.google.android.apps.maps");
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                Uri uri= Uri.parse
                                                        ("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                                                Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if(item.getItemId()==R.id.f_offer) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bOffersFragment).commit();
                }
                if(item.getItemId()==R.id.f_special) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.bres_frame, bSpecialFragment).commit();
                }
                return true;
            }
        });
    }
}