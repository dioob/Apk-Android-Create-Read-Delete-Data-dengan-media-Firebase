package dioobanu.yahoo.employeeku;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputEditText namapegawai;
    private TextInputEditText tempattglpegawai;
    private TextInputEditText alamatpegawai;
    private TextInputEditText jkpegawai;
    private TextInputEditText emailpegawai;
    private TextInputEditText nohppegawai;
    private Button uploadimg;
    private Button saveData;

    private CircleImageView gambar;

    private DatabaseReference dbReference;
    private StorageReference sgReference;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAPTURE_IMAGE = 0;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mToolbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tambah Data Pegawai");

        namapegawai = findViewById(R.id.peg_nama);
        tempattglpegawai = findViewById(R.id.peg_tgl);
        alamatpegawai = findViewById(R.id.peg_alamat);
        jkpegawai = findViewById(R.id.peg_jk);
        emailpegawai = findViewById(R.id.peg_email);
        nohppegawai = findViewById(R.id.peg_nohp);
        gambar = findViewById(R.id.profile_image);

        uploadimg = findViewById(R.id.btnImg);
        saveData = findViewById(R.id.confirmadd);

        dbReference = FirebaseDatabase.getInstance().getReference().child("DataPegawai");
        sgReference = FirebaseStorage.getInstance().getReference().child("foto_pegawai");

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nu = new Intent(AddActivity.this, MainActivity.class);
                startActivity(nu);
                finish();
            }

        });

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nama = namapegawai.getText().toString().trim();
                final String tmptgllahir = tempattglpegawai.getText().toString().trim();
                final String alamat = alamatpegawai.getText().toString().trim();
                final String jk = jkpegawai.getText().toString().trim();
                final String email = emailpegawai.getText().toString().trim();
                final String nomerhp = nohppegawai.getText().toString().trim();

                if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(tmptgllahir) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(jk)
                        && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(nomerhp) && mImageUri != null) {
                    final StorageReference fileStorage = sgReference.child(System.currentTimeMillis() + "." + getFileExtention(mImageUri));

                    fileStorage.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String URLImaage = uri.toString();
                                    String id = dbReference.push().getKey();
                                    DataPegawai datanya = new DataPegawai(nama, tmptgllahir, alamat, jk, email, nomerhp, URLImaage);
                                    dbReference.child(id).setValue(datanya);

                                    namapegawai.setText("");
                                    tempattglpegawai.setText("");
                                    alamatpegawai.setText("");
                                    jkpegawai.setText("");
                                    emailpegawai.setText("");
                                    nohppegawai.setText("");
                                    gambar.setImageResource(R.drawable.defaultavatar);

                                    Toast.makeText(AddActivity.this, "Data Telah Tersimpan", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    /*String id = dbReference.push().getKey();
                    DataPegawai datanya = new DataPegawai(nama, tmptgllahir, alamat, jk, email, nomerhp);
                    dbReference.child(id).setValue(datanya);

                    namapegawai.setText("");
                    tempattglpegawai.setText("");
                    alamatpegawai.setText("");
                    jkpegawai.setText("");
                    emailpegawai.setText("");
                    nohppegawai.setText("");

                    Toast.makeText(AddActivity.this, "Data Telah Tersimpan", Toast.LENGTH_LONG).show();*/
                } else{

                    Toast.makeText(AddActivity.this, "Mohon Isi Data Dengan Lengkap!", Toast.LENGTH_LONG).show();
                }
            }
        });

        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihgambar();
            }
        });
    }

    private String getFileExtention(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    /*private void uploadGambar() {
        if (mImageUri != null){

            final StorageReference fileStorage = sgReference.child(System.currentTimeMillis() + "." + getFileExtention(mImageUri));

            fileStorage.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String URLImaage = uri.toString();
                        }
                    });
                }
            });
        }else{
            Toast.makeText(this,"Gambar belum dipilih!", Toast.LENGTH_SHORT).show();
        }

    }*/

    public void onBackPressed() {
        Intent i = new Intent(AddActivity.this,MainActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    private void pilihgambar() {

        final CharSequence[] pilihan = { "Ambil Gambar", "Pilih dari Galeri","Batal" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih ambil gambar melalui..");

        builder.setItems(pilihan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (pilihan[which].equals("Ambil Gambar")){
                    Intent ambilgambar = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(ambilgambar, CAPTURE_IMAGE);
                }
                else if (pilihan[which].equals("Pilih dari Galeri")){
                    Intent pilih = new Intent();
                    pilih.setType("image/*");
                    pilih.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(pilih, PICK_IMAGE_REQUEST);
                }
                else if (pilihan[which].equals("Batal")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(gambar);
        }

        else if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(gambar);
        }
    }


}
