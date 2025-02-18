package tn.esprit.interfaces;

import tn.esprit.models.Station;

import java.util.List;
public interface IStationService {
    void addStation(Station station);
    void updateStation(Station station);
    void deleteStation(int id);
    Station findById(int id);
    List<Station> findAll();
}
