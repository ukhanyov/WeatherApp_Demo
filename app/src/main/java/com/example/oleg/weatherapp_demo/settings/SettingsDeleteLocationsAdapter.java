package com.example.oleg.weatherapp_demo.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oleg.weatherapp_demo.R;

import java.util.List;

public class SettingsDeleteLocationsAdapter extends RecyclerView.Adapter<SettingsDeleteLocationsAdapter.SettingsLocationViewHolder> {


    private final LayoutInflater mInflater;
    private List<String> mTitles;
    private List<String> mText;
    private Context mContext;

    SettingsDeleteLocationsAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public SettingsLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.location_delete_list_item, parent, false);
        return new SettingsLocationViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsLocationViewHolder holder, int position) {
        if(mTitles != null && mText != null){
            String currentTitle = mTitles.get(position);
            String currentText = mText.get(position);

            holder.title.setText(currentTitle);
            holder.text.setText(currentText);
        }else {
            throw new IllegalArgumentException("Error binding data in Settings Delete Location");
        }

    }

    void setTitles(List<String> titles){
        mTitles = titles;
        notifyDataSetChanged();
    }

    void setTexts(List<String> texts){
        mText = texts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mTitles != null && mText != null){
            if(mTitles.size() == mText.size())return mTitles.size();
            else return 0;
        }
        else return 0;
    }

    class SettingsLocationViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView text;

        public SettingsLocationViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_location_delete_title);
            text = itemView.findViewById(R.id.tv_location_delete_text);
        }
    }
}
