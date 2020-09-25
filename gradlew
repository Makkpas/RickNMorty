package cr.ac.ucr.apiconsumer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cr.ac.ucr.apiconsumer.api.RetrofitBuilder;
import cr.ac.ucr.apiconsumer.api.WeatherService;
import cr.ac.ucr.apiconsumer.models.Main;
import cr.ac.ucr.apiconsumer.models.Sys;
import cr.ac.ucr.apiconsumer.models.Weather;
import cr.ac.ucr.apiconsumer.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private  final int LOCATION_CODE_REQUEST = 1;
    private TextView tvGreetings;
    private TextView tvTemperature;
    private TextView tvDescription;
    private TextView tvMinMax;
    private TextView tvCity;

    private ImageView ivImage;

    private ConstraintLayout clContainer;

    private String day;
    private LocationManager locationManager;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        double latitude = 8.7422451;
        double longitude = -82.9468766;


        clContainer = findViewById(R.id.cl_container);

        tvGreetings = findViewById(R.id.tv_greeting);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvDescription = findViewById(R.id.tv_description);
        tvMinMax = findViewById(R.id.tv_MinMax);
        tvCity = findViewById(R.id.tv_city);

        ivImage = findViewById(R.id.iv_image);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        checkPermission();

        setBackgroundAndGreeting();
        getWeather(latitude, longitude);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_CODE_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            } else {
                //TODO: no dieron permiso
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    },
                        LOCATION_CODE_REQUEST
                );
                return;
            }
        }
    }

    private  void getLocation(){
        
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    public void setBackgroundAndGreeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 5 && timeOfDay <12){
            tvGreetings.setText(R.string.day);
            clContainer.setBackgroundResource(R.drawable.background_day);
        }else if(timeOfDay > 12  && timeOfDay <19){
            tvGreetings.setText(R.string.afternoon);
            clContainer.setBackgroundResource(R.drawable.background_afternoon);
        }else{
            tvGreetings.setText(R.string.night);
            clContainer.setBackgroundResource(R.drawable.background_night);
        }
        day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

    }

    private void getWeather(double latitude, double longitude) {
        WeatherService service = RetrofitBuilder.createService(WeatherService.class);

        Call<WeatherResponse> response = service.getWeatherByCoordinates(latitude, longitude);

        final AppCompatActivity activity = this;

        response.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {

                Log.i(TAG, String.valueOf(call.request().url()));

                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();

                    Main main = weatherResponse.getMain();
                    List<Weather> weatherList = weatherResponse.getWeather();
                    Sys sys = w