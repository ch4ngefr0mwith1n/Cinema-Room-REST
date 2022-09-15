package cinema.handlers;

import cinema.model.Seat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * vraća informacije o povratu karte prilikom poziva "/return" endpoint-a
 */
@Data
@Accessors(chain = true)
public class TicketReturnResponse {
    @JsonProperty("returned_ticket")
    Seat seat;
}
