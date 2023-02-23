package com.shapira.collectorscanner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.shapira.collectorscanner.model.Order;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    private final Context mContext;
    private final MyOrdersAdapterListener mListener;
    private final List<Order> mOrdersList;

    public MyOrdersAdapter(Context context, MyOrdersAdapterListener listener, List<Order> ordersList) {
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
//        myString.append(" מס' הזמנה:" + mOrdersList.get(position).getId()).append(" \n\n");
        if (mOrdersList.get(position).getStatusId() == 1 || mOrdersList.get(position).getStatusId() == 2) {
            myString.append("בתהליך ליקוט").append(" \n\n");
            holder.mRelativeLayout.setBackgroundColor(Color.parseColor("#fcfc0a"));
        } else if (mOrdersList.get(position).getStatusId() == 3) {
            myString.append("ממתין לתשלום").append(" \n\n");
            holder.mRelativeLayout.setBackgroundColor(Color.parseColor("#f5aa42"));
        } else if (mOrdersList.get(position).getStatusId() == 4) {
            holder.mRelativeLayout.setBackgroundColor(Color.parseColor("#0afc5f"));
            myString.append("הזמנה הושלמה").append(" \n\n");
        } else {
            holder.mRelativeLayout.setBackgroundColor(Color.parseColor("#e8e8e3"));
        }
        if (mOrdersList.get(position).getOrderedFrom() != null) {

            myString.append(mOrdersList.get(position).getOrderedFrom()).append(" \n\n");
        }

        holder.mOrderText.setText(myString);
//        mOrdersList.get(position).getUpdatedAt()

        if (mOrdersList.get(position).getUpdatedAt().getDate() == new Date().getDate()
                && mOrdersList.get(position).getUpdatedAt().getMonth() == new Date().getMonth()
                && mOrdersList.get(position).getUpdatedAt().getYear() == new Date().getYear()
        ) {
            holder.mOrderUpdateAtText.setText(" עודכן לאחרונה היום בשעה " + new SimpleDateFormat("hh:mm").format(mOrdersList.get(position).getUpdatedAt()));
        } else {
            holder.mOrderUpdateAtText.setText(" עודכן לאחרונה ב" + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(mOrdersList.get(position).getUpdatedAt()));

        }


        holder.mOrderLinesText.setText(" שורות:" + mOrdersList.get(position).getOrderItems().stream().filter(c-> c.getItem()!=null && !c.getItem().isHide_in_app()).collect(Collectors.toList()).size());
        holder.itemView.setOnClickListener(view -> mListener.onMyListItemClicked(mOrdersList.get(position)));
        holder.mOrderNumberTV.setText(" מס' הזמנה:" + mOrdersList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return mOrdersList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView mOrderText;
        private RelativeLayout mRelativeLayout;
        private TextView mOrderUpdateAtText;
        private TextView mOrderLinesText;
        private TextView mOrderNumberTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mOrderLinesText = itemView.findViewById(R.id.linesTV);
            mOrderUpdateAtText = itemView.findViewById(R.id.updatedAt);
            mOrderText = itemView.findViewById(R.id.MOI_text);
            mOrderNumberTV = itemView.findViewById(R.id.orderNumberTV);
            mRelativeLayout = itemView.findViewById(R.id.rl);
        }
    }


    public interface MyOrdersAdapterListener {

        void onMyOrdersItemListClicked();

        void onMyListItemClicked(Order order);
    }
}
