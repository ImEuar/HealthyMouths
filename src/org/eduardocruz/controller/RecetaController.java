package org.eduardocruz.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.eduardocruz.bean.Doctor;
import org.eduardocruz.bean.Receta;
import org.eduardocruz.db.Conexion;
import org.eduardocruz.report.GenerarReporte;
import org.eduardocruz.system.Principal;

public class RecetaController implements Initializable{
    private Principal escenarioPrincipal;
    
    private DatePicker fReceta;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fReceta = new DatePicker(Locale.ENGLISH);
        fReceta.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fReceta.getCalendarView().todayButtonTextProperty().set("Today");
        fReceta.getCalendarView().setShowWeeks(false);
        grpFechas.add(fReceta, 3, 0);
        fReceta.getStylesheets().add("/org/eduardocruz/resource/DatePicker.css");
        fReceta.setAlignment(Pos.CENTER);
        fReceta.setDisable(true);
        
        cmbNumeroColegiado.setItems(getDoctor());
        cmbNumeroColegiado.setDisable(true);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Receta> listaReceta;
    private ObservableList <Doctor> listaDoctores;
    
    @FXML private TextField txtCodigoReceta;
    @FXML private ComboBox cmbNumeroColegiado;
    @FXML private TableView tblRecetas;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colFechaReceta;
    @FXML private TableColumn colNumeroColegiado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private GridPane grpFechas;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    public ObservableList <Receta> getReceta(){
        ArrayList<Receta> lista = new ArrayList<Receta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Receta(resultado.getInt("codigoReceta"), resultado.getDate("fechaReceta"), resultado.getInt("numeroColegiado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaReceta = FXCollections.observableList(lista);
    }
    
    public void cargarDatos(){
        tblRecetas.setItems(getReceta());
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<Receta, Integer>("codigoReceta"));
        colFechaReceta.setCellValueFactory(new PropertyValueFactory<Receta, Date>("fechaReceta"));
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Receta, Integer>("numeroColegiado"));
    }
    
    public Doctor buscarDoctor(int numeroColegiado){
        Doctor resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDoctor(?)}");
            procedimiento.setInt(1, numeroColegiado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Doctor(registro.getInt("numeroColegiado"),
                                    registro.getString("nombresDoctor"), 
                                    registro.getString("apellidosDoctor"),
                                    registro.getString("telefonoContacto"),
                                    registro.getInt("codigoEspecialidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void seleccionarElemento(){
        try{
            if(tblRecetas.getSelectionModel().getSelectedItem() != null){
                txtCodigoReceta.setText(String.valueOf(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
                fReceta.selectedDateProperty().set(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getFechaReceta());
                cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
                btnReporte.setText("Receta");
                imgReporte.setImage(new Image("/org/eduardocruz/image/RecetaMedica.png"));
            }else{
                JOptionPane.showMessageDialog(null, "¡No has seleccionado nada!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ObservableList<Doctor> getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Doctor(resultado.getInt("numeroColegiado"),
                                    resultado.getString("nombresDoctor"), 
                                    resultado.getString("apellidosDoctor"),
                                    resultado.getString("telefonoContacto"),
                                    resultado.getInt("codigoEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctores = FXCollections.observableArrayList(lista);
    }
    
    public void desactivarControles(){
        txtCodigoReceta.setEditable(false);
        cmbNumeroColegiado.setDisable(true);
        fReceta.setDisable(true);
    }
    public void activarControles(){
        cmbNumeroColegiado.setDisable(false);
        fReceta.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoReceta.clear();
        fReceta.setSelectedDate(null);
        cmbNumeroColegiado.getSelectionModel().clearSelection();
    }
    
    public void guardar(){
            Receta registro = new Receta();
            registro.setFechaReceta(fReceta.getSelectedDate());
            registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarReceta(?, ?)}");
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaReceta().getTime()));
                procedimiento.setInt(2, registro.getNumeroColegiado());
                procedimiento.execute();
                listaReceta.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    public void borrar(){
        Receta registro = new Receta();
        registro.setCodigoReceta(Integer.parseInt(txtCodigoReceta.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarReceta(?)}");
            procedimiento.setInt(1, registro.getCodigoReceta());
            procedimiento.execute();
            listaReceta.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void actualizar(){
        Receta registro = (Receta)tblRecetas.getSelectionModel().getSelectedItem();
        registro.setCodigoReceta(Integer.parseInt(txtCodigoReceta.getText()));
        registro.setFechaReceta(fReceta.getSelectedDate());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarReceta(?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoReceta());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.setInt(3, registro.getNumeroColegiado());
            procedimiento.execute();
            listaReceta.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void estadoOriginal(){
        limpiarControles();
        desactivarControles();
        tblRecetas.getSelectionModel().clearSelection();
        btnNuevo.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnReporte.setText("Reporte");
        imgNuevo.setImage(new Image("/org/eduardocruz/image/Nuevo.png"));
        imgEliminar.setImage(new Image("/org/eduardocruz/image/Eliminar.png"));
        imgReporte.setImage(new Image("/org/eduardocruz/image/Reporte.png"));
        btnEditar.setDisable(false);
        btnReporte.setDisable(false);
        tipoDeOperacion = operaciones.NINGUNO;
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/eduardocruz/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/eduardocruz/image/cancelar.png"));
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if((cmbNumeroColegiado.getSelectionModel().getSelectedItem() !=null) & (fReceta.getSelectedDate() != null)){
                    guardar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos.");
                }
                break;
            case ELIMINAR:
                if(txtCodigoReceta.getText().length()!=0){
                    borrar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "¡No seleccionaste ninguna receta!");
                }
                break;
            case EDITAR:
                if((txtCodigoReceta.getText().length()!=0)&(cmbNumeroColegiado.getSelectionModel().getSelectedItem() !=null) & (fReceta.getSelectedDate() != null)){
                    actualizar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos.");
                }
                break;
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Borrar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/eduardocruz/image/borrar.png"));
                imgEliminar.setImage(new Image("/org/eduardocruz/image/cancelar.png"));
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.ELIMINAR;
                break;
            case GUARDAR:
                estadoOriginal();
                break;
            case ELIMINAR:
                estadoOriginal();
                break;
            case EDITAR:
                estadoOriginal();
                break;
        }
    }
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Actualizar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/eduardocruz/image/ActualizarPlantilla.png"));
                imgEliminar.setImage(new Image("/org/eduardocruz/image/cancelar.png"));
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.EDITAR;
                break;
        }
    }
    
    private final String fondoReceta = "/org/eduardocruz/report/background/FondoReporteHorizontal.jpg";
    private final String fondoReporte = "/org/eduardocruz/report/background/FondoReporte.jpg";
    private final String icono = "/org/eduardocruz/report/background/Recetas.png";
    private final String firmaRecetaMedica = "/org/eduardocruz/report/background/firma.png";
    private final String logoRecetaMedica = "/org/eduardocruz/report/background/Simbolo.png";
    public void imprimirReporte(){
        Map parametros = new HashMap();
        try{
            if(tblRecetas.getSelectionModel().getSelectedItem() != null){
                parametros.put("Fondo", this.getClass().getResourceAsStream(fondoReceta));
                parametros.put("Firma", this.getClass().getResourceAsStream(firmaRecetaMedica));
                parametros.put("Logo", this.getClass().getResourceAsStream(logoRecetaMedica));
                parametros.put("codReceta", ((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
                GenerarReporte.mostrarReporte("RecetaMedica.jasper", "Receta Médica", parametros);
            }else{
                parametros.put("fondo", this.getClass().getResourceAsStream(fondoReporte));
                parametros.put("icono", this.getClass().getResourceAsStream(icono));
                GenerarReporte.mostrarReporte("ReporteRecetas.jasper", "Reporte de recetas", parametros);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                break;
        }
    }
    
}
