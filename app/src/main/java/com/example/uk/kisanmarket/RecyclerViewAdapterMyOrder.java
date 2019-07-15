package com.example.uk.kisanmarket;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapterMyOrder extends RecyclerView.Adapter<RecyclerViewAdapterMyOrder.MyViewHolder> {
    Context mContext;
    List<Order> mData;

    public RecyclerViewAdapterMyOrder(Context mContext, List<Order> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public RecyclerViewAdapterMyOrder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_order_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterMyOrder.MyViewHolder holder, final int position) {
        Picasso.get()
                .load(mData.get(position).getP_image_url())
                .resize(80, 80)
                .centerCrop()
                .into(holder.productImage);
        holder.orderId.setText(mData.get(position).getOrder_id());
        holder.productId.setText(mData.get(position).getP_id());
        holder.productName.setText(mData.get(position).getP_name());
        holder.productQuantity.setText(mData.get(position).getP_quantity());
        holder.productPrice.setText(mData.get(position).getP_price());
        holder.orderAddress.setText(mData.get(position).getAddress());
        holder.orderStatus.setText(mData.get(position).getOrder_date()+", "+mData.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView orderId, productId, productName, productQuantity, productPrice, orderAddress, orderStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            orderId = (TextView) itemView.findViewById(R.id.textViewOrderId);
            productImage = (ImageView) itemView.findViewById(R.id.imageViewProductImage);
            productId = (TextView) itemView.findViewById(R.id.textViewProductId );
            productName = (TextView) itemView.findViewById(R.id.textViewProductName);
            productQuantity = (TextView) itemView.findViewById(R.id.textViewProductQuantity);
            productPrice = (TextView) itemView.findViewById(R.id.textViewProductPrice);
            orderAddress = (TextView) itemView.findViewById(R.id.textViewAddress);
            orderStatus  =(TextView) itemView.findViewById((R.id.textViewStatus));
        }
    }
}
