package car.hey.service;

import car.hey.exception.CsvParsingException;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static com.opencsv.enums.CSVReaderNullFieldIndicator.EMPTY_SEPARATORS;

@Component
public class CsvParser {

    public <T> List<T> parseCsvAsRecord(final InputStream inputStream, final Class<T> type) throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            var cb = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withFieldAsNull(EMPTY_SEPARATORS)
                    .build();
            return cb.parse();
        } catch (RuntimeException ex) {
            throw new CsvParsingException(ex);
        }
    }

}
