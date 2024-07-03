package com.shapira.collectorscanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shapira.collectorscanner.databinding.OrgOrderItemBinding;
import com.shapira.collectorscanner.model.orgorder.OrgOrder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrgOrdersListAdapter extends RecyclerView.Adapter<OrgOrdersListAdapter.ViewHolder> {
    private final Context mContext;
    private final OrgOrdersAdapterListener mListener;
    private final List<OrgOrder> mOrgOrders;

    public OrgOrdersListAdapter(List<OrgOrder> orgOrders,OrgOrdersAdapterListener listener, Context context) {
        this.mOrgOrders =orgOrders;
        this.mListener = listener;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrgOrderItemBinding itemBinding = OrgOrderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int _position  = holder.getAdapterPosition();
        holder.itemBinding.orderNumberTV.setText( "מס' " + mOrgOrders.get(_position).getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String date = simpleDateFormat.format(mOrgOrders.get(_position).getOrderTime());
//        String date = dateFormat.format(mOrgOrders.get(_position).getOrderTime());
        holder.itemBinding.orderTimeTV.setText(date);
        holder.itemBinding.rl.setOnClickListener(v -> mListener.onOrgOrderClicked(mOrgOrders.get(_position)));
        holder.itemBinding.itemNumberTV.setText("ארגזים: " + mOrgOrders.get(_position).itemsSum);
        holder.itemBinding.linesNumberTV.setText("שורות: " + mOrgOrders.get(_position).linesCount);
        if(mOrgOrders.get(_position).getOrganization()!=null){
            holder.itemBinding.orgNameTV.setText(mOrgOrders.get(_position).getOrganization().getName());
        }else{
            holder.itemBinding.orgNameTV.setText("ארגון לא ידוע");
        }
        holder.itemBinding.statusTV.setText(mOrgOrders.get(_position).getOrderStatus().getName());

        holder.itemBinding.printIcon.setOnClickListener(v -> mListener.onOrgOrderPrintClicked(mOrgOrders.get(_position)));


    }

    @Override
    public int getItemCount() {
        return mOrgOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        OrgOrderItemBinding itemBinding;

        public ViewHolder(OrgOrderItemBinding _itemBinding) {
            super(_itemBinding.getRoot());
            itemBinding = _itemBinding;

        }
    }
    public interface OrgOrdersAdapterListener{



        void onOrgOrderClicked(OrgOrder orgOrder);
        void onOrgOrderPrintClicked(OrgOrder orgOrder);
    }
}
