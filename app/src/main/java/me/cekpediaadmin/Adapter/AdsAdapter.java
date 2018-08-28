package me.cekpediaadmin.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.cekpediaadmin.Activity.ImageAdsActivity;
import me.cekpediaadmin.Activity.UpdateActivity;
import me.cekpediaadmin.R;
import me.cekpediaadmin.models.Ads;
import me.cekpediaadmin.models.ImageUpload;

/**
 * Created by rezadwihendarno on 19/04/2018.
 */

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private List<Ads> listImage;
    private String namaMenu = "";

    public AdsAdapter(Activity context, int resource, List<Ads> listImage) {
        this.context = context;
        this.resource = resource;
        this.listImage = listImage;
        this.namaMenu = namaMenu;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTextTitle, tvName;
        ImageView imageView;
        public MyViewHolder(View view){
            super(view);
            tvTextTitle = view.findViewById(R.id.textimageName);
            imageView = view.findViewById(R.id.imgAds);
            tvName = view.findViewById(R.id.textAds);
        }
    }

    public AdsAdapter(List<Ads> listImage){
        this.listImage = listImage;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsor_item, parent, false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(AdsAdapter.MyViewHolder holder, final int position) {
        holder.tvTextTitle.setText(listImage.get(position).getNamaMenu());
        holder.tvName.setText(listImage.get(position).getName());
        Glide.with(context)
                .load(listImage.get(position).getUrl())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageAdsActivity.class);
                intent.putExtra("JUDUL", listImage.get(position).getNamaMenu());
//                if (!namaMenu.equals(""))
//                    intent.putExtra("SUB", namaMenu);
//                else
//                    intent.putExtra("SUB", listImage.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }
}
