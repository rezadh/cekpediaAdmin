package me.cekpediaadmin.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.cekpediaadmin.Fragment.HomeFragment;
import me.cekpediaadmin.R;

public class MainActivity extends AppCompatActivity {
    TextView waitinglist, sponsor, allItem;
    FirebaseAuth mAuth;
    private String userID;
    private FirebaseUser mFirebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
//        userID = mAuth.getCurrentUser().getUid();

        if (mAuth.getCurrentUser() == null){
            mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            startActivity(new Intent(this, LoginActivity.class));
        }
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
