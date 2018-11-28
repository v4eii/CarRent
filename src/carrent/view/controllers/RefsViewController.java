package carrent.view.controllers;

import carrent.MFormController;
import carrent.beans.DBBean;
import carrent.entity.Cars;
import carrent.entity.Clients;
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
import java.util.function.Consumer;
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
import javafx.scene.control.cell.ComboBoxTableCell;
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
    private boolean corrected = true;
    
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
            refsChanged.setValue(Boolean.TRUE);
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
            refsChanged.setValue(Boolean.TRUE);
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
            refsChanged.setValue(Boolean.TRUE);
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
        modifyList.forEach(new Consumer<Refs>()
        {
            @Override
            public void accept(Refs t)
            {
                if (t.getRefsPK().getIdRef() == 0)
                {
                    DBBean.getInstance().showWarningDialog("Внимание", "Укажите пропущенные кодовые значения");
                    corrected = false;
                    return;
                }
            }
        });
        modifyList.forEach((Refs t) ->
        {
            if (corrected)
            {
                switch (t.rec_state)
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
                            DBBean.getInstance().showErrDialog(ex, "Ошибка внесения записи", "");
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
                            DBBean.getInstance().showErrDialog(ex, "Ошибка изменения записи", "");
                        }
                        catch (Exception ex)
                        {
                            DBBean.getInstance().showErrDialog(ex, "Ошибка", "");
                        }
                        break;
                    }

                }
            }
        });
        refsTable.refresh();
        if (corrected)
            refsChanged.setValue(Boolean.FALSE);
        else
            refsChanged.setValue(Boolean.TRUE);
        corrected = true;
        if (MFormController.getCarsViewCtrl() != null)
        {
            //<editor-fold defaultstate="collapsed" desc="Переинициализация справочных колонок">
            MFormController.getCarsViewCtrl().getColMake().setCellValueFactory((TableColumn.CellDataFeatures<Cars, Refs> param) -> new ReadOnlyObjectWrapper<>(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(3, param.getValue() == null ? 0 : param.getValue().getIdMake()))));
            MFormController.getCarsViewCtrl().getColMake().setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Refs>() {
                @Override
                public String toString(Refs object)
                {
                    return object == null ? "" : object.getRefName();
                }

                @Override
                public Refs fromString(String string)
                {
                    for (Refs val : DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(3))
                    {
                        if (val.getRefName().equals(string))
                        {
                            return val;
                        }
                    }
                    return null;
                }
            }, FXCollections.observableArrayList(DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(3))
                    .filtered((Refs t) ->
                    {
                        Date tmp = t.getDateCancel();
                        if (tmp != null && tmp.before(new Date()))
                        {
                            return false;
                        }
                        return true;
                    })));

            MFormController.getCarsViewCtrl().getColModel().setCellValueFactory((TableColumn.CellDataFeatures<Cars, Refs> param) -> new ReadOnlyObjectWrapper<>(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(2, param.getValue() == null ? 0 : param.getValue().getIdModel()))));
            MFormController.getCarsViewCtrl().getColModel().setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Refs>() {
                @Override
                public String toString(Refs object)
                {
                    return object == null ? "" : object.getRefName();
                }

                @Override
                public Refs fromString(String string)
                {
                    for (Refs val : DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(2))
                    {
                        if (val.getRefName().equals(string))
                        {
                            return val;
                        }
                    }
                    return null;
                }
            }, FXCollections.observableArrayList(DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(2))
                    .filtered((Refs t) ->
                    {
                        Date tmp = t.getDateCancel();
                        if (tmp != null && tmp.before(new Date()))
                        {
                            return false;
                        }
                        return true;
                    })));

            MFormController.getCarsViewCtrl().getColStat().setCellValueFactory((TableColumn.CellDataFeatures<Cars, Refs> param) -> new ReadOnlyObjectWrapper<>(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(5, param.getValue() == null ? 0 : param.getValue().getIdStat()))));
            MFormController.getCarsViewCtrl().getColStat().setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Refs>() {
                @Override
                public String toString(Refs object)
                {
                    return object == null ? "" : object.getRefName();
                }

                @Override
                public Refs fromString(String string)
                {
                    for (Refs val : DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(5))
                    {
                        if (val.getRefName().equals(string))
                        {
                            return val;
                        }
                    }
                    return null;
                }
            }, FXCollections.observableArrayList(DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(5))
                    .filtered((Refs t) ->
                    {
                        Date tmp = t.getDateCancel();
                        if (tmp != null && tmp.before(new Date()))
                        {
                            return false;
                        }
                        return true;
                    })));
            //</editor-fold>
        }
        if (MFormController.getClientsViewCtrl() != null)
        {
            //<editor-fold defaultstate="collapsed" desc="Переинициализация справочных колонок">
            MFormController.getClientsViewCtrl().getColDocType().setCellValueFactory((TableColumn.CellDataFeatures<Clients, Refs> param) -> new ReadOnlyObjectWrapper<>(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(1, param.getValue() == null ? 0 : param.getValue().getIdDoc()))));
            MFormController.getClientsViewCtrl().getColDocType().setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Refs>() {
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
                        {
                            return val;
                        }
                    }
                    return null;
                }
            }, FXCollections.observableArrayList(DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(1))
                    .filtered((Refs t) ->
                    {
                        Date tmp = t.getDateCancel();
                        if (tmp != null && tmp.before(new Date()))
                        {
                            return false;
                        }
                        return true;
                    })));
            //</editor-fold>
        }
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
                        DBBean.getInstance().showWarningDialog("Предупреждение", "Данная запись может использоваться. Для блокировки укажите дату.");
                        break;
                    }
                    case SAVED:
                    {
                        DBBean.getInstance().showWarningDialog("Предупреждение", "Данная запись может использоваться. Для блокировки укажите дату.");
                        break;
                    }
                }
                refsTable.refresh();
                if (MFormController.getCarsViewCtrl() != null)
                {
                    //<editor-fold defaultstate="collapsed" desc="Переинициализация справочных колонок">
                    MFormController.getCarsViewCtrl().getColMake().setCellValueFactory((TableColumn.CellDataFeatures<Cars, Refs> param) -> new ReadOnlyObjectWrapper<>(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(3, param.getValue() == null ? 0 : param.getValue().getIdMake()))));
                    MFormController.getCarsViewCtrl().getColMake().setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Refs>() {
                        @Override
                        public String toString(Refs object)
                        {
                            return object == null ? "" : object.getRefName();
                        }

                        @Override
                        public Refs fromString(String string)
                        {
                            for (Refs val : DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(3))
                            {
                                if (val.getRefName().equals(string))
                                {
                                    return val;
                                }
                            }
                            return null;
                        }
                    }, FXCollections.observableArrayList(DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(3))
                            .filtered((Refs t) ->
                            {
                                Date tmp = t.getDateCancel();
                                if (tmp != null && tmp.before(new Date()))
                                {
                                    return false;
                                }
                                return true;
                            })));

                    MFormController.getCarsViewCtrl().getColModel().setCellValueFactory((TableColumn.CellDataFeatures<Cars, Refs> param) -> new ReadOnlyObjectWrapper<>(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(2, param.getValue() == null ? 0 : param.getValue().getIdModel()))));
                    MFormController.getCarsViewCtrl().getColModel().setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Refs>() {
                        @Override
                        public String toString(Refs object)
                        {
                            return object == null ? "" : object.getRefName();
                        }

                        @Override
                        public Refs fromString(String string)
                        {
                            for (Refs val : DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(2))
                            {
                                if (val.getRefName().equals(string))
                                {
                                    return val;
                                }
                            }
                            return null;
                        }
                    }, FXCollections.observableArrayList(DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(2))
                            .filtered((Refs t) ->
                            {
                                Date tmp = t.getDateCancel();
                                if (tmp != null && tmp.before(new Date()))
                                {
                                    return false;
                                }
                                return true;
                            })));

                    MFormController.getCarsViewCtrl().getColStat().setCellValueFactory((TableColumn.CellDataFeatures<Cars, Refs> param) -> new ReadOnlyObjectWrapper<>(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(5, param.getValue() == null ? 0 : param.getValue().getIdStat()))));
                    MFormController.getCarsViewCtrl().getColStat().setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Refs>() {
                        @Override
                        public String toString(Refs object)
                        {
                            return object == null ? "" : object.getRefName();
                        }

                        @Override
                        public Refs fromString(String string)
                        {
                            for (Refs val : DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(5))
                            {
                                if (val.getRefName().equals(string))
                                {
                                    return val;
                                }
                            }
                            return null;
                        }
                    }, FXCollections.observableArrayList(DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(5))
                            .filtered((Refs t) ->
                            {
                                Date tmp = t.getDateCancel();
                                if (tmp != null && tmp.before(new Date()))
                                {
                                    return false;
                                }
                                return true;
                            })));
                    //</editor-fold>
                }
                if (MFormController.getClientsViewCtrl() != null)
                {
                    //<editor-fold defaultstate="collapsed" desc="Переинициализация справочных колонок">
                    MFormController.getClientsViewCtrl().getColDocType().setCellValueFactory((TableColumn.CellDataFeatures<Clients, Refs> param) -> new ReadOnlyObjectWrapper<>(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(1, param.getValue() == null ? 0 : param.getValue().getIdDoc()))));
                    MFormController.getClientsViewCtrl().getColDocType().setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Refs>() {
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
                                {
                                    return val;
                                }
                            }
                            return null;
                        }
                    }, FXCollections.observableArrayList(DBBean.getInstance().getRefsJPACtrl().getRefsByIdRefsName(1))
                            .filtered((Refs t) ->
                            {
                                Date tmp = t.getDateCancel();
                                if (tmp != null && tmp.before(new Date()))
                                {
                                    return false;
                                }
                                return true;
                            })));
                    //</editor-fold>
                }
            }
        }
    };
    
    //</editor-fold>
    
}
