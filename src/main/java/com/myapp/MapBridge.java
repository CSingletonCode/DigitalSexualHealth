package com.myapp;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MapBridge {
    private final BiConsumer<Double, Double> mapClickHandler;
    private final Consumer<String> clinicClickHandler;

    public MapBridge(BiConsumer<Double, Double> mapClick, Consumer<String> clinicClick) {
        this.mapClickHandler = mapClick;
        this.clinicClickHandler = clinicClick;
    }

    public void onMapClick(double lat, double lng) {
        if (mapClickHandler != null) mapClickHandler.accept(lat, lng);
    }

    public void onClinicClick(String clinicId) {
        if (clinicClickHandler != null) clinicClickHandler.accept(clinicId);
    }
}