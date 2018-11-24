package carrent.view.controllers;

import carrent.beans.DBBean;
import carrent.entity.Refs;
import carrent.entity.RefsName;
import carrent.entity.RefsPK;
import carrent.entity.controllers.exceptions.NonexistentEntityException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author v4e
 */
public class RefsViewController implements Initializable {

    @FXML
    private BorderPane bPane;
    @FXML
    private Button addRefs;
    @FXML
    private TableView<Refs> refsTable;
    @FXML
    private TableColumn<Refs, String> colRefName, colDateBegin, colDateCancel;
    @FXML
    private TableColumn<Refs, Integer> colIdRef;
    @FXML
    private ComboBox<RefsName> cbRefsName;
    @FXML
    private MenuItem cmSaveChanges, cmAddRecord, cmDelRecord;
    
    private ObservableList<Refs> refsData = FXCollections.observableArrayList();
    private SimpleBooleanProperty refsChanged = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty usersRole = new SimpleBooleanProperty(Boolean.TRUE);
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        refsTable.setPlaceholder(new Label(""));
        //<editor-fold defaultstate="collapsed" desc="Инициализация колонок таблицы">
        
        colRefName.setCellValueFactory((TableColumn.CellDataFeatures<Refs, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getRefName()));
        colRefName.setCellFactory(TextFieldTableCell.forTableColumn());
        colRefName.setOnEditCommit((TableColumn.CellEditEvent<Refs, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setRefName(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            refsChanged.setValue(Boolean.TRUE);
        });
        
        colDateBegin.setCellValueFactory((TableColumn.CellDataFeatures<Refs, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getDateCreate() == null ? "" : format.format(param.getValue().getDateCreate()))));
        colDateBegin.setCellFactory(TextFieldTableCell.forTableColumn());
        colDateBegin.setOnEditCommit((TableColumn.CellEditEvent<Refs, String> event) ->
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
        });
        
        colDateCancel.setCellValueFactory((TableColumn.CellDataFeatures<Refs, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getDateCancel() == null ? "" : format.format(param.getValue().getDateCancel()))));
        colDateCancel.setCellFactory(TextFieldTableCell.forTableColumn());
        colDateCancel.setOnEditCommit((TableColumn.CellEditEvent<Refs, String> event) ->
        {
            Date tmp = null;
            
            try
            {
                tmp = format.parse(event.getNewValue());
            }
            catch (ParseException ex)
            {
            }
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setDateCancel(tmp);
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
        });
        
        colIdRef.setCellValueFactory((TableColumn.CellDataFeatures<Refs, Integer> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? 0 : param.getValue().getRefsPK().getIdRef()));
        colIdRef.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
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
        colIdRef.setOnEditCommit((TableColumn.CellEditEvent<Refs, Integer> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setRefsPK(new RefsPK(cbRefsName.getSelectionModel().getSelectedIndex(), event.getNewValue()));
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
        });
        //</editor-fold>
        cbRefsName.setItems(FXCollections.observableArrayList(DBBean.getInstance().getRefsNameJPACtrl().findRefsNameEntities()));
        cbRefsName.setConverter(new StringConverter<RefsName>() 
        {
            @Override
            public String toString(RefsName object)
            {
                return object == null ? "" : object.getRefName();
            }

            @Override
            public RefsName fromString(String string)
            {
                for (RefsName val : cbRefsName.getItems())
                {
                    if (val.getRefName().equals(string))
                        return val;
                }
                return null;
            }
        });
        cbRefsName.valueProperty().addListener((ObservableValue<? extends RefsName> observable, RefsName oldValue, RefsName newValue) ->
        {
            refsData.clear();
            refsData.addAll(newValue.getRefsCollection());
            refsTable.setItems(refsData);
            refsTable.getSelectionModel().select(0);
        });
        
        addRefs.addEventHandler(ActionEvent.ACTION, addRefsEvent);
        cmAddRecord.addEventHandler(ActionEvent.ACTION, addRecordEvent);
        cmDelRecord.addEventHandler(ActionEvent.ACTION, delRecordEvent);
        cmSaveChanges.addEventHandler(ActionEvent.ACTION, saveRecordEvent);
        
        cmSaveChanges.disableProperty().bind(refsChanged.not());
        cmDelRecord.disableProperty().bind(Bindings.isEmpty(refsData));
        
        if (DBBean.getInstance().getRole().equals("OPERATOR"))
        {
            usersRole = new SimpleBooleanProperty(Boolean.FALSE);
            cmAddRecord.disableProperty().bind(usersRole.not());
            cmSaveChanges.disableProperty().bind(usersRole.not());
            cmDelRecord.disableProperty().bind(usersRole.not());
            addRefs.disableProperty().bind(usersRole.not());
        }
        
    }
    
    public BorderPane getRefsPane()
    {
        return bPane;
    }
    
    private final EventHandler<ActionEvent> addRefsEvent = (ActionEvent event) ->
    {
        Dialog dialog = new TextInputDialog();
        dialog.setTitle("Введите значение");
        dialog.setHeaderText("Введите название нового справочника");
        dialog.setResizable(true);
        dialog.setWidth(450);
        dialog.initStyle(StageStyle.UTILITY);
        Optional<Object> result = dialog.showAndWait();
        if (result != Optional.empty())
        {
            RefsName obj = new RefsName();
            obj.setRefName(dialog.getResult().toString());
            try
            {
                DBBean.getInstance().getRefsNameJPACtrl().create(obj);
                cbRefsName.getItems().clear();
                cbRefsName.setItems(FXCollections.observableArrayList(DBBean.getInstance().getRefsNameJPACtrl().findRefsNameEntities()));
            }
            catch (Exception ex)
            {
                DBBean.getInstance().showErrDialog(ex, "Ошибка добавления справочника", "");
            }
        }
    };
    
    //<editor-fold defaultstate="collapsed" desc="Обработчики контекстных меню">
    
    private final EventHandler<ActionEvent> addRecordEvent = (ActionEvent event) ->
    {
        if (cbRefsName.getValue() != null)
        {
            try
            {
                Refs obj = new Refs(new RefsPK());
                obj.getRefsPK().setIdRefName(cbRefsName.getValue().getIdRefName());
                obj.setRefsName(cbRefsName.getValue());
                obj.setDateCreate(new Date());
                obj.rec_state = DBBean.RECORD_STATE.NEW;
                refsTable.getItems().add(obj);
                refsTable.getSelectionModel().select(obj);
            }
            catch (Exception ex)
            {
                DBBean.getInstance().showErrDialog(ex, "Ошибка добавления записи", "");
            }
        }
        else
        {
            DBBean.getInstance().showWarningDialog("Выберите справочник", "Для добавления значения необходимо указать справочник!");
        }
    };
    
    private final EventHandler<ActionEvent> saveRecordEvent = (ActionEvent event) ->
    {
        ObservableList<Refs> modifyList = refsTable.getItems().filtered((Refs t) -> t.rec_state != DBBean.RECORD_STATE.SAVED);
        modifyList.forEach((Refs t) ->
        {
            switch(t.rec_state)
            {
                case NEW:
                {
                    try
                    {
                        DBBean.getInstance().getRefsJPACtrl().create(t);
                        t.rec_state = DBBean.RECORD_STATE.SAVED;
                    }
                    catch (NonexistentEntityException ex)
                    {
                        DBBean.getInstance().showErrDialog(ex, "Ошибка изменения записи!", "");
                    }
                    catch (Exception ex)
                    {
                        DBBean.getInstance().showErrDialog(ex, "Ошибка", "");
                    }
                    break;
                }
                case EDIT:
                {
                    try
                    {
                        DBBean.getInstance().getRefsJPACtrl().edit(t);
                        t.rec_state = DBBean.RECORD_STATE.SAVED;
                    }
                    catch (NonexistentEntityException ex)
                    {
                        DBBean.getInstance().showErrDialog(ex, "Ошибка изменения записи!", "");
                    }
                    catch (Exception ex)
                    {
                        DBBean.getInstance().showErrDialog(ex, "Ошибка", "");
                    }
                    break;
                }
                
            }
        });
        refsTable.refresh();
        refsChanged.setValue(Boolean.FALSE);
    };
    
    private final EventHandler<ActionEvent> delRecordEvent = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            if (DBBean.getInstance().showConfirmDialog("Вопрос", "Вы действительно хотите удалить запись?", "").get() == ButtonType.OK)
            {
                Refs obj = refsTable.getSelectionModel().getSelectedItem();
                switch(obj.rec_state)
                {
                    case NEW:
                    {
                        refsData.remove(obj);
                        break;
                    }
                    case EDIT:
                    {
                        try
                        {
                            DBBean.getInstance().getRefsJPACtrl().destroy(obj.getRefsPK());
                            refsData.remove(obj);
                        }
                        catch (NonexistentEntityException ex)
                        {
                            DBBean.getInstance().showErrDialog(ex, "Ошибка удаления записи", "");
                        }
                        break;
                    }
                    case SAVED:
                    {
                        try
                        {
                            DBBean.getInstance().getRefsJPACtrl().destroy(obj.getRefsPK());
                            refsData.remove(obj);
                        }
                        catch (NonexistentEntityException ex)
                        {
                            DBBean.getInstance().showErrDialog(ex, "Ошибка удаления записи", "");
                        }
                        break;
                    }
                }
                refsTable.refresh();
            }
        }
    };
    
    //</editor-fold>
    
}
