package co.edu.unal.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void multiplayer(View view){
        Intent intent = new Intent(this, MultiplayerActivity.class);
        startActivity(intent);
    }

    public void join(View view){
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

    public void robot(View view){
        Intent intent = new Intent(this, AndroidTicTacToeActivity.class);
        startActivity(intent);
    }

    public void quit(View view){
        finish();
        System.exit(0);
    }

}
