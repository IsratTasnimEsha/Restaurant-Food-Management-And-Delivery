package com.example.foodforyou;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphFloatingActionButton;

public class BCartAdapterClass extends RecyclerView.Adapter<BCartAdapterClass.MyViewHolder> {
    Context context;
    String st_time, st_limit, st_location, st_phone;
    int hour, minute;
    ArrayList<BUserClass>bUserClassArrayList;
    BUserClass bUserClass;

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    public BCartAdapterClass(Context context, ArrayList<BUserClass> bUserClassArrayList) {
        this.context = context;
        this.bUserClassArrayList = bUserClassArrayList;
    }

    @NonNull
    @Override
    public BCartAdapterClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.food_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BCartAdapterClass.MyViewHolder holder, int position) {
        BUserClass bUserClass = bUserClassArrayList.get(position);

        databaseReference.child("users").child(bUserClass.getRestaurant_Ref())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.bsc_fname.setText(snapshot.child("Foods").child(bUserClass.getFood_Category())
                                .child(bUserClass.getFood_Code()).child("Food_Name").getValue().toString());
                        holder.bsc_rname.setText(snapshot.child("Restaurant_Name").getValue().toString());
                        String st_fphoto = snapshot.child("Foods").child(bUserClass.getFood_Category())
                                .child(bUserClass.getFood_Code()).child("Food_Photo").getValue().toString();
                        if (!st_fphoto.isEmpty()) {
                            Picasso.get().load(st_fphoto).into(holder.bsc_fphoto);
                        } else {
                            holder.bsc_fphoto.setBackgroundResource(R.drawable.ic_baseline_photo_24);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.bsc_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener= new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour=i;
                        minute=i1;
                        holder.bsc_limit_text.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    }
                };

                holder.timePickerDialog=new TimePickerDialog
                        (context, onTimeSetListener, hour, minute, true);
                holder.timePickerDialog.setTitle("Select Time Limit");
                holder.timePickerDialog.show();
            }
        });

        holder.bsc_count.setText("Quantity: " + bUserClass.getCount());
        holder.bsc_price.setText("Total Price: " + bUserClass.getTotal_Price() + " Taka");

        holder.bsc_cart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.delete_dialog = new Dialog(context);
                holder.delete_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                holder.delete_dialog.setContentView(R.layout.b_cart_delete_dialog);
                holder.delete_dialog.show();

                holder.bcdd_delete = holder.delete_dialog.findViewById(R.id.bcdd_delete);
                holder.bcdd_dismiss = holder.delete_dialog.findViewById(R.id.bcdd_dismiss);

                holder.bcdd_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Cart")
                                .child(bUserClass.getRestaurant_Ref() + bUserClass.getFood_Category() + bUserClass.getFood_Code()).removeValue();
                        holder.delete_dialog.dismiss();
                    }
                });

                holder.bcdd_dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.delete_dialog.dismiss();
                    }
                });
                return true;
            }
        });

        holder.bsc_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.bsc_cod.isChecked()) {
                    st_time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                    st_limit=holder.bsc_limit_text.getText().toString();

                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Location")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    st_location = snapshot.getValue().toString();
                                    databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                            .child(st_time).child("Buyer_Location").setValue(st_location);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                    databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                            .child(st_time).child("Restaurant_Ref").setValue(bUserClass.getRestaurant_Ref());
                    databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                            .child(st_time).child("Buyer_Phone").setValue(bUserClass.getBuyer_Phone());
                    databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                            .child(st_time).child("Food_Category").setValue(bUserClass.getFood_Category());
                    databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                            .child(st_time).child("Food_Code").setValue(bUserClass.getFood_Code());
                    databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                            .child(st_time).child("Count").setValue(bUserClass.getCount());
                    databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                            .child(st_time).child("Total_Price").setValue(bUserClass.getTotal_Price());
                    databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                            .child(st_time).child("Instructions").setValue(bUserClass.getInstructions());
                    databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                            .child(st_time).child("Status").setValue("Waiting");
                    databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                            .child(st_time).child("Time").setValue(st_time);
                    databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                            .child(st_time).child("Time_Limit").setValue(st_limit);
                    databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                            .child(st_time).child("Bill").setValue("Cash On Delivery");

                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                            .child(st_time).child("Buyer_Phone").setValue(bUserClass.getBuyer_Phone());
                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                            .child(st_time).child("Restaurant_Ref").setValue(bUserClass.getRestaurant_Ref());
                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                            .child(st_time).child("Food_Category").setValue(bUserClass.getFood_Category());
                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                            .child(st_time).child("Food_Code").setValue(bUserClass.getFood_Code());
                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                            .child(st_time).child("Count").setValue(bUserClass.getCount());
                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                            .child(st_time).child("Total_Price").setValue(bUserClass.getTotal_Price());
                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                            .child(st_time).child("Instructions").setValue(bUserClass.getInstructions());
                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                            .child(st_time).child("Time").setValue(st_time);
                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                            .child(st_time).child("Time_Limit").setValue(st_limit);
                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                            .child(st_time).child("Status").setValue("Waiting");
                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                            .child(st_time).child("Bill").setValue("Cash On Delivery");

                    Toast.makeText(context, "Food Has Been Ordered", Toast.LENGTH_SHORT).show();

                    databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Cart")
                            .child(bUserClass.getRestaurant_Ref() + bUserClass.getFood_Category() + bUserClass.getFood_Code()).removeValue();

                }

                if(holder.bsc_gpay.isChecked())
                {
                    holder.dialog = new Dialog(context);

                    holder.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    holder.dialog.setContentView(R.layout.b_cart_dialog);
                    holder.dialog.show();

                    holder.bcd_transaction = holder.dialog.findViewById(R.id.bcd_transaction);
                    holder.bcd_gpay = holder.dialog.findViewById(R.id.bcd_gpay);
                    holder.bcd_submit = holder.dialog.findViewById(R.id.bcd_submit);
                    holder.bcd_dismiss = holder.dialog.findViewById(R.id.bcd_dismiss);

                    holder.bcd_dismiss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.dialog.dismiss();
                        }
                    });

                    holder.bcd_gpay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            st_phone=bUserClass.getRestaurant_Ref();
                            ClipboardManager clipboardManager= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clipData=ClipData.newPlainText("EditText", st_phone);
                            clipboardManager.setPrimaryClip(clipData);
                            clipData.getDescription();

                            try {
                                Intent intent=context.getPackageManager().getLaunchIntentForPackage("com.bKash.customerapp");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            } catch (Exception e) {
                                Uri uri= Uri.parse
                                        ("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                                Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }
                    });

                    holder.bcd_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            st_time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                            st_limit=holder.bsc_limit_text.getText().toString();

                            String st_transaction=holder.bcd_transaction.getText().toString();
                            if(st_transaction.isEmpty()) {
                                holder.bcd_transaction.setError("Please Enter Transaction Number");
                            }
                            else {
                                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                        .child(st_time).child("Bill").setValue(st_transaction);
                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                                        .child(st_time).child("Bill").setValue(st_transaction);

                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Location")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                st_location = snapshot.getValue().toString();
                                                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                                        .child(st_time).child("Buyer_Location").setValue(st_location);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                        .child(st_time).child("Restaurant_Ref").setValue(bUserClass.getRestaurant_Ref());
                                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                        .child(st_time).child("Buyer_Phone").setValue(bUserClass.getBuyer_Phone());
                                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                        .child(st_time).child("Food_Category").setValue(bUserClass.getFood_Category());
                                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                        .child(st_time).child("Food_Code").setValue(bUserClass.getFood_Code());
                                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                        .child(st_time).child("Count").setValue(bUserClass.getCount());
                                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                        .child(st_time).child("Total_Price").setValue(bUserClass.getTotal_Price());
                                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                        .child(st_time).child("Instructions").setValue(bUserClass.getInstructions());
                                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                        .child(st_time).child("Status").setValue("Waiting");
                                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                        .child(st_time).child("Time").setValue(st_time);
                                databaseReference.child("users").child(bUserClass.getRestaurant_Ref()).child("Food_Orders")
                                        .child(st_time).child("Time_Limit").setValue(st_limit);

                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                                        .child(st_time).child("Buyer_Phone").setValue(bUserClass.getBuyer_Phone());
                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                                        .child(st_time).child("Restaurant_Ref").setValue(bUserClass.getRestaurant_Ref());
                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                                        .child(st_time).child("Food_Category").setValue(bUserClass.getFood_Category());
                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                                        .child(st_time).child("Food_Code").setValue(bUserClass.getFood_Code());
                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                                        .child(st_time).child("Count").setValue(bUserClass.getCount());
                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                                        .child(st_time).child("Total_Price").setValue(bUserClass.getTotal_Price());
                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                                        .child(st_time).child("Instructions").setValue(bUserClass.getInstructions());
                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                                        .child(st_time).child("Time").setValue(st_time);
                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                                        .child(st_time).child("Time_Limit").setValue(st_limit);
                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Orders")
                                        .child(st_time).child("Status").setValue("Waiting");

                                Toast.makeText(context, "Food Has Been Ordered", Toast.LENGTH_SHORT).show();

                                databaseReference.child("customers").child(bUserClass.getBuyer_Phone()).child("Food_Cart")
                                        .child(bUserClass.getRestaurant_Ref() + bUserClass.getFood_Category() + bUserClass.getFood_Code()).removeValue();


                                holder.dialog.dismiss();
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bUserClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bsc_fname, bsc_rname, bsc_price, bsc_count, bsc_limit_text;
        RadioGroup bsc_bill;
        RadioButton bsc_gpay;
        RadioButton bsc_cod;
        CardView bsc_cart, bsc_limit, bcdd_delete;
        ImageView bsc_confirm, bsc_fphoto;
        TimePickerDialog timePickerDialog;
        Dialog dialog, delete_dialog;
        EditText bcd_transaction;
        NeumorphCardView bcd_gpay, bcd_submit;
        NeumorphFloatingActionButton bcd_dismiss, bcdd_dismiss;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bsc_cart=itemView.findViewById(R.id.bsc_cart);
            bsc_fname=itemView.findViewById(R.id.bsc_fname);
            bsc_rname=itemView.findViewById(R.id.bsc_rname);
            bsc_price=itemView.findViewById(R.id.bsc_price);
            bsc_count=itemView.findViewById(R.id.bsc_count);
            bsc_confirm=itemView.findViewById(R.id.bsc_confirm);
            bsc_limit=itemView.findViewById(R.id.bsc_limit);
            bsc_fphoto=itemView.findViewById(R.id.bsc_fphoto);
            bsc_bill=itemView.findViewById(R.id.bsc_bill);
            bsc_gpay=itemView.findViewById(R.id.bsc_gpay);
            bsc_cod=itemView.findViewById(R.id.bsc_cod);
            bsc_limit_text=itemView.findViewById(R.id.bsc_limit_text);
        }
    }
}