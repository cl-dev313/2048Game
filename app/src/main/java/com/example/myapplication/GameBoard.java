package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.Random;
import java.util.List;  // 新增导入
import java.util.ArrayList;  // 新增导入

public class GameBoard {
    private static final int SIZE = 4;
    private int[][] board;
    private int score;
    private int bestScore;
    private MainActivity activity;
    private Random random;
    private SharedPreferences prefs;
    private DatabaseHelper dbHelper;

    // 游戏状态常量
    private static final int GAME_STATE_PLAYING = 0;
    private static final int GAME_STATE_WON = 1;
    private static final int GAME_STATE_LOST = 2;

    private int gameState = GAME_STATE_PLAYING;

    public GameBoard(Context context) {
        board = new int[SIZE][SIZE];
        random = new Random();
        score = 0;
        activity = (MainActivity) context;
        prefs = context.getSharedPreferences("Game2048", Context.MODE_PRIVATE);
        bestScore = prefs.getInt("bestScore", 0);
        dbHelper = new DatabaseHelper(context);
    }

    public void startNewGame() {
        // 清空棋盘
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = 0;
            }
        }
        score = 0;
        gameState = GAME_STATE_PLAYING;

        // 生成两个初始数字
        addRandomTile();
        addRandomTile();

        Log.d("GameBoard", "新游戏开始");
    }

    private void addRandomTile() {
        int emptyCount = countEmptyTiles();
        if (emptyCount == 0) return;

        int position = random.nextInt(emptyCount);
        int value = random.nextInt(10) < 9 ? 2 : 4; // 90%概率生成2，10%概率生成4

        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    if (count == position) {
                        board[i][j] = value;
                        return;
                    }
                    count++;
                }
            }
        }
    }

    private int countEmptyTiles() {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) count++;
            }
        }
        return count;
    }

    public boolean move(Direction direction) {
        // 如果游戏已结束，不接受移动
        if (gameState != GAME_STATE_PLAYING) {
            return false;
        }

        int[][] oldBoard = copyBoard();
        int oldScore = score;
        boolean moved = false;

        switch (direction) {
            case UP:
                moved = moveUp();
                break;
            case DOWN:
                moved = moveDown();
                break;
            case LEFT:
                moved = moveLeft();
                break;
            case RIGHT:
                moved = moveRight();
                break;
        }

        // 检查是否移动成功
        if (moved) {
            addRandomTile();

            // 检查是否获胜
            if (checkWin()) {
                gameState = GAME_STATE_WON;
                saveBestScore();

                // 检查是否能进入排行榜
                if (dbHelper.canEnterTop10(score) && activity != null) {
                    activity.showGameWin(score);
                } else {
                    activity.showGameWinSimple(score);
                }

                Log.d("GameBoard", "游戏胜利！分数: " + score);
            }

            // 检查是否游戏结束
            if (isGameOver()) {
                gameState = GAME_STATE_LOST;
                saveBestScore();

                // 检查是否能进入排行榜
                if (dbHelper.canEnterTop10(score) && activity != null) {
                    activity.showGameOver(score);
                } else {
                    activity.showGameOverSimple(score);
                }

                Log.d("GameBoard", "游戏结束！最终分数: " + score);
            }

            // 更新最高分
            if (score > bestScore) {
                bestScore = score;
                saveBestScore();
                Log.d("GameBoard", "新最高分: " + bestScore);
            }
        }

        return moved;
    }

    private boolean moveLeft() {
        boolean moved = false;
        for (int i = 0; i < SIZE; i++) {
            // 1. 移除空格
            int[] row = new int[SIZE];
            int index = 0;
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != 0) {
                    row[index++] = board[i][j];
                }
            }

            // 2. 合并相同数字
            for (int j = 0; j < SIZE - 1; j++) {
                if (row[j] != 0 && row[j] == row[j + 1]) {
                    row[j] *= 2;
                    score += row[j];
                    row[j + 1] = 0;
                    moved = true;
                }
            }

            // 3. 再次移除空格
            int[] newRow = new int[SIZE];
            index = 0;
            for (int j = 0; j < SIZE; j++) {
                if (row[j] != 0) {
                    newRow[index++] = row[j];
                }
            }

            // 4. 更新棋盘
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != newRow[j]) {
                    moved = true;
                }
                board[i][j] = newRow[j];
            }
        }
        return moved;
    }

    private boolean moveRight() {
        boolean moved = false;
        for (int i = 0; i < SIZE; i++) {
            // 1. 移除空格
            int[] row = new int[SIZE];
            int index = SIZE - 1;
            for (int j = SIZE - 1; j >= 0; j--) {
                if (board[i][j] != 0) {
                    row[index--] = board[i][j];
                }
            }

            // 2. 合并相同数字（从右向左）
            for (int j = SIZE - 1; j > 0; j--) {
                if (row[j] != 0 && row[j] == row[j - 1]) {
                    row[j] *= 2;
                    score += row[j];
                    row[j - 1] = 0;
                    moved = true;
                }
            }

            // 3. 再次移除空格
            int[] newRow = new int[SIZE];
            index = SIZE - 1;
            for (int j = SIZE - 1; j >= 0; j--) {
                if (row[j] != 0) {
                    newRow[index--] = row[j];
                }
            }

            // 4. 更新棋盘
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != newRow[j]) {
                    moved = true;
                }
                board[i][j] = newRow[j];
            }
        }
        return moved;
    }

    private boolean moveUp() {
        boolean moved = false;
        for (int j = 0; j < SIZE; j++) {
            // 1. 移除空格
            int[] col = new int[SIZE];
            int index = 0;
            for (int i = 0; i < SIZE; i++) {
                if (board[i][j] != 0) {
                    col[index++] = board[i][j];
                }
            }

            // 2. 合并相同数字
            for (int i = 0; i < SIZE - 1; i++) {
                if (col[i] != 0 && col[i] == col[i + 1]) {
                    col[i] *= 2;
                    score += col[i];
                    col[i + 1] = 0;
                    moved = true;
                }
            }

            // 3. 再次移除空格
            int[] newCol = new int[SIZE];
            index = 0;
            for (int i = 0; i < SIZE; i++) {
                if (col[i] != 0) {
                    newCol[index++] = col[i];
                }
            }

            // 4. 更新棋盘
            for (int i = 0; i < SIZE; i++) {
                if (board[i][j] != newCol[i]) {
                    moved = true;
                }
                board[i][j] = newCol[i];
            }
        }
        return moved;
    }

    private boolean moveDown() {
        boolean moved = false;
        for (int j = 0; j < SIZE; j++) {
            // 1. 移除空格
            int[] col = new int[SIZE];
            int index = SIZE - 1;
            for (int i = SIZE - 1; i >= 0; i--) {
                if (board[i][j] != 0) {
                    col[index--] = board[i][j];
                }
            }

            // 2. 合并相同数字（从下向上）
            for (int i = SIZE - 1; i > 0; i--) {
                if (col[i] != 0 && col[i] == col[i - 1]) {
                    col[i] *= 2;
                    score += col[i];
                    col[i - 1] = 0;
                    moved = true;
                }
            }

            // 3. 再次移除空格
            int[] newCol = new int[SIZE];
            index = SIZE - 1;
            for (int i = SIZE - 1; i >= 0; i--) {
                if (col[i] != 0) {
                    newCol[index--] = col[i];
                }
            }

            // 4. 更新棋盘
            for (int i = 0; i < SIZE; i++) {
                if (board[i][j] != newCol[i]) {
                    moved = true;
                }
                board[i][j] = newCol[i];
            }
        }
        return moved;
    }

    private int[][] copyBoard() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    private boolean checkWin() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 2048) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGameOver() {
        // 如果有空位，游戏未结束
        if (countEmptyTiles() > 0) {
            return false;
        }

        // 检查是否有相邻的相同数字
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // 检查右侧
                if (j < SIZE - 1 && board[i][j] == board[i][j + 1]) {
                    return false;
                }
                // 检查下侧
                if (i < SIZE - 1 && board[i][j] == board[i + 1][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isGameWon() {
        return gameState == GAME_STATE_WON;
    }

    public boolean isGameLost() {
        return gameState == GAME_STATE_LOST;
    }

    public boolean isGamePlaying() {
        return gameState == GAME_STATE_PLAYING;
    }

    private void saveBestScore() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("bestScore", bestScore);
        editor.apply();
    }

    public int[][] getBoard() {
        return board;
    }

    public int getScore() {
        return score;
    }

    public int getBestScore() {
        return bestScore;
    }

    public int getGameState() {
        return gameState;
    }

    // 检查当前分数是否能进入排行榜前十
    public boolean canEnterTop10() {
        if (dbHelper != null) {
            return dbHelper.canEnterTop10(score);
        }
        return false;
    }

    // 获取当前排名（如果能进入前十，返回预估排名）
    public int getEstimatedRank() {
        if (dbHelper == null || score <= 0) {
            return -1;
        }

        List<ScoreRecord> top10 = dbHelper.getTop10Scores();
        if (top10.isEmpty()) {
            return 1; // 排行榜为空，当前分数就是第一
        }

        for (int i = 0; i < top10.size(); i++) {
            if (score > top10.get(i).getScore()) {
                return i + 1;
            }
        }

        if (top10.size() < 10) {
            return top10.size() + 1;
        }

        return -1; // 无法进入前十
    }

    // 强制设置分数（用于测试）
    public void setScore(int score) {
        this.score = score;
    }

    // 重置游戏状态
    public void resetGameState() {
        gameState = GAME_STATE_PLAYING;
    }

    // 获取数据库助手（用于排行榜操作）
    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }
}