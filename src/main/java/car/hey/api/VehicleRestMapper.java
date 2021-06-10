package car.hey.api;

import car.hey.api.model.CsvVehicleRecord;
import car.hey.domain.Vehicle;
import car.hey.domain.VehicleSearchParameters;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VehicleRestMapper {

    @Mapping(source = "kW", target = "powerInKW")
    Vehicle restToDomain(car.hey.api.model.Vehicle vehicle);

    @Mapping(source = "powerInKW", target = "kW")
    car.hey.api.model.Vehicle domainToRest(Vehicle vehicle);

    Vehicle csvRecordToDomain(CsvVehicleRecord vehicleRecord);

    VehicleSearchParameters queryToSearchParameters(String make, String model, Integer year, String color);
}
