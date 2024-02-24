package vn.app.tintocshipper.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.List;

import vn.app.tintocshipper.CaptureActivityPortrait;
import vn.app.tintocshipper.R;
import vn.app.tintocshipper.adapter.DeliAdapter;
import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.DeliObj;
import vn.app.tintocshipper.utils.Utility;


public class DeliveryFragment extends BaseFragment implements View.OnClickListener {
    //region Var
    ImageView ivBack;
    RecyclerView recyclerDeliveryOrderList;
    LinearLayout ll_confirm;
    private List<vn.app.tintocshipper.model.DeliObj> DeliObj;
    EditText edt1;
    TextView txtv_data;
    String order_code = "";
    ImageView qrcode,imgSearch;
    SwipeRefreshLayout swipeRefreshLayout2;
    //endregion

    //region Constructor
    public DeliveryFragment() {
        // Required empty public constructor
    }
    //endregion

    //region NewInstance
    // TODO: Rename and change types and number of parameters
    public static DeliveryFragment newInstance() {
        DeliveryFragment fragment = new DeliveryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region OnCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_delivery, container, false);

        }
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad){
            ivBack = view.findViewById(R.id.iv_back);
            ll_confirm = view.findViewById(R.id.ll_confirm);
            swipeRefreshLayout2 = view.findViewById(R.id.swipeRefreshLayout2);
            edt1 = view.findViewById(R.id.edt1);
            qrcode = view.findViewById(R.id.qr_code);
            imgSearch = view.findViewById(R.id.img_search);
            txtv_data = view.findViewById(R.id.txtv_data);
            recyclerDeliveryOrderList = view.findViewById(R.id.recycler_deli_order_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerDeliveryOrderList.setLayoutManager(layoutManager);
            new GetNewOrderListAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            ivBack.setOnClickListener(this);
            qrcode.setOnClickListener(this);
            imgSearch.setOnClickListener(this);
            edt1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        order_code = edt1.getText().toString();
                        new GetNewOrderListAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    return false;
                }
            });
            swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    order_code = edt1.getText().toString();
                    new GetNewOrderListAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    // Refresh items
                }
            });
            edt1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = edt1.getText().toString();
                    if (text.equals("")) {
                        //font & size cua HINT
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edt1.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edt1.setTypeface(customFont);
                    }


                }
            });

            isLoad=true;
        }


    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                ivBack.setOnClickListener(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            ivBack.setOnClickListener(DeliveryFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.qr_code:
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(DeliveryFragment.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();
                break;
            case R.id.img_search:
                if (edt1.getText().toString().equals("")) {
                    edt1.setError("Vui lòng nhập mã đơn hàng!!!");
                    edt1.requestFocus();
                    return;
                }
                order_code = edt1.getText().toString();
                new GetNewOrderListAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;

        }
    }
    //endregion

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Bạn đã hủy scan", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_LONG).show();
                final String code = result.getContents();
                edt1.setText(code + "");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Bạn có muốn xác nhận đơn hàng "+ code );
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new bookDeliveryOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,code);
                        dialog.dismiss();
                        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(DeliveryFragment.this);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                        integrator.setPrompt("Scan");
                        integrator.setCameraId(0);
                        integrator.setBeepEnabled(false);
                        integrator.setBarcodeImageEnabled(false);
                        integrator.setCaptureActivity(CaptureActivityPortrait.class);
                        integrator.initiateScan();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(DeliveryFragment.this);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                        integrator.setPrompt("Scan");
                        integrator.setCameraId(0);
                        integrator.setBeepEnabled(false);
                        integrator.setBarcodeImageEnabled(false);
                        integrator.setCaptureActivity(CaptureActivityPortrait.class);
                        integrator.initiateScan();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else {
            //Quản lý các activity result khác (pickup ảnh, chụp ảnh ...)
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //region Request API

    //region Get new order list
    class GetNewOrderListAPI extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String userName = StorageHelper.get(StorageHelper.USERNAME);

                String value = APIHelper.getListImportOrder(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        userName, StorageHelper.get(StorageHelper.TOKEN),order_code);
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
                    DeliObj = (List<DeliObj>) CmmFunc.tryParseList(jsonObject.getString("data"), DeliObj.class);
                    DeliAdapter adapter = new DeliAdapter(DeliObj, recyclerDeliveryOrderList, DeliveryFragment.this);
                    recyclerDeliveryOrderList.setAdapter(adapter);
                }
                if (DeliObj.size() == 0){
                    recyclerDeliveryOrderList.setVisibility(View.GONE);
                    txtv_data.setVisibility(View.VISIBLE);
                }else
                if (DeliObj.size() > 0){
                    recyclerDeliveryOrderList.setVisibility(View.VISIBLE);
                    txtv_data.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } swipeRefreshLayout2.setRefreshing(false);
        }
    }
    //endregion

    //region BookDeliveryOrder
    class bookDeliveryOrder extends Async {
        DeliObj obj;
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String userName = StorageHelper.get(StorageHelper.USERNAME);

                String code = (String) objects[0];
                String value = APIHelper.bookDeliveryOrder(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        userName, StorageHelper.get(StorageHelper.TOKEN), code);
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
                Toast.makeText(getContext(),"Thành công!",Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion
}
