package vn.app.tintocshipper.fragment;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.utils.Utility;

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {
    ImageView iv_back;
    EditText edtPassword, edtNewPassword, edtNewpassword2;
    Button btnSend;
    FrameLayout frameChangePass;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_change_password, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            iv_back = view.findViewById(R.id.iv_back);
            frameChangePass=view.findViewById(R.id.frame_change_pass);
            btnSend = view.findViewById(R.id.btn_send2);
            edtPassword = view.findViewById(R.id.edt_password);
            edtNewPassword = view.findViewById(R.id.edt_newpassword);
            edtNewpassword2 = view.findViewById(R.id.edt_confirm_password);
            iv_back.setOnClickListener(this);
            btnSend.setOnClickListener(this);

            frameChangePass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Nothing
                }
            });

            isLoad = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                iv_back.setOnClickListener(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            iv_back.setOnClickListener(ChangePasswordFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_send2:
                if (edtPassword.getText().toString().equals("")) {
                    edtPassword.setError(getString(R.string.changepassword));
                    edtPassword.requestFocus();
                    return;
                }
                if (edtNewPassword.getText().toString().equals("")) {
                    edtNewPassword.setError(getString(R.string.newpassword));
                    edtNewPassword.requestFocus();
                    return;
                }
                if (edtNewpassword2.getText().toString().equals("")) {
                    edtNewpassword2.setError(getString(R.string.confirmpassword));
                    edtNewpassword2.requestFocus();
                    return;
                }
                if (edtNewPassword.getText().toString().length() < 6) {
                    edtNewPassword.setError(getString(R.string.err_6_char));
                    edtNewPassword.requestFocus();
                    return;
                }
                if (edtNewpassword2.getText().toString().length() < 6) {
                    edtNewpassword2.setError(getString(R.string.err_6_char));
                    edtNewpassword2.requestFocus();
                    return;
                }
                if (!edtNewPassword.getText().toString().equals(edtNewpassword2.getText().toString())) {
                    Toast.makeText(getContext(), getString(R.string.err_new_confirm), Toast.LENGTH_SHORT).show();
                    return;
                }
                String oldPassword = edtPassword.getText().toString();
                String newpassword = edtNewPassword.getText().toString();
                new ChangePassword().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, oldPassword, newpassword);
        }

    }

    class ChangePassword extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String password = (String) objects[0];
                String newpassword = (String) objects[1];
                String value = APIHelper.changePassword(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN), password, newpassword);
                jsonObject = new JSONObject(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                int code = jsonObject.getInt("code");
                if (code == 1) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    StorageHelper.set(StorageHelper.TOKEN, data.getString("token"));
                    Toast.makeText(getContext(), getString(R.string.change_pass_success), Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
