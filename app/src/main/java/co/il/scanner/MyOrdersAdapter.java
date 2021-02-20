package co.il.scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.il.scanner.R;
import co.il.scanner.model.LoginUser;
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

        holder.mOrderText.setText(String.valueOf(mOrdersList.get(position).getId()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMyListItemClicked(mOrdersList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrdersList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView mOrderText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mOrderText = itemView.findViewById(R.id.MOI_text);
        }
    }




    public interface MyOrdersAdapterListener {

        void onMyOrdersItemListClicked();

        void onMyListItemClicked(Orders orders);
    }
}
