package com.example.grocerystore.Model;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.Main.ShowItemMain;
import com.example.grocerystore.MainActivity;
import com.example.grocerystore.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {
    private List<ViewPager_Model> list;
    private LayoutInflater layoutInflater;
    private Context context;
    TextView titletv,message;
    Button submit;
    ImageView close;



    public ViewPagerAdapter(List<ViewPager_Model> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater =   LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.viewpager_layout,container,false);
        ImageView imageView;
        TextView title,desc;

        Dialog dialog;
        dialog = new Dialog(context);
        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        imageView.setImageResource(list.get(position).getImage());
        title.setText(list.get(position).getTitle());
        desc.setText(list.get(position).getDesc());
        container.addView(view,0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.setContentView(R.layout.positive_popup_layput);
               close  = dialog.findViewById(R.id.close);
               titletv = dialog.findViewById(R.id.titletv);
               message = dialog.findViewById(R.id.message);
               submit = dialog.findViewById(R.id.btnaccept);
               close.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialog.dismiss();
                   }
               });

               submit.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent = new Intent(context, ShowItemMain.class);
                       context.startActivity(intent);

                   }
               });
               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               dialog.show();

            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
