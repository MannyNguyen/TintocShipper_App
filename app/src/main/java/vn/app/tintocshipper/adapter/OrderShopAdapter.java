package vn.app.tintocshipper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.fragment.HomeOrderListFragment;
import vn.app.tintocshipper.fragment.OrderDetailFragment;
import vn.app.tintocshipper.fragment.RunningOrderFragment;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.Order;

import static vn.app.tintocshipper.config.GlobalClass.getActivity;

/**
 * Created by shane2202 on 10/4/17.
 */

public class OrderShopAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> implements View.OnClickListener {
    String userName = StorageHelper.get(StorageHelper.USERNAME);

    Context mContext;
    private List<Order> orderShop;
    RecyclerView recyclerView;
    HomeOrderListFragment homeOrderListFragment;
    int type;
    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.MyViewHolder holder, int position) {
        try {
            final Order order = orderShop.get(position);
            holder.txtNumOrderCode.setText(order.getOrder_code());
            holder.txtAddressPlase.setText(order.getAddress());
            holder.llOrderCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Order>orders = new ArrayList<>();
                    orders.add(order);
                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, RunningOrderFragment.newInstance(CmmFunc.tryParseObject(orders)));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderShop.size();
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        Order item = orderShop.get(position);
        if (item != null) {
            String orderCode = item.getOrder_code();
            String shopAddress = item.getAddress();
            try {
//                ShopOrderFragment fragment = ShopOrderFragment.newInstance(shop_id, shopName);
//                CmmFunc.replaceFragment(getActivity(), R.id.main_container, fragment);
                CmmFunc.replaceFragment(getActivity(), R.id.main_container,
                        OrderDetailFragment.newInstance(orderCode, type, true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNumOrderCode, txtAddressPlase, txtOrderTime, txtStatus, txtStart,txtFullName;
        public LinearLayout llOrderCode;

        public MyViewHolder(View itemView) {
            super(itemView);
            llOrderCode = itemView.findViewById(R.id.ll_order_code);
            txtFullName = itemView.findViewById(R.id.txt_full_name);
            txtNumOrderCode = itemView.findViewById(R.id.txt_number_order_code);
            txtAddressPlase = itemView.findViewById(R.id.txt_address_place);
        }
    }
    //region Var  {
}
