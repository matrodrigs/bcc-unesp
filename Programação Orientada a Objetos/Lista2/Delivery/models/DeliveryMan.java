package models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) 
public class DeliveryMan extends User {
    final private String transportMode;
    private boolean isAvailable = true;

    public DeliveryMan(String name, int id, String email, String transportMode) {
        super(name, id, email); 
        this.transportMode = transportMode;
    }
}