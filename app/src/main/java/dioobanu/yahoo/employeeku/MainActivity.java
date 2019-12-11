package dioobanu.yahoo.employeeku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private Button addButton;

    private RecyclerView mListPegawai;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);

        mListPegawai = findViewById(R.id.recyclerView);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DataPegawai");

        addButton = findViewById(R.id.buttonadd);

        mListPegawai.setHasFixedSize(true);
        mListPegawai.setLayoutManager(new LinearLayoutManager(this));


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tambah = new Intent(MainActivity.this, AddActivity.class);
                startActivity(tambah);
                finish();
            }
        });


    }

    public void onStart() {
        super.onStart();
        startListening();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            addButton.setEnabled(false);
            addButton.setVisibility(View.GONE);
            getSupportActionBar().setTitle("EmployeeKu");

        } else {
            getSupportActionBar().setTitle("EmployeeKu (Admin)");
        }
    }

    private void balikKeAwal() {
        Intent start = new Intent (MainActivity.this, StartActivity.class);
        startActivity(start);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logout){

            FirebaseAuth.getInstance().signOut();
            balikKeAwal();
        }
        return true;
    }

    public void startListening() {

        Query query = FirebaseDatabase.getInstance().getReference().child("DataPegawai").limitToLast(50);
        FirebaseRecyclerOptions<DataPegawai> options = new FirebaseRecyclerOptions.Builder<DataPegawai>().setQuery(query, DataPegawai.class).build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<DataPegawai, PegawaiViewHolder>(options)
        {
            @Override
            public PegawaiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pegawai, parent, false);
                return new PegawaiViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PegawaiViewHolder holder, int position, DataPegawai model) {
                // Bind the Chat object to the ChatHolder
                holder.setNama(model.nama); //masukan nama user ke dalam method setName
                 //masukan nama user ke dalam method setStatus
                holder.setGambarprofil(model. gambarprofil);

                final String user_id = getRef(position).getKey();

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent profileActivity = new Intent(MainActivity.this, ProfilPegawai.class);
                        profileActivity.putExtra("DataPegawai", user_id);
                        startActivity(profileActivity);
                    }
                });
                // ...
            }
        };

        mListPegawai.setAdapter(adapter);
        adapter.startListening();
    }

    public static class PegawaiViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public PegawaiViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setNama(String nama)
        {
            TextView userNamaView = mView.findViewById(R.id.namapegawai);
            userNamaView.setText(nama);
        }


        public void setGambarprofil(String gambarprofil) {
            CircleImageView userThumbImage = mView.findViewById(R.id.pegawaiimg);
            Picasso.with(mView.getContext()).load(gambarprofil).placeholder(R.drawable.defaultavatar).into(userThumbImage);
        }
    }

}
