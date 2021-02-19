package co.il.scanner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import java.util.Objects;

import co.il.scanner.model.Orders;

public class HandScanDialog {

    @SuppressLint("SetTextI18n")
    public void showDialog(Context context) {

        final Dialog dialog = new Dialog(context);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(R.layout.hand_scan_dialog);


        dialog.show();

    }


}
