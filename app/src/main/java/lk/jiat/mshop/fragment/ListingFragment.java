package lk.jiat.mshop.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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

       db.collection("product")
               .whereEqualTo("categoryId", categoryId)
               .orderBy("title", Query.Direction.ASCENDING)
               .get()
               .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot ds) {
                       if(!ds.isEmpty()){
                           List<Product> products = ds.toObjects(Product.class);

                           adapter = new ListingAdapter(products, new ListingAdapter.OnListingItemClickListener() {
                               @Override
                               public void onListingItemClick(Product product) {

                               }
                           });

                           binding.recyclerViewListing.setAdapter(adapter);

                       }
                   }
               });

    }
}