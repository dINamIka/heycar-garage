package car.hey.domain;

import lombok.Getter;
import org.springframework.data.util.Pair;

import java.util.Stack;

@Getter
public class VehicleSearchParameters {

    private Stack<Pair<String, String>> params = new Stack<>();

    public void setMake(final String make) {
        params.push(Pair.of("make", make));
    }

    public void setModel(final String model) {
        params.push(Pair.of("model", model));
    }

    public void setYear(final Integer year) {
        params.push(Pair.of("year", String.valueOf(year)));
    }

    public void setColor(final String color) {
        params.push(Pair.of("color", color));
    }

}
