package co.il.scanner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.util.Objects;

public class PincodeDialog {

    private PincodeDialogListener mListener;

    @SuppressLint("SetTextI18n")
    public void showDialog(Context context, PincodeDialogListener listener) {

        this.mListener = listener;
        final Dialog dialog = new Dialog(context);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(R.layout.enter_pin_dialog);

        EditText handScan = dialog.findViewById(R.id.PCD_pincode_ET);

        dialog.findViewById(R.id.HSD_hand_scan_RL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onPinEntered(handScan.getText().toString());
            }
        });

        dialog.show();

    }


    public  interface PincodeDialogListener{

        void onPinEntered(String pinCode);
    }


}
