package com.example.foodforyou;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {
    Context context;
    ArrayList<UserClass>arrayList;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    public AdapterClass(Context context, ArrayList<UserClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.food_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserClass userClass=arrayList.get(position);
        if(!userClass.getFood_Photo().isEmpty()) {
            Picasso.get().load(userClass.getFood_Photo()).into(holder.fd_photo);
        }
        else {
            holder.fd_photo.setBackgroundResource(R.drawable.ic_baseline_photo_24);
        }
        holder.fd_name.setText(userClass.getFood_Name());
        holder.fd_code.setText("Food Code: " + userClass.getFood_Category() + userClass.getFood_Code());
        holder.fd_price.setText(userClass.getFood_Price());
        if(!userClass.getFood_Offer_Price().isEmpty()) {
            holder.fd_offer.setText(userClass.getFood_Offer_Price());
        }
        if(!userClass.getFood_Description().isEmpty()) {
            holder.fd_description.setText(userClass.getFood_Description());
        }
        else {
            holder.fd_description.setText("");
        }

        holder.fd_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st_price= holder.fd_price.getText().toString();
                String st_offer=holder.fd_offer.getText().toString();
                String st_description=holder.fd_description.getText().toString();
                if(st_price.isEmpty()) {
                    holder.fd_price.setError("Please Enter The Price.");
                }
                else
                {
                    databaseReference.child("users").child(userClass.getFood_Phone_Ref()).child("Foods")
                            .child(userClass.getFood_Category()).child(userClass.getFood_Code())
                            .child("Food_Price").setValue(st_price);
                    databaseReference.child("users").child(userClass.getFood_Phone_Ref()).child("Foods")
                            .child(userClass.getFood_Category()).child(userClass.getFood_Code())
                            .child("Food_Offer_Price").setValue(st_offer);
                    databaseReference.child("users").child(userClass.getFood_Phone_Ref()).child("Foods")
                            .child(userClass.getFood_Category()).child(userClass.getFood_Code())
                            .child("Food_Description").setValue(st_description);
                }
            }
        });

        holder.fd_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.food_delete_dialog = new Dialog(context);
                holder.food_delete_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                holder.food_delete_dialog.setContentView(R.layout.food_delete_dialog);
                holder.food_delete_dialog.show();

                holder.ffd_yes = holder.food_delete_dialog.findViewById(R.id.fdd_yes);
                holder.ffd_no = holder.food_delete_dialog.findViewById(R.id.fdd_no);

                holder.ffd_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseReference.child("users").child(userClass.getFood_Phone_Ref()).child("Foods")
                                .child(userClass.getFood_Category()).child(userClass.getFood_Code()).removeValue();

                        databaseReference.child("food_items").child(userClass.getFood_Category())
                                .child(userClass.getFood_Phone_Ref()).removeValue();

                        FirebaseStorage.getInstance().getReference().child("image").child(userClass.getFood_Phone_Ref())
                                .child(userClass.getFood_Category()).child(userClass.getFood_Code()).delete();

                        holder.food_delete_dialog.dismiss();
                    }
                });

                holder.ffd_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.food_delete_dialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView fd_photo;
        TextView fd_name, fd_code;
        EditText fd_price, fd_offer, fd_description;
        Button fd_update, fd_delete;
        CardView ffd_yes, ffd_no;
        Dialog food_delete_dialog;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fd_photo=itemView.findViewById(R.id.bfd_rphoto);
            fd_name=itemView.findViewById(R.id.fd_name);
            fd_code=itemView.findViewById(R.id.fd_code);
            fd_price=itemView.findViewById(R.id.fd_price);
            fd_offer=itemView.findViewById(R.id.fd_offer);
            fd_description=itemView.findViewById(R.id.fd_description);
            fd_update=itemView.findViewById(R.id.bfd_update);
            fd_delete=itemView.findViewById(R.id.bfd_delete);
        }
    }
}