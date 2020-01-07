package hr.ml.izdajracun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hr.ml.izdajracun.R;
import hr.ml.izdajracun.model.entity.RentalPropertyInfo;
import hr.ml.izdajracun.view.OnItemClickListener;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.PropertyViewHolder> {

    private List<RentalPropertyInfo> properties;
    private Context context;
    private OnItemClickListener onItemClickedListener;

    public PropertiesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.property_item, parent, false);

        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, final int position) {
        RentalPropertyInfo propertyInfo = properties.get(position);

        holder.nameTextView.setText(propertyInfo.getName());
        holder.addressTextView.setText(propertyInfo.getAddress());
        holder.ownerFullNameTextView
                .setText(propertyInfo.getOwnerFirstName() + " " + propertyInfo.getOwnerLastName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickedListener != null){
                    onItemClickedListener.itemClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return properties != null ? properties.size() : 0;
    }

    public void setProperties(List<RentalPropertyInfo> properties) {
        this.properties = properties;
    }

    public void setOnItemClickedListener(OnItemClickListener listener){
        onItemClickedListener = listener;
    }

    public RentalPropertyInfo getPropertyAtIndex(int index) {
        return properties.get(index);
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
