package co.il.scanner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import java.util.Objects;

import co.il.scanner.model.Order;

public class ClientDialog {


        @SuppressLint("SetTextI18n")
        public void showDialog(Context context, Order order){
            final Dialog dialog = new Dialog(context);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            dialog.setContentView(R.layout.client_detiles_dialog);

            TextView clientDetails = dialog.findViewById(R.id.CDD_detiles_TV);

            if (order != null){

                StringBuilder myString = new StringBuilder();
                myString.append("הזמנה מספר: "+ order.getId()).append(" \n\n");
                if(order.getMember()!=null) {
                    myString.append(order.getMember().getLastName()).append(" ");
                    myString.append(order.getMember().getHusbandFirst()).append(" ו");
                    myString.append(order.getMember().getWifeFirst()).append("\n\n");
//                myString.append(order.getCustomer().getFirstname2()).append(" ");
//                myString.append(order.getCustomer().getLastname()).append(" \n\n");
//                myString.append(order.getCustomer().getAddress()).append(" \n\n");
                    myString.append(order.getMember().getStreet()).append(" ");
                    myString.append(order.getMember().getHouseNumber()).append(" ");
                    myString.append(order.getMember().getCity()).append("\n\n");
                    if(order.getMember().getPhone()!=null) {
                        myString.append(order.getMember().getPhone()).append(", ");
                    }
                    if(order.getMember().getHusbandPhone()!=null) {
                        myString.append(order.getMember().getHusbandPhone()).append(", ");
                    }
                    if(order.getMember().getWifePhone()!=null) {
                        myString.append(order.getMember().getWifePhone()).append("\n\n");
                    }

                }

                clientDetails.setText(myString);
            }


            dialog.show();

        }

}
