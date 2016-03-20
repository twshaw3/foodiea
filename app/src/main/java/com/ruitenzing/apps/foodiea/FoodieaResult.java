package com.ruitenzing.apps.foodiea;

import java.io.Serializable;

public class FoodieaResult implements Serializable {
    public final String name;
    public final Double rating;
    public final String address;

    public FoodieaResult(String name, Double rating, String address) {
        this.name = name;
        this.rating = rating;
        this.address = address;
    }

}
