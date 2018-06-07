package com.haha.administrator.courseapplication.fragment;


import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.haha.administrator.courseapplication.R;

/**
 * 弃用
 */
@Deprecated
public class SearchStuDialogFragment extends DialogFragment {

    public interface Callback {
        //void onClick(String userId, String userName);
        void onClick(String request, int type);
    }

    private Callback callback;
/*
    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "SearchStuDialogFragment");
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_search_stu, null);
        builder.setView(view)
                .setPositiveButton("查询", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null) {
                            EditText idEdit = (EditText) view.findViewById(R.id.dialog_search_id);
                            EditText nameEdit = (EditText) view.findViewById(R.id.dialog_search_name);
                            String idStr = idEdit.getText().toString().trim();
                            String nameStr = nameEdit.getText().toString().trim();
                            if (idStr.isEmpty() && nameStr.isEmpty()) {
                                idEdit.setError("不能为空");
                            } else {
                                if (idStr.isEmpty()) {
                                    callback.onClick(nameStr, 2);
                                }
                                if (nameStr.isEmpty()) {
                                    callback.onClick(idStr, 1);
                                }
                                if (!idStr.isEmpty() && !nameStr.isEmpty()) {
                                    callback.onClick(idStr, 1);
                                }
                            }
                            //callback.onClick(idEdit.getText().toString(), nameEdit.getText().toString());
                        }
                    }
                });

        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement Callback");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }

}
