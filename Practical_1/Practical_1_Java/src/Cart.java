import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> products; // Список товарів у кошику

    // Конструктор класу
    public Cart() {
        this.products = new ArrayList<>();
    }

    // Метод для додавання товару до кошика
    public void addProduct(Product product) {
        products.add(product);
    }

    // Метод для видалення товару з кошика
    public boolean removeProduct(Product product) {
        return products.remove(product);
    }

    // Видалення товару з кошика за ID (самостійна робота)
    public boolean removeProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                products.remove(product);
                return true;
            }
        }
        return false;
    }

    // Метод для отримання загальної вартості товарів у кошику
    public double getTotalPrice() {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    // Повертаємо копію списку, щоб запобігти змінам ззовні
    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    // Очищення списку товарів у кошику
    public void clear() {
        products.clear();
    }

    // Метод для виведення інформації про всі товари в кошику
    @Override
    public String toString() {
        if (products.isEmpty()) {
            return "Кошик порожній.";
        }
        StringBuilder sb = new StringBuilder("Товари кошика:\n");
        for (Product product : products) {
            sb.append(product.toString()).append("\n");
        }
        sb.append("Загальна вартість: ").append(String.format("%.2f", getTotalPrice()));
        return sb.toString();
    }
}
