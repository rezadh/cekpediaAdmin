package me.cekpediaadmin.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import me.cekpediaadmin.Activity.UpdateActivity;
import me.cekpediaadmin.R;
import me.cekpediaadmin.models.ImageUpload;

import static me.cekpediaadmin.Activity.UpdateActivity.FB_DATABASE_PATH;

/**
 * Created by rezadwihendarno on 15/04/2018.
 */

public class WaitingListAdapter extends RecyclerView.Adapter<WaitingListAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private List<ImageUpload> listImage;
    private String namaMenu = "";

    public WaitingListAdapter(Activity context, int resource, List<ImageUpload> listImage) {
        this.context = context;
        this.resource = resource;
        this.listImage = listImage;
    }
    public WaitingListAdapter(Activity context, int resource, List<ImageUpload> listImage, String namaMenu) {
        this.context = context;
        this.resource = resource;
        this.listImage = listImage;
        this.namaMenu = namaMenu;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvLokasi, tvTelp;
        ImageView imageView;
        public MyViewHolder(View view){
            super(view);
            tvName = view.findViewById(R.id.judul);
            tvLokasi = view.findViewById(R.id.deskripsi);
            imageView = view.findViewById(R.id.imgview);
            tvTelp = view.findViewById(R.id.telp);
        }
    }
    public WaitingListAdapter(List<ImageUpload> listImage){
        this.listImage = listImage;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
//        itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                final Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.dialog_view);
//                dialog.setTitle("Pilih Aksi");
//                dialog.show();
//
//                Button editButton = (Button) dialog.findViewById(R.id.bt_edit_data);
//                Button delButton = (Button) dialog.findViewById(R.id.bt_delete_data);
//                //apabila tombol edit diklik
//                editButton.setOnClickListener(
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
//                                Toast.makeText(context, "OnClick" + position, Toast.LENGTH_SHORT).show();
////                                context.startActivity(FirebaseDBCreateActivity.getActIntent((Activity) context).putExtra("data", daftarBarang.get(position)));
//                            }
//                        }
//                );
//
//                //apabila tombol delete diklik
//                delButton.setOnClickListener(
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                            }
//                        }
//                );
//
//                return true;
//            }
//        });
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(WaitingListAdapter.MyViewHolder holder, final int position) {
        final ImageUpload imageUpload = listImage.get(position);
        int currentPosition = position;

        holder.tvName.setText(listImage.get(position).getName());
        holder.tvLokasi.setText(listImage.get(position).getLokasi());
        holder.tvTelp.setText(listImage.get(position).getNumber());
        Glide.with(context)
                .load(listImage.get(position).getUrl())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("JUDUL", listImage.get(position).getName());
                if (!namaMenu.equals(""))
                    intent.putExtra("SUB", namaMenu);
                else
                    intent.putExtra("SUB", listImage.get(position).getNameSub());
                context.startActivity(intent);
            }
        });
    }

    private void delete(ImageUpload imageUpload) {
        Task<Void> databaseReference = FirebaseDatabase.getInstance().getReference("cekpedia")
                .child("cekpediaItem").child("waitinglist").child(namaMenu).removeValue();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
//        //creates a storage reference
//        final String FB_STORAGE_PATH = "cekpediaItem/";
        StorageReference storageRef = storage.getReference();
//        StorageReference ref = storageRef.child(FB_STORAGE_PATH);
//        ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show();
//            }
//        });
        int currentposition = listImage.indexOf(imageUpload);
        listImage.remove(currentposition);
        notifyItemRemoved(currentposition);
    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }
}
