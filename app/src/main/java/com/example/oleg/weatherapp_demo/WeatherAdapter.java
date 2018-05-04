package com.example.oleg.weatherapp_demo;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oleg.weatherapp_demo.utils.WeatherDateUtils;
import com.example.oleg.weatherapp_demo.utils.WeatherTempUtils;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherAdapterViewHolder> {

    private final Context mContext;

    final private WeatherAdapterOnClickHandler mClickHandler;


    public interface WeatherAdapterOnClickHandler {
        void onClick(long date);
    }

    private Cursor mCursor;

    public WeatherAdapter(@NonNull Context context, WeatherAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }


    @Override
    public WeatherAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.weather_app_list_item, viewGroup, false);

        view.setFocusable(true);

        return new WeatherAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(WeatherAdapterViewHolder weatherAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);


        /*******************
         * Weather Summary *
         *******************/
        /* Read date from the cursor */
        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);

        /* Get human readable string using our utility method */
        String dateString = WeatherDateUtils.getFriendlyDateString(mContext, dateInMillis, false);

        /* Use the weatherId to obtain the proper description */
        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);

        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);

        /* Read high temperature from the cursor (in degrees celsius) */
        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);

        /* Read low temperature from the cursor (in degrees celsius) */
        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);

        String highAndLowTemperature =
                WeatherTempUtils.formatHighLows(mContext, highInCelsius, lowInCelsius);

        String weatherSummary = dateString + " - " + description + " - " + highAndLowTemperature;

        weatherAdapterViewHolder.weatherSummary.setText(weatherSummary);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }


    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }


    class WeatherAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView weatherSummary;

        WeatherAdapterViewHolder(View view) {
            super(view);

            weatherSummary = view.findViewById(R.id.tv_main_activity);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            mClickHandler.onClick(dateInMillis);
        }
    }
}