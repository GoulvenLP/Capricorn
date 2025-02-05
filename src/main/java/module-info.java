module com.example.capricorn {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.ooxml;
    requires jakarta.json.bind;
    requires org.apache.commons.geometry.euclidean;

    exports enstabretagne.applications.capricorn;
    exports enstabretagne.applications.capricorn.scenario;
    exports enstabretagne.engine;
    exports enstabretagne.moniteur2D;

    exports enstabretagne.base.logger;

    opens com.example.capricorn to javafx.fxml;
    exports com.example.capricorn;
}