package carrent.view.controllers;

import carrent.MFormController;
import carrent.beans.DBBean;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

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
    private Button btnCancel, btnFormRep, btnPrintPDF;
    
    List<String> tt;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        tt = new ArrayList<>();
        cbTypeReport.getItems().addAll("Количество выдач автомобилей", "Количество выданных автомобилей клиенту");
        cbTypeReport.setPromptText("Тип отчета");
        dpAfter.setValue(LocalDate.now().minusMonths(1));
        dpBefore.setValue(LocalDate.now());
        
        btnCancel.addEventHandler(ActionEvent.ACTION, (ActionEvent event) ->
        {
            MFormController.getReportStage().close();
        });
        btnFormRep.addEventHandler(ActionEvent.ACTION, formReportEvent);
        btnPrintPDF.addEventHandler(ActionEvent.ACTION, printPDFEvent);
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
                    textArea.appendText("                                              Number of car issue\n");//Количество выдач автомобилей
                    tt.add("                                              Number of car issue");
                    textArea.appendText(String.format("                                           From %s to %s:\n\n", new SimpleDateFormat("dd.MM.yyyy").format(Date.valueOf(dpAfter.getValue())),
                            new SimpleDateFormat("dd.MM.yyyy").format(Date.valueOf(dpBefore.getValue()))));
                    tt.add("                                           From "+new SimpleDateFormat("dd.MM.yyyy").format(Date.valueOf(dpAfter.getValue()))+" to "+new SimpleDateFormat("dd.MM.yyyy").format(Date.valueOf(dpBefore.getValue()))+":");
                    if (!tmp.isEmpty())
                    {
                        tmp.forEach((Object[] t) ->
                        {
                            textArea.appendText(String.format("%s %s %s: %s times", t[0].toString(), t[1].toString(), t[2].toString(), t[3].toString()));
                            tt.add(String.format("%s %s %s: %s times", t[0].toString(), t[1].toString(), t[2].toString(), t[3].toString()));
                            textArea.appendText(System.getProperty("line.separator"));
                        });
                    }
                    else
                    {
                        textArea.appendText("Нет данных за указанный промежуток...");
                    }
                    break;
                }
                case 1:
                {
                    List<Object[]> tmp = DBBean.getInstance().getDeliveryCarJPACtrl().getReportClientCarCount(new SimpleDateFormat("yyyy.MM.dd").format(Date.valueOf(dpAfter.getValue())),
                            new SimpleDateFormat("yyyy.MM.dd").format(Date.valueOf(dpBefore.getValue())));
                    textArea.appendText("                                         Number of car deliveries to customers\n");///Количество выдач автомобилей клиентам
                    tt.add(textArea.getText());
                    textArea.appendText(String.format("                                           From %s to %s:\n\n", new SimpleDateFormat("dd.MM.yyyy").format(Date.valueOf(dpAfter.getValue())),//За период от %s до %s
                            new SimpleDateFormat("dd.MM.yyyy").format(Date.valueOf(dpBefore.getValue()))));
                    tt.add(textArea.getText());
                    if (!tmp.isEmpty())
                    {
                        tmp.forEach((Object[] t) ->
                        {
                            textArea.appendText(String.format("%s %s %s Received cars: %s times", t[0].toString(), t[1].toString(), t[2].toString(), t[3].toString()));///получал автомобили: %s раз(а)
                            tt.add(textArea.getText());
                            textArea.appendText(System.getProperty("line.separator"));
                        });
                    }
                    else
                    {
                        textArea.appendText("Нет данных за указанный промежуток...");
                    }
                    break;
                }
            }
        }
        else
        {
            DBBean.getInstance().showWarningDialog("Не указан тип отчета", "Выберите тип отчета");
        }
    };

    // Temp
    private EventHandler<ActionEvent> printPDFEvent = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            try
            {
                PDDocument doc = PDDocument.load(new File("D://1.pdf"));
                
                //Creating a PDF Document
                PDPage page = doc.getPage(0);
                PDPageContentStream contentStream = new PDPageContentStream(doc, page);
                
                //Begin the Content stream
                contentStream.beginText();
                
                //Setting the font to the Content stream
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
                
                //Setting the position for the line
                contentStream.newLineAtOffset(25, 725);
                for (String val : tt)
                {
                    contentStream.showText(val);
                    contentStream.newLineAtOffset(0, -20);
                }
                
                //Adding text in the form of string
                
                
                //Ending the content stream
                contentStream.endText();
                System.out.println("Content added");
                
                //Closing the content stream
                contentStream.close();
                
                //Saving the document
                doc.save(new File("D://Report.pdf"));
                
                //Closing the document
                doc.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ReportViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
    public BorderPane getReportPane()
    {
        return reportPane;
    }
    
}
