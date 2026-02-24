package lk.jiat.mshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import lk.jiat.mshop.R;

public class ProductSliderAdapter extends RecyclerView.Adapter<ProductSliderAdapter.ProductsSliderViewHolder> {

    private List<String> images;

    public ProductSliderAdapter(List<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ProductsSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_slider_item, parent, false);
        return new ProductsSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsSliderViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(images.get(position))
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ProductsSliderViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ProductsSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.product_slider_item_image);
        }
    }

}
