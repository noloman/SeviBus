package com.sloy.sevibus.resources.datasource;

import com.sloy.sevibus.model.ArrivalTime;
import com.sloy.sevibus.resources.exceptions.ServerErrorException;

import java.util.List;

import rx.Observable;

public class ApiLlegadaDataSource implements LlegadaDataSource {

    private final SevibusApi sevibusApi;
    private final LlegadaDataSource fallbackDataSource;

    public ApiLlegadaDataSource(SevibusApi sevibusApi, LlegadaDataSource fallbackDataSource) {
        this.sevibusApi = sevibusApi;
        this.fallbackDataSource = fallbackDataSource;
    }

    @Override
    public Observable<ArrivalTime> getLlegadas(Integer parada, List<String> lineas) throws ServerErrorException {
        return sevibusApi.getArrivals(parada, lineas)
          .retry(1)
          .flatMap(Observable::from)
          .onErrorResumeNext(e -> fallbackDataSource.getLlegadas(parada, lineas));
    }
}
