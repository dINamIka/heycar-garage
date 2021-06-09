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
        var searchCriteria = mapper.queryToSearchCriteria(make, model, year, color);
        var vehicles = service.findByCriteria(searchCriteria);
        return ResponseEntity.ok(
                vehicles.stream()
                        .map(mapper::domainToRest)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<Void> saveVehicles(Integer dealerId, @Valid List<Vehicle> vehicles) {
        var vehiclesToSave = vehicles.stream()
                .map(mapper::restToDomain)
                .collect(Collectors.toList());
        service.saveVehicles(vehiclesToSave);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<Void> uploadVehicles(Integer dealerId,
                                               @Valid MultipartFile upload) {
        try (var inputStream = upload.getInputStream()) {
            var vehicleRecords = csvParser.parseCsvAsRecord(inputStream, CsvVehicleRecord.class);
            var vehiclesToSave = vehicleRecords.stream()
                    .map(mapper::csvRecordToDomain)
                    .collect(Collectors.toList());
            LOG.info("CSV: {}", vehiclesToSave);
            service.saveVehicles(vehiclesToSave);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
