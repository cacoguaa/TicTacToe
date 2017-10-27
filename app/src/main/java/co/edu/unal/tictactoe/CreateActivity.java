package co.edu.unal.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateActivity extends AppCompatActivity {

    private DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

    }

    public void send(View view) {
        // Write a message to the database
        EditText name  = (EditText)findViewById(R.id.name);
        EditText pass  = (EditText)findViewById(R.id.password);
        EditText user = (EditText)findViewById(R.id.username);
        DatabaseReference newgame = refDatabase.push();
        GameStruc game = new GameStruc(name.getText().toString(), pass.getText().toString(),false,user.getText().toString());
        newgame.child("name").setValue(game.name);
        newgame.child("pass").setValue(game.pass);
        newgame.child("started").setValue(game.started);
        newgame.child("board").setValue(game.board);
        newgame.child("turn").setValue(game.turn);
        newgame.child("player1").setValue(game.player1);
        finish();
    }

}
