package tehhutan.app.kajianhunter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import tehhutan.app.kajianhunter.utils.FirebaseUtils;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    public View kajianDialog;
    public CircleImageView profilePhoto;
    public Double plcLatitude=86.0, plcLongtitude=181.0;
    //public String locationUri="";
    private final int PLACE_PICKER_REQUEST = 442;
    private static final int PICK_FROM_GALLERY = 532;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragmentContainer, new FindingFragment());
        tx.commit();
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_finding:
                fragment = new FindingFragment();
                break;
            case R.id.navigation_timeline:
                fragment = new TimelineFragment();
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;
        }
        return loadFragment(fragment);
    }

    boolean click_duaKali=false;
    public void onBackPressed() {
        //  super.onBackPressed();
        if(click_duaKali){
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            System.exit(0);
        }
        Toast.makeText(MainActivity.this, "Silahkan tekan kembali untuk keluar!", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                click_duaKali=false;
            }
        },3000);
        click_duaKali=true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Add your code here
        //Toast.makeText(MainAct.this, "Fragment Got it: " + requestCode + ", " + resultCode, Toast.LENGTH_SHORT).show();
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, MainActivity.this);
            String address = String.format("%s", place.getAddress());
            EditText tempat = (EditText) kajianDialog.findViewById(R.id.et_deskripsikegiatan);
            plcLatitude = place.getLatLng().latitude;
            plcLongtitude = place.getLatLng().longitude;
            //  locationUri = String.format("%s", String.valueOf(place.getWebsiteUri()));
            //Toast.makeText(MainAct.this, "alamat " + address + "\nlatitude : " + String.valueOf(latitude) + "\nlongtitude : " + String.valueOf(longitude), Toast.LENGTH_LONG).show();
            tempat.setText(address);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "request223!", Toast.LENGTH_LONG).show();
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            Bundle extras2 = data.getExtras();
            Toast.makeText(getApplicationContext(), "request!", Toast.LENGTH_LONG).show();
            /*if (extras2 != null) {
                Toast.makeText(getApplicationContext(), "request 2!", Toast.LENGTH_LONG).show();
                final Bitmap photo = extras2.getParcelable("data");
                FileOutputStream out = null;
               /* try {
                    out = new FileOutputStream("fotoPP");
                    photo.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                    Uri alamatPhoto = Uri.fromFile(getApplicationContext().getFileStreamPath("fotoPP"));
                    StorageReference filepath = FirebaseUtils.getProfilePhotoRef().child(FirebaseUtils.getUserID(getApplicationContext())).child(alamatPhoto.getLastPathSegment());
                    filepath.putFile(alamatPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String URL_IMAGE = taskSnapshot.getDownloadUrl().toString();
                            FirebaseUtils.getUserRef(FirebaseUtils.getUserID(getApplicationContext())).child("photo").setValue(URL_IMAGE);
                            profilePhoto.setImageBitmap(photo);
                            profilePhoto.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Upload failed!", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                profilePhoto.setImageBitmap(photo);
                profilePhoto.setScaleType(ImageView.ScaleType.FIT_XY);
            } */
            try {
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("uploading");
                dialog.setCancelable(false);
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                uploadPPtofirebase(selectedImage);
               // profilePhoto.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
    public void uploadPPtofirebase(final Bitmap photo) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bao); // bmp is bitmap from user image file
        byte[] byteArray = bao.toByteArray();
        //String imageB64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //FirebaseUtils.getUserRef(FirebaseUtils.getUserID(getApplicationContext())).child("photo").setValue(imageB64);
        //        //  store & retrieve this string to firebase
        final StorageReference filepath = FirebaseUtils.getProfilePhotoRef().child(FirebaseUtils.getUserID(getApplicationContext())).child("KajianPP.png");
        UploadTask uploadTask = filepath.putBytes(byteArray);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                String URL_IMAGE = filepath.getDownloadUrl().toString();
                FirebaseUtils.getUserRef(FirebaseUtils.getUserID(getApplicationContext())).child("photo").setValue(URL_IMAGE);
                profilePhoto.setImageBitmap(photo);
                profilePhoto.setForeground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent)));
            }
        });
    }
}
