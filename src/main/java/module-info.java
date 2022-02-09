module polestarbank.onlinebankingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;


    opens polestarbank to javafx.fxml;
    exports polestarbank;





    exports TableModels;
    opens TableModels to javafx.fxml;
}