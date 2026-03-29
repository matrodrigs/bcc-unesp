package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor 
@NoArgsConstructor
public abstract class User {
    protected String name;
    protected int id;
    protected String email;

    public void checkProfile() {
        
    }
}   