package vn.app.tintocshipper.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import vn.app.tintocshipper.LoginActivity;
import vn.app.tintocshipper.R;
import vn.app.tintocshipper.adapter.HomeReceiverShopAdapter;
import vn.app.tintocshipper.adapter.LeftMenuAdapter;
import vn.app.tintocshipper.adapter.OrderListAdapter;
import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.EditItemTouchHelperCallback;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.LeftMenu;
import vn.app.tintocshipper.model.Order;
import vn.app.tintocshipper.utils.Utility;

public class HomeOrderListFragment extends BaseFragment implements View.OnClickListener {
    //region Var
    ImageView ivMenu, imgAlert1, imgAlert2, imgAlert3;
    View llDelivery, llReceiver, llReturn, llAllMap;
    public TextView txtDelivered;
    TextView txt_receiver, txt_delivery, txt_return, listShop, listOrder, txtv_data;
    public RecyclerView recyclerOrderList;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout ll_linear_sort, ll_delivery;
    Spinner spinner_sort;
    int type = 1;
    public List<Order> orders;
    ItemTouchHelper mItemTouchHelper;
    OrderListAdapter orderListAdapter;

    ArrayAdapter<CharSequence> adapter;

    GetListOrderAPI getListOrderAPI;
    //endregion

    //region Instance
    public static HomeOrderListFragment newInstance() {
        Bundle args = new Bundle();
        HomeOrderListFragment fragment = new HomeOrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeOrderListFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        HomeOrderListFragment fragment = new HomeOrderListFragment();
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
            view = inflater.inflate(R.layout.fragment_home_order_list, container, false);
        }
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivMenu = view.findViewById(R.id.iv_menu);
            txtDelivered = view.findViewById(R.id.txt_delivered);
            llReceiver = view.findViewById(R.id.ll_receiver);
            llDelivery = view.findViewById(R.id.ll_delivery);
            llReturn = view.findViewById(R.id.ll_return);
            txt_receiver = view.findViewById(R.id.txt_receiver);
            txt_delivery = view.findViewById(R.id.txt_delivery);
            txt_return = view.findViewById(R.id.txt_return);
            listShop = view.findViewById(R.id.txt_listShop);
            listOrder = view.findViewById(R.id.txt_listOrder);
            txtv_data = view.findViewById(R.id.txtv_data);
            imgAlert1 = view.findViewById(R.id.imgAlert1);
            imgAlert2 = view.findViewById(R.id.imgAlert2);
            imgAlert3 = view.findViewById(R.id.imgAlert3);
            swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            recyclerOrderList = view.findViewById(R.id.recycler_order_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerOrderList.setLayoutManager(layoutManager);
            llAllMap = view.findViewById(R.id.ll_all_map);
            ll_delivery = view.findViewById(R.id.ll_delivery1);
            spinner_sort = view.findViewById(R.id.spinner_sort);
            ll_linear_sort = view.findViewById(R.id.ll_linear_sort);
            adapter = ArrayAdapter.createFromResource(getContext(), R.array.sort, R.layout.row_spinner);
            adapter.setDropDownViewResource(R.layout.row_spinner);
            spinner_sort.setAdapter(adapter);

            switch (getArguments().getInt("type")) {
                case 2:
                    llDelivery.setBackgroundResource(R.drawable.blue_square_box);
                    txt_delivery.setTextColor(Color.WHITE);
                    llReceiver.setBackgroundResource(R.drawable.gray_left_box);
                    txt_receiver.setTextColor(Color.GRAY);
                    llReturn.setBackgroundResource(R.drawable.gray_right_box);
                    txt_return.setTextColor(Color.GRAY);

                    break;
                case 3:
                    llReturn.setBackgroundResource(R.drawable.blue_right_box);
                    txt_return.setTextColor(Color.WHITE);
                    llReceiver.setBackgroundResource(R.drawable.gray_left_box);
                    txt_receiver.setTextColor(Color.GRAY);
                    llDelivery.setBackgroundResource(R.drawable.gray_square_box);
                    txt_delivery.setTextColor(Color.GRAY);
                    break;
                case 1:
                    llReceiver.setBackgroundResource(R.drawable.blue_left_box);
                    txt_receiver.setTextColor(Color.WHITE);
                    llDelivery.setBackgroundResource(R.drawable.gray_square_box);
                    txt_delivery.setTextColor(Color.GRAY);
                    llReturn.setBackgroundResource(R.drawable.gray_right_box);
                    txt_return.setTextColor(Color.GRAY);
                    break;
            }

            getListOrderAPI = new GetListOrderAPI();
            getListOrderAPI.execute(getArguments().getInt("type"));

            ivMenu.setOnClickListener(this);
            llReceiver.setOnClickListener(this);
            llDelivery.setOnClickListener(this);
            llReturn.setOnClickListener(this);
            llAllMap.setOnClickListener(this);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getListOrderAPI = new GetListOrderAPI();
                    getListOrderAPI.execute(type);
                    // Refresh items
                }
            });
            new GetNotify().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            getListOrderAPI = new GetListOrderAPI();
            getListOrderAPI.execute(1);

            isLoad = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getListOrderAPI != null) {
//            orders.clear();
            getListOrderAPI.cancel(true);
        }
    }

    //endregion
    public void updateUI(int id) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(llReceiver.getWidth(), CmmFunc.dpToPixel(getActivity(), 40));
        layoutParams.gravity = Gravity.BOTTOM;
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(llReceiver.getWidth(), CmmFunc.dpToPixel(getActivity(), 52));
        llReceiver.setLayoutParams(layoutParams);
        llDelivery.setLayoutParams(layoutParams);
        llReturn.setLayoutParams(layoutParams);
        switch (id) {
            case R.id.ll_receiver:
                llReceiver.setLayoutParams(layoutParams1);
                Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "segoeuib.ttf");
                txt_receiver.setTypeface(customFont);
                Typeface customFont2 = Typeface.createFromAsset(getActivity().getAssets(), "segoeui.ttf");
                txt_delivery.setTypeface(customFont2);
                Typeface customFont3 = Typeface.createFromAsset(getActivity().getAssets(), "segoeui.ttf");
                txt_return.setTypeface(customFont3);
                break;
            case R.id.ll_delivery:
                llDelivery.setLayoutParams(layoutParams1);
                Typeface customFont4 = Typeface.createFromAsset(getActivity().getAssets(), "segoeui.ttf");
                txt_receiver.setTypeface(customFont4);
                Typeface customFont5 = Typeface.createFromAsset(getActivity().getAssets(), "segoeuib.ttf");
                txt_delivery.setTypeface(customFont5);
                Typeface customFont6 = Typeface.createFromAsset(getActivity().getAssets(), "segoeui.ttf");
                txt_return.setTypeface(customFont6);
                break;
            case R.id.ll_return:
                llReturn.setLayoutParams(layoutParams1);
                Typeface customFont7 = Typeface.createFromAsset(getActivity().getAssets(), "segoeui.ttf");
                txt_receiver.setTypeface(customFont7);
                Typeface customFont8 = Typeface.createFromAsset(getActivity().getAssets(), "segoeui.ttf");
                txt_delivery.setTypeface(customFont8);
                Typeface customFont9 = Typeface.createFromAsset(getActivity().getAssets(), "segoeuib.ttf");
                txt_return.setTypeface(customFont9);
        }
    }

    //region OnClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
                //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ll_receiver:
                if (getListOrderAPI != null) {
                    orders.clear();
                    recyclerOrderList.getAdapter().notifyDataSetChanged();
                    getListOrderAPI.cancel(true);
                }
                type = 1;
                updateUI(view.getId());
                ll_delivery.setVisibility(View.GONE);
                ll_linear_sort.setVisibility(View.GONE);
                llReceiver.setBackgroundResource(R.drawable.tab_checked);
                txt_receiver.setTextColor(Color.parseColor("#20c1ec"));
                llDelivery.setBackgroundResource(R.drawable.uncheck_left);
                txt_delivery.setTextColor(Color.BLACK);
                llReturn.setBackgroundResource(R.drawable.uncheck_left);
                txt_return.setTextColor(Color.BLACK);
                orders.clear();
                imgAlert1.setVisibility(View.GONE);
                listShop.setVisibility(View.VISIBLE);
                listOrder.setVisibility(View.GONE);
                getListOrderAPI = new GetListOrderAPI();
                getListOrderAPI.execute(1);
                break;
            case R.id.ll_delivery:
                if (getListOrderAPI != null) {
                    orders.clear();
                    recyclerOrderList.getAdapter().notifyDataSetChanged();
                    getListOrderAPI.cancel(true);
                }
                ll_linear_sort.setVisibility(View.GONE);
                type = 2;
                updateUI(view.getId());
                ll_delivery.setVisibility(View.VISIBLE);
                llDelivery.setBackgroundResource(R.drawable.tab_checked);
                llReceiver.setBackgroundResource(R.drawable.tab_checked);
                txt_delivery.setTextColor(Color.parseColor("#20c1ec"));
                llReceiver.setBackgroundResource(R.drawable.uncheck_left);
                txt_receiver.setTextColor(Color.BLACK);
                llReturn.setBackgroundResource(R.drawable.uncheck_left);
                txt_return.setTextColor(Color.BLACK);
                orders.clear();
                imgAlert2.setVisibility(View.GONE);
                listOrder.setVisibility(View.VISIBLE);
                listShop.setVisibility(View.GONE);
                getListOrderAPI = new GetListOrderAPI();
                getListOrderAPI.execute(2);
                break;
            case R.id.ll_return:
                if (getListOrderAPI != null) {
                    orders.clear();
                    recyclerOrderList.getAdapter().notifyDataSetChanged();
                    orders.clear();
                    getListOrderAPI.cancel(true);
                }
                ll_linear_sort.setVisibility(View.GONE);
                updateUI(view.getId());
                type = 3;
                ll_delivery.setVisibility(View.GONE);
                llReturn.setBackgroundResource(R.drawable.tab_checked);
                llReceiver.setBackgroundResource(R.drawable.tab_checked);
                txt_return.setTextColor(Color.parseColor("#20c1ec"));
                llReceiver.setBackgroundResource(R.drawable.uncheck_left);
                txt_receiver.setTextColor(Color.BLACK);
                llDelivery.setBackgroundResource(R.drawable.uncheck_left);
                txt_delivery.setTextColor(Color.BLACK);
                orders.clear();
                listShop.setVisibility(View.VISIBLE);
                imgAlert3.setVisibility(View.GONE);
                listOrder.setVisibility(View.GONE);
                getListOrderAPI = new GetListOrderAPI();
                getListOrderAPI.execute(3);
                break;
            case R.id.ll_all_map:
                String data = CmmFunc.tryParseObject(orders);
                CmmFunc.replaceMainFragment(GlobalClass.getActivity(), R.id.main_container, AllMapFragment.newInstance(data));
                break;
        }

    }
    //endregion

    //region OnResume
    @Override
    public void onResume() {
        super.onResume();
        //Lock left menu after load
        DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        new GetNotify().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //Load data when open this screen
    }
    //endregion

    //region OnPause
    @Override
    public void onPause() {
        super.onPause();
        DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    //endregion

    //Apply new list to sorted list
    public static List<Order> getListSort(List<String> sortedList, List<Order> newList) {
        if (newList == null) {
            return null;
        }
        Map map = new LinkedHashMap();
        for (Order order : newList) {
            map.put(order.getOrder_code(), order);
        }
        List<Order> retValue = new ArrayList<>();
        for (String i : sortedList) {
            if (map.containsKey(i)) {
                retValue.add((Order) map.get(i));
                map.remove(i);
            }
        }
        retValue.addAll(map.values());
        return retValue;
    }

    //region Request API

    //region Get list order
    class GetListOrderAPI extends Async {
        int type;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Trước khi request API, không bấm được vào Bản đồ toàn bộ
            llAllMap.setOnClickListener(null);
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                type = (int) objects[0];
                String value = APIHelper.getOrderList(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN), type);
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
                    //Lấy list order từ data
                    orders = (List<Order>) CmmFunc.tryParseList(jsonObject.getString("data"), Order.class);
                    switch (type) {
                        case 1:
                            //Không kéo thả ở tab này
                            if (mItemTouchHelper != null) {
                                mItemTouchHelper.attachToRecyclerView(null);
                            }
                            HomeReceiverShopAdapter adapter = new HomeReceiverShopAdapter(orders, recyclerOrderList, HomeOrderListFragment.this, type);
                            recyclerOrderList.setAdapter(adapter);
                            break;
                        case 2:
                            String strOrderCode = StorageHelper.get(StorageHelper.ORDER_CODE);
                            if (!strOrderCode.equals("")) {
                                List<String> orderIDList = (List<String>) CmmFunc.tryParseList(strOrderCode, String.class);
                                orders = getListSort(orderIDList, orders);
                            }
                            //Chỉ khởi tạo lại ItemTouchHelper khi = null
                            if (mItemTouchHelper == null) {
//                                List<Order> saveList = getListSort(storageSortedList, orders);
                                orderListAdapter = new OrderListAdapter(HomeOrderListFragment.this, recyclerOrderList, orders, type, mItemTouchHelper);
                                ItemTouchHelper.Callback callback = new EditItemTouchHelperCallback(orderListAdapter);
                                mItemTouchHelper = new ItemTouchHelper(callback);
                            } else {
                                orderListAdapter.orderList = orders;
                            }
                            mItemTouchHelper.attachToRecyclerView(recyclerOrderList);
                            recyclerOrderList.setAdapter(orderListAdapter);
                            break;
                        case 3:
                            //Không kéo thả ở tab này
                            if (mItemTouchHelper != null) {
                                mItemTouchHelper.attachToRecyclerView(null);
                            }
                            HomeReceiverShopAdapter adapter2 = new HomeReceiverShopAdapter(orders, recyclerOrderList, HomeOrderListFragment.this, type);
                            recyclerOrderList.setAdapter(adapter2);
                            break;
                    }
                    //Nếu list đơn hàng trống thì không bấm được vào bản đồ All map.
                    if (orders.size() < 1) {
                        llAllMap.setOnClickListener(null);
                    } else {
                        llAllMap.setOnClickListener(HomeOrderListFragment.this);
                    }
                    // list đơn hàng trống thì hiện textview không có dữ liệu
                    if (orders.size() == 0) {
                        recyclerOrderList.setVisibility(View.GONE);
                        txtv_data.setVisibility(View.VISIBLE);
                    } else if (orders.size() > 0) {
                        recyclerOrderList.setVisibility(View.VISIBLE);
                        txtv_data.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    //endregion

    //region Get notify
    class GetNotify extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getNotify(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN));
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
                    int new_order = data.getInt("notify_new_order");
                    int notify_assign_take = data.getInt("notify_assign_take");
                    int notify_assign_delivery = data.getInt("notify_assign_delivery");
                    int notify_assign_back = data.getInt("notify_assign_back");
                    int notify_not_payment = data.getInt("notify_not_payment");
                    int notify_was_payment = data.getInt("notify_was_payment");

                    RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_left_menu);
                    LeftMenuAdapter adapter = (LeftMenuAdapter) recyclerView.getAdapter();
                    LeftMenu newOrder = LeftMenu.getByType(LeftMenu.NEW_ORDER, adapter.leftMenuList);
                    LeftMenu listOrder = LeftMenu.getByType(LeftMenu.ORDER, adapter.leftMenuList);
                    LeftMenu debtManager = LeftMenu.getByType(LeftMenu.DEBT_MANAGER, adapter.leftMenuList);
                    if (notify_assign_back == 1 || notify_assign_delivery == 1 || notify_assign_take == 1) {
                        listOrder.setShowNotify(true);
                    } else {
                        listOrder.setShowNotify(false);
                    }

                    if (new_order == 1) {
                        newOrder.setShowNotify(true);
                    } else {
                        newOrder.setShowNotify(false);
                    }

                    if (notify_not_payment == 1 || notify_was_payment == 1) {
                        debtManager.setShowNotify(true);
                    } else {
                        debtManager.setShowNotify(false);
                    }

                    adapter.notifyDataSetChanged();

                    if (notify_assign_take == 1) {
                        imgAlert1.setVisibility(View.VISIBLE);
                    }
                    if (notify_assign_delivery == 1) {
                        imgAlert2.setVisibility(View.VISIBLE);
                    }
                    if (notify_assign_back == 1) {
                        imgAlert3.setVisibility(View.VISIBLE);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    //endregion

    //endregion
}

