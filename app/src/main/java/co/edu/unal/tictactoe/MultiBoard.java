package co.edu.unal.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MultiBoard extends AppCompatActivity {

    GameStruc game;
    private TicTacToeGame mGame;
    private BoardView mBoardView;
    int playerId;
    private TextView mInfoTextView;
    DatabaseReference refGame;
    private int turn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_board);
        mGame = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);

        // Reset all buttons
        mBoardView.invalidate();
        mInfoTextView = (TextView) findViewById(R.id.information);
        Intent intent = getIntent();
        String gameid = getIntent().getStringExtra("id");
        playerId = getIntent().getIntExtra("player",-1);
        Log.d("player",String.valueOf(playerId));
        refGame = FirebaseDatabase.getInstance().getReference(gameid);
        refGame.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String board = dataSnapshot.child("board").getValue().toString();
                char[] boards = {'1','2','3','4','5','6','7','8','9'};
                for(int i = 0; i < 9; i++){
                    boards[i] = board.charAt(i);
                }
                mGame.setBoardState(boards);
                mBoardView.invalidate();
                turn = Integer.parseInt(dataSnapshot.child("turn").getValue().toString());
                Log.d("Cambio table", board);
                Log.d("turn", String.valueOf(turn));
                Log.d("player",String.valueOf(playerId));
                if(turn == playerId ){
                    mInfoTextView.setText("Es tu turno");
                }
                else {
                    mInfoTextView.setText("Turno rival");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("turno", "click");
            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;
            if(!mGame.getWin()){
                int winner = mGame.checkForWinner();
                if( winner != 0){
                    mGame.setWin();
                    Log.d("winner",String.valueOf(winner));
                    if( winner == 1)
                        mInfoTextView.setText("Empate");
                    else if( winner-1 == playerId)
                        mInfoTextView.setText("Ganaste");
                    else mInfoTextView.setText("Perdiste");
                    finish();
                } else {
                    if(playerId == 1 && playerId == turn) {
                        setMove(TicTacToeGame.HUMAN_PLAYER, pos);
                    }
                    else if(playerId == turn){
                        setMove(TicTacToeGame.COMPUTER_PLAYER, pos);
                    }
                }
            }
            return false;
        };
    };

    private boolean setMove(char player_char, int location) {

        Log.d("Actual board",mGame.toString());
        if (mGame.setMove(player_char, location)) {

            char[] boards = mGame.getBoardState();
            String board = "";
            for(int i = 0; i < 9; i++){
                board = board + boards[i];
            }
            refGame.child("board").setValue(board);
            Log.d("new board",board);
            if(playerId == 1) refGame.child("turn").setValue(2);
            else  refGame.child("turn").setValue(1);
            return true;
        }
        return false;
    }
}
