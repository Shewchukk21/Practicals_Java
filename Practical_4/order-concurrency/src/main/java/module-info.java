module order.concurrency {
    requires order.storage; // order.processing приходить транзитивно
    requires javafaker;
    requires java.sql;      // потрібен бібліотеці snakeyaml, яку використовує JavaFaker
}
