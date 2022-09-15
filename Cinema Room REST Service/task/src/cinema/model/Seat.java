package cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model objekta koji služi za kreiranje "Seat" objekata
 * ova klasa koristi Lombok anotacije, kako bismo izbjegli gomilu boilerplate koda
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Seat {
    int row;
    int column;
    int price;
    @JsonIgnore
    boolean isPurchased;

    public void setAvaliable() {
        this.isPurchased = false;
    }

    public void setPurchased() {
        this.isPurchased = true;
    }

}
