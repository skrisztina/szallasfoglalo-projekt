package com.example.szallas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SzallasItemAdapter extends RecyclerView.Adapter<SzallasItemAdapter.ViewHolder> implements Filterable {

    public interface OnButtonClickListener {
        void onButtonClick(SzallasItem szallas);
    }

    private static final String LOG_TAG = SzallasItemAdapter.class.getName();
    private ArrayList<SzallasItem> szallasokdata;
    private ArrayList<SzallasItem> szallasokdataall;
    private Context context;
    private int lastpos = -1;

    private OnButtonClickListener mlistener;


    SzallasItemAdapter(Context context, ArrayList<SzallasItem> szallasok){
        this.szallasokdata = szallasok;
        this.szallasokdataall = szallasok;
        this.context = context;
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mlistener = listener;
    }

    @NonNull
    @Override
    public SzallasItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_szallas, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SzallasItemAdapter.ViewHolder holder, int position) {
        SzallasItem current = szallasokdata.get(position);
        holder.bindTo(current);

        if(holder.getAdapterPosition() > lastpos){
            Animation fade_in = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            holder.itemView.startAnimation(fade_in);
            lastpos = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return szallasokdata.size();
    }

    @Override
    public Filter getFilter() {
        return szallasFilter;
    }

    private Filter szallasFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SzallasItem> filtered = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0){
                results.count = szallasokdataall.size();
                results.values = szallasokdataall;
            } else {
                String filter = constraint.toString().toLowerCase().trim();
                for(SzallasItem szallas : szallasokdataall){
                    if(szallas.getPlace().toLowerCase().trim().contains(filter)){
                        Log.i(LOG_TAG, "Vizsgált szállás: " + szallas.getName());
                        filtered.add(szallas);
                        Log.i(LOG_TAG, "Most adta hozzá!!");
                    }
                }
                results.count = filtered.size();
                results.values = filtered;

                for(SzallasItem szurt: filtered){
                    Log.i(LOG_TAG, "Filterben lévő szállás: " + szurt.getName());
                }
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            szallasokdata = (ArrayList<SzallasItem>) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView szallasTitle;
        private TextView szallasPlace;
        private TextView szallasPrice;
        private TextView szallasInfo;
        private ImageView szallasImage;
        private RatingBar szallasRate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            szallasTitle = itemView.findViewById(R.id.szallasTitle);
            szallasPlace = itemView.findViewById(R.id.szallasPlace);
            szallasPrice = itemView.findViewById(R.id.szallasPrice);
            szallasInfo = itemView.findViewById(R.id.szallasInfo);
            szallasImage = itemView.findViewById(R.id.szallasImage);
            szallasRate = itemView.findViewById(R.id.szallasRating);

            itemView.findViewById(R.id.szallasBtn).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(mlistener != null && pos != RecyclerView.NO_POSITION){
                        SzallasItem clicked = szallasokdataall.get(pos);
                        mlistener.onButtonClick(clicked);
                        Log.i(LOG_TAG, "Részletek gomb megnyomva." + clicked.getName());
                    }
                }
            });
        }

        public void bindTo(SzallasItem current) {
            szallasTitle.setText(current.getName());
            szallasPlace.setText(current.getPlace());
            szallasPrice.setText(current.getPrice());
            szallasInfo.setText(current.getInfo());
            szallasRate.setRating(current.getRating());

            Glide.with(context).load(current.getImageRes()).into(szallasImage);

        }

    }

}
