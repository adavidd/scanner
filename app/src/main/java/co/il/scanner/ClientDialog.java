package co.il.scanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import java.util.Objects;

import co.il.scanner.model.Orders;

public class ClientDialog {


        @SuppressLint("SetTextI18n")
        public void showDialog(Context context, Orders orders){
            final Dialog dialog = new Dialog(context);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            dialog.setContentView(R.layout.client_detiles_dialog);

            TextView clientDetails = dialog.findViewById(R.id.CDD_detiles_TV);

            if (orders != null){

                StringBuilder myString = new StringBuilder();
                myString.append("הזמנה מספר: "+ orders.getId()).append(" \n\n");
                myString.append(orders.getCustomer().getFirstname1()).append(" ");
                myString.append(orders.getCustomer().getFirstname2()).append(" ");
                myString.append(orders.getCustomer().getLastname()).append(" \n\n");
                myString.append(orders.getCustomer().getAddress()).append(" \n\n");
                myString.append(orders.getCustomer().getTz1()).append(" \n\n");
                myString.append(orders.getCustomer().getTz2()).append(" \n\n");

                clientDetails.setText(myString);
            }


            dialog.show();

        }

}
