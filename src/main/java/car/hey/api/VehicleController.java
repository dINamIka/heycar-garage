package car.hey.api;

import car.hey.api.model.CsvVehicleRecord;
import car.hey.api.model.Vehicle;
import car.hey.service.CsvParser;
import car.hey.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VehicleController implements VehiclesApi {

    private final VehicleService service;
    private final VehicleRestMapper mapper;
    private final CsvParser csvParser;

    @Override
    public ResponseEntity<List<Vehicle>> listVehicles(@Valid String make,
                                                      @Valid String model,
                                                      @Valid Integer year,
                                                      @Valid String color) {
        var searchParameters = mapper.queryToSearchParameters(make, model, year, color);
        var vehicles = service.searchWithParameters(searchParameters);
        return ResponseEntity.ok(
                vehicles.stream()
                        .map(mapper::domainToRest)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<Void> saveVehicles(Integer dealerId, @Valid List<Vehicle> vehicles) {
        var vehiclesToSave = vehicles.stream()
                .map(mapper::restToDomain);
        service.saveVehicles(vehiclesToSave, dealerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SneakyThrows(IOException.class)
    @Override
    public ResponseEntity<Void> uploadVehicles(Integer dealerId,
                                               @Valid MultipartFile upload) {
        try (var inputStream = upload.getInputStream()) {
            var vehicleRecords = csvParser.parseCsvAsRecord(inputStream, CsvVehicleRecord.class);
            var vehiclesToSave = vehicleRecords.stream()
                    .map(mapper::csvRecordToDomain);
            service.saveVehicles(vehiclesToSave, dealerId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
