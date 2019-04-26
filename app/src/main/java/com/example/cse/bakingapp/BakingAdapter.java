package com.example.cse.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BakingAdapter extends RecyclerView.Adapter<BakingAdapter.AdapterView> {
    ArrayList<BakingModel> arrayList;
    Context context;

    public BakingAdapter(ArrayList<BakingModel> arrayList, MainActivity mainActivity) {
        this.arrayList = arrayList;
        this.context = mainActivity;
    }

    @NonNull
    @Override
    public BakingAdapter.AdapterView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.rowdesign,viewGroup,false);
        return new AdapterView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BakingAdapter.AdapterView adapterView, final int i) {
        BakingModel model=arrayList.get(i);
        adapterView.textView.setText(model.getName());
        adapterView.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ItemListActivity.class);
                intent.putExtra("putextra",i);
                intent.putExtra("appname",arrayList.get(i).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AdapterView extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public AdapterView(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image1);
            textView=itemView.findViewById(R.id.text1);
        }
    }
}
