package carrent.view.controllers;

import carrent.beans.DBBean;
import carrent.entity.Clients;
import carrent.entity.Refs;
import carrent.entity.RefsPK;
import carrent.entity.controllers.exceptions.IllegalOrphanException;
import carrent.entity.controllers.exceptions.NonexistentEntityException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author v4e
 */
public class ClientsViewController implements Initializable {

    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private SimpleBooleanProperty clientChanged = new SimpleBooleanProperty(Boolean.FALSE);
    private ObservableList<Clients> clientData = FXCollections.observableArrayList();
    
    @FXML
    private BorderPane bPane;
    @FXML
    private TableView<Clients> clientsTable;
    @FXML
    private TableColumn<Clients, Integer> colIdClient;
    @FXML
    private TableColumn<Clients, Refs> colDocType;
    @FXML
    private TableColumn<Clients, String> colLastName, colFirstName, colMiddleName,
                                         colBirthday, colPhoneNumber, colRegistration, colSeriesDoc,
                                         colNumberDoc, colDocIssuedDate, colDocIssuedBy;
    @FXML
    private MenuItem cmAddRecord, cmSaveChanges, cmDelRecord;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        clientsTable.setPlaceholder(new Label(""));
        clientsTable.getSelectionModel().setCellSelectionEnabled(true);
        //<editor-fold defaultstate="collapsed" desc="Инициализация колонок">
        
        colIdClient.setCellValueFactory((TableColumn.CellDataFeatures<Clients, Integer> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? 0 : (param.getValue().getIdClient() == null ? 0 : param.getValue().getIdClient())));
        
        colSeriesDoc.setCellValueFactory((TableColumn.CellDataFeatures<Clients, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getSeriesDoc()));
        colSeriesDoc.setCellFactory(TextFieldTableCell.forTableColumn());
        colSeriesDoc.setOnEditCommit((TableColumn.CellEditEvent<Clients, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setSeriesDoc(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            clientChanged.setValue(Boolean.TRUE);
        });
        
        colLastName.setCellValueFactory((TableColumn.CellDataFeatures<Clients, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getLastName()));
        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setOnEditCommit((TableColumn.CellEditEvent<Clients, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setLastName(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            clientChanged.setValue(Boolean.TRUE);
        });
        
        colFirstName.setCellValueFactory((TableColumn.CellDataFeatures<Clients, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getFirstName()));
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setOnEditCommit((TableColumn.CellEditEvent<Clients, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setFirstName(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            clientChanged.setValue(Boolean.TRUE);
        });
        
        colMiddleName.setCellValueFactory((TableColumn.CellDataFeatures<Clients, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getMiddleName()));
        colMiddleName.setCellFactory(TextFieldTableCell.forTableColumn());
        colMiddleName.setOnEditCommit((TableColumn.CellEditEvent<Clients, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setMiddleName(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            clientChanged.setValue(Boolean.TRUE);
        });
        
        colBirthday.setCellValueFactory((TableColumn.CellDataFeatures<Clients, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getBirhday() == null ? "" : format.format(param.getValue().getBirhday()))));
        colBirthday.setCellFactory(TextFieldTableCell.forTableColumn());
        colBirthday.setOnEditCommit((TableColumn.CellEditEvent<Clients, String> event) ->
        {
            Date tmp = null;
            
            try
            {
                tmp = format.parse(event.getNewValue());
            }
            catch (ParseException ex)
            {
                DBBean.getInstance().showWarningDialog("Внимание", "Введен неверный формат даты");
            }
            
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setBirhday(tmp);
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            clientChanged.setValue(Boolean.TRUE);
        });
        
        colPhoneNumber.setCellValueFactory((TableColumn.CellDataFeatures<Clients, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getPhoneNumber()));
        colPhoneNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        colPhoneNumber.setOnEditCommit((TableColumn.CellEditEvent<Clients, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPhoneNumber(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            clientChanged.setValue(Boolean.TRUE);
        });
        
        colRegistration.setCellValueFactory((TableColumn.CellDataFeatures<Clients, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getRegistrationAdress()));
        colRegistration.setCellFactory(TextFieldTableCell.forTableColumn());
        colRegistration.setOnEditCommit((TableColumn.CellEditEvent<Clients, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setRegistrationAdress(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            clientChanged.setValue(Boolean.TRUE);
        });
        
        colDocType.setCellValueFactory((TableColumn.CellDataFeatures<Clients, Refs> param) -> new ReadOnlyObjectWrapper<>(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(1, param.getValue() == null ? 0 : param.getValue().getIdDoc()))));
        colDocType.setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Refs>() {
            @Override
            public String toString(Refs object)
            {
                return object == null ? "" : object.getRefName();
            }

            @Override
            public Refs fromString(String string)
            {
                for (Refs val : DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(1))
                {
                    if (val.getRefName().equals(string))
                        return val;
                }
                return null;
            }
        }, FXCollections.observableArrayList(DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(1))));
        colDocType.setOnEditCommit((TableColumn.CellEditEvent<Clients, Refs> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setIdDoc(event.getNewValue().getRefsPK().getIdRef());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            clientChanged.setValue(Boolean.TRUE);
        });
        
        colNumberDoc.setCellValueFactory((TableColumn.CellDataFeatures<Clients, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getNumberDoc()));
        colNumberDoc.setCellFactory(TextFieldTableCell.forTableColumn());
        colNumberDoc.setOnEditCommit((TableColumn.CellEditEvent<Clients, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setNumberDoc(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            clientChanged.setValue(Boolean.TRUE);
        });
        
        colDocIssuedDate.setCellValueFactory((TableColumn.CellDataFeatures<Clients, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getDateIssueDoc() == null ? "" : format.format(param.getValue().getDateIssueDoc()))));
        colDocIssuedDate.setCellFactory(TextFieldTableCell.forTableColumn());
        colDocIssuedDate.setOnEditCommit((TableColumn.CellEditEvent<Clients, String> event) ->
        {
            Date tmp = null;
            
            try
            {
                tmp = format.parse(event.getNewValue());
            }
            catch (ParseException ex)
            {
                DBBean.getInstance().showWarningDialog("Внимание", "Введен неверный формат даты");
            }
            
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setDateIssueDoc(tmp);
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            clientChanged.setValue(Boolean.TRUE);
        });
        
        colDocIssuedBy.setCellValueFactory((TableColumn.CellDataFeatures<Clients, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getIssuedByDoc()));
        colDocIssuedBy.setCellFactory(TextFieldTableCell.forTableColumn());
        colDocIssuedBy.setOnEditCommit((TableColumn.CellEditEvent<Clients, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setIssuedByDoc(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            clientChanged.setValue(Boolean.TRUE);
        });
        //</editor-fold>
        cmDelRecord.disableProperty().bind(Bindings.isEmpty(clientData));
        cmSaveChanges.disableProperty().bind(clientChanged.not());
        
        cmAddRecord.addEventHandler(ActionEvent.ACTION, addRecordEvent);
        cmSaveChanges.addEventHandler(ActionEvent.ACTION, saveChangesEvent);
        cmDelRecord.addEventHandler(ActionEvent.ACTION, delRecordEvent);
    }
    
    public BorderPane getCLientsPane()
    {
        return bPane;
    }
    
    public void initData()
    {
        clientData.clear();
        clientData.addAll(DBBean.getInstance().getClientJPACtrl().findClientsEntities());
        clientsTable.setItems(clientData);
        clientsTable.getSelectionModel().select(0);
    }

    public TableColumn<Clients, Refs> getColDocType()
    {
        return colDocType;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Обработчики контекстного меню">
    
    private final EventHandler<ActionEvent> addRecordEvent = (ActionEvent event) ->
    {
        Clients obj = new Clients();
        obj.rec_state = DBBean.RECORD_STATE.NEW;
        clientData.add(obj);
        clientsTable.refresh();
        clientsTable.getSelectionModel().select(obj);
        clientChanged.setValue(Boolean.TRUE);
    };
    
    private final EventHandler<ActionEvent> saveChangesEvent = (ActionEvent event) ->
    {
        ObservableList<Clients> modifyList = clientsTable.getItems().filtered((Clients t) -> t.rec_state != DBBean.RECORD_STATE.SAVED);
        modifyList.forEach(new Consumer<Clients>()
        {
            @Override
            public void accept(Clients t)
            {
                switch(t.rec_state)
                {
                    case NEW:
                    {
                        try
                        {
                            DBBean.getInstance().getClientJPACtrl().create(t);
                            t.rec_state = DBBean.RECORD_STATE.SAVED;
                        }
                        catch(Exception ex)
                        {
                            DBBean.getInstance().showErrDialog(ex, "Ошибка создания записи\nПроверьте правильность введенных данных", "");
                        }
                        break;
                    }
                    case EDIT:
                    {
                        try
                        {
                            DBBean.getInstance().getClientJPACtrl().edit(t);
                            t.rec_state = DBBean.RECORD_STATE.SAVED;
                        }
                        catch (NonexistentEntityException ex)
                        {
                            DBBean.getInstance().showErrDialog(ex, "Ошибка изменения записи", "");
                        }
                        catch (Exception ex)
                        {
                            DBBean.getInstance().showErrDialog(ex, "Ошибка изменения записи", "");
                        }
                        break;
                    }
                }
                clientsTable.refresh();
                clientChanged.setValue(Boolean.FALSE);
            }
        });
    };
    
    private final EventHandler<ActionEvent> delRecordEvent = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            if (DBBean.getInstance().showConfirmDialog("Вопрос", "Удаление записи", "Вы действительно хотите удалить запись?").get() == ButtonType.OK)
            {
                Clients obj = clientsTable.getSelectionModel().getSelectedItem();
                switch (obj.rec_state)
                {
                    case NEW:
                    {
                        clientData.remove(obj);
                        break;
                    }
                    case EDIT:
                    {
                        try
                        {
                            DBBean.getInstance().getClientJPACtrl().destroy(obj.getIdClient());
                            clientData.remove(obj);
                        }
                        catch (IllegalOrphanException | NonexistentEntityException ex)
                        {
                            DBBean.getInstance().showErrDialog(ex, "Ошибка удаления записи", "");
                        }
                        break;
                    }
                    case SAVED:
                    {
                        try
                        {
                            DBBean.getInstance().getClientJPACtrl().destroy(obj.getIdClient());
                            clientData.remove(obj);
                        }
                        catch (IllegalOrphanException | NonexistentEntityException ex)
                        {
                            DBBean.getInstance().showErrDialog(ex, "Ошибка удаления записи", "");
                        }
                        break;
                    }
                }
                clientsTable.refresh();
            }
        }
    };
    
    //</editor-fold>
    
}
