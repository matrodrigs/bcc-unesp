package models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String name, rg, ra, cellphoneNumber;
    private Birthday birthday;
    private Address address;
    private float cr;
}