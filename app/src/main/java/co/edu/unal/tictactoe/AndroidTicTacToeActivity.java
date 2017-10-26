package co.edu.unal.tictactoe;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Dialog;
import android.app.AlertDialog;


public class AndroidTicTacToeActivity extends AppCompatActivity {

    private TicTacToeGame mGame;
    static final int DIALOG_QUIT_ID = 1;
    private SharedPreferences mPrefs;

    // Various text displayed
    private TextView mInfoTextView;
    private BoardView mBoardView;
    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;
    MediaPlayer mHumanWin;
    MediaPlayer mComputerWin;
    private int mHumanWins;
    private int mComputerWins;
    private int mTies;
    private boolean mSoundOn = true;
    private TextView mHumanWinsView;
    private TextView mComputerWinsView;
    private TextView mTiesView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSoundOn = mPrefs.getBoolean("sound", true);
        // Restore the scores
        mHumanWins = mPrefs.getInt("mHumanWins", 0);
        mComputerWins = mPrefs.getInt("mComputerWins", 0);
        mTies = mPrefs.getInt("mTies", 0);


        setContentView(R.layout.activity_android_tic_tac_toe);
        mInfoTextView = (TextView) findViewById(R.id.information);
        mHumanWinsView = (TextView) findViewById(R.id.human);
        mComputerWinsView = (TextView) findViewById(R.id.computer);
        mTiesView = (TextView) findViewById(R.id.ties);


        mGame = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);
        if (savedInstanceState == null) {
            startNewGame();
        }
        else {
            // Restore the game's state
            mGame.setBoardState(savedInstanceState.getCharArray("board"));
            mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
            mHumanWins = savedInstanceState.getInt("mHumanWins");
            mComputerWins = savedInstanceState.getInt("mComputerWins");
            mTies = savedInstanceState.getInt("mTies");

        }
        displayScores();

    }

    private void displayScores() {
        mHumanWinsView.setText(Integer.toString(mHumanWins));
        mComputerWinsView.setText(Integer.toString(mComputerWins));
        mTiesView.setText(Integer.toString(mTies));
    }


    // Set up the game board.
    private void startNewGame() {
        mGame.clearBoard();
        // Reset all buttons
        mBoardView.invalidate();
        // Human goes first
        mInfoTextView.setText("You go first");
    } // End of startNewGame

    private boolean setMove(char player, int location) {
        if (mGame.setMove(player, location)) {
            if( mSoundOn ) {
                if (player == TicTacToeGame.HUMAN_PLAYER) mHumanMediaPlayer.start();
                else mComputerMediaPlayer.start();
            }
            mBoardView.invalidate();   // Redraw the board
            return true;
        }
        return false;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startNewGame();
        return true;
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
                return true;
            case R.id.reset:
                resetResults();
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_CANCELED) {
            // Apply potentially new settings
            mSoundOn = mPrefs.getBoolean("sound", true);
            String difficultyLevel = mPrefs.getString("difficulty_level",
                    getResources().getString(R.string.difficulty_harder));
            if (difficultyLevel.equals(getResources().getString(
                    R.string.difficulty_easy))) {
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);

            } else if (difficultyLevel.equals(getResources().getString(
                    R.string.difficulty_harder))) {
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
            } else {
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
            }
        }
    }



    public void resetResults(){
        mComputerWins = 0;
        mHumanWins = 0;
        mTies = 0;
        displayScores();
    }
    int selected;
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_QUIT_ID:
                finish();
                System.exit(0);
                break;
        }

        return dialog;
    }

    // Listen for touches on the board
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if (!mGame.getWin() && setMove(TicTacToeGame.HUMAN_PLAYER, pos))	{

                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText("It's Android's turn.");
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }

                if (winner == 0)
                    mInfoTextView.setText("It's your turn.");
                else if (winner == 1) {
                    mGame.setWin();
                    mTies += 1;
                    mInfoTextView.setText("It's a tie!");
                }
                else if (winner == 2) {
                    mGame.setWin();
                    mHumanWins += 1;
                    mInfoTextView.setText("You won!");
                    if(mSoundOn) mHumanWin.start();
                }
                else {
                    mGame.setWin();
                    mComputerWins += 1;
                    mInfoTextView.setText("Android won!");
                    if( mSoundOn ) mComputerWin.start();
                }
            }
            displayScores();

// So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sword);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.swish);
        mHumanWin = MediaPlayer.create(getApplicationContext(), R.raw.live);
        mComputerWin = MediaPlayer.create(getApplicationContext(), R.raw.dead);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
        mHumanWin.release();
        mComputerWin.release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharArray("board", mGame.getBoardState());
        outState.putInt("mHumanWins", Integer.valueOf(mHumanWins));
        outState.putInt("mComputerWins", Integer.valueOf(mComputerWins));
        outState.putInt("mTies", Integer.valueOf(mTies));
        outState.putCharSequence("info", mInfoTextView.getText());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mGame.setBoardState(savedInstanceState.getCharArray("board"));
        mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
        mHumanWins = savedInstanceState.getInt("mHumanWins");
        mComputerWins = savedInstanceState.getInt("mComputerWins");
        mTies = savedInstanceState.getInt("mTies");

    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save the current scores
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("mHumanWins", mHumanWins);
        ed.putInt("mComputerWins", mComputerWins);
        ed.putInt("mTies", mTies);
        ed.commit();
    }




}
