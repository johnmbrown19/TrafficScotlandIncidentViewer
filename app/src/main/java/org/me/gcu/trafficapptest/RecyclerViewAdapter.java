package org.me.gcu.trafficapptest;
// John Brown S1917384

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private Context context;

    private ArrayList<CurrentIncident> incidents = new ArrayList<>();

    public RecyclerViewAdapter(Context context, ArrayList<CurrentIncident> incidents) {
        this.context = context;
        this.incidents = incidents;
    }

    public RecyclerViewAdapter() {

    }

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public RecyclerViewAdapter(MainActivity mainActivity) {
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<CurrentIncident> filterIncidentsList) {
        // below line is to add our filtered
        // list in our course array list.
        incidents = filterIncidentsList;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.txtTitle.setText(incidents.get(i).getTitle());
        holder.txtDesc.setText(incidents.get(i).getDesc());
        holder.txtPubDate.setText(incidents.get(i).getPubDate());
        holder.txtGeorss.setText(incidents.get(i).getGeorss());

        //String location = holder.txtGeorss.getText().toString();

        /*String location = incidents.get(i).getGeorss();
        String[] latLong = location.split("\\s+");

        String latitude = latLong[0];
        String longitude = latLong[1];*/

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), MapsActivity.class);
                //intent.putExtra("title", incidents.get(i).getTitle());
                //intent.putExtra("description", incidents.get(i).getDesc());
                //intent.putExtra("pubDate", incidents.get(i).getPubDate());
                intent.putExtra("location", incidents.get(i).getGeorss());
                v.getContext().startActivity(intent);

                /*String location = incidents.get(i).getGeorss();
                String[] splitStrLocation = location.split("\\s+");

                String latitude = splitStrLocation[0];
                String longitude = splitStrLocation[1];*/
                /*
                //TESTING GOOGLE MAPS
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                intent.putExtra("location", incidents.get(i).getGeorss());
                //intent.putExtra("longitude", longitude);
                v.getContext().startActivity(intent);
                */
            }
        });
    }

    @Override
    public int getItemCount() {
        return incidents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtDesc, txtPubDate, txtGeorss;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDesc = (TextView) itemView.findViewById(R.id.txtDesc);
            txtPubDate = (TextView) itemView.findViewById(R.id.txtPubDate);
            txtGeorss = (TextView) itemView.findViewById(R.id.txtGeorss);

            parent = (CardView) itemView.findViewById(R.id.parent);
        }
    }

    public void setIncidents(ArrayList<CurrentIncident> incidents) {
        this.incidents = incidents;
        notifyDataSetChanged();
    }
}
