package com.ankuringale.footballlive.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ankuringale.footballlive.R;
import com.ankuringale.footballlive.databinding.PreviousMatchItemBinding;
import com.ankuringale.footballlive.football.Current;


import java.util.List;

public class PreviousHandler extends RecyclerView.Adapter<PreviousHandler.ViewHolder>{
    private List<Current> current;
    private Context context;

    public PreviousHandler(List<Current> events, Context context) {
        this.current = events;
        this.context = context;
    }

    @NonNull
    @Override
    public PreviousHandler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PreviousMatchItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.previous_match_item,
                parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousHandler.ViewHolder holder, int position) {
        Current hour = current.get((position-10+current.size())%current.size());
        holder.previousMatchBinding.setCurrent(hour);
    }

    @Override
    public int getItemCount() {
        return (current == null)?0:current.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public PreviousMatchItemBinding previousMatchBinding;
        public ViewHolder(PreviousMatchItemBinding layoutBinding) {
            super(layoutBinding.getRoot());
            previousMatchBinding = layoutBinding;
        }
    }
}
