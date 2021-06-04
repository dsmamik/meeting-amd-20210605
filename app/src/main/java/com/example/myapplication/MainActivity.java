package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CustomDialog.CustomDialogListener, CustomAdapter.CustomAdapterListener {

    // nama koleksi yang akan didemokan
    final String collectionName = "contacts";

    EditText mainFullName, mainPhoneNumber;
    RecyclerView mainRecyclerView;
    Button mainButtonSave;

    // list data yang akan didemokan
    ArrayList<ContactModel> items = new ArrayList<>();

    CustomAdapter customAdapter;

    // instance firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFullName = findViewById(R.id.mainEditTextFullName);
        mainPhoneNumber = findViewById(R.id.mainEditTextPhoneNumber);
        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        mainButtonSave = findViewById(R.id.mainButtonSave);

        initData();

        customAdapter = new CustomAdapter(this, items);
        mainRecyclerView.setAdapter(customAdapter);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainButtonSave.setOnClickListener(v -> addData());
    }

    // inisialisasi data untuk pertama kali
    void initData() {
        readData();
    }

    // membaca data dari firestore
    void readData() {
        db.collection(collectionName).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                items.clear();
                if(task.getResult() != null) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ContactModel contactModel = new ContactModel(document.getId(), document.getString("fullName"), document.getString("phoneNumber"));
                        items.add(contactModel);
                    }
                    customAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(MainActivity.this, "ERROR getting data", Toast.LENGTH_LONG).show();
            }
        });
    }

    // menambahkan data ke firestore
    void addData() {
        ContactModel tempContactModel = new ContactModel(null, mainFullName.getText().toString(), mainPhoneNumber.getText().toString());

        db.collection(collectionName).add(tempContactModel).addOnSuccessListener(documentReference -> {
            mainFullName.setText("");
            mainPhoneNumber.setText("");
            readData();
        }).addOnFailureListener(e -> Toast.makeText(this, "Cannot save the data", Toast.LENGTH_LONG).show());
    }

    // ubah data item, sumber: dari CustomDialog ketika klik Save
    @Override
    public void setDataFromCustomDialog(String fullName, String phoneNumber, int position) {
        ContactModel contactModel = items.get(position);
        contactModel.setFullName(fullName);
        contactModel.setPhoneNumber(phoneNumber);
        db.collection(collectionName).document(contactModel.getId()).set(contactModel)
                .addOnSuccessListener(unused -> readData()).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "ERROR update the data", Toast.LENGTH_LONG).show());
    }

    // hapus data item, sumber: dari CustomAdapter ketika klik Delete
    @Override
    public void removeDataFromCustomAdapter(int position) {
        ContactModel contactModel = items.get(position);
        db.collection(collectionName).document(contactModel.getId()).delete()
                .addOnSuccessListener(unused -> readData()).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "ERROR remove the data", Toast.LENGTH_LONG).show());
    }
}