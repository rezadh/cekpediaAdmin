package me.cekpediaadmin.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import me.cekpediaadmin.Activity.WaitingListActivity;
import me.cekpediaadmin.Adapter.AdsAdapter;
import me.cekpediaadmin.Adapter.ImageListAdapter;
import me.cekpediaadmin.Adapter.SearchAdapter;
import me.cekpediaadmin.Adapter.WaitingListAdapter;
import me.cekpediaadmin.R;
import me.cekpediaadmin.models.Ads;
import me.cekpediaadmin.models.ImageUpload;

public class SponsorFragment extends Fragment {


    public SponsorFragment() {
        // Required empty public constructor
    }

    RecyclerView mRecyclerView;
    private ImageListAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private SearchView searchView;
    private StorageReference mStorageRef;
    private List<Ads> imgList;
    private AdsAdapter adapter;
    private ProgressDialog mProgressDialog;
    private String namaMenu, namaSub;
    public static final String FB_DATABASE_PATH = "slider";
    SearchAdapter searchAdapter;
    RecyclerView recyclerView;
    AdsAdapter mAdsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sponsor, container, false);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        mRecyclerView = view.findViewById(R.id.sponsor);
        adapter = new AdsAdapter(imgList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        imgList = new ArrayList<>();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Ads img = ds.getValue(Ads.class);
                    imgList.add(img);
                }
                adapter = new AdsAdapter(getActivity(), R.layout.sponsor_item, imgList);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }
}
