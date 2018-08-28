package me.cekpediaadmin.models;

import android.widget.ImageView;

/**
 * Created by rezadwihendarno on 19/04/2018.
 */

public class Ads {
    private String name, namaMenu, Url;

    public Ads(String name, String namaMenu, String url) {
        this.name = name;
        this.namaMenu = namaMenu;
        this.Url = url;
    }
    public Ads(){

    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }
}
