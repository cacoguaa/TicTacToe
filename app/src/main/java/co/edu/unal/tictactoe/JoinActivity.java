package co.edu.unal.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JoinActivity extends AppCompatActivity {
    private DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        getGames();
    }

    List<GameStruc> games;

    public void getGames(){
        games = new ArrayList<>();
        refDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                games.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    GameStruc game = postSnapshot.getValue(GameStruc.class);
                    games.add(game);
                }
                showGames();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void showGames(){
        Log.d("ace","test");
        for(GameStruc game:games){
            Log.d("child",game.getPass());
        }
    }

}
