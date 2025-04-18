package com.shapira.collectorscanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.shapira.collectorscanner.model.OrderItemsItem;


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
    public void onBindViewHolder(@NonNull ViewHolder holder, int p) {
        final int position  = holder.getAdapterPosition();

        if (mOrdersItems.get(position).getItem() != null) {

            holder.mItemName.setText(String.valueOf(mOrdersItems.get(position).getItem().getName()));
            holder.mItemCode.setText(String.valueOf(mOrdersItems.get(position).getItem().getBarcode1() + " " +

                    (mOrdersItems.get(position).getItem().getWarhouseDescription()!=null && mOrdersItems.get(position).getItem().getWarhouseDescription().toString().length()>0?mOrdersItems.get(position).getItem().getWarhouseDescription():"")

            ));
            holder.mItemDescription.setText(
                    (mOrdersItems.get(position).getItem().getSize() !=null && mOrdersItems.get(position).getItem().getSize().length()>0?" מידה:" + mOrdersItems.get(position).getItem().getSize() :"")+

                    (mOrdersItems.get(position).getItem().getColor()!= null && mOrdersItems.get(position).getItem().getColor().length()>0?" צבע: " + mOrdersItems.get(position).getItem().getColor():""));
        }

        int itemCollectedQuantity = mOrdersItems.get(position).getCollectedQuantity();
        int itemOrderQuantity = mOrdersItems.get(position).getOrderQuantity();

        holder.mItemCount.setText(itemCollectedQuantity + "/" + itemOrderQuantity);

        if (itemCollectedQuantity >= itemOrderQuantity) {
//            holder.mItemCount.setVisibility(View.GONE);
            holder.mItemDone.setVisibility(View.VISIBLE);
        } else {

//            holder.mItemCount.setVisibility(View.VISIBLE);
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
        
        holder.mItemMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mOrdersItems.get(position).getCollectedQuantity() > 0) {

                    mListener.onMinusClicked(mOrdersItems.get(position));
                }

            }
        });

        if (itemCollectedQuantity > 0 && itemCollectedQuantity < itemOrderQuantity){

            holder.mItemCountRL.setBackground(ContextCompat.getDrawable(mContext, R.color.order_item_orange));

        }else if (itemCollectedQuantity == itemOrderQuantity){
            holder.mItemCountRL.setBackground(ContextCompat.getDrawable(mContext, R.color.teal_200));

        }else {

            holder.mItemCountRL.setBackground(ContextCompat.getDrawable(mContext, R.color.order_item_red));
        }


    }
    @Override
    public long getItemId(int position) {
        return  this.mOrdersItems.get(position).getId();
    }
    @Override
    public int getItemCount() {
        if (mOrdersItems == null){
            return 0;
        }else {

            return mOrdersItems.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView mItemCount;
        private final TextView mItemName;
        private final TextView mItemCode;
        private final TextView mItemDescription;
        private final ImageView mItemDone;
        private final ImageView mItemDelete;
        private final ImageView mItemMinus;
        private final RelativeLayout mItemCountRL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemDone = itemView.findViewById(R.id.OI_done_icon_IV);
            mItemCount = itemView.findViewById(R.id.OI_text_count_TV);
            mItemName = itemView.findViewById(R.id.OI_item_name_TV);
            mItemCode = itemView.findViewById(R.id.OI_item_code_TV);
            mItemDescription = itemView.findViewById(R.id.OI_item_description_TV);
            mItemDelete = itemView.findViewById(R.id.OI_delete_icon_IV);
            mItemMinus = itemView.findViewById(R.id.OI_minos_icon_IV);
            mItemCountRL = itemView.findViewById(R.id.OI_order_count_RL);
        }
    }


    public interface OrderAdapterListener {

        void onDeleteClicked(OrderItemsItem orderItemsItem);

        void onMinusClicked(OrderItemsItem orderItemsItem);
    }
}
