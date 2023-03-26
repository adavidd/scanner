package com.shapira.collectorscanner.login;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import com.bumptech.glide.Glide;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.shapira.collectorscanner.R;
import com.shapira.collectorscanner.model.LoginUser;

public class LoginListAdapter extends RecyclerView.Adapter<LoginListAdapter.ViewHolder>{

    private final Context mContext;
    private final LoginListAdapterListener mListener;
    private final List<LoginUser> mUsersList;
    private final int mlastUserId;

    public LoginListAdapter(Context context, LoginListAdapterListener listener, List<LoginUser> mUsersList,int mlastUserId) {
        this.mContext = context;
        this.mListener = listener;
        this.mUsersList = mUsersList;
        this.mlastUserId= mlastUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.login_list_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public long getItemId(int position) {
        return mUsersList.get(position).getId();
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        String name =   mUsersList.get(position).getLastname() + " " +mUsersList.get(position).getFirstname();
        holder.mButtonName.setText(name);
        holder.mButtonName.setOnClickListener(view -> mListener.onItemListClicked(mUsersList.get(position)));
        if(this.mlastUserId==mUsersList.get(position).getId() ){
            holder.mButtonName.setTypeface(null, Typeface.BOLD);
            holder.mItemLL.setBackgroundResource(R.color.teal_200);
        }
        String url = mUsersList.get(position).getAvaratUrl();
        if(url!=null && url.length()>0) {
            if(url.endsWith("svg")){

                //GlideToVectorYou.justLoadImage(mContext, url, holder.mAvatarImgView);
                GlideToVectorYou.init().with(mContext).setPlaceHolder(R.drawable.ic_launcher_background,R.drawable.ic_launcher_background)
                        .load(Uri.parse(url),holder.mAvatarImgView);

            }else {
                Glide.with(mContext).load(url).fitCenter().placeholder(R.drawable.ic_launcher_background)
                        .into(holder.mAvatarImgView);
            }
        }


    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private  final LinearLayout mItemLL;
        private final TextView mButtonName;
        private final ImageView mAvatarImgView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mAvatarImgView= itemView.findViewById(R.id.avatarImg);
            this.mButtonName = itemView.findViewById(R.id.LLI_button_item_BTN);
            this.mItemLL = itemView.findViewById(R.id.itemLL);
        }
    }




    public interface LoginListAdapterListener {

        void onItemListClicked(LoginUser loginUser);
    }
}
