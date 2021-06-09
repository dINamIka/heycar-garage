package car.hey.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class VehicleSearchCriteria {

    private final String make;
    private final String model;
    private final Integer year;
    private final String color;

}
