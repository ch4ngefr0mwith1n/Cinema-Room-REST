package cinema.handlers;

import lombok.Data;

@Data
public class TicketPurchaseRequest {
    int row;
    int column;
}
