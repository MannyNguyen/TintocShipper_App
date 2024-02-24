package vn.app.tintocshipper.fragment;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import vn.app.tintocshipper.CaptureActivityPortrait;
import vn.app.tintocshipper.R;
import vn.app.tintocshipper.adapter.ShopOrderAdapter;
import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.ShopOrder;
import vn.app.tintocshipper.utils.Utility;

public class ShopOrderFragment2 extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    //region Var
    ImageView ivBack;
    TextView txt_bts_shop;
    RecyclerView recyclerView;
    private List<ShopOrder> shopOrders = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    public CheckBox ivCheck;
    public TextView txtNum2;
    public static TextView txtBack;
    ImageView qrcode, imgSearch;
    EditText edt1;
    String ordercode = "";
    //endregion

    //region Constructor
    public ShopOrderFragment2() {
        // Required empty public constructor
    }
    //endregion

    //region NewInstance
    // TODO: Rename and change types and number of parameters
    public static ShopOrderFragment2 newInstance(String shopId, String shopName, int type, String shop_address, String shop_username) {
        ShopOrderFragment2 fragment = new ShopOrderFragment2();
        Bundle args = new Bundle();
        args.putString("shopId", shopId);
        args.putString("shopName", shopName);
        args.putInt("type", type);
        args.putString("shop_address", shop_address);
        args.putString("shop_username", shop_username);
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
            view = inflater.inflate(R.layout.fragment_shop_order2, container, false);

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
            txtNum2 = view.findViewById(R.id.txt_num_2);
            edt1 = view.findViewById(R.id.edt1);
            qrcode = view.findViewById(R.id.qr_code);
            imgSearch = view.findViewById(R.id.img_search);
            txt_bts_shop = view.findViewById(R.id.txt_bts_shop);
            txtBack = view.findViewById(R.id.txt_back);
            recyclerView = view.findViewById(R.id.recycler_new_order_list);
            ivCheck = view.findViewById(R.id.iv_check);
//            recyclerView=view.findViewById(R.id.recycler_new_order_list);
            recyclerView = view.findViewById(R.id.recycler_new_order_list);
            swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            ivBack.setOnClickListener(this);
            getView().findViewById(R.id.txtv_confirm).setOnClickListener(this);
            qrcode.setOnClickListener(this);
            imgSearch.setOnClickListener(this);
            edt1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        ordercode = edt1.getText().toString();
//                    new GetListOrderByShop().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getArguments().getString("shop_username"), getArguments().getString("shop_address"));
                        List<ShopOrder> items = ShopOrder.search(ordercode, shopOrders);
                        ShopOrderAdapter adapter = new ShopOrderAdapter(items, recyclerView, ShopOrderFragment2.this, getArguments().getInt("type"));
                        recyclerView.setAdapter(adapter);
                    }
                    return false;
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
            txt_bts_shop.setText(getArguments().getString("shopName"));
            //new GetListOrderByShop().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    txtNum2.setVisibility(View.GONE);
                    new GetListOrderByShop().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getArguments().getString("shop_username"), getArguments().getString("shop_address"));
                }
            });

            isLoad=true;
        }

    }

    //endregion
    @Override
    public void onResume() {
        super.onResume();
        new GetListOrderByShop().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  getArguments().getString("shop_username"), getArguments().getString("shop_address"));
        ivCheck.setChecked(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //Check chọn toàn bộ đơn hàng, chú ý, chỉ lấy những đơn hàng Accepted = true.
        int valueAccept = 0;
        for (ShopOrder shopOrder : shopOrders) {
            if (shopOrder.isAccepted()) {
                shopOrder.setSelected(isChecked);
                valueAccept++;
            }
        }
        if (isChecked) {
            txtNum2.setVisibility(View.VISIBLE);
            txtNum2.setText("Bạn đã chọn " + valueAccept + " đơn hàng");
        } else {
            txtNum2.setVisibility(View.GONE);
        }

        //cập nhật lên giao diện
        recyclerView.getAdapter().notifyDataSetChanged();
    }

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
                            ivBack.setOnClickListener(ShopOrderFragment2.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.txtv_confirm:
                if (shopOrders == null || shopOrders.size() == 0) {
                    return;
                }
                String orderCodes = ShopOrder.getOrderCodes(shopOrders);

                if (orderCodes.equals("")) {
                    return;
                }
                CmmFunc.replaceFragment(getActivity(), R.id.main_container, UpdateStatusOrderFragment.newInstance(orderCodes, 3, "Trả hàng", true));
                break;
            case R.id.qr_code:
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(ShopOrderFragment2.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();
                break;
            case R.id.img_search:
                if (edt1.getText().toString().trim().equals("")) {
                    edt1.setError("Vui lòng nhập mã đơn hàng!!!");
                    edt1.requestFocus();
                    return;
                }
                String ordercode = edt1.getText().toString().trim();
                List<ShopOrder> items = ShopOrder.search(ordercode, shopOrders);
                ShopOrderAdapter adapter = new ShopOrderAdapter(items, recyclerView, ShopOrderFragment2.this, getArguments().getInt("type"));
                recyclerView.setAdapter(adapter);
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


                for (int i = 0; i < shopOrders.size(); i++) {
                    if (code.equals(shopOrders.get(i).getOrder_code())) {
                        if (shopOrders.get(i).isAccepted()) {
                            edt1.setText(code + "");
                            CmmFunc.replaceFragment(getActivity(), R.id.main_container, UpdateStatusOrderFragment.newInstance(code, 3, "Trả Hàng", true));
                        }
                    } else
                        Toast.makeText(getActivity(), "Không tìm thấy đơn hàng khả dụng", Toast.LENGTH_SHORT).show();
                }


//                        CmmFunc.replaceFragment(getActivity(), R.id.main_container, UpdateStatusOrderFragment.newInstance(getArguments().getString("orderCode"), 1, "Nhận Hàng", true));

            }

        }
    }

    private static List<ShopOrder> getListChecked(List<ShopOrder> checkedList, List<ShopOrder> newList) {
        if (newList == null) {
            return null;
        }
        Map map = new LinkedHashMap();
        for (ShopOrder shopOrder : newList) {
            map.put(shopOrder.getOrder_code(), shopOrder);
        }
        List<ShopOrder> retValue = new ArrayList<>();
        for (ShopOrder i : checkedList) {
            if (map.containsKey(i)) {
                retValue.add((ShopOrder) map.get(i));
                map.remove(i);
            }
        }
        retValue.addAll(map.values());
        return retValue;
    }


    //region request API
    //region Get list order by shop
    class GetListOrderByShop extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String shopUserName = (String) objects[0];
                String shopAddress = (String) objects[1];
                String value = APIHelper.getListBackOrder(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN),
                        URLEncoder.encode(shopUserName, "UTF-8"), URLEncoder.encode(shopAddress, "UTF-8"));
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
                    shopOrders.clear();
                    shopOrders = (List<ShopOrder>) CmmFunc.tryParseList(jsonObject.getString("data"), ShopOrder.class);

                    String listShopOrder = StorageHelper.get(StorageHelper.LIST_SHOP_ORDER);
                    if (!listShopOrder.equals("")){
                        List<ShopOrder> listShopOrders = (List<ShopOrder>) CmmFunc.tryParseList(listShopOrder, ShopOrder.class);
                        shopOrders = getListChecked(listShopOrders, shopOrders);
                    }

                    ShopOrderAdapter adapter = new ShopOrderAdapter(shopOrders, recyclerView, ShopOrderFragment2.this, getArguments().getInt("type"));

                    recyclerView.setAdapter(adapter);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    ivCheck.setOnCheckedChangeListener(ShopOrderFragment2.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    //endregion

    //endregion

}
