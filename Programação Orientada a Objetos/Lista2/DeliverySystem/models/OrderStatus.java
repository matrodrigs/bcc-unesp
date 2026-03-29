package models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor 
@Getter
public enum OrderStatus {
    ENTREGUE(null),
    EM_ENTREGA(ENTREGUE),
    EM_PREPARO(EM_ENTREGA),
    REALIZADO(EM_PREPARO);
    
    private final OrderStatus next;
}