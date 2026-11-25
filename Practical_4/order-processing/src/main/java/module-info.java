module order.processing {
    requires static lombok;

    // пакет internal не експортується — його класи недоступні іншим модулям
    exports com.example.order.processing;
}
