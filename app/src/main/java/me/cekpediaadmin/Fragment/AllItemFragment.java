package me.cekpediaadmin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.cekpediaadmin.Activity.MasjidActivity;
import me.cekpediaadmin.Adapter.ImageAdapter;
import me.cekpediaadmin.R;


public class AllItemFragment extends Fragment implements ImageAdapter.ClickListener {
    int[] gambar = {
            R.drawable.ic_masjid,
            R.drawable.ic_wisata,
            R.drawable.ic_penginapan,
            R.drawable.ic_rumah_sakit,
            R.drawable.ic_restoran,
            R.drawable.ic_supermarket,
            R.drawable.ic_sekolah,
            R.drawable.ic_transportasi,
            R.drawable.ic_input_lokasi,
            R.drawable.ic_spbu,
            R.drawable.ic_apotek,
            R.drawable.ic_bidan
    };
    String [] namaMenu = {
            "    Masjid",
            "    Wisata",
            "Penginapan",
            "Rumah Sakit",
            "  Restoran",
            "Supermarket",
            "   Sekolah",
            "Transportasi",
            "Input Lokasi",
            "     SPBU",
            "    Apotek",
            "    Bidan"
    };
    View view;
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    public AllItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_all_item, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        ImageAdapter myAdapter = new ImageAdapter(getActivity(), gambar, namaMenu);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setClickListener(this);
        return view;
    }

    @Override
    public void itemClicked(View view, int position) {
        switch (position) {
            case 0:
                Intent intentmasjid = new Intent(getActivity(), MasjidActivity.class);
                startActivity(intentmasjid);
                break;
//            case 1:
//                Intent intentwisata = new Intent(getActivity(), WisataActivity.class);
//                startActivity(intentwisata);
//                break;
//            case 2:
//                Intent intenthotel = new Intent(getActivity(), HotelActivity.class);
//                startActivity(intenthotel);
//                break;
//            case 3:
//                Intent intentrs = new Intent(getActivity(), RumahsakitActivity.class);
//                startActivity(intentrs);
//                break;
//            case 4:
//                Intent intentrestoran = new Intent(getActivity(), RestoranActivity.class);
//                startActivity(intentrestoran);
//                break;
//            case 5:
//                Intent intentsupermarket = new Intent(getActivity(), SupermarketActivity.class);
//                startActivity(intentsupermarket);
//                break;
//            case 6:
//                Intent intentsekolah = new Intent(getActivity(), SekolahActivity.class);
//                startActivity(intentsekolah);
//                break;
//            case 7:
//                Intent intenttransport = new Intent(getActivity(), TransportasiActivity.class);
//                startActivity(intenttransport);
//                break;
//            case 8:
//                Intent intentinput = new Intent(getActivity(), InputLokasiActivity.class);
//                startActivity(intentinput);
//                break;
//            case 9:
//                Intent intentspbu = new Intent(getActivity(), SPBUActivity.class);
//                startActivity(intentspbu);
//                break;
//            case 10:
//                Intent intentapotek = new Intent(getActivity(), ApotekActivity.class);
//                startActivity(intentapotek);
//                break;
//            case 11:
//                Intent intentbidan = new Intent(getActivity(), BidanActivity.class);
//                startActivity(intentbidan);
//                break;
//
        }
    }
}
