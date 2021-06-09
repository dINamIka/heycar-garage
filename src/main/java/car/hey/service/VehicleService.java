package car.hey.service;

import car.hey.domain.Vehicle;
import car.hey.domain.VehicleSearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {

    public List<Vehicle> findByCriteria(final VehicleSearchCriteria criteria) {
        return null;
    }

    public void saveVehicles(final List<Vehicle> vehicles) {
        return;
    }
}
