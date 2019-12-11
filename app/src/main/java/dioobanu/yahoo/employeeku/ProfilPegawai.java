package dioobanu.yahoo.employeeku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfilPegawai extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView nama;
    private TextView tmptgllahir;
    private TextView alamat;
    private TextView jeniskelamin;
    private TextView email;
    private TextView nomerhp;

    private Button deleteprofil;

    private ImageView fotoprofil;

    private DatabaseReference mPegawaiDatabase;
    private StorageReference mPegawaiStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_pegawai);

        final String idPegawai = getIntent().getStringExtra("DataPegawai");

        mPegawaiDatabase = FirebaseDatabase.getInstance().getReference().child("DataPegawai").child(idPegawai);

        /*StorageReference mFoto = mPegawaiStorage.child("foto_pegawai/"+.jpg.png).getDownloadUrl();*/


        nama = findViewById(R.id.profile_nama);
        tmptgllahir = findViewById(R.id.profile_tmpttgl);
        alamat = findViewById(R.id.profile_alamat);
        jeniskelamin = findViewById(R.id.profile_jk);
        email = findViewById(R.id.profil_email);
        nomerhp = findViewById(R.id.profil_nohp);
        fotoprofil = findViewById(R.id.profil_image);

        deleteprofil = findViewById(R.id.buttonDelete);

        mAuth = FirebaseAuth.getInstance();

        mPegawaiDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String namapegawai = dataSnapshot.child("nama").getValue().toString();
                String tanggallahirpegawai = dataSnapshot.child("tempattgllahir").getValue().toString();
                String alamatpegawai = dataSnapshot.child("alamat").getValue().toString();
                String jeniskelaminpegawai = dataSnapshot.child("jeniskelamin").getValue().toString();
                String emailpegawai = dataSnapshot.child("email").getValue().toString();
                String nomorhppegawai = dataSnapshot.child("nohp").getValue().toString();
                String gambarprofil = dataSnapshot.child("gambarprofil").getValue().toString();

                nama.setText(namapegawai);
                tmptgllahir.setText(tanggallahirpegawai);
                alamat.setText(alamatpegawai);
                jeniskelamin.setText(jeniskelaminpegawai);
                email.setText(" | "+emailpegawai);
                nomerhp.setText("No. HP: "+nomorhppegawai);

                Picasso.with(ProfilPegawai.this).load(gambarprofil).placeholder(R.drawable.defaultavatar).into(fotoprofil);

                deleteprofil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(ProfilPegawai.this);
                        dialog.setCancelable(false);
                        dialog.setTitle("Hapus profil pegawai");
                        dialog.setMessage("Apa anda yakin ingin menghapusnya?" );
                        dialog.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //Action for "Delete".
                                mPegawaiDatabase.removeValue();
                                Intent balik = new Intent(ProfilPegawai.this, MainActivity.class);
                                startActivity(balik);
                            }
                        })
                                .setNegativeButton("Batal ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Action for "Cancel".
                                    }
                                });

                        final AlertDialog alert = dialog.create();
                        alert.show();

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onStart(){

        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            deleteprofil.setEnabled(false);
            deleteprofil.setVisibility(View.GONE);
        }
    }
}
