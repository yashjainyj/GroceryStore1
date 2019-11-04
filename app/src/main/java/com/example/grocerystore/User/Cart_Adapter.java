package com.example.grocerystore.User;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.grocerystore.Admin.Item_data_model;
import com.example.grocerystore.Admin.Show_Item_Adapter;
import com.example.grocerystore.Main.Product_Detail;
import com.example.grocerystore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.Adapter_ViewHolder> {

    private Context context;

    public Cart_Adapter(Context context, List<Item_data_model> list) {
        this.context = context;
        this.list = list;
    }

    private List<Item_data_model> list;
    @NonNull
    @Override
    public Adapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_layout,viewGroup,false);
        Adapter_ViewHolder adapter_viewHolder=new Adapter_ViewHolder(view);
        return adapter_viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull Adapter_ViewHolder adapter_viewHolder, int i) {
        Item_data_model item_data_model = list.get(i);
        adapter_viewHolder.name.setText(item_data_model.getItemName());
        adapter_viewHolder.price.setText("Rs."+item_data_model.getItemPrice());
        Glide.with(context).load(item_data_model.getImageUrl()).into(adapter_viewHolder.item_image);
        adapter_viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Product_Detail.class);
                intent.putExtra("item_id",item_data_model.getItemId());
                context.startActivity(intent);

            }
        });
        adapter_viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_viewHolder.databaseReference = adapter_viewHolder.databaseReference.child(adapter_viewHolder.firebaseAuth.getCurrentUser().getUid()).child("Cart").child(item_data_model.getItemId());
                adapter_viewHolder.databaseReference.removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Adapter_ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_image,delete;
        TextView name,price;
        DatabaseReference databaseReference;
        FirebaseAuth firebaseAuth;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.image_item);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            delete = itemView.findViewById(R.id.delete);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            firebaseAuth = FirebaseAuth.getInstance();
        }
    }
}
