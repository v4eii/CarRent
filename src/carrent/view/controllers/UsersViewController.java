package carrent.view.controllers;

import carrent.beans.DBBean;
import carrent.entity.Users;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
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
public class UsersViewController implements Initializable {

    
    private ObservableList<Users> usersData = FXCollections.observableArrayList();
    private SimpleBooleanProperty usersChanged = new SimpleBooleanProperty(Boolean.FALSE);
    private SimpleBooleanProperty usersRole = new SimpleBooleanProperty(Boolean.TRUE);
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    
    @FXML
    private BorderPane bPane;
    @FXML
    private TableView<Users> usersTable;
    @FXML
    private TableColumn<Users, Integer> colId;
    @FXML
    private TableColumn<Users, String> colName, colPassword, colRole, colDateCreate, colDateLock;
    @FXML
    private MenuItem cmAdd, cmSave, cmDel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        usersTable.setPlaceholder(new Label(""));
        usersTable.getSelectionModel().setCellSelectionEnabled(true);
        //<editor-fold defaultstate="collapsed" desc="Инициализация колонок">
        
        colId.setCellValueFactory((TableColumn.CellDataFeatures<Users, Integer> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? 0 : (param.getValue().getIdUser() == null ? 0 : param.getValue().getIdUser())));
        
        colName.setCellValueFactory((TableColumn.CellDataFeatures<Users, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getUserLogin()));
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setUserLogin(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            usersChanged.setValue(Boolean.TRUE);
        });
        
        colPassword.setCellValueFactory((TableColumn.CellDataFeatures<Users, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getUserPassword()));
        colPassword.setCellFactory(TextFieldTableCell.forTableColumn());
        colPassword.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setUserPassword(DBBean.getInstance().getMD5String(event.getNewValue()));
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            usersChanged.setValue(Boolean.TRUE);
        });
        
        colRole.setCellValueFactory((TableColumn.CellDataFeatures<Users, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getRole()));
        colRole.setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<String>() {
            @Override
            public String toString(String object)
            {
                return object == null ? "" : object;
            }

            @Override
            public String fromString(String string)
            {
                return string;
            }
        }, FXCollections.observableArrayList("ADMIN", "OPERATOR")));
        colRole.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setRole(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            usersChanged.setValue(Boolean.TRUE);
        });
        
        colDateCreate.setCellValueFactory((TableColumn.CellDataFeatures<Users, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getDateCreate() == null ? "" : format.format(param.getValue().getDateCreate()))));
        colDateCreate.setCellFactory(TextFieldTableCell.forTableColumn());
        colDateCreate.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) ->
        {
            Date tmp = null;
            
            try
            {
                tmp = format.parse(event.getNewValue());
            }
            catch (ParseException ex)
            {
            }
            
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setDateCreate(tmp);
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            usersChanged.setValue(Boolean.TRUE);
        });
        
        colDateLock.setCellValueFactory((TableColumn.CellDataFeatures<Users, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getDateDelete() == null ? "" : format.format(param.getValue().getDateDelete()))));
        colDateLock.setCellFactory(TextFieldTableCell.forTableColumn());
        colDateLock.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) ->
        {
            Date tmp = null;
            
            try
            {
                tmp = format.parse(event.getNewValue());
            }
            catch (ParseException ex)
            {
            }
            
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setDateDelete(tmp);
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            usersChanged.setValue(Boolean.TRUE);
        });
        //</editor-fold>
        cmSave.disableProperty().bind(usersChanged.not());
        cmDel.disableProperty().bind(Bindings.isEmpty(usersData));
        
        if (DBBean.getInstance().getRole().equals("OPERATOR"))
        {
            usersRole = new SimpleBooleanProperty(Boolean.FALSE);
            cmAdd.disableProperty().bind(usersRole.not());
            cmSave.disableProperty().bind(usersRole.not());
            cmDel.disableProperty().bind(usersRole.not());
        }
        
        cmSave.addEventHandler(ActionEvent.ACTION, saveEvent);
        cmAdd.addEventHandler(ActionEvent.ACTION, addEvent);
        cmDel.addEventHandler(ActionEvent.ACTION, deleteEvent);
        
    }
    
    public BorderPane getUsersPane()
    {
        return bPane;
    }
    
    public void initData()
    {
        usersData.clear();
        usersData.addAll(DBBean.getInstance().getUsersJPACtrl().findUsersEntities());
        usersTable.setItems(usersData);
        usersTable.getSelectionModel().select(0);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Обработчики контекстного меню">
    
    private final EventHandler<ActionEvent> addEvent = (ActionEvent event) ->
    {
        Users obj = new Users();
        obj.rec_state = DBBean.RECORD_STATE.NEW;
        obj.setDateCreate(new Date());
        usersData.add(obj);
        usersTable.refresh();
        usersTable.getSelectionModel().select(obj);
        usersChanged.setValue(Boolean.TRUE);
    };
    
    private final EventHandler<ActionEvent> saveEvent = (ActionEvent event) ->
    {
        ObservableList<Users> modifyList = usersTable.getItems().filtered((Users t) -> t.rec_state != DBBean.RECORD_STATE.SAVED);
        modifyList.forEach((Users t) ->
        {
            switch(t.rec_state)
            {
                case NEW:
                {
                    try
                    {
                        DBBean.getInstance().getUsersJPACtrl().create(t);
                        t.rec_state = DBBean.RECORD_STATE.SAVED;
                    }
                    catch(Exception ex)
                    {
                        DBBean.getInstance().showErrDialog(ex, "Ошибка", "");
                    }
                    break;
                }
                case EDIT:
                {
                    try
                    {
                        DBBean.getInstance().getUsersJPACtrl().edit(t);
                        t.rec_state = DBBean.RECORD_STATE.SAVED;
                    }
                    catch (Exception ex)
                    {
                        DBBean.getInstance().showErrDialog(ex, "Ошибка", "");
                    }
                    break;
                }
            }
        });
        usersTable.refresh();
        usersChanged.setValue(Boolean.FALSE);
    };
    
    private final EventHandler<ActionEvent> deleteEvent = (ActionEvent event) ->
    {
        if (DBBean.getInstance().showConfirmDialog("Вопрос", "Вы действительно хотите удалить запись?", "").get() == ButtonType.OK)
        {
            Users obj = usersTable.getSelectionModel().getSelectedItem();
            switch(obj.rec_state)
            {
                case NEW:
                    usersData.remove(obj);
                    break;
                case EDIT:
                    DBBean.getInstance().showWarningDialog("Невозможно удалить пользователя", "Данный пользователь уже существует в базе. Для блокировки укажите дату.");
                    break;
                case SAVED:
                    DBBean.getInstance().showWarningDialog("Невозможно удалить пользователя", "Данный пользователь уже существует в базе. Для блокировки укажите дату.");
                    break;
            }
            usersTable.refresh();
        }
    };
    
    //</editor-fold>
    
    
}
