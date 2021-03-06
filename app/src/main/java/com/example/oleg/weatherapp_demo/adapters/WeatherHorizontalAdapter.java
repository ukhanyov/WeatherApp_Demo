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

import static com.example.oleg.weatherapp_demo.utils.Constants.*;
import static com.example.oleg.weatherapp_demo.utils.NormalizeDate.*;

public class WeatherHorizontalAdapter extends RecyclerView.Adapter<WeatherHorizontalAdapter.WeatherHorizontalViewHolder> {

    private final LayoutInflater mInflater;
    private List<Weather> mWeather; // Cached copy of weather
    private List<Weather> mWeatherList;
    private Context mContext;
    private String mSunrise = null;
    private String mSunset = null;

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
        mSunrise = getTimeWithLocality(Long.parseLong(mWeatherList.get(0).getSunriseTime()), mWeatherList.get(0).getTimezone());
        mSunset = getTimeWithLocality(Long.parseLong(mWeatherList.get(0).getSunsetTime()), mWeatherList.get(0).getTimezone());
    }

    public interface WeatherHorizontalAdapterOnClickHandler {
        void onClick(List<Weather> weatherList, String key);
    }


    @NonNull
    @Override
    public WeatherHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.weather_app_horizontal_list_item, parent, false);
        return new WeatherHorizontalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHorizontalViewHolder holder, int position) {
        if (mWeather != null) {

            Weather current = mWeather.get(position);

            // Set icon
            String time = getHumanFriendlyTimeFromDB(Long.parseLong(current.getDate()));

            if (checkIfTimeIsNow(Long.parseLong(current.getDate()))) {
                holder.weatherTime.setText(R.string.now);
                holder.weatherIcon.setImageResource(WeatherIconInterpreter.interpretIcon(current.getSummary()));
            } else {
                holder.weatherTime.setText(time);
                holder.weatherIcon.setImageResource(WeatherIconInterpreter.interpretIcon(current.getSummary()));
            }

            if (time.equals(mSunrise)) {
                holder.weatherTime.setText(mContext.getString(R.string.sunrise));
                holder.weatherIcon.setImageResource(R.drawable.ic_weather_sunrise);
            }

            if (time.equals(mSunset)) {
                holder.weatherTime.setText(mContext.getString(R.string.sunset));
                holder.weatherIcon.setImageResource(R.drawable.ic_weather_sunset);
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

    public List<Weather> getAllWeather() {
        return mWeather;
    }

    public class WeatherHorizontalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView weatherIcon;
        TextView weatherTime;
        TextView weatherTemperature;

        WeatherHorizontalViewHolder(View itemView) {
            super(itemView);

            weatherIcon = itemView.findViewById(R.id.iv_horizontal_icon);
            weatherTime = itemView.findViewById(R.id.tv_horizontal_time);
            weatherTemperature = itemView.findViewById(R.id.tv_horizontal_temperature);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAllWeather(), KEY_HORIZONTAL);
        }
    }
}
