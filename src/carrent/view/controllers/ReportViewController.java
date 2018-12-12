package carrent.view.controllers;

import carrent.MFormController;
import carrent.beans.DBBean;
import carrent.entity.CarReport;
import carrent.entity.Cars;
import carrent.entity.RefsPK;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author v4e
 */
public class ReportViewController implements Initializable {

    @FXML
    private ComboBox<String> cbTypeReport;
    @FXML
    private DatePicker dpAfter, dpBefore;
    @FXML
    private TextArea textArea;
    @FXML
    private BorderPane reportPane;
    @FXML
    private Button btnCancel, btnFormRep;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        cbTypeReport.getItems().addAll("Количество выдач автомобилей", "Количество выданных автомобилей клиенту");
        cbTypeReport.setPromptText("Тип отчета");
        dpAfter.setValue(LocalDate.now().minusMonths(1));
        dpBefore.setValue(LocalDate.now());
        
        btnCancel.addEventHandler(ActionEvent.ACTION, (ActionEvent event) ->
        {
            MFormController.getReportStage().close();
        });
        btnFormRep.addEventHandler(ActionEvent.ACTION, formReportEvent);
    }
    
    private final EventHandler<ActionEvent> formReportEvent = (ActionEvent event) ->
    {
        switch(cbTypeReport.getSelectionModel().getSelectedIndex())
        {
            case 0:
            {
                List<CarReport> tmp = DBBean.getInstance().getDeliveryCarJPACtrl().getReportCarCount(new SimpleDateFormat("yyyy.MM.dd").format(Date.valueOf(dpAfter.getValue())), new SimpleDateFormat("yyyy.MM.dd").format(Date.valueOf(dpBefore.getValue())));
                DBBean.getInstance().getCarsJPACtrl().findCarsEntities().forEach((Cars t) ->
                {
                    textArea.appendText(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(3, t.getIdMake())).getRefName() + " " + String.valueOf(t.getDeliveryCarCollection().size()) + "\n");
                });
            }
        }
    };

    public BorderPane getReportPane()
    {
        return reportPane;
    }
    
}
