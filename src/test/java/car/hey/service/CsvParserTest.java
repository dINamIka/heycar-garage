package car.hey.service;

import car.hey.api.model.CsvVehicleRecord;
import car.hey.exception.CsvParsingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class CsvParserTest {

    private final CsvParser parser = new CsvParser();

    @Test
    void shouldParseValidCsvListing() throws IOException {
        try (var csvIS = this.getClass().getResourceAsStream("/csv/valid_listing.csv")) {
            var vehicleRecords = parser.parseCsvAsRecord(csvIS, CsvVehicleRecord.class);
            assertThat(vehicleRecords).hasSize(2);
            assertThat(vehicleRecords).containsExactlyElementsOf(validCsvRecords());
        }
    }

    public static List<CsvVehicleRecord> validCsvRecords() {
        return List.of(
                CsvVehicleRecord.builder()
                        .code("1")
                        .makeAndModel("mercedes/a 180")
                        .powerInKW(123)
                        .year(2014)
                        .color("black")
                        .price(15950L)
                        .build(),
                CsvVehicleRecord.builder()
                        .code("2")
                        .makeAndModel("audi/a3")
                        .powerInKW(111)
                        .year(2016)
                        .price(17210L)
                        .build()
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("shouldFailParsingForInvalidCsvListingArgs")
    void shouldFailParsingForInvalidCsvListing(final String caseDescription,
                                               final String filePath,
                                               final Class<? extends Exception> expectedException) throws IOException {
        try (var csvIS = this.getClass().getResourceAsStream(filePath)) {
            var exception = catchThrowable(() -> parser.parseCsvAsRecord(csvIS, CsvVehicleRecord.class));
            assertThat(exception).isInstanceOf(expectedException);
        }
    }

    private static Stream<Arguments> shouldFailParsingForInvalidCsvListingArgs() {
        return Stream.of(
                Arguments.of("Missed fields", "/csv/invalid_listing.csv", CsvParsingException.class),
                Arguments.of("Missed header", "/csv/no_header_listing.csv", CsvParsingException.class)
        );
    }
}