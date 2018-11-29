/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrent.view.controllers;

import carrent.beans.DBBean;
import carrent.entity.Cars;
import carrent.entity.Refs;
import carrent.entity.RefsPK;
import carrent.entity.controllers.exceptions.IllegalOrphanException;
import carrent.entity.controllers.exceptions.NonexistentEntityException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CarsViewController implements Initializable {

    private final ObservableList<Cars> carsData = FXCollections.observableArrayList();
    private final SimpleBooleanProperty carsChanged = new SimpleBooleanProperty(false);
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    
    /**
     * Добавить Кнопку обновить
     */
    
    @FXML
    private BorderPane carsPane;
    @FXML
    private TableView<Cars> carTable;
    @FXML
    private TableColumn<Cars, Integer> colIdCar, colDriveLength;
    @FXML
    private TableColumn<Cars, Refs> colMake, colModel, colStat;
    @FXML
    private TableColumn<Cars, String> colVin, colRegNum, colDateRelease, colOsago, colKasko, colTi;
    @FXML
    private MenuItem cmAddRecord, cmSaveChanges, cmDelRecord;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        carTable.setPlaceholder(new Label(""));
        carTable.getSelectionModel().setCellSelectionEnabled(true);
        //<editor-fold defaultstate="collapsed" desc="Инициализация колонок">
        
        colIdCar.setCellValueFactory((TableColumn.CellDataFeatures<Cars, Integer> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? 0 : (param.getValue().getIdCar() == null ? 0 : param.getValue().getIdCar())));
        
        colMake.setCellValueFactory((TableColumn.CellDataFeatures<Cars, Refs> param) -> new ReadOnlyObjectWrapper<>(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(3, param.getValue() == null ? 0 : param.getValue().getIdMake()))));
        colMake.setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Refs>() {
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
                        return val;
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
        colMake.setOnEditCommit((TableColumn.CellEditEvent<Cars, Refs> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setIdMake(event.getNewValue().getRefsPK().getIdRef());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            carsChanged.setValue(Boolean.TRUE);
        });
        
        colModel.setCellValueFactory((TableColumn.CellDataFeatures<Cars, Refs> param) -> new ReadOnlyObjectWrapper<>(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(2, param.getValue() == null ? 0 : param.getValue().getIdModel()))));
        colModel.setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Refs>() {
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
                        return val;
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
        colModel.setOnEditCommit((TableColumn.CellEditEvent<Cars, Refs> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setIdModel(event.getNewValue().getRefsPK().getIdRef());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            carsChanged.setValue(Boolean.TRUE);
        });
        
        colStat.setCellValueFactory((TableColumn.CellDataFeatures<Cars, Refs> param) -> new ReadOnlyObjectWrapper<>(DBBean.getInstance().getRefsJPACtrl().findRefs(new RefsPK(5, param.getValue() == null ? 0 : param.getValue().getIdStat()))));
        colStat.setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<Refs>() {
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
                        return val;
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
        colStat.setOnEditCommit((TableColumn.CellEditEvent<Cars, Refs> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setIdStat(event.getNewValue().getRefsPK().getIdRef());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            carsChanged.setValue(Boolean.TRUE);
        });
        
        colDateRelease.setCellValueFactory((TableColumn.CellDataFeatures<Cars, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getReleasedate() == null ? "" : format.format(param.getValue().getReleasedate()))));
        colDateRelease.setCellFactory(TextFieldTableCell.forTableColumn());
        colDateRelease.setOnEditCommit((TableColumn.CellEditEvent<Cars, String> event) ->
        {
            Date tmp = null;
            
            try
            {
                tmp = format.parse(event.getNewValue());
            }
            catch (ParseException ex)
            {
            }
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setReleasedate(tmp);
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            carsChanged.setValue(Boolean.TRUE);
        });
        
        colVin.setCellValueFactory((TableColumn.CellDataFeatures<Cars, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : param.getValue().getVin()));
        colVin.setCellFactory(TextFieldTableCell.forTableColumn());
        colVin.setOnEditCommit((TableColumn.CellEditEvent<Cars, String> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setVin(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            carsChanged.setValue(Boolean.TRUE);
        });
        
        colRegNum.setCellValueFactory((TableColumn.CellDataFeatures<Cars, String> p_param) -> new ReadOnlyObjectWrapper<>(p_param.getValue() == null ? "" : p_param.getValue().getNumberregistration()));
        colRegNum.setCellFactory(TextFieldTableCell.forTableColumn());
        colRegNum.setOnEditCommit((TableColumn.CellEditEvent<Cars, String> p_event) ->
        {
            p_event.getTableView().getItems().get(p_event.getTablePosition().getRow()).setNumberregistration(p_event.getNewValue());
            if (p_event.getTableView().getItems().get(p_event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                p_event.getTableView().getItems().get(p_event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            carsChanged.setValue(Boolean.TRUE);
        });
        
        colOsago.setCellValueFactory((TableColumn.CellDataFeatures<Cars, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getOsago() == null ? "" : format.format(param.getValue().getOsago()))));
        colOsago.setCellFactory(TextFieldTableCell.forTableColumn());
        colOsago.setOnEditCommit((TableColumn.CellEditEvent<Cars, String> event) ->
        {
            Date tmp = null;
            try
            {
                tmp = format.parse(event.getNewValue());
            }
            catch (ParseException ex)
            {
            }
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setOsago(tmp);
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            carsChanged.setValue(Boolean.TRUE);
        });
        
        colKasko.setCellValueFactory((TableColumn.CellDataFeatures<Cars, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getKacko() == null ? "" : format.format(param.getValue().getKacko()))));
        colKasko.setCellFactory(TextFieldTableCell.forTableColumn());
        colKasko.setOnEditCommit((TableColumn.CellEditEvent<Cars, String> event) ->
        {
            Date tmp = null;
            try
            {
                tmp = format.parse(event.getNewValue());
            }
            catch (ParseException ex)
            {
            }
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setKacko(tmp);
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            carsChanged.setValue(Boolean.TRUE);
        });
        
        colTi.setCellValueFactory((TableColumn.CellDataFeatures<Cars, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? "" : (param.getValue().getTechnicalinspection() == null ? "" : format.format(param.getValue().getTechnicalinspection()))));
        colTi.setCellFactory(TextFieldTableCell.forTableColumn());
        colTi.setOnEditCommit((TableColumn.CellEditEvent<Cars, String> event) ->
        {
            Date tmp = null;
            try
            {
                tmp = format.parse(event.getNewValue());
            }
            catch (ParseException ex)
            {
            }
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setTechnicalinspection(tmp);
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            carsChanged.setValue(Boolean.TRUE);
        });
        
        colDriveLength.setCellValueFactory((TableColumn.CellDataFeatures<Cars, Integer> param) -> new ReadOnlyObjectWrapper<>(param.getValue() == null ? 0 : param.getValue().getMilleage()));
        colDriveLength.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object)
            {
                return object == null ? "0" : object.toString();
            }

            @Override
            public Integer fromString(String string)
            {
                return string == null ? 0 : Integer.parseInt(string);
            }
        }));
        colDriveLength.setOnEditCommit((TableColumn.CellEditEvent<Cars, Integer> event) ->
        {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setMilleage(event.getNewValue());
            if (event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state != DBBean.RECORD_STATE.NEW)
                event.getTableView().getItems().get(event.getTablePosition().getRow()).rec_state = DBBean.RECORD_STATE.EDIT;
            carsChanged.setValue(Boolean.TRUE);
        });
        //</editor-fold>
        cmDelRecord.disableProperty().bind(Bindings.isEmpty(carsData));
        cmSaveChanges.disableProperty().bind(carsChanged.not());
        
        cmAddRecord.addEventHandler(ActionEvent.ACTION, addRecord);
        cmDelRecord.addEventHandler(ActionEvent.ACTION, delRecord);
        cmSaveChanges.addEventHandler(ActionEvent.ACTION, saveRecord);
    }    
    
    public BorderPane getCarsPane()
    {
        return carsPane;
    }
    
    public void initData()
    {
        carsData.clear();
        carsData.addAll(DBBean.getInstance().getCarsJPACtrl().findCarsEntities());
        carTable.setItems(carsData);
        carTable.getSelectionModel().select(0);
    }

    public TableColumn<Cars, Refs> getColMake()
    {
        return colMake;
    }

    public TableColumn<Cars, Refs> getColModel()
    {
        return colModel;
    }

    public TableColumn<Cars, Refs> getColStat()
    {
        return colStat;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Обработчики контекстного меню">
    private final EventHandler<ActionEvent> addRecord = (ActionEvent event) ->
    {
        Cars obj = new Cars();
        obj.rec_state = DBBean.RECORD_STATE.NEW;
        carsData.add(obj);
        carTable.refresh();
        carTable.getSelectionModel().select(obj);
        carsChanged.setValue(Boolean.TRUE);
    };
    
    private final EventHandler<ActionEvent> delRecord = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            if (DBBean.getInstance().showConfirmDialog("Вопрос", "Вы действительно хотите удалить запись?", "").get() == ButtonType.OK)
            {
                Cars obj = carTable.getSelectionModel().getSelectedItem();
                switch (obj.rec_state)
                {
                    case NEW:
                        carsData.remove(obj);
                        break;
                    case EDIT:
                        try
                        {
                            DBBean.getInstance().getCarsJPACtrl().destroy(obj.getIdCar());
                            carsData.remove(obj);
                        }
                        catch (NonexistentEntityException ex)
                        {
                            Logger.getLogger(CarsViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        catch (IllegalOrphanException ex)
                        {
                            Logger.getLogger(CarsViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                        break;

                    case SAVED:
                        try
                        {
                            DBBean.getInstance().getCarsJPACtrl().destroy(obj.getIdCar());
                            carsData.remove(obj);
                        }
                        catch (NonexistentEntityException ex)
                        {
                            Logger.getLogger(CarsViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        catch (IllegalOrphanException ex)
                        {
                            Logger.getLogger(CarsViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        break;

                }
                carTable.refresh();
                //carsChanged.setValue(Boolean.FALSE);
            }
        }
    };
    
    private final EventHandler<ActionEvent> saveRecord = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            ObservableList<Cars> modifyList = carTable.getItems().filtered((Cars t) -> t.rec_state != DBBean.RECORD_STATE.SAVED);
            modifyList.forEach((Cars obj) ->
            {
                switch(obj.rec_state)
                {
                    case EDIT:
                    {
                        try
                        {
                            DBBean.getInstance().getCarsJPACtrl().edit(obj);
                            obj.rec_state = DBBean.RECORD_STATE.SAVED;
                        }
                        catch(NonexistentEntityException ex)
                        {
                            DBBean.getInstance().showErrDialog(ex, "Ошибка изменения записи", "");
                        }
                        catch (Exception ex)
                        {
                            Logger.getLogger(CarsViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }
                    case NEW:
                        try
                        {
                            DBBean.getInstance().getCarsJPACtrl().create(obj);
                            obj.rec_state = DBBean.RECORD_STATE.SAVED;
                        }
                        catch (Exception ex)
                        {
                            DBBean.getInstance().showErrDialog(ex, "Ошибка", "");
                        }
                        break;
                    
                }
            });
            carTable.refresh();
            carsChanged.setValue(Boolean.FALSE);
        }
    };
    //</editor-fold>
    
    
}
