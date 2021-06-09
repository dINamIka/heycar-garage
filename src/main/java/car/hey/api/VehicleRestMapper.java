package car.hey.api;

import car.hey.api.model.CsvVehicleRecord;
import car.hey.domain.Vehicle;
import car.hey.domain.VehicleSearchCriteria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface VehicleRestMapper {

    @Mapping(source = "kW", target = "powerInKW")
    @Mapping(source = "make", target = "manufacturer")
    Vehicle restToDomain(car.hey.api.model.Vehicle vehicle);

    @Mapping(source = "powerInKW", target = "kW")
    @Mapping(source = "manufacturer", target = "make")
    car.hey.api.model.Vehicle domainToRest(Vehicle vehicle);

    Vehicle csvRecordToDomain(CsvVehicleRecord vehicleRecord);

    VehicleSearchCriteria queryToSearchCriteria(String make, String model, Integer year, String color);
}
