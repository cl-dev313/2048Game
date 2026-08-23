package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class GameView extends View {
    private GameBoard gameBoard;
    private Paint tilePaint, textPaint, bgPaint;
    private TextView scoreTextView;
    private TextView bestScoreTextView;
    private TextView rankTextView;
    private Context context;

    // 用于手势检测
    private float startX, startY;
    private static final float MIN_SWIPE_DISTANCE = 30;

    // 定义回调接口
    public interface GameEventListener {
        void onScoreChanged(int newScore);
        void onGameStateChanged();
        void updateRankDisplay();
    }

    private GameEventListener gameEventListener;

    public void setGameEventListener(GameEventListener listener) {
        this.gameEventListener = listener;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        // 初始化画笔
        tilePaint = new Paint();
        tilePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);

        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#BBADA0"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (gameBoard == null) return;

        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height) - 40;
        int startX = (width - size) / 2;
        int startY = (height - size) / 2;

        // 绘制背景
        canvas.drawRoundRect(new RectF(startX, startY, startX + size, startY + size),
                20, 20, bgPaint);

        int[][] board = gameBoard.getBoard();
        int tileSize = size / 4;
        int padding = 10;

        // 绘制所有方块
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int value = board[i][j];
                int x = startX + j * tileSize + padding;
                int y = startY + i * tileSize + padding;
                int tileWidth = tileSize - 2 * padding;

                // 设置方块颜色（根据数字值）
                setTileColor(tilePaint, value);

                // 绘制方块
                canvas.drawRoundRect(new RectF(x, y, x + tileWidth, y + tileWidth),
                        10, 10, tilePaint);

                // 绘制数字
                if (value > 0) {
                    // 根据数字大小调整文字颜色
                    textPaint.setColor(value <= 4 ? Color.parseColor("#776E65") : Color.WHITE);

                    // 根据数字大小调整文字尺寸
                    float textSize = getTextSizeForValue(value);
                    textPaint.setTextSize(textSize);

                    // 绘制数字
                    canvas.drawText(String.valueOf(value),
                            x + tileWidth / 2,
                            y + tileWidth / 2 + getTextOffset(value),
                            textPaint);
                }
            }
        }
    }

    private float getTextSizeForValue(int value) {
        if (value < 10) return 50;      // 1位数
        if (value < 100) return 45;     // 2位数
        if (value < 1000) return 40;    // 3位数
        if (value < 10000) return 35;   // 4位数
        return 30;                      // 更多位数
    }

    private float getTextOffset(int value) {
        if (value < 10) return 15;      // 1位数
        if (value < 100) return 12;     // 2位数
        if (value < 1000) return 10;    // 3位数
        if (value < 10000) return 8;    // 4位数
        return 6;                       // 更多位数
    }

    private void setTileColor(Paint paint, int value) {
        // 根据2048游戏的标准配色方案
        switch (value) {
            case 0: paint.setColor(Color.parseColor("#CDC1B4")); break;   // 空方块
            case 2: paint.setColor(Color.parseColor("#EEE4DA")); break;   // 2
            case 4: paint.setColor(Color.parseColor("#EDE0C8")); break;   // 4
            case 8: paint.setColor(Color.parseColor("#F2B179")); break;   // 8
            case 16: paint.setColor(Color.parseColor("#F59563")); break;  // 16
            case 32: paint.setColor(Color.parseColor("#F67C5F")); break;  // 32
            case 64: paint.setColor(Color.parseColor("#F65E3B")); break;  // 64
            case 128: paint.setColor(Color.parseColor("#EDCF72")); break; // 128
            case 256: paint.setColor(Color.parseColor("#EDCC61")); break; // 256
            case 512: paint.setColor(Color.parseColor("#EDC850")); break; // 512
            case 1024: paint.setColor(Color.parseColor("#EDC53F")); break; // 1024
            case 2048: paint.setColor(Color.parseColor("#EDC22E")); break; // 2048
            default: paint.setColor(Color.parseColor("#3C3A32")); break;   // >2048
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                return true;

            case MotionEvent.ACTION_UP:
                float endX = event.getX();
                float endY = event.getY();

                // 计算滑动距离
                float dx = endX - startX;
                float dy = endY - startY;

                // 判断是否满足滑动阈值
                if (Math.abs(dx) > MIN_SWIPE_DISTANCE || Math.abs(dy) > MIN_SWIPE_DISTANCE) {
                    handleSwipe(startX, startY, endX, endY);
                }
                return true;

            case MotionEvent.ACTION_CANCEL:
                // 处理触摸取消事件
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void handleSwipe(float startX, float startY, float endX, float endY) {
        float dx = endX - startX;
        float dy = endY - startY;

        // 记录滑动信息用于调试
        Log.d("GameView", String.format("Swipe - dx: %.1f, dy: %.1f", dx, dy));

        // 确定滑动方向
        Direction direction = getSwipeDirection(dx, dy);

        // 如果检测到有效方向，执行移动
        if (direction != null && gameBoard != null) {
            boolean moved = gameBoard.move(direction);
            Log.d("GameView", "Move executed: " + moved + ", Direction: " + direction);

            if (moved) {
                // 更新游戏界面
                invalidate();

                // 更新分数和排名显示
                updateScoreDisplay();

                // 通过回调接口通知主Activity更新排名
                if (gameEventListener != null) {
                    gameEventListener.onScoreChanged(gameBoard.getScore());
                    gameEventListener.updateRankDisplay();
                }

                // 检查游戏状态
                if (gameBoard.isGameWon() || gameBoard.isGameLost()) {
                    if (gameEventListener != null) {
                        gameEventListener.onGameStateChanged();
                    }
                }
            }
        }
    }

    private Direction getSwipeDirection(float dx, float dy) {
        // 计算水平和垂直滑动距离的绝对值
        float absDx = Math.abs(dx);
        float absDy = Math.abs(dy);

        // 判断主要滑动方向
        if (absDx > absDy) {
            // 水平滑动为主
            if (absDx > MIN_SWIPE_DISTANCE) {
                if (dx > 0) {
                    return Direction.RIGHT;
                } else {
                    return Direction.LEFT;
                }
            }
        } else {
            // 垂直滑动为主
            if (absDy > MIN_SWIPE_DISTANCE) {
                if (dy > 0) {
                    return Direction.DOWN;
                } else {
                    return Direction.UP;
                }
            }
        }

        // 不满足最小滑动距离要求
        return null;
    }

    private void updateScoreDisplay() {
        if (scoreTextView != null && gameBoard != null) {
            String scoreText = "分数: " + gameBoard.getScore();
            scoreTextView.setText(scoreText);
            Log.d("GameView", "Score updated: " + scoreText);
        }

        if (bestScoreTextView != null && gameBoard != null) {
            String bestScoreText = "最高: " + gameBoard.getBestScore();
            bestScoreTextView.setText(bestScoreText);
        }
    }

    // 设置游戏板对象
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    // 设置分数文本视图
    public void setScoreTextView(TextView textView) {
        this.scoreTextView = textView;
    }

    // 设置最高分文本视图
    public void setBestScoreTextView(TextView textView) {
        this.bestScoreTextView = textView;
    }

    // 设置排名文本视图
    public void setRankTextView(TextView textView) {
        this.rankTextView = textView;
    }

    // 刷新游戏界面（公开方法）
    public void refreshGameView() {
        invalidate();
        updateScoreDisplay();
    }

    // 重置游戏视图状态
    public void resetView() {
        if (gameBoard != null) {
            gameBoard.startNewGame();
        }
        invalidate();
        updateScoreDisplay();

        // 通知主Activity更新排名显示
        if (gameEventListener != null) {
            gameEventListener.updateRankDisplay();
        }
    }

    // 获取游戏板
    public GameBoard getGameBoard() {
        return gameBoard;
    }
}