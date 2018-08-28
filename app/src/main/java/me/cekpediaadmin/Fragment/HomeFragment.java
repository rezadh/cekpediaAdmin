package me.cekpediaadmin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.cekpediaadmin.Activity.WaitingListActivity;
import me.cekpediaadmin.R;

public class HomeFragment extends Fragment {
    TextView waitinglist, sponsor, allItem;
    View view;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        waitinglist = view.findViewById(R.id.tvwaitinglist);
        sponsor = view.findViewById(R.id.tvsponsor);
        allItem = view.findViewById(R.id.tvallitem);

        waitinglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), WaitingListActivity.class));
            }
        });
        allItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllItemFragment fragment = new AllItemFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            }
        });
        sponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SponsorFragment fragment = new SponsorFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}
