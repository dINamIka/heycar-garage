package car.hey.api;

import car.hey.api.model.CsvVehicleRecord;
import car.hey.api.model.Error;
import car.hey.domain.Vehicle;
import car.hey.exception.DealerNotFoundException;
import car.hey.service.CsvParser;
import car.hey.service.VehicleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static car.hey.service.CsvParserTest.validCsvRecords;
import static java.util.Objects.requireNonNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes =
        {VehicleController.class, VehicleExceptionHandler.class, VehicleRestMapperImpl.class}
)
@WebMvcTest
class VehicleControllerTest {

    @MockBean
    private VehicleService serviceMock;
    @MockBean
    private CsvParser parserMock;
    @Captor
    private ArgumentCaptor<Stream<Vehicle>> vehicleStreamCaptor;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSaveVehiclesWhenRequestIsValid() throws Exception {
        // given
        try (var requestIS = this.getClass()
             .getResourceAsStream("/requests/valid_request.json")) {

            // when
            mockMvc.perform(
                    post("/vehicle_listings/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requireNonNull(requestIS).readAllBytes()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            verify(serviceMock).saveVehicles(vehicleStreamCaptor.capture(), eq(1));
            assertThat(vehicleStreamCaptor.getValue())
                    .containsExactlyElementsOf(validVehicleListing().collect(Collectors.toList()));
        }
    }

    @Test
    void shouldReturn404_whenSaveListingInvokedAndIfDealerIsNotFoundAndRequestBodyIsValid()throws Exception {
        // given
        try (var requestIS = this.getClass()
                .getResourceAsStream("/requests/valid_request.json")) {

            doThrow(DealerNotFoundException.class).when(serviceMock).saveVehicles(any(), eq(1));

            // when
            var mvcResult = mockMvc.perform(
                    post("/vehicle_listings/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requireNonNull(requestIS).readAllBytes()))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            // then
            verify(serviceMock).saveVehicles(vehicleStreamCaptor.capture(), eq(1));
            assertThat(vehicleStreamCaptor.getValue())
                    .containsExactlyElementsOf(validVehicleListing().collect(Collectors.toList()));
            var errMsg = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Error.class);
            assertThat(errMsg.getCode()).isEqualTo(4040);
        }
    }

    @Test
    void shouldUploadVehiclesWhenCsvIsValid() throws Exception {
        // given
        try (var csvIS = this.getClass() .getResourceAsStream("/csv/valid_listing.csv");) {
            when(parserMock.parseCsvAsRecord(notNull(), eq(CsvVehicleRecord.class))).thenReturn(validCsvRecords());

            // when
            mockMvc.perform(
                    multipart("/upload_csv/1")
                            .file("csvFile", Objects.requireNonNull(csvIS).readAllBytes())
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            // then
            verify(serviceMock).saveVehicles(vehicleStreamCaptor.capture(), eq(1));
            assertThat(vehicleStreamCaptor.getValue())
                    .containsExactlyElementsOf(validVehicleListing().collect(Collectors.toList()));
        }
    }

    @Test
    void shouldReturn404_whenUploadCsvInvokedAndIfDealerIsNotFoundAndCsvIsValid() throws Exception {
        // given
        try (var csvIS = this.getClass() .getResourceAsStream("/csv/valid_listing.csv");) {
            when(parserMock.parseCsvAsRecord(notNull(), eq(CsvVehicleRecord.class))).thenReturn(validCsvRecords());
            doThrow(DealerNotFoundException.class).when(serviceMock).saveVehicles(any(), eq(1));

            // when
            var mvcResult = mockMvc.perform(
                    multipart("/upload_csv/1")
                            .file("csvFile", requireNonNull(csvIS).readAllBytes())
            )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn();

            // then
            verify(serviceMock).saveVehicles(vehicleStreamCaptor.capture(), eq(1));
            assertThat(vehicleStreamCaptor.getValue())
                    .containsExactlyElementsOf(validVehicleListing().collect(Collectors.toList()));
            var errMsg = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Error.class);
            assertThat(errMsg.getCode()).isEqualTo(4040);
        }
    }
    @Test
    void shouldReturnVehiclesListing() throws Exception {
        // given
        try (var requestIS = this.getClass()
                .getResourceAsStream("/requests/valid_request.json")) {

            when(serviceMock.searchWithParameters(any()))
                    .thenReturn(validVehicleListing().collect(Collectors.toList()));

            // when
            var mvcResult = mockMvc.perform(
                    get("/search"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            // then
            var listing = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
                    new TypeReference<List<car.hey.api.model.Vehicle>>(){});

            assertThat(listing).containsExactlyElementsOf(validRestVehicleListing());
        }
    }

    private static List<car.hey.api.model.Vehicle> validRestVehicleListing() {
        return List.of(
                new car.hey.api.model.Vehicle()
                        .code("1")
                        .make("mercedes")
                        .model("a 180")
                        .kW(123)
                        .year(2014)
                        .color("black")
                        .price(15950),
                new car.hey.api.model.Vehicle()
                        .code("2")
                        .make("audi")
                        .model("a3")
                        .kW(111)
                        .year(2016)
                        .price(17210)
        );
    }

    private static Stream<Vehicle> validVehicleListing() {
        return Stream.of(
                Vehicle.builder()
                        .code("1")
                        .make("mercedes")
                        .model("a 180")
                        .powerInKW(123)
                        .year(2014)
                        .color("black")
                        .price(15950L)
                        .build(),
                Vehicle.builder()
                        .code("2")
                        .make("audi")
                        .model("a3")
                        .powerInKW(111)
                        .year(2016)
                        .price(17210L)
                        .build()
        );
    }
}