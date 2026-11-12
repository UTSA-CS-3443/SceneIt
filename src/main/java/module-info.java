module edu.utsa.cs3443.sceneit {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.csv;


    opens edu.utsa.cs3443.sceneit to javafx.fxml;
    opens edu.utsa.cs3443.sceneit.controller to javafx.fxml;
    exports edu.utsa.cs3443.sceneit;

}