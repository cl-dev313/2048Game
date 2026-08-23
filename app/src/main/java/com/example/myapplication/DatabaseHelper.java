package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "game2048.db";
    private static final int DATABASE_VERSION = 1;

    // 排行榜表
    private static final String TABLE_SCORES = "scores";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PLAYER_ID = "player_id";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_DATE = "date";

    // 创建表的SQL语句
    private static final String CREATE_SCORES_TABLE =
            "CREATE TABLE " + TABLE_SCORES + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_PLAYER_ID + " TEXT NOT NULL," +
                    COLUMN_SCORE + " INTEGER NOT NULL," +
                    COLUMN_DATE + " TEXT NOT NULL" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    // 添加新记录
    public boolean addScoreRecord(String playerId, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // 获取当前日期时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        values.put(COLUMN_PLAYER_ID, playerId);
        values.put(COLUMN_SCORE, score);
        values.put(COLUMN_DATE, currentDate);

        long result = db.insert(TABLE_SCORES, null, values);
        db.close();

        return result != -1;
    }

    // 获取前10名记录
    public List<ScoreRecord> getTop10Scores() {
        List<ScoreRecord> scoreList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_SCORES +
                " ORDER BY " + COLUMN_SCORE + " DESC LIMIT 10";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ScoreRecord record = new ScoreRecord();
                record.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                record.setPlayerId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_ID)));
                record.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE)));
                record.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));

                scoreList.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return scoreList;
    }

    // 检查分数是否能进入前十
    public boolean canEnterTop10(int score) {
        SQLiteDatabase db = this.getReadableDatabase();

        // 获取当前第10名的分数
        String query = "SELECT " + COLUMN_SCORE + " FROM " + TABLE_SCORES +
                " ORDER BY " + COLUMN_SCORE + " DESC LIMIT 10";

        Cursor cursor = db.rawQuery(query, null);

        // 如果记录少于10条，肯定能进入
        if (cursor.getCount() < 10) {
            cursor.close();
            return true;
        }

        // 移动到第10条记录
        cursor.moveToLast();
        int tenthScore = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE));

        cursor.close();
        db.close();

        return score > tenthScore;
    }

    // 获取记录总数
    public int getRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_SCORES;
        Cursor cursor = db.rawQuery(query, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    // 清空所有记录（可选功能）
    public void clearAllRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCORES, null, null);
        db.close();
    }
}