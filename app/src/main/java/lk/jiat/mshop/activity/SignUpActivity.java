package lk.jiat.mshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import lk.jiat.mshop.R;
import lk.jiat.mshop.databinding.ActivitySignUpBinding;
import lk.jiat.mshop.model.User;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        binding.signupBtnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.signupBtnSignup.setOnClickListener(view -> {

            String name = binding.signupInputUsername.getText().toString().trim();
            String email = binding.signupInputEmail.getText().toString().trim();
            String password = binding.signupInputPassword.getText().toString().trim();
            String retypePassword = binding.signupInputRetypePassword.getText().toString().trim();

            if (name.isEmpty()) {
                binding.signupInputUsername.setError("Name is required");
                binding.signupInputUsername.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                binding.signupInputEmail.setError("Email is required");
                binding.signupInputEmail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.signupInputEmail.setError("Enter valid email");
                binding.signupInputEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                binding.signupInputPassword.setError("Password is required");
                binding.signupInputPassword.requestFocus();
                return;
            }

            if (password.length() < 6) {
                binding.signupInputPassword.setError("Password must be at least 6 characters");
                binding.signupInputPassword.requestFocus();
                return;
            }

            if (retypePassword.isEmpty()) {
                binding.signupInputRetypePassword.setError("Retype Password is required");
                binding.signupInputRetypePassword.requestFocus();
                return;
            }

            if (!retypePassword.equals(password)) {
                binding.signupInputRetypePassword.setError("Password and Retype Password must be the same");
                binding.signupInputRetypePassword.requestFocus();
                return;
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        String uid = task.getResult().getUser().getUid();

                        User user = User.builder().uid(uid).name(name).email(email).build();

                        firebaseFirestore.collection("users")
                                .document(uid)
                                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Saved success",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(SignUpActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                }
            });


        });


    }
}