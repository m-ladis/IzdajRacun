package hr.ml.izdajracun.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.PropertyViewHolder> {

    private List<RentalPropertyInfo> properties;
    private Context context;

    public PropertiesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.property_item, parent, false);
        final PropertyViewHolder propertyViewHolder = new PropertyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RentalPropertyInfo propertyInfo = properties
                        .get(propertyViewHolder.getAdapterPosition());

                Bundle bundle = new Bundle();
                bundle.putSerializable("property", propertyInfo);

                Navigation.findNavController(parent)
                        .navigate(R.id.action_propertiesFragment_to_propertyDashboard, bundle);
            }
        });

        return propertyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, final int position) {
        RentalPropertyInfo propertyInfo = properties.get(position);

        holder.nameTextView.setText(propertyInfo.getName());
        holder.addressTextView.setText(propertyInfo.getAddress());
        holder.ownerFullNameTextView.setText(String.format(
                "%s %s", propertyInfo.getOwnerFirstName(), propertyInfo.getOwnerLastName()));
    }

    @Override
    public int getItemCount() {
        return properties != null ? properties.size() : 0;
    }

    public void setProperties(List<RentalPropertyInfo> properties) {
        this.properties = properties;
    }

    public class PropertyViewHolder extends RecyclerView.ViewHolder{

        private TextView nameTextView;
        private TextView addressTextView;
        private TextView ownerFullNameTextView;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.property_item_name);
            addressTextView = itemView.findViewById(R.id.property_item_address);
            ownerFullNameTextView = itemView.findViewById(R.id.property_item_owner_full_name);
        }
    }
}
