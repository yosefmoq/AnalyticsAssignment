package com.example.analyticsassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    List<Product> products;

    public  ProductsAdapter(List<Product> products){
        this.products = products;
    }
    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_design, parent, false);
        return new ProductsAdapter.ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        TextView productId;
        TextView productName;
        TextView productPrice;
        TextView productDescription;
        TextView productAmount;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            productId = itemView.findViewById(R.id.productId);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDescription = itemView.findViewById(R.id.productDescription);
            productAmount= itemView.findViewById(R.id.productAmount);
        }

        public void bind(Product product) {
            productId.setText(product.getId()+"");
            productName.setText(product.getName());
            productPrice.setText(product.getPrice()+"");
            productDescription.setText(product.getDescription());
            productAmount.setText(product.getAmount()+"");
        }
    }
}
