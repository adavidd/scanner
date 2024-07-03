package com.shapira.collectorscanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shapira.collectorscanner.R;
import com.shapira.collectorscanner.databinding.OrgOrderItemBinding;
import com.shapira.collectorscanner.databinding.OrgOrderLineItemBinding;
import com.shapira.collectorscanner.model.OrderItemsItem;
import com.shapira.collectorscanner.model.orgorder.OrgOrder;
import com.shapira.collectorscanner.model.orgorder.OrgOrderItemsItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrgOrderItemListAdapter extends RecyclerView.Adapter<OrgOrderItemListAdapter.ViewHolder> {
    private final Context mContext;
    private final OrgOrderItemAdapterListener mListener;
    private final List<OrgOrderItemsItem> mOrgOrderItems;

    public OrgOrderItemListAdapter(List<OrgOrderItemsItem> orgOrderItems, OrgOrderItemAdapterListener listener, Context context) {
        this.mOrgOrderItems =orgOrderItems;
        this.mListener = listener;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrgOrderLineItemBinding itemBinding = OrgOrderLineItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int _position  = holder.getAdapterPosition();
//        holder.itemBinding.orderNumberTV.setText( "מס' " + mOrgOrderItems.get(_position).getId());
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
//        String date = simpleDateFormat.format(mOrgOrderItems.get(_position).getOrderTime());
//        String date = dateFormat.format(mOrgOrders.get(_position).getOrderTime());
//        holder.itemBinding.orderTimeTV.setText(date);
        holder.itemBinding.OIItemNameTV.setText(mOrgOrderItems.get(_position).getItem().getName());
        holder.itemBinding.OIItemCodeTV.setText(mOrgOrderItems.get(_position).getItem().getBarcode1());
        holder.itemBinding.OIOrderCountTV.setText( mOrgOrderItems.get(_position).getOrderQuantity()+"");
        holder.itemBinding.OICollectedCountTV.setText( mOrgOrderItems.get(_position).getCollectedQuantity()+"");
        holder.itemBinding.OiUnitsInPackageTV.setText("כמות בחבילה: "+ mOrgOrderItems.get(_position).getUnitsInPackage());
        holder.itemBinding.OIItemDescriptionTV.setText("צבע: " + mOrgOrderItems.get(_position).getItem().getColor() +" מידה: " + mOrgOrderItems.get(_position).getItem().getSize());
        holder.itemBinding.OIMinosIconIV.setOnClickListener(v -> mListener.onMinusClicked(mOrgOrderItems.get(_position)));
        if(mOrgOrderItems.get(_position).getCollectedQuantity() == mOrgOrderItems.get(_position).getOrderQuantity()) {
            holder.itemBinding.OIDoneIconIV.setVisibility(View.VISIBLE);
            holder.itemBinding.OILeftCountTitle.setVisibility(View.GONE);
            holder.itemBinding.OILeftCountTV.setVisibility(View.GONE);
            holder.itemBinding.OIOrderCountRL.setBackground(ContextCompat.getDrawable(mContext, R.color.teal_200));
        }else{
            holder.itemBinding.OIDoneIconIV.setVisibility(View.GONE);
            holder.itemBinding.OILeftCountTitle.setVisibility(View.VISIBLE);
            holder.itemBinding.OILeftCountTV.setVisibility(View.VISIBLE);
            holder.itemBinding.OILeftCountTV.setText( (mOrgOrderItems.get(_position).getOrderQuantity() - mOrgOrderItems.get(_position).getCollectedQuantity()) +"");
            holder.itemBinding.OIOrderCountRL.setBackground(ContextCompat.getDrawable(mContext, R.color.order_item_orange));
        }

        holder.itemBinding.OIDeleteIconIV.setOnClickListener(v -> mListener.onDeleteClicked(mOrgOrderItems.get(_position)));
//        holder.itemBinding.itemNumberTV.setText("ארגזים: " + mOrgOrderItems.get(_position).itemsSum);
//        holder.itemBinding.linesNumberTV.setText("שורות: " + mOrgOrderItems.get(_position).linesCount);
//        if(mOrgOrderItems.get(_position).getOrganization()!=null){
//            holder.itemBinding.orgNameTV.setText(mOrgOrders.get(_position).getOrganization().getName());
//        }else{
//            holder.itemBinding.orgNameTV.setText("ארגון לא ידוע");
//        }

    }

    @Override
    public int getItemCount() {
        return mOrgOrderItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        OrgOrderLineItemBinding itemBinding;

        public ViewHolder(OrgOrderLineItemBinding _itemBinding) {
            super(_itemBinding.getRoot());
            itemBinding = _itemBinding;

        }
    }
    public interface OrgOrderItemAdapterListener{

        void onDeleteClicked(OrgOrderItemsItem orderItemsItem);

        void onMinusClicked(OrgOrderItemsItem orderItemsItem);


    }
}
