import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private static int nextId = 1;          // Лічильник для генерації номерів замовлень

    private final int id;                   // Номер замовлення
    private final List<Product> products;   // Список товарів у замовленні
    private final double totalPrice;        // Загальна вартість замовлення
    private final LocalDateTime createdAt;  // Дата й час створення
    private String status;                  // Статус замовлення

    // Конструктор
    public Order(Cart cart) {
        this.id = nextId++;
        this.products = new ArrayList<>(cart.getProducts()); // Копіювання товарів з кошика
        this.totalPrice = cart.getTotalPrice();
        this.createdAt = LocalDateTime.now();
        this.status = "Нове"; // Стандартний статус при створенні замовлення
    }

    // Метод для зміни статусу замовлення
    public void setStatus(String status) {
        this.status = status;
    }

    // Геттери
    public int getId() {
        return id;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Метод для виведення інформації про замовлення
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Замовлення №").append(id)
                .append(" (").append(createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                .append("):\n");
        for (Product product : products) {
            sb.append(product.toString()).append("\n");
        }
        sb.append("Загальна вартість: ").append(String.format("%.2f", totalPrice)).append("\n");
        sb.append("Статус: ").append(status);
        return sb.toString();
    }
}
