package dioobanu.yahoo.employeeku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private Button bLogin;
    private Button bLiatData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        bLogin = findViewById(R.id.button2);
        bLiatData = findViewById(R.id.button);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        bLiatData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent liatdata = new Intent(StartActivity.this, MainActivity.class);
                startActivity(liatdata);
            }
        });

    }
}
