package car.hey.service;

import car.hey.domain.Vehicle;
import car.hey.domain.VehicleSearchParameters;
import car.hey.exception.DealerNotFoundException;
import car.hey.persistence.DealerRepository;
import car.hey.persistence.VehicleEntity;
import car.hey.persistence.VehiclePersistenceMapper;
import car.hey.persistence.VehicleRepository;
import car.hey.persistence.VehicleSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@Validated
public class VehicleService {

    private final VehiclePersistenceMapper vehicleMapper;
    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;

    public List<Vehicle> searchWithParameters(final VehicleSearchParameters searchParameters) {
        return vehicleRepository.findAll(buildSpecification(searchParameters))
                .stream()
                .map(vehicleMapper::mapToDomain)
                .collect(Collectors.toList());
    }

    private Specification<VehicleEntity> buildSpecification(final VehicleSearchParameters searchParameters) {
        if (searchParameters == null) {
            return null;
        }
        var spec = Specification.where(
                new VehicleSpecification(searchParameters.getParams().pop())
        );
        while (!searchParameters.getParams().isEmpty()) {
            spec.and(new VehicleSpecification(searchParameters.getParams().pop()));
        }
        return spec;
    }

    public void saveVehicles(final Stream<Vehicle> vehicles, int dealerId) {
        var dealer = dealerRepository.findById(dealerId)
                .orElseThrow(
                        () -> new DealerNotFoundException(String.format("Dealer with id: %d doesn't exist!", dealerId))
                );
        vehicles.forEach(vehicle -> {
                    var vehicleEntity = vehicleRepository.findByDealerAndCode(dealer, vehicle.getCode())
                            .map(entity -> updateVehicle(vehicle, entity))
                            .orElseGet(() -> createNewVehicle(dealer, vehicle));
                    vehicleRepository.save(vehicleEntity);
                }
        );
    }

    private VehicleEntity updateVehicle(Vehicle vehicle, VehicleEntity entity) {
        LOG.info("Updating existing vehicle: {}", vehicle);
        vehicleMapper.updateVehicle(vehicle, entity);
        return entity;
    }

    private VehicleEntity createNewVehicle(car.hey.persistence.DealerEntity dealer, Vehicle v) {
        LOG.info("Creating a new vehicle: {}", v);
        var newVehicle = vehicleMapper.mapToEntity(v);
        newVehicle.setDealer(dealer);
        return newVehicle;
    }
}
