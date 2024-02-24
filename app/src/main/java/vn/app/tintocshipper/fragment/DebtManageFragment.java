package vn.app.tintocshipper.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.adapter.DebtManagerAdapter;
import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.DebtObj;
import vn.app.tintocshipper.utils.Utility;

public class DebtManageFragment extends BaseFragment implements View.OnClickListener {
    //region Var
    final String FORMAT = "yyyy-MM-dd";
    ImageView ivBack, imgAlert1, imgAlert2;
    public LinearLayout llNumDebtOrder;
    public TextView txtNumberOrder;
    TextView txt_from_date, txt_to_date, txt_payed, txt_not_payed, txt_total_debt, txtv_data;
    FrameLayout llPayed, llNotPayed;
    private RecyclerView recyclerListDebt;
    private List<DebtObj> debtObjs;
    int type = 1;
    private SwipeRefreshLayout swipeRefreshLayout;
    //minusWeek: trừ số tuần
    DateTime fromDate = new DateTime().minusWeeks(1);
    DateTime toDate = new DateTime();
    DateTimeFormatter df = DateTimeFormat.forPattern(FORMAT);
    int sttPay;
    //endregion

    //region Instance
    public static DebtManageFragment newInstance() {
        DebtManageFragment fragment = new DebtManageFragment();
        return fragment;
    }
    //endregion

    //region OnCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_debt_manage, container, false);
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
            imgAlert1 = view.findViewById(R.id.imgAlertDebt1);
            imgAlert2 = view.findViewById(R.id.imgAlertDebt2);
            txt_from_date = view.findViewById(R.id.txt_from_date);
            txtv_data = view.findViewById(R.id.txtv_data);
            txt_from_date.setText(getDate(fromDate));
            txt_to_date = view.findViewById(R.id.txt_to_date);
            txt_to_date.setText(getDate(toDate));
            llNumDebtOrder = view.findViewById(R.id.ll_num_debt_order);
            txtNumberOrder = view.findViewById(R.id.txt_number_order);

            llPayed = view.findViewById(R.id.ll_payed);
            llNotPayed = view.findViewById(R.id.ll_not_payed);
            txt_payed = view.findViewById(R.id.txt_payed);
            txt_not_payed = view.findViewById(R.id.txt_not_payed);
            txt_total_debt = view.findViewById(R.id.txt_total_debt);
            recyclerListDebt = view.findViewById(R.id.recycler_debt);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerListDebt.setLayoutManager(layoutManager);
            swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            new GetDebtManager().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
            new GetNotify().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getView().findViewById(R.id.iv_back).setOnClickListener(this);
            getView().findViewById(R.id.ll_from_date).setOnClickListener(this);
            getView().findViewById(R.id.ll_to_date).setOnClickListener(this);
            getView().findViewById(R.id.ll_payed).setOnClickListener(this);
            getView().findViewById(R.id.ll_not_payed).setOnClickListener(this);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new GetDebtManager().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, type);
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
                ivBack.setOnClickListener(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            ivBack.setOnClickListener(DebtManageFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.ll_from_date:
                getFromDate();
                break;
            case R.id.ll_to_date:
                getTodate();
                break;
            case R.id.ll_payed:
                llNumDebtOrder.setVisibility(View.GONE);
                type = 1;
                updateUI(view.getId());
                sttPay = 1;
                new GetDebtManager().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
                break;
            case R.id.ll_not_payed:
                llNumDebtOrder.setVisibility(View.GONE);
                updateUI(view.getId());
                type = 2;
                sttPay = 2;
                new GetDebtManager().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 2);
                break;
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetDebtManager().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, type);
                // Refresh items
            }
        });
    }
    //endregion

    //region get from date
    private void getFromDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        DateTime dateTime = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
                        if (dateTime.getMillis() > toDate.getMillis()) {
                            Toast.makeText(getActivity(), R.string.err_from_to_time, Toast.LENGTH_LONG).show();
                            return;
                        }
                        fromDate = dateTime;
                        txt_from_date.setText(fromDate.toString("dd - MM - yyyy"));

                        if (sttPay == 1) {
                            new GetDebtManager().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
                        } else {
                            new GetDebtManager().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 2);
                        }
                    }
                }, fromDate.getYear(), fromDate.getMonthOfYear() - 1, fromDate.getDayOfMonth());
        datePickerDialog.show();
    }
    //endregion

    //region ToDate
    private void getTodate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        DateTime dateTime = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
                        if (dateTime.getMillis() < fromDate.getMillis()) {
                            Toast.makeText(getActivity(), R.string.err_from_to_time, Toast.LENGTH_LONG).show();
                            return;
                        }
                        toDate = dateTime;
                        txt_to_date.setText(toDate.toString("dd - MM - yyyy"));

                        if (type == 1) {
                            new GetDebtManager().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
                        } else {
                            new GetDebtManager().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 2);
                        }
                    }
                }, toDate.getYear(), toDate.getMonthOfYear() - 1, toDate.getDayOfMonth());
        datePickerDialog.show();
    }

    private void updateUI(int id) {
        llNotPayed.setBackgroundResource(R.drawable.uncheck_left);
        txt_not_payed.setTextColor(Color.BLACK);
        llPayed.setBackgroundResource(R.drawable.uncheck_left);
        txt_payed.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(llNotPayed.getWidth(), CmmFunc.dpToPixel(getActivity(), 40));
        layoutParams.gravity = Gravity.BOTTOM;
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(llNotPayed.getWidth(), CmmFunc.dpToPixel(getActivity(), 52));
        llNotPayed.setLayoutParams(layoutParams);
        llPayed.setLayoutParams(layoutParams);
        switch (id) {
            case R.id.ll_not_payed:
                llNotPayed.setBackgroundResource(R.drawable.tab_checked);
                llNotPayed.setLayoutParams(layoutParams1);
                txt_not_payed.setTextColor(Color.parseColor("#20c1ec"));
                imgAlert2.setVisibility(View.GONE);

                Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "segoeuib.ttf");
                txt_not_payed.setTypeface(customFont);
                Typeface customFont2 = Typeface.createFromAsset(getActivity().getAssets(), "segoeui.ttf");
                txt_payed.setTypeface(customFont2);
//                txtType.setVisibility(View.GONE);
                break;
            case R.id.ll_payed:
                llPayed.setBackgroundResource(R.drawable.tab_checked);
                llPayed.setLayoutParams(layoutParams1);
                txt_payed.setTextColor(Color.parseColor("#20c1ec"));
//                txtType.setVisibility(View.VISIBLE);
                imgAlert2.setVisibility(View.GONE);

                Typeface customFont3 = Typeface.createFromAsset(getActivity().getAssets(), "segoeuib.ttf");
                txt_payed.setTypeface(customFont3);
                Typeface customFont4 = Typeface.createFromAsset(getActivity().getAssets(), "segoeui.ttf");
                txt_not_payed.setTypeface(customFont4);
                break;
        }
    }

    //region Format date
    private String formatDate(DateTime date) {
        if (date == null) {
            return null;
        }
        return date.getYear() + "-" + StringUtils.leftPad(date.getMonthOfYear() + "", 2, "0") + "-"
                + StringUtils.leftPad(date.getDayOfMonth() + "", 2, "0");
    }

    private String getDate(DateTime dateTime) {
        if (dateTime == null) {
            return StringUtils.EMPTY;
        }
        return StringUtils.leftPad(dateTime.getDayOfMonth() + "", 2, "0") + " - " +
                StringUtils.leftPad(dateTime.getMonthOfYear() + "", 2, "0") + " - " + dateTime.getYear();
    }
    //endregion
    //endregion

    //endregion

    //region Request API
    class GetDebtManager extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            int type;
            JSONObject jsonObject = null;
            try {
                type = (int) objects[0];
                String dateFrom = formatDate(fromDate);
                String dateTo = formatDate(toDate);
                String value = APIHelper.getShipperIncome(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()), StorageHelper.get(StorageHelper.USERNAME),
                        StorageHelper.get(StorageHelper.TOKEN), dateFrom, dateTo, type);
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
                    txt_total_debt.setText(NumberFormat.getNumberInstance(Locale.US)
                            .format(data.getDouble("total_take")) + " VND");

                    debtObjs = (List<DebtObj>) CmmFunc.tryParseList(data.getString("orders"),
                            DebtObj.class);
                    DebtManagerAdapter incomeAdapter = new DebtManagerAdapter(DebtManageFragment.this, recyclerListDebt, debtObjs);
                    recyclerListDebt.setAdapter(incomeAdapter);
                    if (debtObjs.size() == 0) {
                        recyclerListDebt.setVisibility(View.GONE);
                        txtv_data.setVisibility(View.VISIBLE);
                    } else if (debtObjs.size() > 0) {
                        recyclerListDebt.setVisibility(View.VISIBLE);
                        txtv_data.setVisibility(View.GONE);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }

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
                    int notify_not_payment = data.getInt("notify_not_payment");
                    int notify_was_payment = data.getInt("notify_was_payment");
                    if (notify_was_payment == 1) {
                        imgAlert1.setVisibility(View.VISIBLE);
                    }
                    if (notify_not_payment == 1) {
                        imgAlert2.setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

//endregion

