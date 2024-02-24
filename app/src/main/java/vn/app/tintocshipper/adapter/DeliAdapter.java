package vn.app.tintocshipper.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.fragment.DeliveryFragment;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.DeliObj;
import vn.app.tintocshipper.utils.Utility;
import vn.app.tintocshipper.R;

/**
 * Created by Admin on 9/6/2017.
 */

public class DeliAdapter extends RecyclerView.Adapter<DeliAdapter.MyViewHolder> implements View.OnClickListener {
//    private final List<DeliObj> deliObjList;
    //region Var
     List<vn.app.tintocshipper.model.DeliObj> deliObjs;
    RecyclerView recyclerView;
    DeliveryFragment deliveryFragment;

    private List<DeliObj> DeliObj;
    //endregion

    //region Constructor
//    public DeliAdapter(List<DeliObj> deliObjList, List<NewOrder> newOrderList) {
//        this.deliObjList = deliObjList;
//        this.newOrderList = newOrderList;
//    }
    //endregion

    //region Constructor
    public DeliAdapter(List<DeliObj> deliObjList, RecyclerView recyclerView, DeliveryFragment deliveryFragment) {
        this.deliObjs = deliObjList;
        this.recyclerView = recyclerView;
        this.deliveryFragment = deliveryFragment;
    }
    //endregion

    //region OnCreateViewHolder
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
//        return new MyViewHolder(itemView);
//    }



    @Override
    public DeliAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

    }

    //endregion


    @Override
    public void onBindViewHolder(DeliAdapter.MyViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        try {
            final DeliObj deliObj = deliObjs.get(position);
            holder.txt_shop_address.setText(deliObj.getAddress());
            holder.order_code.setText(deliObj.getOrder_code());
            holder.txt_date.setText(deliObj.getDate());
            holder.txt_time.setText(deliObj.getTime());
            holder.confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GlobalClass.getActivity());
                    builder.setTitle("Bạn có muốn xác nhận!");
                    builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new bookDeliveryOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,deliObj);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });


            holder.iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callSender = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", deliObj.getPhone(), null));
                    GlobalClass.getActivity().startActivity(callSender);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region OnBindViewHolder


    //endregion

    //region GetItemCount
    @Override
    public int getItemCount() {
        return deliObjs.size();
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {

    }
    //endregion

    //region MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout confirm, ll_receive_commodity, ll_contact;
        public TextView txt_shop_name, txt_shop_address, txt_phone_num,order_code,txt_status,txt_time,txt_date;
        public ImageView iv_call;

        public MyViewHolder(View itemView) {
            super(itemView);
            confirm = itemView.findViewById(R.id.ll_confirm3);
//            ll_receive_commodity = itemView.findViewById(R.id.ll_receive_commodity);
            txt_shop_address = itemView.findViewById(R.id.txt_shop_address);
            ll_contact = itemView.findViewById(R.id.ll_contact);
            txt_phone_num = itemView.findViewById(R.id.txt_phone_num);
            iv_call = itemView.findViewById(R.id.iv_call);
            order_code = itemView.findViewById(R.id.order_code);
            txt_date = itemView.findViewById(R.id.txt_time_delivery_to);
            txt_time = itemView.findViewById(R.id.txt_time);



        }
    }
    //endregion

    //region Request API
    class bookDeliveryOrder extends Async {
        DeliObj obj;
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String userName = StorageHelper.get(StorageHelper.USERNAME);

                obj = (DeliObj) objects[0];
                String value = APIHelper.bookDeliveryOrder(Config.COUNTRY_CODE, Utility.getDeviceId(GlobalClass.getContext()),
                        userName, StorageHelper.get(StorageHelper.TOKEN), obj.getOrder_code());
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
                    int position = deliObjs.indexOf(obj);
                    deliObjs.remove(position);
                    //notifyItemRemoved(position);
                    notifyDataSetChanged();
                    //CmmFunc.replaceFragment(GlobalClass.getActivity(), R.id.main_container, HomeOrderListFragment.newInstance());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
