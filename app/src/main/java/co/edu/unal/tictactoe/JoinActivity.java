package co.edu.unal.tictactoe;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JoinActivity extends ListActivity {
    private DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference();

    ArrayList<String> gameNames;
    List<GameStruc> games;
    GameStruc gameSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_join);

        getGames();


    }



    public void getGames(){
        games = new ArrayList<>();
        gameNames = new ArrayList<>();
        refDatabase.orderByChild("started").equalTo(false).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                games.clear();
                gameNames.clear();
                if(dataSnapshot.exists()) {
                    Log.d("dad", dataSnapshot.getValue().toString());
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        GameStruc game = postSnapshot.getValue(GameStruc.class);
                        game.setId(postSnapshot.getKey().toString());
                        gameNames.add(game.getPlayer1() + " " + game.getName());
                        games.add(game);
                    }
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
        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_join, gameNames));
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                joinGame(position);
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void joinGame(int position){
        GameStruc gameToJoin = games.get(position);
        gameToJoin.setPlayer2("new player");
        DatabaseReference refGame = FirebaseDatabase.getInstance().getReference(gameToJoin.getId());
        refGame.child("id").setValue(gameToJoin.getId());
        refGame.child("player2").setValue(gameToJoin.getPlayer2());
        refGame.child("started").setValue(true);
        gameSelected = gameToJoin;

        Log.d("position", String.valueOf(position));
        Log.d("game", gameSelected.getId());

        join();
    }

    public void join(){
        Intent intent = new Intent(this, MultiBoard.class);
        intent.putExtra("id",gameSelected.getId());
        intent.putExtra("player", 2);
        startActivity(intent);
    }
}
