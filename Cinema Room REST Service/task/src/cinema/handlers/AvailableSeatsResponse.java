package cinema.handlers;

import cinema.model.Seat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * pri pozivu "/seats" endpoint-a vraća listu slobodnih sjedišta
 */

@Data
@Accessors(chain = true)
public class AvailableSeatsResponse {
    @JsonProperty("total_rows")
    int rows;
    @JsonProperty("total_columns")
    int columns;
    @JsonProperty("available_seats")
    List<Seat> seatList;
}
