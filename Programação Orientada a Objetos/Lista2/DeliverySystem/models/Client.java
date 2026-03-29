package models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter 
@EqualsAndHashCode(callSuper = true)
public class Client extends User {
    public Client() {
        super();
    }

    public Client(String name, int id, String email) {
        super(name, id, email);
    }
}