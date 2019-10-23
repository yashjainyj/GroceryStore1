package com.example.grocerystore.Admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grocerystore.R;
import com.example.grocerystore.User.Address_Adapter;

import java.util.List;

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
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_main_layout,viewGroup,false);
        Adapter_ViewHolder adapter_viewHolder=new Adapter_ViewHolder(view);
        return adapter_viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_ViewHolder adapter_viewHolder, int i) {
        Shop_Detais_Modal shop_detais_modal = list.get(i);
        adapter_viewHolder.shopName.setText(shop_detais_modal.getShop_Name());
        adapter_viewHolder.shopAddress.setText(shop_detais_modal.getShop_Address());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Adapter_ViewHolder extends RecyclerView.ViewHolder {
        ImageView shop_image;
        TextView shopName,shopAddress;
        ImageButton delete;
        public Adapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            shop_image = itemView.findViewById(R.id.shop_image);
            shopName = itemView.findViewById(R.id.shop_name);
            shopAddress = itemView.findViewById(R.id.shop_address);
            delete = itemView.findViewById(R.id.delete);

        }
    }
}
