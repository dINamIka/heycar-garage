package car.hey.service;

import car.hey.domain.Vehicle;
import car.hey.domain.VehicleSearchParameters;
import car.hey.persistence.DealerEntity;
import car.hey.persistence.VehicleEntity;
import car.hey.persistence.VehiclePersistenceMapper;
import car.hey.persistence.VehiclePersistenceMapperImpl;
import car.hey.persistence.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Spy
    private VehiclePersistenceMapper vehicleMapper = new VehiclePersistenceMapperImpl();
    @Mock
    private VehicleRepository vehicleRepository;

    @Captor
    private ArgumentCaptor<Specification<VehicleEntity>> searchSpecCaptor;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void shouldReturnSomeVehiclesWhenSearchSpecIsPresent() {
        // given
        var sp = new VehicleSearchParameters();
        sp.setMake("audi");
        sp.setColor("white");

        when(vehicleRepository.findAll(searchSpecCaptor.capture()))
                .thenReturn(validEntities().subList(1, 2));

        // when
        var actualVehicles = vehicleService.searchWithParameters(sp);

        // then
        //todo: need to find out how to properly assert Specification
        assertThat(actualVehicles).containsExactlyElementsOf(validVehicles().subList(1, 2));
    }

    private List<Vehicle> validVehicles() {
        return List.of(
                Vehicle.builder()
                        .dealerId(1)
                        .code("1")
                        .make("mercedes")
                        .model("a 180")
                        .powerInKW(123)
                        .year(2014)
                        .color("black")
                        .price(15950L)
                        .build(),
                Vehicle.builder()
                        .dealerId(1)
                        .code("2")
                        .make("audi")
                        .model("a3")
                        .powerInKW(111)
                        .color("white")
                        .year(2016)
                        .price(17210L)
                        .build()
        );
    }


    private static List<VehicleEntity> validEntities() {
        var dealer = DealerEntity.builder()
                .id(1)
                .name("Sport Autos")
                .build();
        return List.of(
                VehicleEntity.builder()
                        .id(1L)
                        .dealer(dealer)
                        .code("1")
                        .make("mercedes")
                        .model("a 180")
                        .powerInKW(123)
                        .year(2014)
                        .color("black")
                        .price(15950L)
                        .build(),
                VehicleEntity.builder()
                        .id(1L)
                        .dealer(dealer)
                        .code("2")
                        .make("audi")
                        .model("a3")
                        .powerInKW(111)
                        .year(2016)
                        .color("white")
                        .price(17210L)
                        .build()
        );
    }

}