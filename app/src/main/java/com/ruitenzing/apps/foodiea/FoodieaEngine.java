package com.ruitenzing.apps.foodiea;

import android.location.Location;
import android.util.Log;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by tenzing on 3/13/16.
 */
public class FoodieaEngine {
    private final PriceLevel priceLevel;
    private final Location location;

    public static enum PriceLevel {
        POBOY("cheap"),
        DINER("affordable"),
        COUTEAUX("fancy");
        private String price;
        PriceLevel(String p){
            price = p;
        }
        String showPrice(){
            return price;
        }

    }

    public FoodieaEngine(PriceLevel priceLevel, Location location) {
        this.priceLevel = priceLevel;
        this.location = location;
    }

    public FoodieaResult search() throws IOException {
        YelpAPIFactory apiFactory = YelpAPIFactoryProducer.getAPIFactory();

        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();

        params.put("term", priceLevel.showPrice()+" "+ Constants.DINNER_SEARCH);
        params.put("limit", "20");
        params.put("sort", "2");
        params.put("radius_filter", "5000");

        CoordinateOptions coordinateOptions = CoordinateOptions.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();

        Call<SearchResponse> call = yelpAPI.search(coordinateOptions, params);

        Response<SearchResponse> response = call.execute();

        List<Business> businesses = response.body().businesses();

        List<Business> filteredBusinesses = filterBusinesses(businesses);

        Random random = new Random(System.currentTimeMillis());
        int i = random.nextInt(filteredBusinesses.size());

        Business chosen = filteredBusinesses.get(i);

        return new FoodieaResult(chosen.name(), chosen.rating(), chosen.location().displayAddress().get(0));
    }

    private List<Business> filterBusinesses(List<Business> businesses) {
        List<Business> filteredBusinesses = new ArrayList<>();
        for (Business b : businesses) {
            if (!b.isClosed() && b.rating() >= 3.5) {
                filteredBusinesses.add(b);
                Log.d(Constants.TAG, b.name());
            }
        }
        return filteredBusinesses;
    }
}
