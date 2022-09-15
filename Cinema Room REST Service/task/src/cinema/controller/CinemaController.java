package cinema.controller;

import cinema.exceptions.BadRequestException;
import cinema.exceptions.UnauthorizedException;
import cinema.handlers.*;
import cinema.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * "@RestController" klasa - služi za upravljanje API "endpoint"-ima unutar aplikacije i sav output se vraća u JSON formatu
 * zadati su sledeći "endpoint"-i:
 *      /seats      -->     vraća sva slobodna sjedišta
 *      /purchase   -->     vrši se kupovina karte, a odgovarajuće sjedište se uklanja iz liste slobodnih
 *      /return     -->     vrši se povrat karte, a odgovarajuće sjedište se vraća u listu slobodnih
 *      /stats      -->     generalna statistika (prodate ulaznice, slobodna i rezervisana sjedišta)
 */

@RestController
public class CinemaController {

    private CinemaService cinemaService;

    @Autowired
    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/seats")
    public AvailableSeatsResponse getSeats() {
        return cinemaService.allAvailableSeats();
    }

    @PostMapping("/purchase")
    public ResponseEntity<TicketPurchaseResponse> purchase(@RequestBody TicketPurchaseRequest body) {
        try {
            return new ResponseEntity<>(cinemaService.purchaseTicket(body.getRow(), body.getColumn()), HttpStatus.OK);
        } catch (BadRequestException e) {
            return getErrorEntity(e, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/return")
    public ResponseEntity<TicketReturnResponse> returnTicket(@RequestBody TicketReturnRequest body) {
        try {
            return new ResponseEntity<>(cinemaService.returnTicket(body.getToken()), HttpStatus.OK);
        } catch (BadRequestException e) {
            return getErrorEntity(e, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/stats")
    public ResponseEntity<StatsResponse> stats(@RequestParam(defaultValue = "") String password) {
        try {
            return new ResponseEntity<>(cinemaService.returnStats(password), HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return getErrorEntity(e, HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * Metoda za upravljanje greškama
     * @param e - "Exception" promjenjiva.
     * @param status - HTTP status (400, 401, 500, itd).
     * @param <T> - generički tip
     * @return - Formatirana poruka sa greškom i HTTP status odgovor
     */
    private <T> ResponseEntity<T> getErrorEntity(Exception e, HttpStatus status) {
        return new ResponseEntity(Collections.singletonMap("error", e.getMessage()), status);
    }


}
