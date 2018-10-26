package com.mengyu.pdfrender;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meng.openglt.R;

import java.util.List;

public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.MyViewHolder> {

    private List<Bitmap> mDatas;

    NormalAdapter(List<Bitmap> data) {
        this.mDatas = data;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.imageView.setImageBitmap(mDatas.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_pdf_item, parent, false);
        return new MyViewHolder(v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_pdf_show);
        }
    }

    public void notifyData(List<Bitmap> mDatas) {
        this.mDatas = mDatas;
        this.notifyDataSetChanged();
    }


}
