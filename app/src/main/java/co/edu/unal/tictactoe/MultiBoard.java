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
    int player;
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
        player = getIntent().getIntExtra("player",-1);
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
                if(turn == player ){
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
                    else if( winner-1 == player)
                        mInfoTextView.setText("Ganaste");
                    else mInfoTextView.setText("Perdiste");
                } else {
                    if(player == 1 && player == turn) {
                        setMove(TicTacToeGame.HUMAN_PLAYER, pos);
                    }
                    else if(player == turn){
                        setMove(TicTacToeGame.COMPUTER_PLAYER, pos);
                    }
                }
            }
            return false;
        };
    };

    private boolean setMove(char player, int location) {
        Log.d(String.valueOf(player), String.valueOf(location));
        Log.d("Actual board",mGame.toString());
        if (mGame.setMove(player, location)) {
            //if(player == 0) refGame.child("turn").setValue(1);
            //else  refGame.child("turn").setValue(0);
            mBoardView.invalidate();   // Redraw the board
            return true;
        }
        return false;
    }
}
