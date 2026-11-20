module com.campoycosecha {
    requires javafx.controls;
    requires com.google.gson;
    
    opens com.campoycosecha to javafx.graphics, javafx.fxml;
    opens com.campoycosecha.model to com.google.gson;
    
    exports com.campoycosecha;
    exports com.campoycosecha.model;
    exports com.campoycosecha.repository;
    opens imagenes;
}