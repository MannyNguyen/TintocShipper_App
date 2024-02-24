package vn.app.tintocshipper.adapter;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.fragment.OrderDetailFragment;
import vn.app.tintocshipper.fragment.RunningOrderFragment;
import vn.app.tintocshipper.fragment.ShopOrderFragment;
import vn.app.tintocshipper.fragment.ShopOrderFragment2;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.ShopOrder;

import static vn.app.tintocshipper.config.GlobalClass.getActivity;

/**
 * Created by Admin on 9/7/2017.
 */

public class ShopOrderAdapter extends RecyclerView.Adapter<ShopOrderAdapter.MyViewHolder> implements View.OnClickListener {
    //region Var
    private List<ShopOrder> shopOrderList;
    RecyclerView recyclerView;
    Fragment fragment;
    int type;
    //endregion

    //region Constructor

    public ShopOrderAdapter(List<ShopOrder> shopOrderList, RecyclerView recyclerView, Fragment fragment, int type) {
        this.shopOrderList = shopOrderList;
        this.recyclerView = recyclerView;
        this.fragment = fragment;
        this.type = type;
    }
    //endregion

    //region OnCreateViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_order, parent, false);
        itemView.setOnClickListener(ShopOrderAdapter.this);
        return new MyViewHolder(itemView);
    }
    //endregion

    //region OnBindViewHolder
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final ShopOrder shopOrder = shopOrderList.get(position);
            holder.txt_shop_order_code.setText(shopOrder.getOrder_code());
            holder.txt_receive_place.setText(shopOrder.getAddress());
            holder.txt_recive_time.setText(shopOrder.getTime());
            holder.txt_status.setText(shopOrder.getStatus());

            holder.ll_linearlayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<ShopOrder> orders = new ArrayList<>();
                    orders.add(shopOrder);
                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, RunningOrderFragment.newInstance(CmmFunc.tryParseObject(orders)));

                }
            });

            if (!shopOrder.isSelected()) {
                holder.iv_check.setImageDrawable(fragment.getResources().getDrawable(R.drawable.check_box));
                //add list đã check vào shared preference
                StorageHelper.set(StorageHelper.LIST_SHOP_ORDER, CmmFunc.tryParseObject(shopOrderList));
            } else {
                holder.iv_check.setImageDrawable(fragment.getResources().getDrawable(R.drawable.ic_tick));
                //add list đã check vào shared preference
                StorageHelper.set(StorageHelper.LIST_SHOP_ORDER, CmmFunc.tryParseObject(shopOrderList));
            }

            if (!shopOrder.isAccepted()) {
                holder.ll_linearlayout1.setBackgroundResource(R.color.disable_color);
                holder.txt_status.setTextColor(Color.parseColor("#889db2"));
                holder.txt_recive_time.setTextColor(Color.parseColor("#889db2"));
                holder.iv_check.setImageDrawable(fragment.getResources().getDrawable(R.drawable.ic_disable));
            } else {
                holder.ll_linearlayout1.setBackgroundResource(R.drawable.blue_box);
            }
            holder.iv_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!shopOrder.isAccepted()) {
                        return;
                    }
                    shopOrder.setSelected(!shopOrder.isSelected());

                    //instance vào fragment tương ứng, tab "Nhận"
                    if (fragment instanceof ShopOrderFragment) {
                        ShopOrderFragment shopOrderFragment = (ShopOrderFragment) fragment;
                        int value = numOrder();
                        if (value == 0) {
                            shopOrderFragment.txtNum.setVisibility(View.GONE);
                        } else {
                            shopOrderFragment.txtNum.setVisibility(View.VISIBLE);
                            shopOrderFragment.txtNum.setText("Bạn đã chọn " + value + " đơn hàng");
                        }

                        if (!shopOrder.isSelected()) {
                            shopOrderFragment.ivCheck.setOnCheckedChangeListener(null);
                            shopOrderFragment.ivCheck.setChecked(false);
                            shopOrderFragment.ivCheck.setOnCheckedChangeListener(shopOrderFragment);
                        }
                    }

                    //instance vào fragment tương ứng, tab "Trả hàng"
                    if (fragment instanceof ShopOrderFragment2) {
                        ShopOrderFragment2 shopOrderFragment = (ShopOrderFragment2) fragment;
                        int value = numOrder();
                        if (value == 0) {
                            shopOrderFragment.txtNum2.setVisibility(View.GONE);
                        } else {
                            shopOrderFragment.txtNum2.setVisibility(View.VISIBLE);
                            shopOrderFragment.txtNum2.setText("Bạn đã chọn " + value + " đơn hàng");
                        }

                        if (!shopOrder.isSelected()) {
                            shopOrderFragment.ivCheck.setOnCheckedChangeListener(null);
                            shopOrderFragment.ivCheck.setChecked(false);
                            shopOrderFragment.ivCheck.setOnCheckedChangeListener(shopOrderFragment);
                        }

                    }

                    notifyDataSetChanged();
                }


            });
            int value = numOrderReceive();
            ShopOrderFragment.txtReceive.setText(value + "/" + shopOrderList.size());

            int value1 = numOrderBack();
            ShopOrderFragment2.txtBack.setText(value1 + "/" + shopOrderList.size());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion
    private int numOrderReceive() {
        int value = 0;
        for (ShopOrder order : shopOrderList) {
            if (order.isAccepted()) {
                value++;
            }
        }
        return value;
    }

    private int numOrderBack() {
        int value1 = 0;
        for (ShopOrder order : shopOrderList) {
            if (order.isAccepted()) {
                value1++;
            }
        }
        return value1;
    }

    private int numOrder() {
        //đếm các đơn hàng được chọn
        int value = 0;
//        for (int i = 0; i < shopOrderList.size(); i++) {
//            if (shopOrderList.get(i).isSelected()) {
//                value++;
//            }
//        }
        for (ShopOrder shopOrder : shopOrderList) {
            if (shopOrder.isSelected()) {
                value++;
            }
        }
        return value;
    }

    //region GetItemCount
    @Override
    public int getItemCount() {
        return shopOrderList.size();
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        ShopOrder item = shopOrderList.get(position);
        if (item != null) {
            String orderCode = item.getOrder_code();
            CmmFunc.addFragment(getActivity(), R.id.main_container,
                    OrderDetailFragment.newInstance(orderCode, type, item.isAccepted()));
        }
    }
    //endregion

    //region MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_check;
        TextView txt_shop_order_code, txt_receive_place, txt_recive_time, txt_status;
        LinearLayout ll_linearlayout1;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_check = itemView.findViewById(R.id.iv_check);
            txt_shop_order_code = itemView.findViewById(R.id.txt_shop_order_code);
            txt_receive_place = itemView.findViewById(R.id.txt_receive_place);
            ll_linearlayout1 = itemView.findViewById(R.id.ll_id_shop_order);
            txt_recive_time = itemView.findViewById(R.id.txt_recive_time);
            txt_status = itemView.findViewById(R.id.status);
        }
    }
    //endregion

}
