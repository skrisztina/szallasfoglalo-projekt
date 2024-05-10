package com.example.szallas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FoglalasAdapter extends RecyclerView.Adapter<FoglalasAdapter.ViewHolder> {

    public interface OnButtonClickListener {
        void onButtonClick(Foglalas foglalas);
    }
    private ArrayList<Foglalas> foglalasok;
    private ArrayList<Foglalas> foglalasokall;
    private Context context;
    private int lastPos = -1;

    private OnButtonClickListener mlistener;

    FoglalasAdapter(Context context, ArrayList<Foglalas> foglalasok){
        this.foglalasok = foglalasok;
        this.foglalasokall = foglalasok;
        this.context = context;
    }
    public void setOnButtonClickListener(FoglalasAdapter.OnButtonClickListener listener) {
        mlistener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_foglalasok, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoglalasAdapter.ViewHolder holder, int position) {
        Foglalas currentFogl = foglalasok.get(position);
        holder.bindTo(currentFogl);

    }

    @Override
    public int getItemCount() {
        return foglalasok.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView foglNev;
        private TextView foglHely;
        private RatingBar foglRating;
        private TextView foglPrice;
        private TextView foglInfo;
        private TextView foglKezd;
        private TextView foglVeg;
        private ImageView foglImage;

        private Button deleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foglNev = itemView.findViewById(R.id.foglalasNev);
            foglHely = itemView.findViewById(R.id.foglalasHely);
            foglRating = itemView.findViewById(R.id.foglalasRating);
            foglPrice=itemView.findViewById(R.id.foglalasPrice);
            foglInfo = itemView.findViewById(R.id.foglalasinfo);
            foglKezd = itemView.findViewById(R.id.foglalasKezd);
            foglVeg = itemView.findViewById(R.id.foglalasVeg);
            foglImage=itemView.findViewById(R.id.foglalasImg);
            deleteBtn = itemView.findViewById(R.id.foglalasDelete);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(mlistener != null && pos != RecyclerView.NO_POSITION){
                        Foglalas clicked = foglalasokall.get(pos);
                        mlistener.onButtonClick(clicked);
                    }
                }
            });
        }

        public void bindTo(Foglalas currentFogl) {
            foglNev.setText(currentFogl.getSzallasName());
            foglHely.setText(currentFogl.getSzallasHely());
            foglPrice.setText(currentFogl.getSzallasPrice());
            foglInfo.setText(currentFogl.getSzallasInfo());
            foglKezd.setText(currentFogl.getKezdoDatum());
            foglVeg.setText(currentFogl.getVegDatum());
            foglRating.setRating(currentFogl.getSzallasRating());
            Glide.with(context).load(currentFogl.getImageRes()).into(foglImage);
        }
    }
}
