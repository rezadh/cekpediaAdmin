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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import me.cekpediaadmin.R;
import me.cekpediaadmin.Utils.Constants;
import me.cekpediaadmin.models.ImageUpload;

public class UpdateActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAMERA_REQUEST_CODE = 7777;
    private static final int SELECT_PHOTO = 100;
    private static final int PLACE_PICKER_REQUEST = 2;
    public static final String FB_STORAGE_PATH = "cekpediaItem/";
    public static final String FB_DATABASE_PATH = "cekpedia";
    private String namaMenu, namaSub, title, address, nmphone, nomortelp, UserID,
            favorit, favorit1, favorit2, url,addressLL, addressMaps, kategori, deskripsi;
    private double kordinatMaps1, kordinatMaps2;
    private Double longitude, langitude;
    private MapView mMapView;
    private GoogleMap m_map;
    private StorageTask mUploadTask;
    FirebaseDatabase mDb;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageRef,storageReference;
    DatabaseReference database, mDatabase;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    ImageView imageView;
    EditText etNama, etLokasi, etPhone, etDeskripsi;
    TextView tvKategori;
    Spinner myspinner;
    Button submit;
    FirebaseAuth firebaseAuth;
    GoogleSignInAccount account;
    final FirebaseDatabase FBdatabase = FirebaseDatabase.getInstance();
    final DatabaseReference mDatabaseRef = FBdatabase.getReference(Constants.USER_KEY);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        etNama = (EditText) findViewById(R.id.etNamaTempat);
        etLokasi = (EditText) findViewById(R.id.etSetLokasi);
        etPhone = findViewById(R.id.etPhone);
        progressDialog = new ProgressDialog(this);
        imageView = (ImageView) findViewById(R.id.iv_camera);
        submit = (Button) findViewById(R.id.bt_camera);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        //creates a storage reference
        storageRef = storage.getReference();
        myspinner = findViewById(R.id.spinner);
        tvKategori = findViewById(R.id.tvkategori);
        etDeskripsi = findViewById(R.id.etdeskripsi);
        mDb = FirebaseDatabase.getInstance();
        Intent i = getIntent();
        namaMenu = i.getStringExtra("JUDUL");
        namaSub = i.getStringExtra("SUB");
        nmphone = i.getStringExtra("NUMBER");
        final DatabaseReference submenu = mDb.getReference(FB_DATABASE_PATH).child("waitinglist").child(namaSub).child(namaMenu);
        submenu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map<String, Object> detailMenu = (Map<String, Object>) dataSnapshot.getValue();
                //String s = detailMenu.get("lokasi").toString();
                kordinatMaps1 = Double.parseDouble(detailMenu.get("longi").toString());
                kordinatMaps2 = Double.parseDouble(detailMenu.get("lang").toString());

                Glide.with(getBaseContext())
                        .load(detailMenu.get("url").toString())
                        .into(imageView);

                title = detailMenu.get("name").toString();
                address = detailMenu.get("lokasi").toString();
                nomortelp = detailMenu.get("number").toString();
                url = detailMenu.get("url").toString();
                kategori = detailMenu.get("nameSub").toString();
                deskripsi = detailMenu.get("deskripsi").toString();


                etNama.setText(title);
                etLokasi.setText(address);
                etPhone.setText(nomortelp);
                tvKategori.setText(kategori);
                etDeskripsi.setText(deskripsi);
//                myspinner.setSelection(Integer.parseInt(kategori));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
            super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
            if(requestCode == SELECT_PHOTO){
                if (resultCode == RESULT_OK) {
                    Toast.makeText(UpdateActivity.this,"Image selected, click on upload button",Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        imageView.setImageBitmap(bm);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//
                }
            }else if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(UpdateActivity.this,imageReturnedIntent);
                addressLL = place.getLatLng().toString();
                longitude = Double.parseDouble (addressLL.substring(addressLL.indexOf("(")+1, addressLL.indexOf(",")));
                langitude = Double.parseDouble (addressLL.substring(addressLL.indexOf(",")+1, addressLL.indexOf(")")));
                addressMaps = place.getAddress().toString();
                etLokasi.setText(addressMaps);
            }
        }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage(View view) {
        //create reference to images folder and assing a name to the file that will be uploaded

        if (etNama.getText().toString().equals("") && etLokasi.getText().toString().equals("") && imageView.getDrawable() == null) {
            Toast.makeText(UpdateActivity.this, "Data is Empty", Toast.LENGTH_SHORT).show();
        } else {

            //creating and showing progress dialog
            progressDialog = new ProgressDialog(this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Uploading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);
            String kategori = myspinner.getSelectedItem().toString();
            ImageUpload imageUpload = new ImageUpload(etNama.getText().toString(), etLokasi.getText().toString(), etPhone.getText().toString()
                    , url, kordinatMaps2, kordinatMaps1, etDeskripsi.getText().toString(), false, kategori + "");
//                    final FirebaseUser FBUser = firebaseAuth.getCurrentUser();
//                    mDatabaseRef.child(FBUser.getEmail().replace(".",",")).child(etNama.getText().toString()).child("favourite").setValue(false);
//                    database.child(account.getEmail().replace(".", ",")).setValue(user);
            DatabaseReference Update = FBdatabase.getReference(FB_DATABASE_PATH);
            database.child("cekpediaItem").child(kategori).child(etNama.getText().toString()).setValue(imageUpload)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                            database.child("waitinglist").child(namaSub).child(namaMenu).removeValue();
                            startActivity(new Intent(UpdateActivity.this, MainActivity.class));
                            finish();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Upload finished...", Toast.LENGTH_SHORT).show();
                }
            });
            //starting upload
//            StorageReference ref = storageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(selectedImage));

//            mUploadTask = ref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "Upload finished...", Toast.LENGTH_SHORT).show();
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressDialog.setProgress(0);
//                        }
//                    }, 500);

//                    database.child("keterangan").child(etNama.getText().toString() ).setValue(kategori);


//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                    Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
////                    //sets and increments value of progressbar
//                    progressDialog.incrementProgressBy((int) progress);
//
//                }
//            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                    database.child("waitinglist").child(namaSub).removeValue();
//                    startActivity(new Intent(UpdateActivity.this, MainActivity.class));
//                }
//            });
//            uploadTask = imageRef.putFile(selectedImage);
//            // Observe state change events such as progress, pause, and resume
//            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                    //sets and increments value of progressbar
//                    progressDialog.incrementProgressBy((int) progress);
//                }
//            });
//            // Register observers to listen for when the download is done or if it fails
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                    Toast.makeText(InputLokasiActivity.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                    Toast.makeText(InputLokasiActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                    etNama.setText("");
//                    etLokasi.setText("");
//                    //showing the uploaded image in ImageView using the download url
//                    Picasso.with(InputLokasiActivity.this).load(downloadUrl).into(imageView);
//                }
//            });
        }
        database.child("waitinglist").child(namaSub).child(namaMenu).removeValue();
    }

    public void selectImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public void openMaps(View view){
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(this);
            //i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();
        if (id == R.id.action_delete){
//            database.child("waitinglist").child(namaSub).child(namaMenu).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    final Map<String, Object> detailMenu = (Map<String, Object>) dataSnapshot.getValue();
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
            storageReference = storage.getReferenceFromUrl(url);

            database.child("waitinglist").child(namaSub).child(namaMenu).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            storageReference.delete();
                            Toast.makeText(getApplicationContext(), "File Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(UpdateActivity.this, WaitingListActivity.class));
                    finish();
                }
            });
            return true;
        }
        return true;
    }
}
