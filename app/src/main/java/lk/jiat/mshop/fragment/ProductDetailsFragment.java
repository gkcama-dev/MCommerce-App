package lk.jiat.mshop.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import lk.jiat.mshop.R;
import lk.jiat.mshop.adapter.ProductSliderAdapter;
import lk.jiat.mshop.databinding.FragmentProductDetailsBinding;
import lk.jiat.mshop.model.Product;


public class ProductDetailsFragment extends Fragment {

    private FragmentProductDetailsBinding binding;
    private String productId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getString("productId");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);

        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Load Product Details
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("products")
                .whereEqualTo("productId", productId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot qds) {
                        if (!qds.isEmpty()) {
                            Product product = qds.getDocuments().get(0).toObject(Product.class);

                            ProductSliderAdapter adapter = new ProductSliderAdapter(product.getImages());
                            binding.productImageSlider.setAdapter(adapter);

                            binding.dotsIndicator.attachTo(binding.productImageSlider);

                            binding.productDetailsTitle.setText(product.getTitle());
                            binding.productDetailsPrice.setText("LKR " + product.getPrice());
                            binding.productDetailAvbQty.setText(String.valueOf(product.getStockCount()));
                            binding.productDetailsRating.setRating(Float.parseFloat(String.valueOf(product.getRating())));

                            if (product.getAttributes() != null) {

                                binding.productDetailAttributeContainer.removeAllViews();

                                product.getAttributes().forEach(attribute -> {
                                    renderAttribute(attribute, binding.productDetailAttributeContainer);
                                });

                            }
                        }
                    }
                });
    }

    private void renderAttribute(Product.Attribute attribute, ViewGroup container) {

        if (attribute == null || attribute.getValues() == null) {
            return;
        }
        LinearLayout raw = new LinearLayout(getContext());
        raw.setOrientation(LinearLayout.HORIZONTAL);

        //create label
        TextView label = new TextView(getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                100,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        label.setLayoutParams(layoutParams);

        label.setText(attribute.getName() + ": ");

        raw.addView(label);

        //create option
        ChipGroup group = new ChipGroup(getContext());
        group.setSelectionRequired(true);
        group.setSingleSelection(true);
        group.setChipSpacing(10);
        group.setSingleLine(false);

        attribute.getValues().forEach(value ->{
            Chip chip = new Chip(getContext());
            chip.setCheckable(true);
            chip.setChipStrokeWidth(3f);

            if("color".equals(attribute.getType())){
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(value)));
            }else{
                chip.setText(value);
            }

            group.addView(chip);
        });

        raw.addView(group);
        container.addView(raw);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
    }
}