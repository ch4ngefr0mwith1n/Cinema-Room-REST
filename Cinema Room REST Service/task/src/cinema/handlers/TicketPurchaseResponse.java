package cinema.handlers;

import cinema.model.Seat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * nakon poziva "/purchase" endpoint-a, vraća informacije o rezervisanom sjedištu
 */
@Data
@Accessors(chain = true)
public class TicketPurchaseResponse {
    String token;
    @JsonProperty("ticket")
    Seat seat;
}
