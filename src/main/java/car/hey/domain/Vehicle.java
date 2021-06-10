package car.hey.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @NotNull
    private Integer dealerId;
    @NotBlank
    private String code;
    @NotBlank
    private String make;
    @NotBlank
    private String model;
    @Positive
    private int powerInKW;
    @Positive
    private int year;
    private String color;
    @PositiveOrZero
    private long price;

}
