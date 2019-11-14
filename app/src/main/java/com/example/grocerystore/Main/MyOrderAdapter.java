package com.example.grocerystore.Main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.grocerystore.Admin.Item_data_model;
import com.example.grocerystore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrderAdapter  extends RecyclerView.Adapter<MyOrderAdapter.Adapter_ViewHolder> {

    private Context context;

    public MyOrderAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    private List<OrderModel> list;
    @NonNull
    @Override
    public Adapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_main_layout,viewGroup,false);
        Adapter_ViewHolder adapter_viewHolder=new Adapter_ViewHolder(view);
        return adapter_viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull Adapter_ViewHolder adapter_viewHolder, int i) {
        OrderModel orderModel = list.get(i);
        DocumentReference documentReference;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection("Items").document(orderModel.getItemId());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Item_data_model item_data_model = documentSnapshot.toObject(Item_data_model.class);
                adapter_viewHolder.name1.setText(item_data_model.getItemName());
                Glide.with(context).load(item_data_model.getImageUrl()).into(adapter_viewHolder.image);
                adapter_viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MyOrder_Info.class);
                        intent.putExtra("orderId", orderModel.getOrderId());
                        context.startActivity(intent);
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Adapter_ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name,name1;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shop_name);
            name1 = itemView.findViewById(R.id.shop_address);
            // desc = itemView.findViewById(R.id.description);
            //category = itemView.findViewById(R.id.category);
        image = itemView.findViewById(R.id.shop_image);
        }
    }
}

