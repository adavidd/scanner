package co.il.scanner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.util.Objects;

public class HandScanDialog {

    private HandScanDialogListener mListener;

    @SuppressLint("SetTextI18n")
    public void showDialog(Context context, HandScanDialogListener listener) {

        this.mListener = listener;
        final Dialog dialog = new Dialog(context);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(R.layout.hand_scan_dialog);

        EditText handScan = dialog.findViewById(R.id.HSD_detiles_ET);

        dialog.findViewById(R.id.HSD_hand_scan_RL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onHandScanClicked(handScan.getText().toString());
            }
        });

        dialog.show();

    }


    public  interface HandScanDialogListener{

        void onHandScanClicked(String handScan);
    }


}
