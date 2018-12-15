package carrent.view.controllers;

import carrent.MFormController;
import carrent.beans.DBBean;
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
        cbTypeReport.getItems().addAll("���������� ����� �����������", "���������� �������� ����������� �������");
        cbTypeReport.setPromptText("��� ������");
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
        textArea.clear();
        if(cbTypeReport.getValue() != null)
        {
            switch (cbTypeReport.getSelectionModel().getSelectedIndex())
            {
                case 0:
                {
                    List<Object[]> tmp = DBBean.getInstance().getDeliveryCarJPACtrl().getReportCarCount(new SimpleDateFormat("yyyy.MM.dd").format(Date.valueOf(dpAfter.getValue())),
                            new SimpleDateFormat("yyyy.MM.dd").format(Date.valueOf(dpBefore.getValue())));
                    textArea.appendText("                                              ���������� ����� �����������\n");
                    textArea.appendText(String.format("                                           �� ������ �� %s �� %s:\n\n", new SimpleDateFormat("dd.MM.yyyy").format(Date.valueOf(dpAfter.getValue())),
                            new SimpleDateFormat("dd.MM.yyyy").format(Date.valueOf(dpBefore.getValue()))));
                    if (!tmp.isEmpty())
                    {
                        tmp.forEach((Object[] t) ->
                        {
                            textArea.appendText(String.format("%s %s %s: %s ���(�)", t[0].toString(), t[1].toString(), t[2].toString(), t[3].toString()));
                            textArea.appendText(System.getProperty("line.separator"));
                        });
                    }
                    else
                    {
                        textArea.appendText("��� ������ �� ��������� ����������...");
                    }
                    break;
                }
                case 1:
                {
                    List<Object[]> tmp = DBBean.getInstance().getDeliveryCarJPACtrl().getReportClientCarCount(new SimpleDateFormat("yyyy.MM.dd").format(Date.valueOf(dpAfter.getValue())),
                            new SimpleDateFormat("yyyy.MM.dd").format(Date.valueOf(dpBefore.getValue())));
                    textArea.appendText("                                         ���������� ����� ����������� ��������\n");
                    textArea.appendText(String.format("                                           �� ������ �� %s �� %s:\n\n", new SimpleDateFormat("dd.MM.yyyy").format(Date.valueOf(dpAfter.getValue())),
                            new SimpleDateFormat("dd.MM.yyyy").format(Date.valueOf(dpBefore.getValue()))));
                    if (!tmp.isEmpty())
                    {
                        tmp.forEach((Object[] t) ->
                        {
                            textArea.appendText(String.format("%s %s %s ������� ����������: %s ���(�)", t[0].toString(), t[1].toString(), t[2].toString(), t[3].toString()));
                            textArea.appendText(System.getProperty("line.separator"));
                        });
                    }
                    else
                    {
                        textArea.appendText("��� ������ �� ��������� ����������...");
                    }
                    break;
                }
            }
        }
        else
        {
            DBBean.getInstance().showWarningDialog("�� ������ ��� ������", "�������� ��� ������");
        }
    };

    public BorderPane getReportPane()
    {
        return reportPane;
    }
    
}
