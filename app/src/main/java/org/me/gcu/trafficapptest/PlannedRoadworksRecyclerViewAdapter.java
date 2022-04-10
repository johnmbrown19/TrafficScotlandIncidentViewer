package org.me.gcu.trafficapptest;
// John Brown S1917384

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlannedRoadworksRecyclerViewAdapter extends RecyclerView.Adapter<PlannedRoadworksRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "PlannedRoadworksRecyclerViewAdapter";

    private Context context;

    private ArrayList<PlannedRoadwork> plannedRoadworks = new ArrayList<>();


    public PlannedRoadworksRecyclerViewAdapter(Context context, ArrayList<PlannedRoadwork> plannedRoadworks) {
        this.context = context;
        this.plannedRoadworks = plannedRoadworks;
    }

    public PlannedRoadworksRecyclerViewAdapter() {

    }

    public PlannedRoadworksRecyclerViewAdapter(PlannedRoadworksActivity plannedRoadworksActivity) {
    }

    public PlannedRoadworksRecyclerViewAdapter(MainActivity mainActivity) {
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<PlannedRoadwork> filterPlannedRoadworksList) {
        // below line is to add our filtered
        // list in our course array list.
        plannedRoadworks = filterPlannedRoadworksList;
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

        holder.txtTitle.setText(plannedRoadworks.get(i).getTitle());
        holder.txtDesc.setText(plannedRoadworks.get(i).getDesc());
        holder.txtPubDate.setText(plannedRoadworks.get(i).getPubDate());
        holder.txtGeorss.setText(plannedRoadworks.get(i).getGeorss());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (v.getContext(), MapsActivity.class);
                //intent.putExtra("title", incidents.get(i).getTitle());
                //intent.putExtra("description", incidents.get(i).getDesc());
                //intent.putExtra("pubDate", incidents.get(i).getPubDate());
                intent.putExtra("location", plannedRoadworks.get(i).getGeorss());
                v.getContext().startActivity(intent);

                /*
                Intent  intent = new Intent (context, WebViewActivity.class);
                intent.putExtra("url", plannedRoadworks.get(i).getLink());
                context.startActivity(intent);
                 */
            }
        });
    }

    @Override
    public int getItemCount() {
        return plannedRoadworks.size();
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

    public void setPlannedRoadworks(ArrayList<PlannedRoadwork> plannedRoadworks) {
        this.plannedRoadworks = plannedRoadworks;
        notifyDataSetChanged();
    }
}
