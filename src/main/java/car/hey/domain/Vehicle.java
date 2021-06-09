package car.hey.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Vehicle {
    private int dealerId;
    private String code;
    private String manufacturer;
    private String model;
    private int powerInKW;
    private int year;
    private String color;
    private Long price;
    private Instant creationTime;
    private Instant lastUpdate;
}
