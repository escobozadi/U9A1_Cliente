module com.example.u9a1_cliente {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.u9a1_cliente to javafx.fxml;
    exports com.example.u9a1_cliente;
}