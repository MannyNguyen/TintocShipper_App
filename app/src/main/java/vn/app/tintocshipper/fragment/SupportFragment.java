package vn.app.tintocshipper.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.R;
import vn.app.tintocshipper.utils.Utility;

public class SupportFragment extends BaseFragment implements View.OnClickListener {
    //region Var
    LinearLayout txtSpHotLine_1;
    LinearLayout txtSpHotline_2;
    ImageView ivBack;
    EditText edtContentSupport;
    LinearLayout llSupportSend;
    //endregion

    //region Constructor
    public SupportFragment() {
        // Required empty public constructor
    }
    //endregion

    //region Instance
    public static SupportFragment newInstance() {
        SupportFragment fragment = new SupportFragment();
        return fragment;
    }
    //endregion

    //region OnCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_support, container, false);
        }
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            txtSpHotLine_1 = view.findViewById(R.id.txt_sphotline_1);
            txtSpHotline_2 = view.findViewById(R.id.txt_sphotline_2);
            ivBack = view.findViewById(R.id.iv_back);
            edtContentSupport = view.findViewById(R.id.edt_content_support);
            llSupportSend = view.findViewById(R.id.ll_support_send);

            getView().findViewById(R.id.iv_back).setOnClickListener(this);
            getView().findViewById(R.id.iv_back).setOnClickListener(SupportFragment.this);
            getView().findViewById(R.id.txt_sphotline_1).setOnClickListener(SupportFragment.this);
            getView().findViewById(R.id.txt_sphotline_2).setOnClickListener(SupportFragment.this);
            getView().findViewById(R.id.ll_support_send).setOnClickListener(SupportFragment.this);
            getView().findViewById(R.id.btn_feedback).setOnClickListener(this);
            getView().findViewById(R.id.btn_send_request).setOnClickListener(this);

            edtContentSupport.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String content=edtContentSupport.getText().toString();
                    if (content.equals("")){
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtContentSupport.setTypeface(customFont);
                        return;
                    }else {
                        if (edtContentSupport.length()>1){
                            return;
                        }
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtContentSupport.setTypeface(customFont);
                    }
                }
            });

            isLoad = true;
        }
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                ivBack.setOnClickListener(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            ivBack.setOnClickListener(SupportFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.txt_sphotline_1:
                Intent iHotline_1 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getString(R.string.hotline_number), null));
                startActivity(iHotline_1);
                break;
            case R.id.txt_sphotline_2:
                Intent iHotline_2 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getString(R.string.hotline_number_2), null));
                startActivity(iHotline_2);
                break;
            case R.id.ll_support_send:
                break;
            case R.id.btn_feedback:
                break;
            case R.id.btn_send_request:
                break;
        }
    }
    //endregion
}
