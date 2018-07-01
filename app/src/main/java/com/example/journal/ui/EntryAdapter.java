package com.example.journal.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.journal.R;
import com.example.journal.core.models.Entry;
import com.example.journal.core.utility.Consumer;
import com.example.journal.databinding.ItemEntryBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder>{

    private List<Entry> _entries = new ArrayList<>();

    private Consumer<Entry> _consumer = value -> {

    };

    public void onEntrySelected(Consumer<Entry> entryConsumer){
        _consumer = entryConsumer;
    }

    public void set_entries(List<Entry> newEntries){
        //Keep a reference to the old List
        List<Entry> oldEntries = this._entries;

        //Update Video Feeds
        this._entries = newEntries;

        //Notify Adapter of changes
        if(oldEntries.size() == 0){
            //If the Feed Videos is empty just notify that all the items are new
            this.notifyDataSetChanged();
        }
        else {
            //Do a diff comparison and update the adapter with the individual changes
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return oldEntries.size();
                }

                @Override
                public int getNewListSize() {
                    return newEntries.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return oldEntries.get(oldItemPosition).entryId == newEntries.get(newItemPosition).entryId;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return oldEntries.get(oldItemPosition).equals(newEntries.get(newItemPosition));
                }
            });
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemEntryBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_entry,parent,false);
        return new ViewHolder(binding, this._consumer);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(this._entries.get(position));
    }

    @Override
    public int getItemCount() {
        return this._entries.size();
    }

    public void removeItem(int position) {
        this._entries.remove(this._entries.get(position));
        this.notifyDataSetChanged();
    }

    public Entry getEntry(int position) {
        return this._entries.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemEntryBinding _binding;
        private final Consumer<Entry> _consumer;

        ViewHolder(ItemEntryBinding binding, Consumer<Entry> consumer) {
            super(binding.getRoot());
            _binding = binding;
            _consumer = consumer;
        }


        void bind(Entry entry) {
            _binding.setEntry(entry);

            if(entry.createdDate != null) {
                SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                _binding.txtDate.setText(format.format(entry.createdDate));
            }
            _binding.getRoot().setOnClickListener(v ->{
                if(_consumer != null) _consumer.accept(entry);
            });
        }
    }
}
