package com.haha.administrator.courseapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.haha.administrator.courseapplication.R;
import com.haha.administrator.courseapplication.StuInfoPersonActivity;

public class StuInfoFragment extends Fragment {

    private Button moreInfoBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stu_info_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        moreInfoBtn=(Button)getActivity().findViewById(R.id.stu_info_inner_person_btn);
        moreInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), StuInfoPersonActivity.class);
                startActivity(intent);
            }
        });
    }
}
