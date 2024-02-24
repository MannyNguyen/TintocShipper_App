package vn.app.tintocshipper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import vn.app.tintocshipper.R;

public class OrderBillCodeFragment extends BaseFragment implements View.OnClickListener {
    ImageView ivBack;

    public static OrderBillCodeFragment newInstance() {
        OrderBillCodeFragment fragment = new OrderBillCodeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_order_bill_code, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivBack = view.findViewById(R.id.iv_back);
            ivBack.setOnClickListener(this);
            isLoad = true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                ivBack.setOnClickListener(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            ivBack.setOnClickListener(OrderBillCodeFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
}
