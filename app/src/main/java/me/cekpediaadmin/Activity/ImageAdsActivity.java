package me.cekpediaadmin.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import me.cekpediaadmin.R;
import me.cekpediaadmin.models.Ads;

public class ImageAdsActivity extends AppCompatActivity {

    private TextView etSubMenu;
    private EditText etNamaTempat;
    private ImageView imgAds;
    Uri selectedImage;
    public static final String FB_STORAGE_PATH = "imageAds/";
    public static final String FB_DATABASE_PATH = "slider";
    DatabaseReference database;
    StorageReference storageRef;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    FirebaseDatabase mDb;
    private String title, url, parentName, nameMenu;
    private StorageTask mUploadTask;
    private static final int SELECT_PHOTO = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_ads);
        etSubMenu = findViewById(R.id.etNameSub);
        etNamaTempat = findViewById(R.id.etNamaTempatAds);
        imgAds = findViewById(R.id.iv_camera);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mDb = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference();
        Intent i = getIntent();
        nameMenu = i.getStringExtra("JUDUL");
        final DatabaseReference submenu = mDb.getReference(FB_DATABASE_PATH).child(nameMenu);
        submenu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map<String, Object> detailMenu = (Map<String, Object>) dataSnapshot.getValue();
                //String s = detailMenu.get("lokasi").toString();
                Glide.with(getBaseContext())
                        .load(detailMenu.get("url").toString())
                        .into(imgAds);

                title = detailMenu.get("name").toString();
                parentName = detailMenu.get("namaMenu").toString();
                url = detailMenu.get("url").toString();
                etNamaTempat.setText(title);
                etSubMenu.setText(parentName);
//                myspinner.setSelection(Integer.parseInt(kategori));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void selectImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void uploadImageAds(View view){
        if (etNamaTempat.getText().toString().equals("") || imgAds.getDrawable() == null){
            Toast.makeText(this, "Data is empty", Toast.LENGTH_SHORT).show();
        }else {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Uploading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);
//            storageRef = storage.getReferenceFromUrl(url);

            StorageReference ref = storageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(selectedImage));
            mUploadTask = ref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
//                    storageRef.delete();
                    Toast.makeText(getApplicationContext(), "Upload finished...", Toast.LENGTH_SHORT).show();
                    finish();
                    Ads ads = new Ads( etNamaTempat.getText().toString(),etSubMenu.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                    database.child(FB_DATABASE_PATH).child(etSubMenu.getText().toString()).setValue(ads);
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    startActivity(new Intent(ImageAdsActivity.this, MainActivity.class));
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                    //sets and increments value of progressbar
                    progressDialog.incrementProgressBy((int) progress);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ImageAdsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(ImageAdsActivity.this, "Image selected, click on upload button", Toast.LENGTH_SHORT).show();
                selectedImage = imageReturnedIntent.getData();
                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    imgAds.setImageBitmap(bm);
                    if (bm == null){
                        Toast.makeText(this, "Data is empty", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//
            }
        }
    }
}
