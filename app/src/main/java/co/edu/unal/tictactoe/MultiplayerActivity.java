package co.edu.unal.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MultiplayerActivity extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

    }

    public void send(View view) {
        // Write a message to the database
        EditText name  = (EditText)findViewById(R.id.name);
        mDatabase.push().setValue(name.getText().toString());
    }
}
