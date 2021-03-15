package co.il.scanner.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.il.scanner.R;
import co.il.scanner.model.LoginUser;

public class LoginListAdapter extends RecyclerView.Adapter<LoginListAdapter.ViewHolder>{

    private final Context mContext;
    private final LoginListAdapterListener mListener;
    private final List<LoginUser> mUsersList;

    public LoginListAdapter(Context context, LoginListAdapterListener listener, List<LoginUser> mUsersList) {
        this.mContext = context;
        this.mListener = listener;
        this.mUsersList = mUsersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.login_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name =   mUsersList.get(position).getLastname() + " " +mUsersList.get(position).getFirstname();
        holder.mButtonName.setText(name);
        holder.mButtonName.setOnClickListener(view -> mListener.onItemListClicked(mUsersList.get(position)));

    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        private final TextView mButtonName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mButtonName = itemView.findViewById(R.id.LLI_button_item_BTN);
        }
    }




    public interface LoginListAdapterListener {

        void onItemListClicked(LoginUser loginUser);
    }
}
