package com.example.oleg.weatherapp_demo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oleg.weatherapp_demo.R;
import com.example.oleg.weatherapp_demo.data.entities.Weather;
import com.example.oleg.weatherapp_demo.utils.WeatherIconInterpreter;

import java.util.ArrayList;
import java.util.List;

import static com.example.oleg.weatherapp_demo.utils.NormalizeDate.*;

public class WeatherHorizontalAdapter extends RecyclerView.Adapter<WeatherHorizontalAdapter.WeatherViewHolder> {

    private final LayoutInflater mInflater;
    private List<Weather> mWeather; // Cached copy of weather
    private List<Weather> mWeatherList;
    private Context mContext;

    // Item click stuff
    private final WeatherHorizontalAdapter.WeatherHorizontalAdapterOnClickHandler mClickHandler;

    public WeatherHorizontalAdapter(Context context, WeatherHorizontalAdapterOnClickHandler clickHandler) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        // Item click stuff
        mClickHandler = clickHandler;
    }

    public void setWeatherList(List<Weather> list) {
        mWeatherList = new ArrayList<>();
        mWeatherList.addAll(list);
    }

    public interface WeatherHorizontalAdapterOnClickHandler {
        void onClick(Weather id);
    }


    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.weather_app_horizontal_list_item, parent, false);
        return new WeatherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        if (mWeather != null) {
            // Binding data to a view here

            Weather current = mWeather.get(position);

            // Set icon
            String time = getHumanFriendlyTimeFromDB(Long.parseLong(current.getDate()));
            String sSunrise = null;
            String sSunset = null;
            boolean check = false;
            if (mWeatherList != null && mWeatherList.size() > 0) {
                sSunrise = getTimeWithLocality(Long.parseLong(mWeatherList.get(0).getSunriseTime()), mWeatherList.get(0).getTimezone());
                sSunset = getTimeWithLocality(Long.parseLong(mWeatherList.get(0).getSunsetTime()), mWeatherList.get(0).getTimezone());
                check = true;
            }

            if (checkIfTimeIsNow(Long.parseLong(current.getDate()))) {
                holder.weatherTime.setText(R.string.now);
                holder.weatherIcon.setImageResource(WeatherIconInterpreter.interpretIcon(current.getSummary()));
            } else {

                holder.weatherTime.setText(time);
                holder.weatherIcon.setImageResource(WeatherIconInterpreter.interpretIcon(current.getSummary()));
            }

            if (check) {
                if (time.equals(sSunrise)) {
                    holder.weatherTime.setText(time);
                    holder.weatherIcon.setImageResource(R.drawable.ic_weather_sunrise);
                }

                if (time.equals(sSunset)) {
                    holder.weatherTime.setText(time);
                    holder.weatherIcon.setImageResource(R.drawable.ic_weather_sunset);
                }
            }

            // Set temperature
            holder.weatherTemperature.setText(
                    mContext.getString(R.string.temperature_view_holder_degrees_celsius,
                            String.valueOf(Math.round(Double.parseDouble(current.getTemperatureMax())))));
        } else {
            throw new IllegalArgumentException("Some error with binding data for horyzontal recycler view");
        }
    }

    public void setWeather(List<Weather> weather) {
        mWeather = weather;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mWeather != null) return mWeather.size();
        else return 0;
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView weatherIcon;
        TextView weatherTime;
        TextView weatherTemperature;

        WeatherViewHolder(View itemView) {
            super(itemView);

            weatherIcon = itemView.findViewById(R.id.iv_horizontal_icon);
            weatherTime = itemView.findViewById(R.id.tv_horizontal_time);
            weatherTemperature = itemView.findViewById(R.id.tv_horizontal_temperature);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Weather weather = mWeather.get(adapterPosition);
            mClickHandler.onClick(weather);
        }
    }
}
