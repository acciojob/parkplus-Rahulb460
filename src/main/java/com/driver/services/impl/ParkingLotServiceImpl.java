package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();

        parkingLot.setName(name);
        parkingLot.setAddress(address);

        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = new Spot();
        spot.setPricePerHour(pricePerHour);
        spot.setOccupied(false);
        if(numberOfWheels == 2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        }
        else if(numberOfWheels == 4){
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }
        else {
            spot.setSpotType(SpotType.OTHERS);
        }

        spot.setParkingLot(parkingLot);

        parkingLot.getSpotList().add(spot);


        parkingLotRepository1.save(parkingLot);
        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Spot spot = spotRepository1.findById(spotId).get();
        ParkingLot parkingLot = spot.getParkingLot();
        List<Spot> parkingLotSpotList= parkingLot.getSpotList();
        parkingLotSpotList.remove(spot);
        parkingLot.setSpotList(parkingLotSpotList);

        spotRepository1.delete(spot);
        parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = spotRepository1.findById(spotId).get();
        spot.setPricePerHour(pricePerHour);

        List<Spot> spotList = parkingLot.getSpotList();
        for(Spot spot1 : spotList) {
            if(spot1 == spot) {
                spot1.setPricePerHour(pricePerHour);
            }
        }


        spotRepository1.save(spot);
        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList = parkingLot.getSpotList();

        for(Spot spot : spotList) {
            spotRepository1.delete(spot);
        }

        parkingLotRepository1.delete(parkingLot);
    }
}
