package vn.app.tintocshipper.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import vn.app.tintocshipper.R;

/**
 * Created by Admin on 7/25/2017.
 */

public abstract class BaseFragment extends Fragment{
    public View view;
    public boolean isLoad;
    public View progress;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress = view.findViewById(R.id.progress);
        //Hide keyboard after change fragment
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Ẩn/hiện Progress loading.
    public void showProgress(){
        if(progress!=null){
            progress.setVisibility(View.VISIBLE);
        }
    }
    public void hideProgress(){
        if(progress!=null){
            progress.setVisibility(View.GONE);
        }
    }
}
