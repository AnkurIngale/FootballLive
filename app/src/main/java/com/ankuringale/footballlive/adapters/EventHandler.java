package com.ankuringale.footballlive.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ankuringale.footballlive.R;
import com.ankuringale.footballlive.databinding.MyEventItemBinding;
import com.ankuringale.footballlive.football.Event;

import java.util.List;


public class EventHandler extends RecyclerView.Adapter<EventHandler.ViewHolder> {
    private List<Event> events;
    private Context context;

    public EventHandler(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public EventHandler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyEventItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.my_event_item,
                parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event hour = events.get(position);
        holder.hourlyListItemBinding.setEvent(hour);
    }

    @Override
    public int getItemCount() {
        return (events == null)?0:events.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public MyEventItemBinding hourlyListItemBinding;
        public ViewHolder(MyEventItemBinding layoutBinding) {
            super(layoutBinding.getRoot());
            hourlyListItemBinding = layoutBinding;
        }
    }
}
