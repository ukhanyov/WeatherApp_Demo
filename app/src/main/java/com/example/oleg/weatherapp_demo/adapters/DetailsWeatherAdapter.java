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
import com.example.oleg.weatherapp_demo.utils.Constants;
import com.example.oleg.weatherapp_demo.utils.NormalizeDate;
import com.example.oleg.weatherapp_demo.utils.WeatherIconInterpreter;

import java.util.List;

public class DetailsWeatherAdapter extends RecyclerView.Adapter<DetailsWeatherAdapter.DetailsWeatherViewHolder> {

    private final LayoutInflater mInflater;
    private List<Weather> mWeather; // Cached copy of weather
    private Context mContext;
    private String mKey;

    public DetailsWeatherAdapter(Context context, String key) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mKey = key;
    }


    @NonNull
    @Override
    public DetailsWeatherAdapter.DetailsWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = mInflater.inflate(R.layout.weather_app_list_item, viewGroup, false);
        return new DetailsWeatherAdapter.DetailsWeatherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsWeatherAdapter.DetailsWeatherViewHolder holder, int position) {
        if(mWeather != null){
            // Binding data to a view here

            Weather current = mWeather.get(position);

            // Weather Icon
            holder.weatherIcon.setImageResource(WeatherIconInterpreter.interpretIcon(current.getSummary()));

            //Weather Date
            if(mKey.equals(Constants.KEY_VERTICAL)){
                holder.weatherDate.setText(NormalizeDate.getHumanFriendlyDayOfWeek(Long.parseLong(current.getDate())));
            } else if(mKey.equals(Constants.KEY_HORIZONTAL)){
                holder.weatherDate.setText(NormalizeDate.getHumanFriendlyTimeFromDB(Long.parseLong(current.getDate())));
            }

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

    public void setWeather(List<Weather> weather){
        mWeather = weather;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mWeather != null)
            return mWeather.size();
        else return 0;
    }

    public List<Weather> getAllWeather(){
        if(mWeather != null) return mWeather;
        else return null;
    }

    class DetailsWeatherViewHolder extends RecyclerView.ViewHolder{

        ImageView weatherIcon;
        TextView weatherDate;
        TextView weatherTemperatureHigh;
        TextView weatherTemperatureLow;

        DetailsWeatherViewHolder(View view) {
            super(view);

            weatherIcon = view.findViewById(R.id.weather_icon);
            weatherDate = view.findViewById(R.id.tv_weather_date);
            weatherTemperatureHigh = view.findViewById(R.id.tv_high_temperature);
            weatherTemperatureLow = view.findViewById(R.id.tv_low_temperature);

        }
    }
}
