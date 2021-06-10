package car.hey.api.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvVehicleRecord {

    @CsvBindByName(column = "code", required = true)
    private String code;
    @CsvBindByName(column = "make/model", required = true)
    @Getter(AccessLevel.NONE)
    private String makeAndModel;
    @CsvBindByName(column = "power-in-ps", required = true)
    private int powerInKW;
    @CsvBindByName(column = "year", required = true)
    private int year;
    @CsvBindByName(column = "color")
    private String color;
    @CsvBindByName(column = "price", required = true)
    private Long price;

    public String getMake() {
        return makeAndModel.substring(0, makeAndModel.indexOf("/"));
    }

    public String getModel() {
        return makeAndModel.substring(makeAndModel.indexOf("/") + 1);
    }
}
