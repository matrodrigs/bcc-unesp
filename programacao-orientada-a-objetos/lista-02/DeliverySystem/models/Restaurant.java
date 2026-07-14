package models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter 
@EqualsAndHashCode(callSuper = true) 
public class Restaurant extends User {
    final private String nickname;
    private List<MenuItem> menu = new ArrayList<>();

    public Restaurant(String name, int id, String email, String nickname, List<MenuItem> menu) {
        super(name, id, email); 
        this.nickname = nickname;
        this.menu = menu;
    }

    public boolean hasDish(String dishName) {
        return menu.stream().anyMatch(d -> dishName.equalsIgnoreCase(d.getDishName()));
    }

    public BigDecimal getDishPrice(String dishName) {
        return menu.stream()
                .filter(d -> dishName.equalsIgnoreCase(d.getDishName()))
                .findFirst()
                .map(d -> d.getValue())
                .orElse(BigDecimal.ZERO); 
    }
}