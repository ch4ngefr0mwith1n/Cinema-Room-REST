package cinema.service;

import cinema.exceptions.BadRequestException;
import cinema.exceptions.UnauthorizedException;
import cinema.handlers.AvailableSeatsResponse;
import cinema.handlers.StatsResponse;
import cinema.handlers.TicketPurchaseResponse;
import cinema.handlers.TicketReturnResponse;
import cinema.model.Seat;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ovu klasu koristimo za kontrolu različitih endpoint-a unutar aplikacije
 * ima @Repository anotaciju - zato što enkapsulira čuvanjem pretragu i vraćanje različitih objekata unutar projekta
 * sadrži "service" i "utility" metode - koje rade skupa pri kreiranju kina
 * kao i pri kupovini karata, otkazivanju rezervacija i generisanju statistike
 */
@Repository
public class CinemaService {

    private int total_rows = 9;
    private int total_columns = 9;
    // lista sa sjedištima:
    List<Seat> seatList;
    // mapa sa kartama (token - sjedište format):
    Map<String, Seat> ticketList = new HashMap<>();

    {
        seatList = new ArrayList<>();

        for (int row = 1; row <= total_rows; row++) {
            for (int col = 1; col <= total_columns; col++) {
                seatList.add(new Seat(row, col, row <= 4 ? 10 : 8, false));  // kreiranje novog "Seat" objekta i ubacivanje u listu
            }
        }

    }

    /**
     * "Service" metoda, koristi se za prikazivanje trenutne statistike unutar "Cinema" objekta
     * prilikom toga, izvršiće se validacija lozinke (najjednostavniji mogući primjer)
     * posle validacije, trenutna statistika će biti vraćena i prikazana u JSON formatu
     * @param password - lozinka za pristup podacima
     * @return - novi "StatsResponse" objekat, koji služi za prikazivanje podataka
     * @throws UnauthorizedException
     */
    public StatsResponse returnStats(String password) throws UnauthorizedException {
        if (!password.equals("super_secret")) {
            throw new UnauthorizedException("The password is wrong!");
        }

        return new StatsResponse()
                .setCurrentIncome(ticketList.values().stream().mapToInt(Seat::getPrice).sum())
                .setAvaliableSeats(seatList.size() - ticketList.size())
                .setPurchasedTickets(ticketList.size());
    }


    /**
     * "Service" metoda - koristi se za povrat karte preko generisanog tokena, a on je kreiran prilikom rezervacije
     * nakon što se izvrši povrat, "Seat" objekat će opet biti dodat u listu slobodnih sjedišta
     * @param token
     * @return
     */
    public TicketReturnResponse returnTicket(String token) {
        // uklanjamo sjedište iz mape karata:
        Seat seat = ticketList.remove(token);
        if (seat == null) {
            throw new BadRequestException("Wrong token!");
        }

        // oslobadjamo sjedište:
        seat.setAvaliable();

        return new TicketReturnResponse().setSeat(seat);
    }

    /**
     * "Service" metoda - koristi se za kupovinu karte, na osnovu reda i kolone zadatog sjedišta (u JSON formatu)
     * vrši se provjera reda/kolone i rezervacije, a nakon toga se sjedište označava kao rezervisano
     * vrši se dodavanje vrijenosti "token:seat" u mapu rezervisanih karata
     * @param row - red "Seat" objekta
     * @param column - kolona "Seat" objekta
     * @return - "TicketPurchaseResponse" objekti, unutar kojih se nalaze UUID (token) i informacije o sjedištu
     */
    public TicketPurchaseResponse purchaseTicket(int row, int column) {
        Seat seat = findSeatByCoordinates(row, column).get();

        checkSeatAvailability(seat);
        // sjedište označavamo kao rezervisano i stavljamo ga skupa sa tokenom u mapu karata:
        seat.setPurchased();
        String token = UUID.randomUUID().toString();
        ticketList.put(token, seat);

        return new TicketPurchaseResponse()
                .setToken(token)
                .setSeat(seat);

    }

    /** "Service" metoda
     * @return  - vraća listu slobodnih "Seat" objekata - koja služi za "response" objekat i ona će biti u JSON formatu
     */
    public AvailableSeatsResponse allAvailableSeats() {
        return new AvailableSeatsResponse()
                .setRows(this.total_rows)
                .setColumns(this.total_columns)
                .setSeatList(getAvaliableSeats());
    }

    // ---------------------------- UTILITY METODE ----------------------------

    /**
     * "Utility" metoda - vrši provjeru statusa sjedišta prilikom kupovine karte
     * @param seat
     */
    private void checkSeatAvailability(Seat seat) {
        if (seat.isPurchased()) {
            throw new BadRequestException("The ticket has been already purchased!");
        }
    }


    /**
     * "Utility" metoda, vraća "Seat" objekat na osnovu broja reda i broja kolone
     * ukoliko koordinate nisu u validnom formatu, baciće se Exception
     * @param row - red
     * @param column - kolona
     * @return - "Seat" objekat
     */
    public Optional<Seat> findSeatByCoordinates(int row, int column) {
        if (row < 1 || column < 1 || row > 9 || column > 9) {
            throw new BadRequestException("The number of a row or a column is out of bounds!");
        }
        return seatList.stream()
                .filter(x -> x.getRow() == row && x.getColumn() == column)
                .findFirst();
    }

    /** "Utility" metoda
     * @return - vraća sva slobodna mjesta unutar kina u vidu liste
     */
    public List<Seat> getAvaliableSeats() {
        return seatList.stream()
                .filter(x -> !x.isPurchased())
                .collect(Collectors.toList());
    }


}
