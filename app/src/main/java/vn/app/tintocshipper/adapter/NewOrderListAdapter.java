package vn.app.tintocshipper.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.fragment.HomeOrderListFragment;
import vn.app.tintocshipper.fragment.NewOrderFragment;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.model.NewOrder;

/**
 * Created by Admin on 9/6/2017.
 */

public class NewOrderListAdapter extends RecyclerView.Adapter<NewOrderListAdapter.MyViewHolder> implements View.OnClickListener {
    //region Var
    private List<NewOrder> newOrderList;
    RecyclerView recyclerView;
    NewOrderFragment newOrderFragment;
    //endregion

    //region Constructor
    public NewOrderListAdapter(List<NewOrder> newOrderList) {
        this.newOrderList = newOrderList;
    }
    //endregion

    //region Constructor
    public NewOrderListAdapter(List<NewOrder> newOrderList, RecyclerView recyclerView, NewOrderFragment newOrderFragment) {
        this.newOrderList = newOrderList;
        this.recyclerView = recyclerView;
        this.newOrderFragment = newOrderFragment;
    }
    //endregion

    //region OnCreateViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_order, parent, false);
        return new MyViewHolder(itemView);
    }
    //endregion

    //region OnBindViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            final NewOrder newOrder = newOrderList.get(position);
            holder.txt_shop_name.setText(newOrder.getShop_fullname());
            holder.txt_shop_address.setText(newOrder.getAddress());
            holder.txt_phone_num.setText(newOrder.getPhone());

            holder.iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callSender = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", newOrder.getPhone(), null));
                    GlobalClass.getActivity().startActivity(callSender);
                }
            });

            if (newOrder.isAccepted()) {
                holder.ll_confirm.setBackgroundResource(R.color.gray);
                holder.ll_confirm.setOnClickListener(null);
                holder.ll_contact.setVisibility(View.VISIBLE);

                holder.ll_receive_commodity.setBackgroundResource(R.color.bg2_color);
                holder.ll_receive_commodity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String value = APIHelper.bookNewOrderShop(2, newOrder.getShop_id());
                                    JSONObject jsonObject = new JSONObject(value);
                                    int code = jsonObject.getInt("code");
                                    if (code == 1) {
                                        //Toast.makeText(GlobalClass.getContext(), "Success", Toast.LENGTH_SHORT).show();
                                        //GlobalClass.getActivity().getSupportFragmentManager().popBackStackImmediate(HomeOrderListFragment.class.getName(),0);
                                        CmmFunc.replaceFragment(GlobalClass.getActivity(), R.id.main_container, HomeOrderListFragment.newInstance());

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                    }
                });
            } else {
                holder.ll_receive_commodity.setBackgroundResource(R.color.gray);
                holder.ll_receive_commodity.setOnClickListener(null);
                holder.ll_confirm.setVisibility(View.VISIBLE);
                holder.ll_confirm.setBackgroundResource(R.color.colorShopInfoOne);
                holder.ll_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String value = APIHelper.bookNewOrderShop(1, newOrder.getShop_id());
                                    JSONObject jsonObject = new JSONObject(value);
                                    int code = jsonObject.getInt("code");
                                    if (code == 1) {
                                        newOrderList.get(position).setAccepted(true);
                                        GlobalClass.getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                notifyDataSetChanged();
                                            }
                                        });
                                    } else {
                                        GlobalClass.getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(GlobalClass.getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region GetItemCount
    @Override
    public int getItemCount() {
        return newOrderList.size();
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {

    }
    //endregion

    //region MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_confirm, ll_receive_commodity, ll_contact;
        public TextView txt_shop_name, txt_shop_address, txt_phone_num;
        public ImageView iv_call;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_confirm = itemView.findViewById(R.id.ll_confirm);
            ll_receive_commodity = itemView.findViewById(R.id.ll_receive_commodity);
            txt_shop_name = itemView.findViewById(R.id.txt_shop_name);
            txt_shop_address = itemView.findViewById(R.id.txt_shop_address);
            ll_contact = itemView.findViewById(R.id.ll_contact);
            txt_phone_num = itemView.findViewById(R.id.txt_phone_num);
            iv_call = itemView.findViewById(R.id.iv_call);
        }
    }
    //endregion
}
