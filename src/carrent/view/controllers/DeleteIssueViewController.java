package carrent.view.controllers;

import carrent.beans.DBBean;
import carrent.entity.Cars;
import carrent.entity.Clients;
import carrent.entity.DeliveryCar;
import carrent.entity.Refs;
import carrent.entity.RefsPK;
import carrent.entity.Reservation;
import carrent.entity.controllers.exceptions.IllegalOrphanException;
import carrent.entity.controllers.exceptions.NonexistentEntityException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author v4e
 */
public class DeleteIssueViewController implements Initializable {

    @FXML
    private Button btnDeleteRecord;
    @FXML
    private TableView<DeliveryCar> delivTable;
    @FXML
    private TableColumn<DeliveryCar, Integer> colIdContract,
                                              colNumPowerAttorney;
    @FXML
    private TableColumn<DeliveryCar, Cars> colCar;
    @FXML
    private TableColumn<DeliveryCar, Clients> colClient;
    @FXML
    private TableColumn<DeliveryCar, Refs> colReasonDeliv;
    @FXML
    private TableColumn<DeliveryCar, String> colDateEndPowAtt,
                                             colDateDeliv,
                                             colDateBack;
    
    private ObservableList<DeliveryCar> delivData = FXCollections.observableArrayList();
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        delivTable.setPlaceholder(new Label(""));
        delivTable.getSelectionModel().setCellSelectionEnabled(true);
        //<editor-fold defaultstate="collapsed" desc="Инициализация колонок">
        colIdContract.setCellValueFactory((TableColumn.CellDataFeatures<DeliveryCar, Integer> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? 0 : param.getValue().getDeliveryCarPK().getIdContract()));
        colIdContract.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object)
            {
                return object == null ? "" : object.toString();
            }

            @Override
            public Integer fromString(String string)
            {
                return string == null ? 0 : Integer.parseInt(string);
            }
        }));
        
        colNumPowerAttorney.setCellValueFactory((TableColumn.CellDataFeatures<DeliveryCar, Integer> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? 0 : param.getValue().getNumPowerAttorney()));
        colNumPowerAttorney.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object)
            {
                return object == null ? "" : object.toString();
            }

            @Override
            public Integer fromString(String string)
            {
                return string == null ? 0 : Integer.parseInt(string);
            }
        }));
        
        colCar.setCellValueFactory((TableColumn.CellDataFeatures<DeliveryCar, Cars> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? null : param.getValue().getCars()));
        colCar.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Cars>() {
            @Override
            public String toString(Cars object)
            {
                return object == null ? "" : DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(3, object.getIdMake())).getRefName() + " "
                                             + DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(2, object.getIdModel())).getRefName() + " "
                                             + object.getVin();
            }

            @Override
            public Cars fromString(String string)
            {
                for (Cars val : DBBean.getInstance().getCarsJPACtrl().findCarsEntities())
                {
                    if ((DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(3, val.getIdMake())).getRefName() + " "
                         + DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(2, val.getIdModel())).getRefName() + " "
                         + val.getVin()).equals(string))
                    {
                        return val;
                    }
                }
                return null;
            }
        }));
        
        colClient.setCellValueFactory((TableColumn.CellDataFeatures<DeliveryCar, Clients> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? null : param.getValue().getClients()));
        colClient.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Clients>() {
            @Override
            public String toString(Clients object)
            {
                return object == null ? "" : object.getFirstName() + " " + object.getLastName() + " " + object.getMiddleName();
            }

            @Override
            public Clients fromString(String string)
            {
                for (Clients val : DBBean.getInstance().getClientJPACtrl().findClientsEntities())
                {
                    if ((val.getFirstName() + " " + val.getLastName() + " " + val.getMiddleName()).equals(string))
                        return val;
                }
                return null;
            }
        }));
        
        colReasonDeliv.setCellValueFactory((TableColumn.CellDataFeatures<DeliveryCar, Refs> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? null : DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(6, param.getValue().getIdReason()))));
        colReasonDeliv.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Refs>() {
            @Override
            public String toString(Refs object)
            {
                return object == null ? "" : object.getRefName();
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
        }));
        
        colDateEndPowAtt.setCellValueFactory((TableColumn.CellDataFeatures<DeliveryCar, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getEndNumpa() == null ? "" : format.format(param.getValue().getEndNumpa()))));
        colDateBack.setCellValueFactory((TableColumn.CellDataFeatures<DeliveryCar, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getDateBack() == null ? "" : format.format(param.getValue().getDateBack()))));
        colDateDeliv.setCellValueFactory((TableColumn.CellDataFeatures<DeliveryCar, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getDateDelivery() == null ? "" : format.format(param.getValue().getDateDelivery()))));
        //</editor-fold>
        btnDeleteRecord.addEventHandler(ActionEvent.ACTION, deleteRecordEvent);
    }
    
    public void initData()
    {
        delivData.clear();
        delivData.addAll(DBBean.getInstance().getDeliveryCarJPACtrl().findDeliveryCarEntities());
        delivTable.setItems(delivData);
        delivTable.getSelectionModel().select(0);
    }
    
    private final EventHandler<ActionEvent> deleteRecordEvent = (ActionEvent event) ->
    {
        if (DBBean.getInstance().showConfirmDialog("Вопрос", "Вы действительно хотите удалить запись?", "").get() == ButtonType.OK)
        {
            try
            {
                DeliveryCar deliv = delivTable.getSelectionModel().getSelectedItem();
                DBBean.getInstance().getDeliveryCarJPACtrl().destroy(deliv.getDeliveryCarPK());
                if (DBBean.getInstance().showConfirmDialog("Вопрос", "Запись о выдаче удалена", "Хотите ли вы удалить соответствующую запись резервирования?").get() == ButtonType.OK)
                {
                    Reservation reserv = deliv.getReservation();
                    DBBean.getInstance().getReservationJPACtrl().destroy(reserv.getIdContract());
                }
                
                ReservViewController.getDeleteStage().close();
            }
            catch (NonexistentEntityException | IllegalOrphanException ex)
            {
                DBBean.getInstance().showErrDialog(ex, "Ошибка удаления записи", "");
                ReservViewController.getDeleteStage().close();
            }
        }
    };

}
