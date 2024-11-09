// File: ResourcesAdapter.java
package com.example.bookme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Map;

public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesAdapter.ResourceViewHolder> {

    private ArrayList<Map<String, String>> resourceList;

    public ResourcesAdapter(ArrayList<Map<String, String>> resourceList) {
        this.resourceList = resourceList;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resource_admin, parent, false);
        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceViewHolder holder, int position) {
        Map<String, String> resource = resourceList.get(position);
        holder.textViewResourceID.setText("ID: " + resource.get("resourcesID"));
        holder.textViewResourceType.setText("Type: " + resource.get("resourcesType"));
        holder.textViewResourceName.setText("Name: " + resource.get("resourcesName"));
        holder.textViewAvailability.setText("Availability: " + resource.get("availability"));
    }

    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    public static class ResourceViewHolder extends RecyclerView.ViewHolder {
        TextView textViewResourceID, textViewResourceType, textViewResourceName, textViewAvailability;

        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewResourceID = itemView.findViewById(R.id.textViewResourceID);
            textViewResourceType = itemView.findViewById(R.id.textViewResourceType);
            textViewResourceName = itemView.findViewById(R.id.textViewResourceName);
            textViewAvailability = itemView.findViewById(R.id.textViewAvailability);
        }
    }


}
