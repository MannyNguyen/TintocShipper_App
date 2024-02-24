package vn.app.tintocshipper.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.Order;
import vn.app.tintocshipper.utils.Utility;

/**
 * Created by Admin on 9/11/2017.
 */

public class UpdateStatusOrderFragment extends BaseFragment implements View.OnClickListener {
    //region Var
    ImageView ivBack;
    TextView txtOrderType;
    EditText edtReason;
    Button btn_back_cancel_order, btn_back_receive_success;
    Button btn_cancel_order, btn_send_success, btn_return_order, btn_send;
    Button btn_recive_success, btn_recive_faild, btn_cancel;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    FrameLayout frameUpdateStt;
    int type;
    //endregion

    //region NewInstance
    public static UpdateStatusOrderFragment newInstance(String orderCode, int type, String orderType, boolean mustCmt) {
        Bundle args = new Bundle();
        args.putString("orderCode", orderCode);
        args.putInt("type", type);
        args.putString("orderType", orderType);
        args.putBoolean("mustCmt", mustCmt);
        UpdateStatusOrderFragment fragment = new UpdateStatusOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region OnCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.update_order_status_dialogfragment, container, false);
        }
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivBack = view.findViewById(R.id.iv_back);
            frameUpdateStt = view.findViewById(R.id.frame_update_stt);
            txtOrderType = view.findViewById(R.id.txt_order_type);
            edtReason = view.findViewById(R.id.edt_reason);
            btn_back_cancel_order = view.findViewById(R.id.btn_back_cancel_order);
            btn_back_receive_success = view.findViewById(R.id.btn_back_receive_success);
            btn_cancel_order = view.findViewById(R.id.btn_cancel_order);
            btn_send_success = view.findViewById(R.id.btn_send_success);
            btn_return_order = view.findViewById(R.id.btn_return_order);
            btn_send = view.findViewById(R.id.btn_send);
            btn_recive_success = view.findViewById(R.id.btn_recive_success);
            btn_recive_faild = view.findViewById(R.id.btn_recive_faild);
            btn_cancel = view.findViewById(R.id.btn_cancel);
            spinner = view.findViewById(R.id.spinner);

            frameUpdateStt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Nothing
                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i > 0) {
                        String obj = (String) spinner.getSelectedItem();
                        edtReason.setText(obj);
                    } else {
                        return;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            ivBack.setOnClickListener(this);
            txtOrderType.setText("(" + getArguments().getString("orderType") + ")");

            //region UI tab
            //tab "Nhận"
            if (getArguments().getInt("type") == 1) {
                btn_back_cancel_order.setVisibility(View.GONE);
                btn_back_receive_success.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_send_success.setVisibility(View.GONE);
                btn_return_order.setVisibility(View.GONE);
                btn_recive_success.setVisibility(View.VISIBLE);
                btn_recive_faild.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                adapter = ArrayAdapter.createFromResource(getContext(), R.array.reason, R.layout.row_spinner_transparent);
            }
            //tab "Giao"
            else if (getArguments().getInt("type") == 2) {
                btn_back_cancel_order.setVisibility(View.GONE);
                btn_back_receive_success.setVisibility(View.GONE);
                btn_recive_success.setVisibility(View.GONE);
                btn_recive_faild.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                btn_cancel_order.setVisibility(View.VISIBLE);
                btn_send_success.setVisibility(View.VISIBLE);
                btn_return_order.setVisibility(View.VISIBLE);
                adapter = ArrayAdapter.createFromResource(getContext(), R.array.reason1, R.layout.row_spinner_transparent);


            }
            //tab "Trả"
            else if (getArguments().getInt("type") == 3) {
                btn_back_cancel_order.setVisibility(View.VISIBLE);
                btn_back_receive_success.setVisibility(View.VISIBLE);
                btn_cancel_order.setVisibility(View.GONE);
                btn_send_success.setVisibility(View.GONE);
                btn_return_order.setVisibility(View.GONE);
                btn_recive_success.setVisibility(View.GONE);
                btn_recive_faild.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                adapter = ArrayAdapter.createFromResource(getContext(), R.array.reason2, R.layout.row_spinner_transparent);

            }
            spinner.setAdapter(adapter);
            adapter.setDropDownViewResource(R.layout.row_spinner);
            //endregion UI tab

            btn_back_cancel_order.setOnClickListener(this);
            btn_back_receive_success.setOnClickListener(this);
            btn_cancel_order.setOnClickListener(this);
            btn_send_success.setOnClickListener(this);
            btn_return_order.setOnClickListener(this);
            btn_send.setOnClickListener(this);
            btn_recive_success.setOnClickListener(this);
            btn_recive_faild.setOnClickListener(this);
            btn_cancel.setOnClickListener(this);
        }
        isLoad = true;
    }

    //endregion

    //region OnClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                ivBack.setOnClickListener(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            ivBack.setOnClickListener(UpdateStatusOrderFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            //region button tab "nhan"
            case R.id.btn_recive_success:
                updateUI(v.getId());
                break;

            case R.id.btn_recive_faild:
                updateUI(v.getId());
                break;

            case R.id.btn_cancel:
                updateUI(v.getId());
                break;

            //region button tab "Giao"
            case R.id.btn_send_success:
                updateUI(v.getId());
                break;
            case R.id.btn_return_order:
                updateUI(v.getId());
                break;
            case R.id.btn_cancel_order:
                updateUI(v.getId());
                break;
            //endregion

            //region button tab "Trả hàng"
            case R.id.btn_back_receive_success:
                updateUI(v.getId());
                break;
            case R.id.btn_back_cancel_order:
                updateUI(v.getId());
                break;
            //endregion

            case R.id.btn_send:
                send(type);
                break;
        }
    }
    //endregion

    //region Request API
    //region confirm take order

    class GetConfirmTakeOrder extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                int action = (int) objects[0];
                String reason = (String) objects[1];
                String value = APIHelper.getConfirmTakeOrder(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN), action, getArguments().getString("orderCode"), reason);
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

                    Toast.makeText(getActivity(), getString(R.string.confirm_success), Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStackImmediate(ShopOrderFragment.class.getName(), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //region Confirm delivery order
    class getConfirmDeliveryOrder extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                int type = (int) objects[0];
                String reason = (String) objects[1];
                String value = APIHelper.confirmDeliveryOrder(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN), type, reason, getArguments().getString("orderCode"));
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
                    HomeOrderListFragment homeOrderListFragment = (HomeOrderListFragment) getActivity().getSupportFragmentManager().findFragmentByTag(HomeOrderListFragment.class.getName());
                    if (homeOrderListFragment != null) {
                        Order order = Order.getValueByOrderCode(getArguments().getString("orderCode"), homeOrderListFragment.orders);
                        order.setAccepted(false);
                        homeOrderListFragment.recyclerOrderList.getAdapter().notifyDataSetChanged();
                    }
                    Toast.makeText(getActivity(), getString(R.string.confirm_success), Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStackImmediate(HomeOrderListFragment.class.getName(), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Confirm come back order
    class getConfirmComeBackOrder extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String userName = StorageHelper.get(StorageHelper.USERNAME);

                int type = (int) objects[0];
                String reason = (String) objects[1];
                String value = APIHelper.confirmCombackOrder(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        userName, StorageHelper.get(StorageHelper.TOKEN), type, getArguments().getString("orderCode"), reason);
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
                    Toast.makeText(getActivity(), getString(R.string.confirm_success), Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStackImmediate(ShopOrderFragment2.class.getName(), 0);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion

    //region Methods
    //region Update UI
    private void updateUI(int id) {
        //tab "nhận"
        btn_recive_success.setBackgroundResource(R.color.shipper_info);
        btn_recive_faild.setBackgroundResource(R.color.shipper_info);
        btn_cancel.setBackgroundResource(R.color.shipper_info);
        //tab "Giao"
        btn_send_success.setBackgroundResource(R.color.shipper_info);
        btn_back_cancel_order.setBackgroundResource(R.color.shipper_info);
        btn_cancel_order.setBackgroundResource(R.color.shipper_info);
        //tab "Trả hàng"
        btn_return_order.setBackgroundResource(R.color.shipper_info);
        btn_back_receive_success.setBackgroundResource(R.color.shipper_info);
        type = id;
        switch (id) {
            case R.id.btn_recive_success:
                btn_recive_success.setBackgroundResource(R.drawable.blue_border_box);
                btn_recive_success.setTextColor(Color.parseColor("#20c1ec"));
                btn_recive_faild.setTextColor(Color.BLACK);
                btn_cancel.setTextColor(Color.BLACK);
                break;
            case R.id.btn_recive_faild:
                btn_recive_faild.setBackgroundResource(R.drawable.blue_border_box);
                btn_recive_faild.setTextColor(Color.parseColor("#20c1ec"));
                btn_cancel.setTextColor(Color.BLACK);
                btn_recive_success.setTextColor(Color.BLACK);
                break;
            case R.id.btn_cancel:
                btn_cancel.setBackgroundResource(R.drawable.blue_border_box);
                btn_cancel.setTextColor(Color.parseColor("#20c1ec"));
                btn_recive_success.setTextColor(Color.BLACK);
                btn_recive_faild.setTextColor(Color.BLACK);
                break;
            case R.id.btn_send_success:
                btn_send_success.setBackgroundResource(R.drawable.blue_border_box);
                btn_send_success.setTextColor(Color.parseColor("#20c1ec"));
                btn_cancel_order.setTextColor(Color.BLACK);
                btn_return_order.setTextColor(Color.BLACK);
                break;
            case R.id.btn_back_cancel_order:
                btn_back_cancel_order.setBackgroundResource(R.drawable.blue_border_box);
                btn_back_cancel_order.setTextColor(Color.parseColor("#20c1ec"));
                btn_back_receive_success.setTextColor(Color.BLACK);
                break;
            case R.id.btn_cancel_order:
                btn_cancel_order.setBackgroundResource(R.drawable.blue_border_box);
                btn_cancel_order.setTextColor(Color.parseColor("#20c1ec"));
                btn_send_success.setTextColor(Color.BLACK);
                btn_return_order.setTextColor(Color.BLACK);
                break;
            case R.id.btn_return_order:
                btn_return_order.setBackgroundResource(R.drawable.blue_border_box);
                btn_return_order.setTextColor(Color.parseColor("#20c1ec"));
                btn_back_receive_success.setTextColor(Color.BLACK);
                btn_cancel_order.setTextColor(Color.BLACK);
                break;
            case R.id.btn_back_receive_success:
                btn_back_receive_success.setBackgroundResource(R.drawable.blue_border_box);
                btn_back_receive_success.setTextColor(Color.parseColor("#20c1ec"));
                btn_back_cancel_order.setTextColor(Color.BLACK);
                break;
        }
    }
    //endregion

    //region Button send API
    private void send(int id) {
        switch (id) {
            //region tab " nhận"
            //nhận thành công
            case R.id.btn_recive_success:
                new GetConfirmTakeOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 2,
                        edtReason.getText().toString());
                break;
            case R.id.btn_recive_faild:
                if (getArguments().getBoolean("mustCmt") == true) {
                    if (edtReason.getText().toString().equals("")) {
                        edtReason.setError(getString(R.string.err_reason));
                        edtReason.requestFocus();
                        return;
                    }
                }
                new GetConfirmTakeOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 3,
                        edtReason.getText().toString());
                break;
            case R.id.btn_cancel:
                if (getArguments().getBoolean("mustCmt") == true) {
                    if (edtReason.getText().toString().equals("")) {
                        edtReason.setError(getString(R.string.err_reason));
                        edtReason.requestFocus();
                        return;
                    }
                }
                new GetConfirmTakeOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1,
                        edtReason.getText().toString());
                break;
            //region Tab "Giao"
            //Giao thành công
            case R.id.btn_send_success:
                new getConfirmDeliveryOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 2,
                        edtReason.getText().toString());
                break;
            //Giao không thành công
            case R.id.btn_cancel_order:
                if (getArguments().getBoolean("mustCmt") == true) {
                    if (edtReason.getText().toString().equals("")) {
                        edtReason.setError(getString(R.string.err_reason));
                        edtReason.requestFocus();
                        return;
                    }
                }
                new getConfirmDeliveryOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 3,
                        edtReason.getText().toString());
                break;
            //Hủy giao hàng
            case R.id.btn_return_order:
                if (getArguments().getBoolean("mustCmt") == true) {
                    if (edtReason.getText().toString().equals("")) {
                        edtReason.setError(getString(R.string.err_reason));
                        edtReason.requestFocus();
                        return;
                    }
                }
                new getConfirmDeliveryOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1,
                        edtReason.getText().toString());
                break;

            //endregion

            //region tab "Trả hàng"
            //Trả thành công
            case R.id.btn_back_receive_success:
                new getConfirmComeBackOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 2,
                        edtReason.getText().toString());
                break;
            //Hủy trả hàng
            case R.id.btn_back_cancel_order:
                if (edtReason.getText().toString().equals("")) {
                    edtReason.setError(getString(R.string.err_reason));
                    edtReason.requestFocus();
                    return;
                }
                new getConfirmComeBackOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1,
                        edtReason.getText().toString());
                break;

            case 0:
                if (edtReason.getText().toString().trim().equals("")) {
                    edtReason.setError(getString(R.string.err_reason));
                    edtReason.requestFocus();
                    return;
                }
                switch (getArguments().getInt("type")) {
                    case 1:
                        new GetConfirmTakeOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0,
                                edtReason.getText().toString());
                        break;
                    case 2:
                        new getConfirmDeliveryOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0,
                                edtReason.getText().toString());
                        break;
                    case 3:
                        new getConfirmComeBackOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0,
                                edtReason.getText().toString());
                        break;
                }
                break;

            //endregion
        }
    }
    //endregion
    //endregion
}
