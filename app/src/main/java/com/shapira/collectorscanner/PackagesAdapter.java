package com.shapira.collectorscanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.shapira.collectorscanner.model.Package;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.ViewHolder>
{
    private final Context mContext;
    private final PackagesAdapter.PackageAdapterListener mListener;
    private final List<Package> mPackageList;
    public PackagesAdapter(Context context, PackagesAdapter.PackageAdapterListener listener, List<Package> packageList) {
        this.mContext = context;
        this.mListener = listener;
        this.mPackageList = packageList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_item, parent, false);
        return new PackagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mOrderNumberTV.setText("הזמנה: "+mPackageList.get(position).getOrder_id() + " חבילה " + mPackageList.get(position).getNumber());
        holder.deleteBtn.setOnClickListener(v -> {
            mListener.onPackageDeleteClicked(mPackageList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return mPackageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView mOrderNumberTV;

        private ImageView deleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mOrderNumberTV = itemView.findViewById(R.id.orderNumberTV);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

        }
    }
    public interface PackageAdapterListener {

        void onPackageItemClicked();

        void onPackageItemClicked(Package pckg);
        void onPackageDeleteClicked(Package pckg);

    }

}
