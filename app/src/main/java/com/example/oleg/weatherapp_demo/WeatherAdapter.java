package com.example.oleg.weatherapp_demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oleg.weatherapp_demo.data.Weather;
import com.example.oleg.weatherapp_demo.utils.NormalizeDate;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    class WeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView weatherIcon;
        TextView weatherDate;
        TextView weatherSummary;
        TextView weatherTemperatureHigh;
        TextView weatherTemperatureLow;

        WeatherViewHolder(View view) {
            super(view);

            weatherIcon = view.findViewById(R.id.weather_icon);
            weatherDate = view.findViewById(R.id.tv_weather_date);
            weatherSummary = view.findViewById(R.id.tv_weather_summary);
            weatherTemperatureHigh = view.findViewById(R.id.tv_high_temperature);
            weatherTemperatureLow = view.findViewById(R.id.tv_low_temperature);

            view.setOnClickListener(this);
        }

        // Item click stuff
        @Override
        public void onClick(View v) {
            //int adapterPosition = getAdapterPosition();
            //mCursor.moveToPosition(adapterPosition);
            //long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_COLUMN_DATE);
            //mClickHandler.onClick(dateInMillis);
        }
    }

    private final LayoutInflater mInflater;
    private List<Weather> mWeather; // Cached copy of weather

    public WeatherAdapter(Context context) {
        mInflater =LayoutInflater.from(context);
    }

    public interface WeatherAdapterOnClickHandler {
        void onClick(long date);
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = mInflater.inflate(R.layout.weather_app_list_item, viewGroup, false);
        return new WeatherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        if(mWeather != null){
        // Binding data to a view here

        Weather current = mWeather.get(position);

        /******************
        //* Weather Icon *
        //****************/
        //implement getting icon, a dummy for now
        holder.weatherIcon.setImageResource(R.drawable.ic_brightness_medium_black_48dp);

        /****************
         * Weather Date *
         ****************/

        holder.weatherDate.setText(NormalizeDate.getHumanFriendlyDate(Long.getLong(current.getDate())));

        /*******************
         * Weather Summary *
         *******************/
        holder.weatherSummary.setText(current.getSummary());

        /**************************
         * High (max) temperature *
         **************************/
        holder.weatherTemperatureHigh.setText(String.valueOf(
                Math.round(Double.valueOf(current.getTemperatureMax()))) + "\u00b0");

        /*************************
         * Low (min) temperature *
         *************************/
        holder.weatherTemperatureLow.setText(String.valueOf(
                Math.round(Double.valueOf(current.getTemperatureMin())))  + "\u00b0");

        } else {
            throw new IllegalArgumentException("Some error with binding data");
        }
    }

    void setWeather(List<Weather> weather){
        mWeather = weather;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mWeather != null)
            return mWeather.size();
        else return 0;
    }

}