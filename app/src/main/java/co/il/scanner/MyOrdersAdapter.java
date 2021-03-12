package co.il.scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.il.scanner.model.Orders;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>{

    private final Context mContext;
    private final MyOrdersAdapterListener mListener;
    private final List<Orders> mOrdersList;

    public MyOrdersAdapter(Context context, MyOrdersAdapterListener listener, List<Orders> ordersList) {
        this.mContext = context;
        this.mListener = listener;
        this.mOrdersList = ordersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_orders_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StringBuilder myString = new StringBuilder();
        myString.append(mOrdersList.get(position).getId()).append(" \n\n");
        if (mOrdersList.get(position).getStatusId() == 1 || mOrdersList.get(position).getStatusId() == 2){
            myString.append("מצב הזמנה: בתהליך ליקוט").append(" \n\n");
        }else if (mOrdersList.get(position).getStatusId() == 3) {
            myString.append("מצב הזמנה: ממתין לתשלום").append(" \n\n");
        }else if (mOrdersList.get(position).getStatusId() == 4) {
            myString.append("מצב הזמנה: הזמנה הושלמה").append(" \n\n");
        }
        if (mOrdersList.get(position).getOrderedFrom() != null){

            myString.append(mOrdersList.get(position).getOrderedFrom()).append(" \n\n");
        }

        holder.mOrderText.setText(myString);
        holder.mOrderUpdateAtText.setText( " שורות:" +mOrdersList.get(position).getOrderItems().size() + " עודכן לאחרונה: " + mOrdersList.get(position).getUpdatedAt());
        holder.itemView.setOnClickListener(view -> mListener.onMyListItemClicked(mOrdersList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mOrdersList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView mOrderText;
        private TextView mOrderUpdateAtText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mOrderText = itemView.findViewById(R.id.MOI_text);
            mOrderUpdateAtText = itemView.findViewById(R.id.updatedAt);
        }
    }




    public interface MyOrdersAdapterListener {

        void onMyOrdersItemListClicked();

        void onMyListItemClicked(Orders orders);
    }
}
