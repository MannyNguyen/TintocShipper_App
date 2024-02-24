package vn.app.tintocshipper.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.adapter.OrderStatusAdapter;
import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.OrderStatusObj;
import vn.app.tintocshipper.utils.Utility;

public class OrderDetailFragment extends BaseFragment implements View.OnClickListener {
    //region Var
    private TextView txtOrderCode, txtComodity, txtWeight, txtOrderNote, txtSenderName, txtSenderPhone, txtSenderAddress, txtReceiverName, txtReceiverPhone,
            txtReceiverAddress, txtAddonText, txtOrderType, txtTotal, txtCod;
    ImageView ivBack, ivCallSender, ivSMSSender, ivCallReceiver, ivSMSReceiver, ivShowHide;
    RecyclerView recyclerStatus;
    RelativeLayout rlCod;
    private List<OrderStatusObj> orderStatusObjs;
    private int statusId;
    Button btnConfirm;
    boolean isShow = true;
    LinearLayout llSenderInfo, llCallSuport;
    //endregion

    //region Constructor
    public OrderDetailFragment() {
        // Required empty public constructor
    }
    //endregion

    public static OrderDetailFragment newInstance() {
        Bundle args = new Bundle();
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static OrderDetailFragment newInstance(String orderCode, int type, boolean isAccept) {
        Bundle args = new Bundle();
        args.putString("orderCode", orderCode);
        args.putInt("type", type);
        args.putBoolean("is_accept", isAccept);
        OrderDetailFragment fragment = new OrderDetailFragment();
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
            view = inflater.inflate(R.layout.fragment_order_detail, container, false);
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
            rlCod = view.findViewById(R.id.rl_cod);
            txtCod = view.findViewById(R.id.txt_cod);
            llSenderInfo = view.findViewById(R.id.ll_sender_info);
            txtAddonText = view.findViewById(R.id.txt_addon_text);
            txtOrderType = view.findViewById(R.id.txt_order_type);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerStatus = view.findViewById(R.id.recycler_status);
            ivShowHide = view.findViewById(R.id.iv_show_hide);
            recyclerStatus.setLayoutManager(linearLayoutManager);
            txtOrderCode = view.findViewById(R.id.txt_order_code);
            txtComodity = view.findViewById(R.id.txt_commodity);
            txtWeight = view.findViewById(R.id.txt_weight);
            txtOrderNote = view.findViewById(R.id.txt_order_note);
            txtSenderName = view.findViewById(R.id.txt_sender_name);
            txtSenderPhone = view.findViewById(R.id.txt_sender_phone);
            txtSenderAddress = view.findViewById(R.id.txt_sender_address);
            txtReceiverName = view.findViewById(R.id.txt_receiver_name);
            txtReceiverPhone = view.findViewById(R.id.txt_receiver_phone);
            txtReceiverAddress = view.findViewById(R.id.txt_receiver_address);
            txtTotal = view.findViewById(R.id.txt_total_guest_receipts);
            ivCallSender = view.findViewById(R.id.iv_call_sender);
            ivSMSSender = view.findViewById(R.id.iv_sms_sender);
            ivCallReceiver = view.findViewById(R.id.iv_call_receiver);
            ivSMSReceiver = view.findViewById(R.id.iv_sms_receiver);
            btnConfirm = view.findViewById(R.id.btn_confirm);
            llCallSuport=view.findViewById(R.id.btn_call_support);
            ivShowHide.setBackgroundResource(R.drawable.arrow_down);

            if (!getArguments().getBoolean("is_accept")) {
                btnConfirm.setVisibility(View.GONE);
            }

            new GetOrderByCode().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            ivBack.setOnClickListener(this);
            llCallSuport.setOnClickListener(this);
            btnConfirm.setOnClickListener(this);
            getView().findViewById(R.id.btn_sms_support).setOnClickListener(this);

            ivShowHide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isShow) {
                        ivShowHide.setBackgroundResource(R.drawable.arrow_up);
                        llSenderInfo.setVisibility(View.VISIBLE);
                        isShow = false;
                    } else {
                        ivShowHide.setBackgroundResource(R.drawable.arrow_down);
                        llSenderInfo.setVisibility(View.GONE);
                        isShow = true;
                    }
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
                            ivBack.setOnClickListener(OrderDetailFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_call_support:
                Intent iHotline_1 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "0938264867", null));
                startActivity(iHotline_1);
                break;
            case R.id.btn_sms_support:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "0938264867", null)));
                break;
            case R.id.btn_confirm:
                CmmFunc.replaceFragment(getActivity(), R.id.main_container, UpdateStatusOrderFragment.newInstance(getArguments().getString("orderCode"), 2, "Giao Hàng", true));
                break;
        }
    }
    //endregion

    //region RequestAPI
    class GetOrderByCode extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String userName = StorageHelper.get(StorageHelper.USERNAME);

                String value = APIHelper.getOrderByCode(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        userName, StorageHelper.get(StorageHelper.TOKEN), getArguments().getString("orderCode"));
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
                    statusId = data.getInt("status_id");
                    txtAddonText.setText("(" + data.getString("addon_text") + ")");
                    txtOrderType.setText("(" + data.getString("order_type") + ")");
                    final String orderType = data.getString("order_type");

                    orderStatusObjs = (List<OrderStatusObj>) CmmFunc.tryParseList(data.getString("status_process"),
                            OrderStatusObj.class);
                    OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(OrderDetailFragment.this, recyclerStatus,
                            orderStatusObjs, statusId);
                    recyclerStatus.setAdapter(orderStatusAdapter);

                    final String orderCode = data.getString("order_code");

                    txtOrderCode.setText(data.getString("order_code"));
                    txtComodity.setText(data.getString("product_type"));
                    txtWeight.setText(data.getString("product_weight_text"));
                    txtSenderName.setText(data.getString("sender_fullname"));
                    txtSenderPhone.setText(data.getString("sender_phone"));
//                    txtTotal.setText(data.getString("money_must_take_user"));
                    txtTotal.setText(CmmFunc.formatMoney(Math.round(data.getDouble("money_must_take_user"))) + " VND");

                    if (data.getDouble("money_cash_advance") == 0 || data.getDouble("money_cash_advance") == 0.0) {
                        rlCod.setVisibility(View.GONE);
                    } else {
                        rlCod.setVisibility(View.VISIBLE);
                        txtCod.setText(CmmFunc.formatMoney(Math.round(data.getDouble("money_cash_advance"))) + " VND");
                    }

                    String note = data.getString("note");
                    String noteFormDeliveryCar = data.getString("note_form_deliver_car");
                    String noteChangeService = data.getString("note_change_service");
                    if (!noteFormDeliveryCar.equals("")) {
                        if (!note.equals(""))
                            note += ", ";
                        note += "nhà xe: " + noteFormDeliveryCar;
                    }
                    if (!noteChangeService.equals("")) {
                        if (!note.equals(""))
                            note += ", ";
                        note += noteChangeService;
                    }

                    if (data.getInt("allow_see_order") == 1) {
                        if (!note.equals(""))
                            note += ", ";
                        note += "cho xem hàng";
                    }
                    if (!note.equals(""))
                        note = "Ghi chú: " + note;
                    txtOrderNote.setText(note);


                    txtSenderAddress.setText(data.getString("sender_full_address"));
                    txtReceiverName.setText(data.getString("receiver_fullname"));
                    txtReceiverPhone.setText(data.getString("receiver_phone"));
                    txtReceiverAddress.setText(data.getString("receiver_full_address"));

                    String str = data.getString("images");
                    JSONArray images = new JSONArray(str);
                    for (int i = 0; i < images.length(); i++) {
                        final String URL = images.getString(i);
                        if (i == 0) {
                            Glide.with(getActivity()).load(URL).into((ImageView) view.findViewById(R.id.iv_photo_1));
                            if (URL != null && URL != "") {
                                getView().findViewById(R.id.iv_photo_1).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CmmFunc.addFragment(getActivity(), R.id.main_container, ShowPhotoFragment.newInstance(URL));
                                    }
                                });
                            }

                        } else if (i == 1) {
                            Glide.with(getActivity()).load(URL).into((ImageView) view.findViewById(R.id.iv_photo_2));
                            if (URL != null && URL != "") {
                                getView().findViewById(R.id.iv_photo_2).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CmmFunc.addFragment(getActivity(), R.id.main_container, ShowPhotoFragment.newInstance(URL));
                                    }
                                });
                            }
                        } else if (i == 2) {
                            Glide.with(getActivity()).load(URL).into((ImageView) view.findViewById(R.id.iv_photo_3));
                            if (URL != null && URL != "") {
                                getView().findViewById(R.id.iv_photo_3).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CmmFunc.addFragment(getActivity(), R.id.main_container, ShowPhotoFragment.newInstance(URL));
                                    }
                                });
                            }
                        }
                    }
                    recyclerStatus.scrollToPosition(OrderStatusObj.getLastPositionDone(orderStatusObjs));

                    DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                    String createDate = data.getString("create_date");
                    if (createDate != null && createDate != "") {
                        DateTime dateTime = DateTime.parse(createDate, df);
                        String value = formatDate(dateTime);
//                        txtOrderCreateDate.setText(value);
                    }

                    String timeDeliveryFrom = data.getString("time_delivery_from");
                    if (timeDeliveryFrom != null && timeDeliveryFrom != "") {
                        DateTime dateTime = DateTime.parse(timeDeliveryFrom, df);
                        String value = formatDate(dateTime);
//                        txtDateTimeFrom.setText(value);
                    }

                    String timeDeliveryTo = data.getString("time_delivery_to");
                    if (timeDeliveryTo != null && timeDeliveryTo != "") {
                        DateTime dateTime = DateTime.parse(timeDeliveryTo, df);
                        String value = formatDate(dateTime);
//                        txtDateTimeTo.setText(value);
                    }

                    final boolean mustCmt = data.getBoolean("must_comment");

                    final String senderPhone = data.getString("sender_phone");
                    final String receiverPhone = data.getString("receiver_phone");

                    //region Call and SMS
                    ivCallSender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent callSender = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", senderPhone, null));
                            startActivity(callSender);
                        }
                    });

                    ivSMSSender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", senderPhone, null)));
                        }
                    });

                    ivCallReceiver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent callSender = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", receiverPhone, null));
                            startActivity(callSender);
                        }
                    });

                    ivSMSReceiver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", receiverPhone, null)));
                        }
                    });
                    //endregion

                    getView().findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CmmFunc.addFragment(getActivity(), R.id.main_container,
                                    UpdateStatusOrderFragment.newInstance(getArguments().getString("orderCode"),
                                            getArguments().getInt("type"), orderType, mustCmt));
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Methods
    private String formatDate(DateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        String retValue = StringUtils.leftPad(dateTime.getHourOfDay() + "", 2, "0") + ":" +
                StringUtils.leftPad(dateTime.getMinuteOfHour() + "", 2, "0") + " Ngày " +
                StringUtils.leftPad(dateTime.getDayOfMonth() + "", 2, "0") + "-" +
                StringUtils.leftPad(dateTime.getMonthOfYear() + "", 2, "0") + "-" + dateTime.getYear();
        return retValue;
    }
    //endregion
}
