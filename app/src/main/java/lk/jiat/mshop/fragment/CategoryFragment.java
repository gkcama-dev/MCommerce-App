package lk.jiat.mshop.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;

import lk.jiat.mshop.R;
import lk.jiat.mshop.adapter.CategoryAdapter;
import lk.jiat.mshop.databinding.FragmentCategoryBinding;
import lk.jiat.mshop.model.Category;


public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;

    private CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerViewCategories.setLayoutManager(new GridLayoutManager(getContext(), 3));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        Category c1 = new Category("cat1","Home Decoration","");
//        Category c2 = new Category("cat2","Pet Supplies","");
//        Category c3 = new Category("cat3","Home & Garden","");
//        Category c4 = new Category("cat4","Health & Medical","");
//        Category c5 = new Category("cat5","Sports & Outdoor","");
//        Category c6 = new Category("cat6","Automobiles","");
//        Category c7 = new Category("cat7","Jewellery & Watches","");
//        Category c8 = new Category("cat8","Kids & Toys","");
//
//        List<Category> cats = List.of(c1,c2,c3,c4,c5,c6,c7,c8);
//
//        WriteBatch batch = db.batch();
//
//        for(Category c : cats){
//            DocumentReference ref = db.collection("categories").document();
//            batch.set(ref,c);
//        }
//
//        batch.commit();

//        db.collection("categories").add()

        db.collection("categories").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot result = task.getResult();

//                        List<Category> categories = result.toObjects(Category.class);

                        List<Category> categories = task.getResult().toObjects(Category.class);
                        adapter = new CategoryAdapter(categories, new CategoryAdapter.OnCategoryClickListener() {
                            @Override
                            public void onCategoryClick(Category category) {

                               Bundle bundle = new Bundle();
                               bundle.putString("categoryId", category.getCategoryId());

                               ListingFragment fragment = new ListingFragment();
                               fragment.setArguments(bundle);

                               getParentFragmentManager().beginTransaction()
                                       .replace(R.id.fragment_container,fragment)
                                       .addToBackStack(null)
                                       .commit();

                            }
                        });
                        binding.recyclerViewCategories.setAdapter(adapter);
                    }
                });


    }
}