 package me.cekpediaadmin.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import me.cekpediaadmin.Adapter.ImageListAdapter;
import me.cekpediaadmin.Adapter.SearchAdapter;
import me.cekpediaadmin.Adapter.WaitingListAdapter;
import me.cekpediaadmin.R;
import me.cekpediaadmin.models.ImageUpload;

 public class WaitingListActivity extends AppCompatActivity {
     RecyclerView mRecyclerView;
     private ImageListAdapter mAdapter;
     private DatabaseReference mDatabaseRef;
     private SearchView searchView;
     private StorageReference mStorageRef;
     private List<ImageUpload> imgList;
     private WaitingListAdapter adapter;
     private ProgressDialog mProgressDialog;
     private String namaMenu, namaSub;
     public static final String FB_DATABASE_PATH = "cekpedia";
     SearchAdapter searchAdapter;
     RecyclerView recyclerView;
     ImageListAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH).child("waitinglist");
        mRecyclerView = findViewById(R.id.waiting_list);
        adapter = new WaitingListAdapter(imgList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        imgList = new ArrayList<>();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    for (DataSnapshot Snapshot : ds.getChildren()){
                        ImageUpload img = Snapshot.getValue(ImageUpload.class);
                        imgList.add(img);
                    }
                }
                adapter = new WaitingListAdapter(WaitingListActivity.this, R.layout.list_item, imgList);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
