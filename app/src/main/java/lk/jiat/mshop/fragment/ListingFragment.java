package lk.jiat.mshop.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lk.jiat.mshop.R;
import lk.jiat.mshop.adapter.ListingAdapter;
import lk.jiat.mshop.databinding.FragmentListingBinding;
import lk.jiat.mshop.model.Product;

public class ListingFragment extends Fragment {

    private FragmentListingBinding binding;
    private ListingAdapter adapter;
    private String categoryId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerViewListing.setLayoutManager(new GridLayoutManager(getContext(), 2));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        Product p1 = new Product("pid1", "Modern Ceramic Vase", "Minimalist matte white finish, perfect for pampas grass.", 2450, "cat1", Arrays.asList("imageurl1", "imageurl2"), 10, true);
//        Product p2 = new Product("pid2", "Scented Soy Candle Set", "Set of 3 (Lavender, Vanilla, and Sandalwood).", 1850, "cat1", Arrays.asList("imageurl1", "imageurl2"), 20, true);
//        Product p3 = new Product("pid3", "LED Moon Lamp", "3D printed rechargeable lamp with 16 color modes.", 4100, "cat1", Arrays.asList("imageurl1", "imageurl2"), 10, true);
//        Product p4 = new Product("pid4", "Velvet Throw Pillow", "Soft touch, 18x18 inch cover with hidden zipper.", 1200, "cat1", Arrays.asList("imageurl1", "imageurl2"), 10, true);
//        Product p5 = new Product("pid5", "Golden Sunburst Mirror", "Decorative wall mirror for living room accents.", 8900, "cat1", Arrays.asList("imageurl1", "imageurl2"), 10, true);
//        Product p6 = new Product("pid6", "Electric Essential Oil Diffuser", "500ml Ultrasonic humidifier with LED lights.", 5450, "cat1", Arrays.asList("imageurl1", "imageurl2"), 10, true);
//        Product p7 = new Product("pid7", "Abstract Canvas Print", "Large framed contemporary painting (24x36 inch).", 12500, "cat1", Arrays.asList("imageurl1", "imageurl2"), 10, true);
//
//        List<Product> list = List.of(p1, p2, p3, p4, p5, p6, p7);
//
//        WriteBatch batch = db.batch();
//
//        for (Product p : list) {
//            DocumentReference ref = db.collection("products").document();
//            batch.set(ref, p);
//        }
//
//        batch.commit();

        db.collection("products")
                .whereEqualTo("categoryId", categoryId)
                .orderBy("title", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(ds -> {
                    if (!ds.isEmpty()) {
                        List<Product> products = ds.toObjects(Product.class);

                        adapter = new ListingAdapter(products, product -> {

                            Bundle bundle = new Bundle();
                            bundle.putString("productId", product.getProductId());

                            ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                            productDetailsFragment.setArguments(bundle);

                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, productDetailsFragment)
                                    .addToBackStack(null)
                                    .commit();

                        });
                        binding.recyclerViewListing.setAdapter(adapter);
                    }
                }).addOnFailureListener(e -> {
                    Log.e("FirebaseError", "Error: " + e.getMessage());
                });

        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}