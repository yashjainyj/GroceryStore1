package com.example.grocerystore.User;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.grocerystore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Address_Adapter extends RecyclerView.Adapter<Address_Adapter.AddressViewHolder> {
    Context context;
    List<Address_DataModel> address_dataModels;
    public Address_Adapter(Context context, List<Address_DataModel> address_dataModels) {
        this.address_dataModels = address_dataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.address_layout,viewGroup,false);
        AddressViewHolder addressViewHolder=new AddressViewHolder(view);
        return addressViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder addressViewHolder, int i) {
        Address_DataModel address_dataModel = address_dataModels.get(i);
         addressViewHolder.name.setText("Name:- "+address_dataModel.getSalutation() + ""+address_dataModel.getName());
         addressViewHolder.flat.setText("Flat:- "+address_dataModel.getFlat());
        addressViewHolder.street.setText("Street:- "+address_dataModel.getStreet());
        addressViewHolder.local.setText("Locality:- "+address_dataModel.getLocality());
        addressViewHolder.nick.setText("Type:- "+address_dataModel.getNickname());
        addressViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Add_Address.class);
                intent.putExtra("s",address_dataModel.getKey());
                context.startActivity(intent);
            }
        });
        addressViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressViewHolder.databaseReference = FirebaseDatabase.getInstance().getReference().child(addressViewHolder.mauth.getCurrentUser().getUid()).child("Address").child(address_dataModel.getKey());
                addressViewHolder.databaseReference.removeValue();
                address_dataModels.clear();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return address_dataModels.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView name,flat,street,local,nick;
        Button edit,delete;
        FirebaseAuth mauth;
        DatabaseReference databaseReference;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            mauth = FirebaseAuth.getInstance();
            name = itemView.findViewById(R.id.name);
            flat = itemView.findViewById(R.id.flat);
            street = itemView.findViewById(R.id.street);
            local = itemView.findViewById(R.id.locality);
            nick = itemView.findViewById(R.id.nick);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
