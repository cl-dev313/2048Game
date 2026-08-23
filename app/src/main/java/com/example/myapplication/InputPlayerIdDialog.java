package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;

public class InputPlayerIdDialog extends DialogFragment {
    private DatabaseHelper dbHelper;
    private int currentScore;
    private OnPlayerIdSubmittedListener listener;

    public interface OnPlayerIdSubmittedListener {
        void onPlayerIdSubmitted();
    }

    public InputPlayerIdDialog(int score) {
        this.currentScore = score;
    }

    public void setOnPlayerIdSubmittedListener(OnPlayerIdSubmittedListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_input_player_id, null);

        // 初始化视图
        TextView scoreTextView = view.findViewById(R.id.score_text);
        EditText playerIdEditText = view.findViewById(R.id.player_id_edit);

        // 显示当前分数
        scoreTextView.setText("恭喜！您获得了 " + currentScore + " 分");

        // 设置对话框
        builder.setView(view)
                .setTitle("记录您的成绩")
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String playerId = playerIdEditText.getText().toString().trim();

                        if (playerId.isEmpty()) {
                            Toast.makeText(getActivity(), "请输入您的ID或昵称", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (playerId.length() > 20) {
                            Toast.makeText(getActivity(), "ID长度不能超过20个字符", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // 保存到数据库
                        dbHelper = new DatabaseHelper(getActivity());
                        boolean success = dbHelper.addScoreRecord(playerId, currentScore);

                        if (success) {
                            Toast.makeText(getActivity(), "成绩已保存到排行榜！", Toast.LENGTH_SHORT).show();
                            if (listener != null) {
                                listener.onPlayerIdSubmitted();
                            }
                        } else {
                            Toast.makeText(getActivity(), "保存失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}