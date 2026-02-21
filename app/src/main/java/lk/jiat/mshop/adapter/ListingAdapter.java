package lk.jiat.mshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import lk.jiat.mshop.R;
import lk.jiat.mshop.model.Category;
import lk.jiat.mshop.model.Product;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder> {

    private List<Product> products;
    private OnListingItemClickListener listener;
    public ListingAdapter(List<Product> products, OnListingItemClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingAdapter.ViewHolder holder, int position) {

        Product product = products.get(position);
        holder.productTitle.setText(product.getTitle());
        holder.productPrice.setText(product.getPrice()+"");
        Glide.with(holder.itemView.getContext())
                .load(product.getImages().get(0))
                .centerCrop()
                .into(holder.productImage);

        holder.itemView.setOnClickListener(v->{

            Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.click_animation);
            v.startAnimation(animation);

            if(listener != null){
                listener.onListingItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView productImage;
        TextView productTitle;
        TextView productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.listing_item_image);
            productTitle = itemView.findViewById(R.id.listing_item_name);
            productPrice = itemView.findViewById(R.id.listing_item_price);
        }
    }

    public interface OnListingItemClickListener{
        void onListingItemClick(Product product);
    }
}
