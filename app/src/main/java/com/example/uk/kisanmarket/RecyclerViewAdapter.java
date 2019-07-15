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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    Context mContext;
    List<Product> mData;

    public RecyclerViewAdapter(Context mContext, List<Product> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, final int position) {
        Picasso.get()
                .load(mData.get(position).getP_image_url())
                .resize(80, 80)
                .centerCrop()
                .into(holder.productImage);
        holder.productId.setText(mData.get(position).getP_id());
        holder.productName.setText(mData.get(position).getP_name());
        holder.productOwner.setText(mData.get(position).getP_owner());
        holder.productQuantity.setText(mData.get(position).getP_quantity());
        holder.productPrice.setText(mData.get(position).getP_price());
        holder.product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductViewActivity.class);
                intent.putExtra("productImage", mData.get(position).getP_image_url());
                intent.putExtra("productId", mData.get(position).getP_id());
                intent.putExtra("productName", mData.get(position).getP_name());
                intent.putExtra("productPrice", mData.get(position).getP_price());
                intent.putExtra("productOwner", mData.get(position).getP_owner());
                intent.putExtra("productQuantity", mData.get(position).getP_quantity());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productId, productName, productOwner, productQuantity, productPrice;
        private CardView product;

        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.imageViewProductImage);
            productId = (TextView) itemView.findViewById(R.id.textViewProductId );
            productName = (TextView) itemView.findViewById(R.id.textViewProductName);
            productOwner = (TextView) itemView.findViewById(R.id.textViewProductOwner);
            productQuantity = (TextView) itemView.findViewById(R.id.textViewProductQuantity);
            productPrice = (TextView) itemView.findViewById(R.id.textViewProductPrice);
            product = (CardView) itemView.findViewById(R.id.cardViewProduct);
        }
    }
}
