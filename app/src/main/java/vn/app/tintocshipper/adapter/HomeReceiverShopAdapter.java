package vn.app.tintocshipper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.fragment.HomeOrderListFragment;
import vn.app.tintocshipper.fragment.ShopOrderFragment;
import vn.app.tintocshipper.fragment.ShopOrderFragment2;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.Order;

import static vn.app.tintocshipper.config.GlobalClass.getActivity;

/**
 * Created by Admin on 9/11/2017.
 */

public class HomeReceiverShopAdapter extends RecyclerView.Adapter<HomeReceiverShopAdapter.MyViewHolder> implements View.OnClickListener {
    //region Var
    String userName = StorageHelper.get(StorageHelper.USERNAME);

    Context mContext;
    private List<Order> receiverShopList;
    RecyclerView recyclerView;
    HomeOrderListFragment homeOrderListFragment;
    int type;
    //endregion

    //region Constructor
    public HomeReceiverShopAdapter(List<Order> receiverShopList) {
        this.receiverShopList = receiverShopList;
    }

    public HomeReceiverShopAdapter(List<Order> receiverShopList, RecyclerView recyclerView, HomeOrderListFragment homeOrderListFragment, int type) {
        this.receiverShopList = receiverShopList;
        this.recyclerView = recyclerView;
        this.homeOrderListFragment = homeOrderListFragment;
        this.type = type;
    }
    //endregion

    //region OnCreateViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_receiver_list, parent, false);
        itemView.setOnClickListener(HomeReceiverShopAdapter.this);
        return new MyViewHolder(itemView);
    }
    //endregion

    //region OnBindViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            final Order receiverShop = receiverShopList.get(position);
            holder.txtNumOrderCode.setText(receiverShop.getShop_id());
            holder.txtShopName.setText(receiverShop.getShop_fullname()+" - "+receiverShop.getSender_fullname());
            holder.txtAddressPlase.setText(receiverShop.getAddress());
            holder.ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callSender = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", receiverShop.getPhone(), null));
                    GlobalClass.getActivity().startActivity(callSender);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region GetItemCount
    @Override
    public int getItemCount() {
        return receiverShopList.size();
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        Order item = receiverShopList.get(position);
        if (item != null) {
            String shop_id = item.getShop_id();
            String shopName = item.getShop_fullname();
            String shopAddress=item.getShop_address();
            String shopUserName=item.getShop_username();
            try {
                if (type == 1) {
                    ShopOrderFragment shopOrderFragment = ShopOrderFragment.newInstance(shop_id, shopName, type, shopAddress, shopUserName);
                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, shopOrderFragment);
                }
                else
                    if (type == 3){
                        ShopOrderFragment2 shopOrderFragment = ShopOrderFragment2.newInstance(shop_id, shopName, type, shopAddress, shopUserName);
                        CmmFunc.replaceFragment(getActivity(), R.id.main_container, shopOrderFragment);
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNumOrderCode,txtShopName, txtAddressPlase, txtOrderTime, txtStatus, txtStart;
        public LinearLayout llShopId;
        public ImageView ivCall;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtNumOrderCode = itemView.findViewById(R.id.txt_number_order_code);
            txtShopName=itemView.findViewById(R.id.txt_shop_name);
            txtAddressPlase = itemView.findViewById(R.id.txt_address_place);
            llShopId = itemView.findViewById(R.id.ll_shop_id);
            txtStatus = itemView.findViewById(R.id.status);
            ivCall=itemView.findViewById(R.id.iv_call);
        }
    }
    //endregion
}
