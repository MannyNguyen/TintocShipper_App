package vn.app.tintocshipper.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.adapter.NewOrderListAdapter;
import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.NewOrder;
import vn.app.tintocshipper.utils.Utility;


public class NewOrderFragment extends BaseFragment implements View.OnClickListener {
    //region Var
    ImageView ivBack;
    RecyclerView recyclerNewOrderList;
    LinearLayout ll_confirm, ll_receive_commodity;
    private List<NewOrder> newOrders;
    SwipeRefreshLayout swipeRefreshLayout2;
    TextView txtv_data;
    //endregion

    //region Constructor
    public NewOrderFragment() {
        // Required empty public constructor
    }
    //endregion

    //region NewInstance
    // TODO: Rename and change types and number of parameters
    public static NewOrderFragment newInstance() {
        NewOrderFragment fragment = new NewOrderFragment();
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
            view = inflater.inflate(R.layout.fragment_new_order, container, false);

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
            ll_confirm = view.findViewById(R.id.ll_confirm);
            ll_receive_commodity = view.findViewById(R.id.ll_receive_commodity);
            recyclerNewOrderList = view.findViewById(R.id.recycler_new_order_list);
            swipeRefreshLayout2 = view.findViewById(R.id.swipeRefreshLayout2);
            txtv_data = view.findViewById(R.id.txtv_data);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerNewOrderList.setLayoutManager(layoutManager);
            new GetNewOrderListAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            ivBack.setOnClickListener(this);
            swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new GetNewOrderListAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    // Refresh items
                }
            });
            isLoad = true;
        }
    }

    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
//                ivBack.setOnClickListener(null);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(2000);
//                            ivBack.setOnClickListener(NewOrderFragment.this);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }

    }
    //endregion

    //region Request API

    //region Get new order list
    class GetNewOrderListAPI extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String userName = StorageHelper.get(StorageHelper.USERNAME);


                String value = APIHelper.getListNewOrder(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        userName, StorageHelper.get(StorageHelper.TOKEN));
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
                    newOrders = (List<NewOrder>) CmmFunc.tryParseList(jsonObject.getString("data"), NewOrder.class);
                    NewOrderListAdapter adapter = new NewOrderListAdapter(newOrders, recyclerNewOrderList, NewOrderFragment.this);
                    recyclerNewOrderList.setAdapter(adapter);
                }
                if (newOrders.size() == 0) {
                    recyclerNewOrderList.setVisibility(View.GONE);
                    txtv_data.setVisibility(View.VISIBLE);
                } else if (newOrders.size() > 0) {
                    recyclerNewOrderList.setVisibility(View.VISIBLE);
                    txtv_data.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            swipeRefreshLayout2.setRefreshing(false);
        }
    }
    //endregion

    //endregion
}
