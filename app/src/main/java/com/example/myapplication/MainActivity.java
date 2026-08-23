package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity
        implements InputPlayerIdDialog.OnPlayerIdSubmittedListener,
        GameView.GameEventListener {

    private GameView gameView;
    private TextView scoreTextView;
    private TextView bestScoreTextView;
    private TextView rankTextView;
    private GameBoard gameBoard;
    private Button restartButton;
    private Button leaderboardButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据库
        dbHelper = new DatabaseHelper(this);

        // 初始化UI组件
        gameView = findViewById(R.id.gameView);
        scoreTextView = findViewById(R.id.scoreTextView);
        bestScoreTextView = findViewById(R.id.bestScoreTextView);
        rankTextView = findViewById(R.id.rankTextView);
        restartButton = findViewById(R.id.restartButton);
        leaderboardButton = findViewById(R.id.leaderboardButton);

        // 初始化游戏逻辑
        gameBoard = new GameBoard(this);
        gameView.setGameBoard(gameBoard);
        gameView.setScoreTextView(scoreTextView);
        gameView.setBestScoreTextView(bestScoreTextView);
        gameView.setRankTextView(rankTextView);

        // 设置游戏事件监听器
        gameView.setGameEventListener(this);

        // 设置重新开始按钮
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBoard.startNewGame();
                gameView.refreshGameView();
                updateRankDisplayFromGame();  // 调用正确的方法
            }
        });

        // 设置排行榜按钮
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeaderboard();
            }
        });

        // 开始新游戏
        gameBoard.startNewGame();
        updateScoreDisplay();
        updateRankDisplayFromGame();  // 调用正确的方法
    }

    private void updateScoreDisplay() {
        if (scoreTextView != null && gameBoard != null) {
            scoreTextView.setText("分数: " + gameBoard.getScore());
        }
        if (bestScoreTextView != null && gameBoard != null) {
            bestScoreTextView.setText("最高: " + gameBoard.getBestScore());
        }
    }

    // 更新排名显示（从游戏内部调用）
    public void updateRankDisplayFromGame() {
        if (rankTextView == null || gameBoard == null) return;

        int currentScore = gameBoard.getScore();
        if (dbHelper != null && dbHelper.canEnterTop10(currentScore)) {
            rankTextView.setText("当前排名: 可进入前十！");
            rankTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            rankTextView.setText("当前排名: 未进入前十");
            rankTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    public void showGameOver(int finalScore) {
        // 检查是否能进入排行榜
        if (dbHelper.canEnterTop10(finalScore) && finalScore > 0) {
            // 显示输入ID对话框
            InputPlayerIdDialog dialog = new InputPlayerIdDialog(finalScore);
            dialog.setOnPlayerIdSubmittedListener(this);

            FragmentManager fm = getSupportFragmentManager();
            dialog.show(fm, "input_player_id");
        } else {
            Toast.makeText(this, "游戏结束！最终分数: " + finalScore, Toast.LENGTH_LONG).show();
        }
    }

    // 添加这个方法以修复GameBoard.java中的调用
    public void showGameOverSimple(int finalScore) {
        Toast.makeText(this, "游戏结束！最终分数: " + finalScore, Toast.LENGTH_LONG).show();
    }

    public void showGameWin(int score) {
        // 显示输入ID对话框
        InputPlayerIdDialog dialog = new InputPlayerIdDialog(score);
        dialog.setOnPlayerIdSubmittedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        dialog.show(fm, "input_player_id");
    }

    // 添加这个方法以修复GameBoard.java中的调用
    public void showGameWinSimple(int score) {
        Toast.makeText(this, "恭喜你达到2048！当前分数: " + score, Toast.LENGTH_LONG).show();
    }

    // 显示排行榜
    private void showLeaderboard() {
        LeaderboardDialog dialog = new LeaderboardDialog();
        FragmentManager fm = getSupportFragmentManager();
        dialog.show(fm, "leaderboard_dialog");
    }

    // InputPlayerIdDialog回调接口
    @Override
    public void onPlayerIdSubmitted() {
        // 更新排名显示
        updateRankDisplayFromGame();
    }

    // GameView.GameEventListener接口方法 - 分数变化回调
    @Override
    public void onScoreChanged(int newScore) {
        // 分数改变时的处理
        updateScoreDisplay();
    }

    // GameView.GameEventListener接口方法 - 游戏状态变化回调
    @Override
    public void onGameStateChanged() {
        // 游戏状态改变时的处理
        if (gameBoard.isGameWon()) {
            // 游戏胜利
            if (dbHelper.canEnterTop10(gameBoard.getScore())) {
                showGameWin(gameBoard.getScore());
            } else {
                showGameWinSimple(gameBoard.getScore());
            }
        } else if (gameBoard.isGameLost()) {
            // 游戏结束
            if (dbHelper.canEnterTop10(gameBoard.getScore())) {
                showGameOver(gameBoard.getScore());
            } else {
                showGameOverSimple(gameBoard.getScore());
            }
        }
    }

    // GameView.GameEventListener接口方法 - 更新排名显示回调
    @Override
    public void updateRankDisplay() {
        // 这个方法来自接口，直接调用内部方法
        updateRankDisplayFromGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}