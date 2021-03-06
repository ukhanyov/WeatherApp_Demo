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
import com.example.oleg.weatherapp_demo.utils.NormalizeDate;
import com.example.oleg.weatherapp_demo.utils.WeatherIconInterpreter;

import java.util.List;

import static com.example.oleg.weatherapp_demo.utils.Constants.KEY_VERTICAL;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private final LayoutInflater mInflater;
    private List<Weather> mWeather; // Cached copy of weather
    private Context mContext;

    // Item click stuff
    final private WeatherAdapterOnClickHandler mClickHandler;

    public WeatherAdapter(Context context, WeatherAdapterOnClickHandler clickHandler) {
        mInflater = LayoutInflater.from(context);
        mContext = context;

        // Item click stuff
        mClickHandler = clickHandler;
    }

    public interface WeatherAdapterOnClickHandler {
        void onClick(List<Weather> weatherList, String key);
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
        holder.weatherDate.setText(NormalizeDate.getHumanFriendlyDayOfWeek(Long.parseLong(current.getDate())));

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

    class WeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView weatherIcon;
        TextView weatherDate;
        TextView weatherTemperatureHigh;
        TextView weatherTemperatureLow;

        WeatherViewHolder(View view) {
            super(view);

            weatherIcon = view.findViewById(R.id.weather_icon);
            weatherDate = view.findViewById(R.id.tv_weather_date);
            weatherTemperatureHigh = view.findViewById(R.id.tv_high_temperature);
            weatherTemperatureLow = view.findViewById(R.id.tv_low_temperature);

            view.setOnClickListener(this);
        }

        // Item click stuff
        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAllWeather(), KEY_VERTICAL);
        }
    }

}