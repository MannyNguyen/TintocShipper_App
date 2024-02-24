package vn.app.tintocshipper.config;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.control.ICallback;

/**
 * Created by IPP on 3/6/2018.
 */

public class CustomDialog {
    public static void Dialog2Button(final Activity activity, final String titleText, final String messageText, final String titleOk, final String titleCancel,
                                     final ICallback okCallback, final ICallback cancelCallback){
        try {
            AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            LayoutInflater inflater=activity.getLayoutInflater();
            View dialogView=inflater.inflate(R.layout.dialog_confirm, null);
            builder.setView(dialogView);
            builder.setCancelable(false);
            TextView title = dialogView.findViewById(R.id.title);
            title.setText(titleText);
            if (titleText.equals("")){
                title.setVisibility(View.GONE);
            }
            TextView message = dialogView.findViewById(R.id.message);
            message.setText(messageText);
            final CardView cardOk =dialogView.findViewById(R.id.ok);
            final CardView cardCancel=dialogView.findViewById(R.id.cancel);

            TextView okText=dialogView.findViewById(R.id.okText);
            if (titleOk.equals("")){
                okText.setText(R.string.confirm);
            }else {
                okText.setText(titleOk);
            }

            TextView cancelText=dialogView.findViewById(R.id.cancelText);
            if (titleCancel.equals("")){
                cancelText.setText(R.string.cancel);
            }else {
                cancelText.setText(titleCancel);
            }

            final AlertDialog dialog=builder.create();
            Window window=dialog.getWindow();

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount=0.5f;
            lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            dialog.getWindow().setAttributes(lp);
            dialog.show();
            cardOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (okCallback != null) {
                        okCallback.execute();
                    }
                }
            });
            cardCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    if (cancelCallback != null) {
                        cancelCallback.execute();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
