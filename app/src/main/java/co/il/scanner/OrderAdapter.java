package co.il.scanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.il.scanner.model.OrderItemsItem;
import co.il.scanner.model.Orders;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context mContext;
    private final OrderAdapterListener mListener;
    private final List<OrderItemsItem> mOrdersItems;

    public OrderAdapter(Context context, OrderAdapterListener listener, List<OrderItemsItem> ordersItems) {
        this.mContext = context;
        this.mListener = listener;
        this.mOrdersItems = ordersItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mOrdersItems.get(position).getItem() != null) {

            holder.mItemName.setText(String.valueOf(mOrdersItems.get(position).getItem().getName()));
        }

        int itemCollectedQuantity = mOrdersItems.get(position).getCollectedQuantity();
        int itemOrderQuantity = mOrdersItems.get(position).getOrderQuantity();

        holder.mItemCount.setText(itemCollectedQuantity + "/" + itemOrderQuantity);

        if (itemCollectedQuantity == itemOrderQuantity) {
            holder.mItemCount.setVisibility(View.GONE);
            holder.mItemDone.setVisibility(View.VISIBLE);
        } else {

            holder.mItemCount.setVisibility(View.VISIBLE);
            holder.mItemDone.setVisibility(View.GONE);
        }


        holder.mItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mOrdersItems.get(position).getCollectedQuantity() > 0) {

                    mListener.onDeleteClicked(mOrdersItems.get(position));
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return mOrdersItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView mItemCount;
        private final TextView mItemName;
        private final ImageView mItemDone;
        private final RelativeLayout mItemDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemDone = itemView.findViewById(R.id.OI_done_icon_IV);
            mItemCount = itemView.findViewById(R.id.OI_text_count_TV);
            mItemName = itemView.findViewById(R.id.OI_item_name_TV);
            mItemDelete = itemView.findViewById(R.id.OI_delete_icon_RL);
        }
    }


    public interface OrderAdapterListener {

        void onDeleteClicked(OrderItemsItem orderItemsItem);
    }
}
