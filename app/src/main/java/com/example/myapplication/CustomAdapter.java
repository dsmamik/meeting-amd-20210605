package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    Context context;
    ArrayList<ContactModel> items;

    // listener untuk menghapus data item karena fungsi hapus tersebut ada di adapter ini
    CustomAdapterListener listener;

    // contructor
    public CustomAdapter(Context context, ArrayList<ContactModel> items){
        this.context = context;
        this.items = items;
    }

    // membuka dialog untuk edit item
    void openCustomDialog(CustomAdapter.ViewHolder holder, int position) {
        String fullName = holder.itemTextViewFullName.getText().toString();
        String phoneNumber = holder.itemTextViewPhoneNumber.getText().toString();
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        CustomDialog customDialog = new CustomDialog(fullName, phoneNumber, position);
        customDialog.show(fragmentManager, "Custom Dialog");
    }

    // ketika mulai membuat view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contact_item, parent, false);

        return new ViewHolder(view);
    }

    // binding view untuk generate data item
    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        holder.itemTextViewId.setText(items.get(position).getId());
        holder.itemTextViewFullName.setText(items.get(position).getFullName());
        holder.itemTextViewPhoneNumber.setText(items.get(position).getPhoneNumber());

        holder.itemButtonUpdate.setOnClickListener(v -> openCustomDialog(holder, position));

        holder.itemButtonDelete.setOnClickListener(v -> listener.removeDataFromCustomAdapter(position));

        try {
            listener = (CustomAdapterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CustomAdapterListener");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // handle item view di recycler view
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemTextViewId, itemTextViewFullName, itemTextViewPhoneNumber;
        Button itemButtonUpdate, itemButtonDelete;
        ConstraintLayout itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTextViewId = itemView.findViewById(R.id.itemTextViewId);
            itemTextViewFullName = itemView.findViewById(R.id.itemTextViewFullName);
            itemTextViewPhoneNumber = itemView.findViewById(R.id.itemTextViewPhoneNumber);
            itemButtonUpdate = itemView.findViewById(R.id.itemButtonUpdate);
            itemButtonDelete = itemView.findViewById(R.id.itemButtonDelete);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }

    // interface untuk listener penghapusan data item
    public interface CustomAdapterListener {
        void removeDataFromCustomAdapter(int position);
    }
}
