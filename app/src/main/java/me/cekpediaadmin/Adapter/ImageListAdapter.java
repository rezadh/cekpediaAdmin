package me.cekpediaadmin.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.cekpediaadmin.Activity.MasjidActivity;
import me.cekpediaadmin.Activity.UpdateActivity;
import me.cekpediaadmin.R;
import me.cekpediaadmin.models.ImageUpload;

/**
 * Created by rezadwihendarno on 14/04/2018.
 */
//
public class ImageListAdapter extends ArrayAdapter<ImageUpload> {
    private Activity context;
    private int resource;
    private List<ImageUpload> listImage;
    private String nameSub="";

    public ImageListAdapter(@NonNull Activity context, int resource, @NonNull List<ImageUpload> objects, String nameSub) {
        super(context, 0, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
        this.nameSub = nameSub;
    }
    public ImageListAdapter(@NonNull Activity context, int resource, @NonNull List<ImageUpload> objects) {
        super(context, 0, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.card_row, parent, false);
        }
        TextView tvName = listItemView.findViewById(R.id.judul);
        TextView tvLokasi = listItemView.findViewById(R.id.deskripsi);

        ImageView imageView = listItemView.findViewById(R.id.imgview);
        TextView tvTelp = listItemView.findViewById(R.id.telp);

        tvName.setText(listImage.get(position).getName());
        tvLokasi.setText(listImage.get(position).getLokasi());
        tvTelp.setText(listImage.get(position).getNumber());
        Glide.with(context)
                .load(listImage.get(position).getUrl())
                .into(imageView);

//        listItemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, UpdateActivity.class);
//                intent.putExtra("JUDUL", listImage.get(position).getName());
////                intent.putExtra("FAV", listImage.get(position).isFavourite());
//                if (!nameSub.equals(""))
//                    intent.putExtra("SUB", nameSub);
//                else
//                    intent.putExtra("SUB", listImage.get(position).getNameSub());
//                context.startActivity(intent);
//            }
//        });

        return listItemView;

    }
}
