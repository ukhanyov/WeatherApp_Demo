package com.example.oleg.weatherapp_demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oleg.weatherapp_demo.data.entities.Weather;
import com.example.oleg.weatherapp_demo.utils.NormalizeDate;
import com.example.oleg.weatherapp_demo.utils.WeatherIconInterpreter;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private final LayoutInflater mInflater;
    private List<Weather> mWeather; // Cached copy of weather
    private Context mContext;

    // Item click stuff
    final private WeatherAdapterOnClickHandler mClickHandler;

    WeatherAdapter(Context context, WeatherAdapterOnClickHandler clickHandler) {
        mInflater = LayoutInflater.from(context);
        mContext = context;

        // Item click stuff
        mClickHandler = clickHandler;
    }

    public interface WeatherAdapterOnClickHandler {
        void onClick(Weather id);
    }

    @NonNull
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

        // Weather Icon
        holder.weatherIcon.setImageResource(WeatherIconInterpreter.interpretIcon(current.getSummary()));

        //Weather Date
        holder.weatherDate.setText(NormalizeDate.getHumanFriendlyDateFromDB(Long.parseLong(current.getDate())));

        // Weather Summary
        holder.weatherSummary.setText(WeatherIconInterpreter.interpretDescription(current.getSummary()));

        // High (max) temperature
        holder.weatherTemperatureHigh.setText(
                        mContext.getString(R.string.temperature_view_holder_degrees_celsius,
                        String.valueOf(Math.round(Double.parseDouble(current.getTemperatureMax())))));

         // Low (min) temperature
        holder.weatherTemperatureLow.setText(
                        mContext.getString(R.string.temperature_view_holder_degrees_celsius,
                        String.valueOf(Math.round(Double.parseDouble(current.getTemperatureMin())))));
        } else {
            throw new IllegalArgumentException("Some error with binding data for vertical recycler view");
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
            int adapterPosition = getAdapterPosition();
            Weather weather = mWeather.get(adapterPosition);
            mClickHandler.onClick(weather);
        }
    }

}