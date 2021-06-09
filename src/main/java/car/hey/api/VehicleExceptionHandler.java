package car.hey.api;

import car.hey.api.model.Error;
import car.hey.exception.CsvParsingException;
import car.hey.exception.DealerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class VehicleExceptionHandler {

    @ExceptionHandler(CsvParsingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleCsvParsingException(CsvParsingException ex) {
        return new Error().code(4001).message(ex.getMessage());
    }

    @ExceptionHandler(DealerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handleDealerNotFoundException(DealerNotFoundException ex) {
        return new Error().code(4040).message(ex.getMessage());
    }
}
