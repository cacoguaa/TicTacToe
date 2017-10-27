package co.edu.unal.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateActivity extends AppCompatActivity {

    public boolean create;
    private DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        create = false;
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

    }

    public void send(View view) {
        // Write a message to the database
        if(!create) {
            create = true;
            EditText name = (EditText) findViewById(R.id.name);
            EditText pass = (EditText) findViewById(R.id.password);
            EditText user = (EditText) findViewById(R.id.username);
            DatabaseReference newgame = refDatabase.push();
            GameStruc game = new GameStruc(name.getText().toString(), pass.getText().toString(), false, user.getText().toString());
            String id = newgame.getKey().toString();
            newgame.child("id").setValue(id);
            newgame.child("name").setValue(game.name);
            newgame.child("pass").setValue(game.pass);
            newgame.child("started").setValue(game.started);
            newgame.child("board").setValue(game.board);
            newgame.child("turn").setValue(game.turn);
            newgame.child("player1").setValue(game.player1);
            waitForRival(id);
        }
    }
    DatabaseReference refGame;
    public void waitForRival(String id){
        refGame = FirebaseDatabase.getInstance().getReference(id);
        refGame.child("started").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString() == "true"){
                    join();
                }
                else Log.d("started",dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
    public void join(){
        Intent intent = new Intent(this, MultiBoard.class);
        intent.putExtra("id",refGame.getKey().toString());
        intent.putExtra("player", 1);
        create = false;
        startActivity(intent);
    }
}
