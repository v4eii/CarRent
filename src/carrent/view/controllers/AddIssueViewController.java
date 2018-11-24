package carrent.view.controllers;

import carrent.beans.DBBean;
import carrent.entity.Cars;
import carrent.entity.Clients;
import carrent.entity.DeliveryCar;
import carrent.entity.Refs;
import carrent.entity.RefsPK;
import carrent.entity.Reservation;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author v4e
 */
public class AddIssueViewController implements Initializable {
    
    private Reservation reserv;
    private DeliveryCar deliv;
    private boolean recordAdd = false;
    
    @FXML
    private ComboBox<Cars> cbCar;
    @FXML
    private ComboBox<Clients> cbClient;
    @FXML
    private ComboBox<Refs> cbReasonDeliv;
    @FXML
    private TextField tMilleage, tNumPowerAtorney, tConfidant, tComment;
    @FXML
    private DatePicker dpDateDeliv, dpDateBack, dpDateEndNumPowAtt;
    @FXML
    private Button btnAddRec, btnCancel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        cbCar.getItems().addAll(DBBean.getInstance().getCarsJPACtrl().findCarsEntities());
        cbCar.setConverter(new StringConverter<Cars>() {
            @Override
            public String toString(Cars object)
            {
                return DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(3, object.getIdMake())).getRefName().concat(
                    " " + DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(2, object.getIdModel())).getRefName()).concat(
                    " " + object.getNumberregistration());
            }

            @Override
            public Cars fromString(String string)
            {
                for (Cars val : DBBean.getInstance().getCarsJPACtrl().findCarsEntities())
                {
                    if ((DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(3, val.getIdMake())).getRefName().concat(
                    " " + DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(2, val.getIdModel())).getRefName()).concat(
                    " " + val.getNumberregistration())).equals(string))
                    {
                        return val;
                    }
                }
                return null;
            }
        });
        cbClient.getItems().addAll(DBBean.getInstance().getClientJPACtrl().findClientsEntities());
        cbClient.setConverter(new StringConverter<Clients>() {
            @Override
            public String toString(Clients object)
            {
                return object == null ? "" : object.getLastName().concat(" " + object.getFirstName()).concat(" " + object.getMiddleName());
            }

            @Override
            public Clients fromString(String string)
            {
                for (Clients val : DBBean.getInstance().getClientJPACtrl().findClientsEntities())
                {
                    if ((val.getLastName().concat(" " + val.getFirstName()).concat(" " + val.getMiddleName())).equals(string))
                        return val;
                }
                return null;
            }
        });
        cbReasonDeliv.getItems().addAll(DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(6));
        cbReasonDeliv.setConverter(new StringConverter<Refs>() {
            @Override
            public String toString(Refs object)
            {
                return object == null ? "" : DBBean.getInstance().getRefsJPACtrl().findRefs(object.getRefsPK()).getRefName();
            }

            @Override
            public Refs fromString(String string)
            {
                for (Refs val : DBBean.getInstance().getRefsJPACtrl().findRefsEntities())
                {
                    if (val.getRefName().equals(string))
                        return val;
                }
                return null;
            }
        });
        dpDateDeliv.setValue(LocalDate.now());
        dpDateBack.setValue(LocalDate.now().plusDays(5));
        dpDateEndNumPowAtt.setValue(LocalDate.now());
        
        btnAddRec.addEventHandler(ActionEvent.ACTION, btnAddEvent);
        btnCancel.addEventHandler(ActionEvent.ACTION, btnCancelEvent);
        
    }
    
    public void setReservationObj(Reservation obj)
    {
        this.reserv = obj;
    }
    
    public void setDeliveryObj(DeliveryCar obj)
    {
        this.deliv = obj;
    }

    public boolean getRecordAdd()
    {
        return recordAdd;
    }

    public Reservation getReservation()
    {
        return reserv;
    }

    public DeliveryCar getDelivery()
    {
        return deliv;
    }
    
    private final EventHandler<ActionEvent> btnAddEvent = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            if (tNumPowerAtorney.getText().isEmpty())
                DBBean.getInstance().showWarningDialog("Внимание", "Укажите номер доверенности");
            else if (tConfidant.getText().isEmpty())
                DBBean.getInstance().showWarningDialog("Внимание", "Укажите имя оператора");
            else if (tNumPowerAtorney.getText().isEmpty())
                DBBean.getInstance().showWarningDialog("Внимание", "Укажите номер доверенности");
            else if (cbCar.getSelectionModel().isEmpty())
                DBBean.getInstance().showWarningDialog("Внимание", "Укажите автомобиль для выдачи");
            else if (cbReasonDeliv.getSelectionModel().isEmpty())
                DBBean.getInstance().showWarningDialog("Внимание", "Укажите программу выдачи");
            else if (cbClient.getSelectionModel().isEmpty())
                DBBean.getInstance().showWarningDialog("Внимание", "Укажите клиента\nЕсли клиента не существует, создайте его в меню Администрирование->Клиенты");
            else
            {
                if (reserv != null && deliv != null)
                {
                    if (!tComment.getText().isEmpty())
                        reserv.setComment(tComment.getText());
                    reserv.setConfidant(tConfidant.getText());
                    reserv.setEndreserve(Date.valueOf(dpDateBack.getValue()));
                    reserv.setStartreserve(Date.valueOf(dpDateDeliv.getValue()));
                    reserv.setIdCar(cbCar.getValue());
                    reserv.setIdClient(cbClient.getValue());
                    
                    deliv.setReservation(reserv);
                    deliv.setClients(cbClient.getValue());
                    deliv.setDateBack(Date.valueOf(dpDateBack.getValue()));
                    deliv.setDateDelivery(Date.valueOf(dpDateDeliv.getValue()));
                    deliv.setEndNumpa(Date.valueOf(dpDateEndNumPowAtt.getValue()));
                    deliv.setIdReason(cbReasonDeliv.getValue().getRefsPK().getIdRef());
                    if (!tMilleage.getText().isEmpty())
                        deliv.setMilleage(Integer.parseInt(tMilleage.getText()));
                    deliv.setNumPowerAttorney(Integer.parseInt(tNumPowerAtorney.getText()));
                    deliv.setCars(cbCar.getValue());
                    
                    try
                    {
                        DBBean.getInstance().getReservationJPACtrl().create(reserv);
                        DBBean.getInstance().getDeliveryCarJPACtrl().create(deliv);
                    }
                    catch (Exception ex)
                    {
                        DBBean.getInstance().showErrDialog(ex, "Ошибка добавления записи", "");
                        ReservViewController.getStage().close();
                    }
                    
                    recordAdd = true;
                    ReservViewController.getStage().close();
                    
                }
                else                
                    DBBean.getInstance().showErrDialog(new Exception("Ошибка инициализации объекта"), "Ошибка при добавлении записи", "");
            }
        }
    };
    
    private final EventHandler<ActionEvent> btnCancelEvent = (ActionEvent event) ->
    {
        ReservViewController.getStage().close();
    };
    
}
