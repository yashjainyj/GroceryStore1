package com.example.grocerystore.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.grocerystore.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Shop_Main_Adapter extends RecyclerView.Adapter<Shop_Main_Adapter.Adapter_ViewHolder> {

    private Context context;

    public Shop_Main_Adapter(Context context, List<Shop_Detais_Modal> list) {
        this.context = context;
        this.list = list;
    }

    private List<Shop_Detais_Modal> list;
    @NonNull
    @Override
    public Adapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_design,viewGroup,false);
        Adapter_ViewHolder adapter_viewHolder=new Adapter_ViewHolder(view);
        return adapter_viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull Adapter_ViewHolder adapter_viewHolder, int i) {
        Shop_Detais_Modal shop_detais_modal = list.get(i);
        adapter_viewHolder.shopName.setText(shop_detais_modal.getShop_Name());
        adapter_viewHolder.shopAddress.setText(shop_detais_modal.getShop_Address());
        adapter_viewHolder.time.setText(shop_detais_modal.getMin()+"min");
        adapter_viewHolder.raiting.setText(shop_detais_modal.getShop_rating());
        adapter_viewHolder.phn.setText(shop_detais_modal.getContact_number());
        Glide.with(context).load(shop_detais_modal.getImage_Url()).into(adapter_viewHolder.shop_image);
        adapter_viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Show_Item.class);
                intent.putExtra("shopId",shop_detais_modal.getShop_Id());
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Adapter_ViewHolder extends RecyclerView.ViewHolder {
        ImageView shop_image;
        TextView shopName,shopAddress,raiting,time,phn;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            shop_image = itemView.findViewById(R.id.img3);
            shopName = itemView.findViewById(R.id.shop_name);
            shopAddress = itemView.findViewById(R.id.shop_address);
            raiting = itemView.findViewById(R.id.raiting);
            time = itemView.findViewById(R.id.min);
            phn= itemView.findViewById(R.id.tv13);


        }
    }
}
