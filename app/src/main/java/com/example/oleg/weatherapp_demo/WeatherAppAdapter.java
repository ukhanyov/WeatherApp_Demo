package com.example.oleg.weatherapp_demo;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherAppAdapter extends RecyclerView.Adapter<WeatherAppAdapter.WeatherAppAdapterViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    final private WeatherAppAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface WeatherAppAdapterOnClickHandler {
        void onClick(long date);
    }

    /*
     * Flag to determine if we want to use a separate view for the list item that represents
     * today. This flag will be true when the phone is in portrait mode and false when the phone
     * is in landscape. This flag will be set in the constructor of the adapter by accessing
     * boolean resources.
     */
    private boolean mUseTodayLayout;

    private Cursor mCursor;

    public WeatherAppAdapter(@NonNull Context context, WeatherAppAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mUseTodayLayout = mContext.getResources().getBoolean(R.bool.use_today_layout);
    }

    @Override
    public WeatherAppAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutId;

        switch (viewType) {

            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.weather_app_list_item;
                break;
            }

            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.weather_app_list_item;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);

        view.setFocusable(true);

        return new WeatherAppAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherAppAdapterViewHolder weatherAppAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

//        /****************
//         * Weather Icon *
//         ****************/
//        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
//        int weatherImageId;
//
//        int viewType = getItemViewType(position);
//
//        switch (viewType) {
//
//            case VIEW_TYPE_TODAY:
//                weatherImageId = SunshineWeatherUtils
//                        .getLargeArtResourceIdForWeatherCondition(weatherId);
//                break;
//
//            case VIEW_TYPE_FUTURE_DAY:
//                weatherImageId = SunshineWeatherUtils
//                        .getSmallArtResourceIdForWeatherCondition(weatherId);
//                break;
//
//            default:
//                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
//        }
//
//        weatherAppAdapterViewHolder.iconView.setImageResource(weatherImageId);
//
//        /****************
//         * Weather Date *
//         ****************/
//        /* Read date from the cursor */
//        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
//        /* Get human readable string using our utility method */
//        String dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateInMillis, false);
//
//        /* Display friendly date string */
//        weatherAppAdapterViewHolder.dateView.setText(dateString);
//
//        /***********************
//         * Weather Description *
//         ***********************/
//        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);
//        /* Create the accessibility (a11y) String from the weather description */
//        String descriptionA11y = mContext.getString(R.string.a11y_forecast, description);
//
//        /* Set the text and content description (for accessibility purposes) */
//        weatherAppAdapterViewHolder.descriptionView.setText(description);
//        weatherAppAdapterViewHolder.descriptionView.setContentDescription(descriptionA11y);
//
//        /**************************
//         * High (max) temperature *
//         **************************/
//        /* Read high temperature from the cursor (in degrees celsius) */
//        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
//        /*
//         * If the user's preference for weather is fahrenheit, formatTemperature will convert
//         * the temperature. This method will also append either °C or °F to the temperature
//         * String.
//         */
//        String highString = SunshineWeatherUtils.formatTemperature(mContext, highInCelsius);
//        /* Create the accessibility (a11y) String from the weather description */
//        String highA11y = mContext.getString(R.string.a11y_high_temp, highString);
//
//        /* Set the text and content description (for accessibility purposes) */
//        weatherAppAdapterViewHolder.highTempView.setText(highString);
//        weatherAppAdapterViewHolder.highTempView.setContentDescription(highA11y);
//
//        /*************************
//         * Low (min) temperature *
//         *************************/
//        /* Read low temperature from the cursor (in degrees celsius) */
//        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);
//        /*
//         * If the user's preference for weather is fahrenheit, formatTemperature will convert
//         * the temperature. This method will also append either °C or °F to the temperature
//         * String.
//         */
//        String lowString = SunshineWeatherUtils.formatTemperature(mContext, lowInCelsius);
//        String lowA11y = mContext.getString(R.string.a11y_low_temp, lowString);
//
//        /* Set the text and content description (for accessibility purposes) */
//        weatherAppAdapterViewHolder.lowTempView.setText(lowString);
//        weatherAppAdapterViewHolder.lowTempView.setContentDescription(lowA11y);
    }


    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseTodayLayout && position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class WeatherAppAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView iconView;

        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;

        WeatherAppAdapterViewHolder(View view) {
            super(view);

            iconView = view.findViewById(R.id.weather_icon);
            dateView = view.findViewById(R.id.date);
            descriptionView = view.findViewById(R.id.weather_description);
            highTempView = view.findViewById(R.id.high_temperature);
            lowTempView = view.findViewById(R.id.low_temperature);

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
