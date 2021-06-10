package car.hey.persistence;

import car.hey.domain.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface VehiclePersistenceMapper {

    void updateVehicle(Vehicle vehicle, @MappingTarget VehicleEntity entity);

    @Mapping(source = "dealer.id", target = "dealerId")
    Vehicle mapToDomain(VehicleEntity vehicle);

    VehicleEntity mapToEntity(Vehicle vehicle);

}
