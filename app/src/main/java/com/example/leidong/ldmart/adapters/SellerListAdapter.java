package com.example.leidong.ldmart.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leidong.ldmart.R;
import com.example.leidong.ldmart.beans.Seller;
import com.example.leidong.ldmart.constants.Constants;
import com.example.leidong.ldmart.ui.UserActivity;

/**
 * 卖家列表适配器
 */
public class SellerListAdapter extends RecyclerView.Adapter<SellerListAdapter.ViewHolder>{
    private Context mContext;
    private Seller[] mSellers;

    public SellerListAdapter(Context context, Seller[] mSellers) {
        this.mContext = context;
        this.mSellers = mSellers;
    }

    @Override
    public SellerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.seller_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SellerListAdapter.ViewHolder holder, final int position) {
        if(mSellers != null){
            holder.sellerId.setText(mSellers[position].getId() + "");
            holder.sellerName.setText(mSellers[position].getUsername());

            holder.sellerItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, UserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constants.USER_ID, mSellers[position].getId());
                    bundle.putInt(Constants.USER_MODE, Constants.SELLER_MODE);
                    intent.putExtra(Constants.USER_DATA, bundle);
                    mContext.startActivity(intent);
                }
            });

            holder.sellerItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mSellers != null){
            return mSellers.length;
        }
        return 0;
    }

    /**
     * ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout sellerItemLayout;
        TextView sellerId;
        TextView sellerName;

        ViewHolder(View itemView) {
            super(itemView);

            sellerItemLayout = itemView.findViewById(R.id.seller_item_layout);
            sellerId = itemView.findViewById(R.id.seller_id);
            sellerName = itemView.findViewById(R.id.seller_name);
        }
    }
}
