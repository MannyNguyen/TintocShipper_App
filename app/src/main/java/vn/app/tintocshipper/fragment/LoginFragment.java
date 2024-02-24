package vn.app.tintocshipper.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import vn.app.tintocshipper.MainActivity;
import vn.app.tintocshipper.R;
import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.utils.ConnectivityReceiver;
import vn.app.tintocshipper.utils.Utility;

public class LoginFragment extends BaseFragment implements ConnectivityReceiver.ConnectivityReceiverListener,
        View.OnClickListener {
    //region Var
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private String email, password;
    //endregion

    //region OnCreateView
    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //region Constructor
    public LoginFragment() {
    }
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_login, container, false);
        }
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            checkConnection();
            edtEmail = view.findViewById(R.id.edt_login_email);
            edtPassword = view.findViewById(R.id.edt_login_password);
            btnLogin = view.findViewById(R.id.btn_login);
            edtEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = edtEmail.getText().toString();
                    if (text.equals("")) {
                        //font & size cua HINT
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtEmail.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtEmail.setTypeface(customFont);
                    }
                }
            });


            edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i== EditorInfo.IME_ACTION_DONE){
                        email = edtEmail.getText().toString();
                        password = edtPassword.getText().toString();
                        new GetApiLoginShipper().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    return false;
                }
            });

            btnLogin.setOnClickListener(this);

            isLoad = true;
        }
    }
    //endregion

    //region Methods
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (!isConnected) {
            message = "Không tìm thấy kết nối!";
            color = Color.RED;

            Snackbar snackbar = Snackbar
                    .make(view.findViewById(R.id.ll_login_fragment), message, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Check", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }
                    });

            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }
    //endregion

    //region OnResum
    @Override
    public void onResume() {
        super.onResume();
        GlobalClass.getInstance().setConnectivityListener(this);
    }
    //endregion

    //region ConnectionChanged
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (edtEmail.getText().toString().equals("")) {
                    edtEmail.setError(getString(R.string.err_email));
                    edtEmail.requestFocus();
                    return;
                }
                if (edtPassword.getText().toString().equals("")) {
                    edtPassword.setError(getString(R.string.err_pass));
                    edtPassword.requestFocus();
                    return;
                }
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                new GetApiLoginShipper().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
        }
    }
    //endregion

    //region Request API
    class GetApiLoginShipper extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getAppLoginAPI(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        email, password);
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
                    StorageHelper.set(StorageHelper.USERNAME, data.getString("username"));
                    Intent i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion
}
