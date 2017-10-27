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

    private DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference();

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
        EditText pass  = (EditText)findViewById(R.id.password);
        DatabaseReference newgame = refDatabase.child(name.getText().toString());
        GameStruc game = new GameStruc(pass.getText().toString(),false);
        newgame.child("pass").setValue(game.pass);
        newgame.child("started").setValue(game.started);
        newgame.child("board").setValue(game.board);
        finish();
    }

    public class GameStruc{
        String pass;
        String board = "000000000";
        boolean started;

        public GameStruc(String pass, boolean started){
            this.pass = pass;
            this.started = started;
        }
    }
}
