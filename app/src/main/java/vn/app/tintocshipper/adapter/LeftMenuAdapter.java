package vn.app.tintocshipper.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang.StringUtils;

import java.util.List;

import vn.app.tintocshipper.LoginActivity;
import vn.app.tintocshipper.R;
import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.fragment.DebtManageFragment;
import vn.app.tintocshipper.fragment.DeliveryFragment;
import vn.app.tintocshipper.fragment.HistoryOrderFragment;
import vn.app.tintocshipper.fragment.HomeOrderListFragment;
import vn.app.tintocshipper.fragment.NewOrderFragment;
import vn.app.tintocshipper.fragment.OrderBillCodeFragment;
import vn.app.tintocshipper.fragment.ShipperInfoFragment;
import vn.app.tintocshipper.fragment.SupportFragment;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.LeftMenu;

import static vn.app.tintocshipper.config.GlobalClass.getActivity;

/**
 * Created by Admin on 7/26/2017.
 */

public class LeftMenuAdapter extends RecyclerView.Adapter<LeftMenuAdapter.MyViewHolder> implements View.OnClickListener {
    //region Var
    public List<LeftMenu> leftMenuList;
    RecyclerView recyclerView;
    //endregion

    //region Constructor
    public LeftMenuAdapter(RecyclerView recyclerView, List<LeftMenu> leftMenuList) {
        this.recyclerView = recyclerView;
        this.leftMenuList = leftMenuList;
    }
    //endregion

    //region MyViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_left_menu, parent, false);
        itemView.setOnClickListener(LeftMenuAdapter.this);
        return new MyViewHolder(itemView);
    }
    //endregion

    //region OnBindViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LeftMenu leftMenu = leftMenuList.get(position);
        holder.txtTextMenu.setText(leftMenu.getTextMenu());
        holder.ivIconMenu.setImageResource(leftMenu.getIvIconMenu());
        if (leftMenu.getType() == LeftMenu.NEW_ORDER || leftMenu.getType() == LeftMenu.DEBT_MANAGER || leftMenu.getType() == leftMenu.ORDER) {
            if (leftMenu.isShowNotify()) {
                holder.imgAlert.setVisibility(View.VISIBLE);
            } else {
                holder.imgAlert.setVisibility(View.GONE);
            }
        } else {
            holder.imgAlert.setVisibility(View.GONE);
        }
    }
    //endregion

    //region GetItemCount
    @Override
    public int getItemCount() {
        return leftMenuList.size();
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(Gravity.START);
        int position = recyclerView.getChildLayoutPosition(view);
        LeftMenu item = leftMenuList.get(position);
        if (item != null) {
            switch (item.getType()) {
                case LeftMenu.INFO:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(400);
                                CmmFunc.replaceMainFragment(getActivity(), R.id.main_container, ShipperInfoFragment.newInstance());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case LeftMenu.RUNNING_ORDER:
                    //new GetOrderRunning().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;
                case LeftMenu.ORDER:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(400);
                                CmmFunc.replaceFragment(getActivity(), R.id.main_container, HomeOrderListFragment.newInstance());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case LeftMenu.NEW_ORDER:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(400);
                                CmmFunc.replaceFragment(getActivity(), R.id.main_container, NewOrderFragment.newInstance());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    break;
                case LeftMenu.HISTORY:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(400);
                                CmmFunc.replaceFragment(getActivity(), R.id.main_container, HistoryOrderFragment.newInstance());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    break;
                case LeftMenu.DEBT_MANAGER:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(400);
                                CmmFunc.replaceFragment(getActivity(), R.id.main_container, DebtManageFragment.newInstance());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case LeftMenu.BILL:
                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, OrderBillCodeFragment.newInstance());
                    break;
                case LeftMenu.SUPPORT:
                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, SupportFragment.newInstance());
                    break;
                case LeftMenu.IMPORT:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(400);
                                CmmFunc.replaceFragment(getActivity(), R.id.main_container, DeliveryFragment.newInstance());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case LeftMenu.LOGOUT:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.logout_title);
                    builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            StorageHelper.set(StorageHelper.TOKEN, StringUtils.EMPTY);
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            getActivity().startActivity(intent);
                            getActivity().finish();
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
                    break;
            }
        }
    }
    //endregion

    //region MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTextMenu;
        public ImageView ivIconMenu, imgAlert;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTextMenu = itemView.findViewById(R.id.txt_text_menu);
            ivIconMenu = itemView.findViewById(R.id.iv_item_menu);
            imgAlert = itemView.findViewById(R.id.imgAlert);
        }
    }
    //endregion

    //region Request API
//    class GetOrderRunning extends Async {
//        @Override
//        protected JSONObject doInBackground(Object... objects) {
//            JSONObject jsonObject = null;
//            try {
//                String value = APIHelper.getOrderRunning(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
//                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN));
//                jsonObject = new JSONObject(value);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return jsonObject;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject jsonObject) {
//            super.onPostExecute(jsonObject);
//            try {
//                int code = jsonObject.getInt("code");
//                if (code == 1) {
////                    JSONObject data = jsonObject.getJSONObject("data");
//                    String data = CmmFunc.tryParseObject(jsonObject.getJSONObject("data"));
//                    CmmFunc.replaceMainFragment(GlobalClass.getActivity(), R.id.main_container, RunningOrderFragment.newInstance(data));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
    //endregion

}
