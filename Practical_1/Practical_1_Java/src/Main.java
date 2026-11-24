import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<Category> categories = new ArrayList<>(); // Категорії магазину
    private static final List<Product> catalog = new ArrayList<>();     // Каталог товарів
    private static final List<Order> orderHistory = new ArrayList<>();  // Історія замовлень
    private static final Cart cart = new Cart();                        // Кошик користувача
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initCatalog();

        while (true) {
            System.out.println("\nВиберіть опцію:");
            System.out.println("1 - Переглянути список товарів");
            System.out.println("2 - Додати товар до кошика");
            System.out.println("3 - Переглянути кошик");
            System.out.println("4 - Видалити товар з кошика");
            System.out.println("5 - Зробити замовлення");
            System.out.println("6 - Історія замовлень");
            System.out.println("7 - Пошук товару за назвою");
            System.out.println("8 - Пошук товарів за категорією");
            System.out.println("0 - Вийти");

            int choice = readInt();
            switch (choice) {
                case 1:
                    printProducts(catalog);
                    break;
                case 2:
                    addToCart();
                    break;
                case 3:
                    System.out.println(cart);
                    break;
                case 4:
                    removeFromCart();
                    break;
                case 5:
                    makeOrder();
                    break;
                case 6:
                    showOrderHistory();
                    break;
                case 7:
                    searchByName();
                    break;
                case 8:
                    searchByCategory();
                    break;
                case 0:
                    System.out.println("Дякуємо, що використовували наш магазин!");
                    return;
                default:
                    System.out.println("Невідома опція. Спробуйте ще раз.");
                    break;
            }
        }
    }

    // Створення категорій і товарів
    private static void initCatalog() {
        Category electronics = new Category(1, "Електроніка");
        Category smartphones = new Category(2, "Смартфони");
        Category accessories = new Category(3, "Аксесуари");
        Category clothes = new Category(4, "Одяг");
        categories.add(electronics);
        categories.add(smartphones);
        categories.add(accessories);
        categories.add(clothes);

        catalog.add(new Product(1, "Ноутбук", 19999.99, "Високопродуктивний ноутбук для роботи та ігор", electronics));
        catalog.add(new Product(2, "Смартфон", 12999.50, "Смартфон з великим екраном", smartphones));
        catalog.add(new Product(3, "Навушники", 2499.00, "Бездротові навушники з шумозаглушенням", accessories));
        catalog.add(new Product(4, "Планшет", 9999.00, "Планшет з екраном 11 дюймів", electronics));
        catalog.add(new Product(5, "Чохол для смартфона", 399.00, "Силіконовий чохол", accessories));
        catalog.add(new Product(6, "Футболка", 549.00, "Бавовняна футболка", clothes));
    }

    // Безпечне зчитування цілого числа
    private static int readInt() {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Введіть ціле число:");
            }
        }
    }

    private static Product findProductById(int id) {
        for (Product product : catalog) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    private static void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Товарів не знайдено.");
            return;
        }
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static void addToCart() {
        System.out.println("Введіть ID товару для додавання до кошика:");
        Product product = findProductById(readInt());
        if (product != null) {
            cart.addProduct(product);
            System.out.println("Товар \"" + product.getName() + "\" додано до кошика.");
        } else {
            System.out.println("Товар з таким ID не знайдено");
        }
    }

    // Самостійна робота: видалення товарів з кошика
    private static void removeFromCart() {
        if (cart.isEmpty()) {
            System.out.println("Кошик порожній.");
            return;
        }
        System.out.println(cart);
        System.out.println("Введіть ID товару для видалення з кошика:");
        int id = readInt();
        if (cart.removeProductById(id)) {
            System.out.println("Товар видалено з кошика.");
        } else {
            System.out.println("Товару з таким ID немає в кошику.");
        }
    }

    private static void makeOrder() {
        if (cart.isEmpty()) {
            System.out.println("Кошик порожній. Додайте товари перед оформленням замовлення.");
        } else {
            Order order = new Order(cart);
            orderHistory.add(order); // Збереження в історію
            System.out.println("Замовлення оформлено:");
            System.out.println(order);
            cart.clear();
        }
    }

    // Самостійна робота: історія замовлень
    private static void showOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("Історія замовлень порожня.");
            return;
        }
        System.out.println("Історія замовлень:");
        for (Order order : orderHistory) {
            System.out.println(order);
            System.out.println("------------------------------");
        }
    }

    // Самостійна робота: пошук за назвою
    private static void searchByName() {
        System.out.println("Введіть назву (або частину назви) товару:");
        String query = scanner.nextLine().trim().toLowerCase();
        List<Product> result = new ArrayList<>();
        for (Product product : catalog) {
            if (product.getName().toLowerCase().contains(query)) {
                result.add(product);
            }
        }
        printProducts(result);
    }

    // Самостійна робота: пошук за категорією
    private static void searchByCategory() {
        System.out.println("Доступні категорії:");
        for (Category category : categories) {
            System.out.println(category);
        }
        System.out.println("Введіть назву або ID категорії:");
        String query = scanner.nextLine().trim().toLowerCase();
        List<Product> result = new ArrayList<>();
        for (Product product : catalog) {
            Category category = product.getCategory();
            if (category == null) continue;
            if (category.getName().toLowerCase().contains(query)
                    || String.valueOf(category.getId()).equals(query)) {
                result.add(product);
            }
        }
        printProducts(result);
    }
}
