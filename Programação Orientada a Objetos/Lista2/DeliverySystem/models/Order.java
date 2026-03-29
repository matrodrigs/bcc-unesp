package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter 
public class Order {
    private Integer id;
    private Client client;
    private Restaurant restaurant;
    private List<MenuItem> items = new ArrayList<>();
    private BigDecimal totalValue;
    private OrderStatus status = OrderStatus.REALIZADO;
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Setter
    private DeliveryMan deliveryMan;

    public Order(Integer id, Client client, Restaurant restaurant, List<MenuItem> items) {
        this.id = id;
        this.client = client;
        this.restaurant = restaurant;
        this.items = items;
        this.totalValue = calculateTotal();
    }

    private BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        
        for (MenuItem item : this.items) {
            total = total.add(item.getValue());
        }
        
        return total;
    }

    public void updateStatus() {
        if (this.status.getNext() != null) {
            this.status = this.status.getNext();
        }
    }

    public String getOrderSummary() {
        StringBuilder sb = new StringBuilder();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        sb.append(String.format("Pedido Nº: %d\n", this.id));
        sb.append("========================================\n");
        sb.append("            RESUMO DO PEDIDO            \n");
        sb.append("========================================\n");

        sb.append(String.format("Data/Hora: %s\n", this.createdAt.format(formatter)));
        sb.append("----------------------------------------\n");
        
        sb.append(String.format("Restaurante: %s\n", this.restaurant.getName()));
        sb.append(String.format("Cliente: %s\n", this.client.getName()));
        sb.append(String.format("Status atual: %s\n", this.status));

        if (this.deliveryMan != null) {
            sb.append(String.format("Entregador: %s\n", this.deliveryMan.getName()));
        } else {
            sb.append("Entregador: (Aguardando atribuição)\n");
        }
        
        sb.append("----------------------------------------\n");
        sb.append("ITENS DO PEDIDO:\n");
        
        for (MenuItem item : this.items) {
            sb.append(String.format("- %-25s R$ %8.2f\n", item.getDishName(), item.getValue()));
        }
        
        sb.append("----------------------------------------\n");
        sb.append(String.format("TOTAL:                      R$ %8.2f\n", this.totalValue));
        sb.append("========================================\n");
        
        return sb.toString();
    }
}