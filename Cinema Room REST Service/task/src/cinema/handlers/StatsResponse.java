package cinema.handlers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * nakon poziva "/stats" endpoint-a vraća informacije o kinu
 */
@Data
@Accessors(chain = true)
public class StatsResponse {
    @JsonProperty("current_income")
    int currentIncome;
    @JsonProperty("number_of_available_seats")
    int avaliableSeats;
    @JsonProperty("number_of_purchased_tickets")
    int purchasedTickets;
}
