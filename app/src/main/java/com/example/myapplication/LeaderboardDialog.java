package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import java.util.List;

public class LeaderboardDialog extends DialogFragment {
    private DatabaseHelper dbHelper;
    private ListView listView;
    private TextView emptyView;
    private Button clearButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_leaderboard, null);

        // 初始化视图
        listView = view.findViewById(R.id.leaderboard_list);
        emptyView = view.findViewById(R.id.empty_view);
        clearButton = view.findViewById(R.id.clear_button);

        // 初始化数据库
        dbHelper = new DatabaseHelper(getActivity());

        // 加载数据
        loadLeaderboardData();

        // 清空按钮点击事件
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearConfirmationDialog();
            }
        });

        builder.setView(view)
                .setTitle("排行榜 - 前十名")
                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    private void loadLeaderboardData() {
        List<ScoreRecord> scoreList = dbHelper.getTop10Scores();

        if (scoreList.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            clearButton.setEnabled(false);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            clearButton.setEnabled(true);

            // 创建适配器
            String[] items = new String[scoreList.size()];
            for (int i = 0; i < scoreList.size(); i++) {
                ScoreRecord record = scoreList.get(i);
                items[i] = (i + 1) + ". " + record.toString();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    items
            );

            listView.setAdapter(adapter);
        }
    }

    private void showClearConfirmationDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("确认清空")
                .setMessage("确定要清空所有排行榜记录吗？此操作不可恢复。")
                .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.clearAllRecords();
                        loadLeaderboardData();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}