package vn.app.tintocshipper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.fragment.DebtManageFragment;
import vn.app.tintocshipper.model.DebtObj;
import vn.app.tintocshipper.R;

;

/**
 * Created by Admin on 9/28/2017.
 */

public class DebtManagerAdapter extends RecyclerView.Adapter<DebtManagerAdapter.MyViewHolder> implements View.OnClickListener {
    //region Var
    List<DebtObj> debtObjList;
    DebtManageFragment debtManageFragment;
    RecyclerView recyclerView;
    int type;
    //endregion

    //region Constructor
    public DebtManagerAdapter(DebtManageFragment debtManageFragment, RecyclerView recyclerView, List<DebtObj> debtObjList) {
        this.debtManageFragment = debtManageFragment;
        this.recyclerView = recyclerView;
        this.debtObjList = debtObjList;
    }
    //endregion

    //region OnCreateViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_debt_screen, parent, false);
        itemView.setOnClickListener(DebtManagerAdapter.this);
        return new MyViewHolder(itemView);
    }
    //endregion

    //region OnBindViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            DebtObj debtObj = debtObjList.get(position);
//            holder.txtStt.setText((position+1)+"");
            holder.txtOrderCode.setText(debtObj.getOrder_code() + "-" + debtObj.getFullname());
            holder.txtAddress.setText("Địa chỉ: " + debtObj.getAddress());
//            holder.txtfullname.setText(debtObj.getFullname());
            holder.txtMoneyAdvance.setText(CmmFunc.formatMoney(Math.round(Double.parseDouble(debtObj.getMoney_cash_advance() + ""))) + " VND");
            holder.txtTotalIncome.setText(CmmFunc.formatMoney(Math.round(Double.parseDouble(debtObj.getTotal_cod() + ""))) + " VND");

            int value = numOrder();
            if (value > 0) {
                debtManageFragment.llNumDebtOrder.setVisibility(View.VISIBLE);
                debtManageFragment.txtNumberOrder.setText(value + " đơn");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region GetItemCount
    @Override
    public int getItemCount() {
        return debtObjList.size();
    }
    //endregion

    //region count of order
    private int numOrder() {
        int value = 0;
        for (DebtObj debtObj : debtObjList) {
            value++;
        }
        return value;
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {

    }
    //endregion

    //region MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtStt, txtOrderCode, txtAddress, txtfullname, txtTotalIncome, txtMoneyAdvance;

        public MyViewHolder(View itemView) {
            super(itemView);
//            txtStt = itemView.findViewById(R.id.txt_stt);
            txtOrderCode = itemView.findViewById(R.id.txt_order_code);
            txtAddress = itemView.findViewById(R.id.txt_address);
//            txtfullname = itemView.findViewById(R.id.txt_name);
            txtTotalIncome = itemView.findViewById(R.id.txt_total_income);
            txtMoneyAdvance = itemView.findViewById(R.id.txt_money_advance);
        }
    }
    //endregion
}
