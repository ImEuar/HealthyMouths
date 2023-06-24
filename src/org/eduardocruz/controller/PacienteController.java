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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.eduardocruz.bean.Paciente;
import org.eduardocruz.db.Conexion;
import org.eduardocruz.report.GenerarReporte;
import org.eduardocruz.system.Principal;

public class PacienteController implements Initializable{
    private Principal escenarioPrincipal;
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Paciente> listaPaciente;
    private DatePicker fNacimiento, fPV;
    @FXML private TextField txtCodigoPaciente;
    @FXML private TextField txtNombresPaciente;
    @FXML private TextField txtApellidosPaciente;
    @FXML private TextField txtSexo;
    @FXML private TextField txtDireccionPaciente;
    @FXML private TextField txtTelefonoPersonal;
    @FXML private GridPane grpFechas;
    @FXML private TableView tblPacientes;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private TableColumn colNombresPaciente;
    @FXML private TableColumn colApellidosPaciente;
    @FXML private TableColumn colSexo;
    @FXML private TableColumn colDireccionPaciente;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colFechaNacimiento;
    @FXML private TableColumn colFechaPrimeraVisita;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fNacimiento = new DatePicker(Locale.ENGLISH);
        fNacimiento.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fNacimiento.getCalendarView().todayButtonTextProperty().set("Today");
        fNacimiento.getCalendarView().setShowWeeks(false);
        grpFechas.add(fNacimiento, 3, 1);
        fPV = new DatePicker(Locale.ENGLISH);
        fPV.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fPV.getCalendarView().todayButtonTextProperty().set("Today");
        fPV.getCalendarView().setShowWeeks(false);
        grpFechas.add(fPV, 3, 2);
        fNacimiento.getStylesheets().add("/org/eduardocruz/resource/DatePicker.css");
        fNacimiento.setAlignment(Pos.CENTER);
        fPV.getStylesheets().add("/org/eduardocruz/resource/DatePicker.css");
        fPV.setAlignment(Pos.CENTER);
        fPV.setDisable(true);
        fNacimiento.setDisable(true);
    }
    
    public void cargarDatos(){
        tblPacientes.setItems(getPaciente());
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("codigoPaciente"));
        colNombresPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, String>("nombresPaciente"));
        colApellidosPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, String>("apellidosPaciente"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente, String>("sexo"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente, Date>("fechaNacimiento"));
        colDireccionPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, String>("direccionPaciente"));
        colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<Paciente, String>("telefonoPersonal"));
        colFechaPrimeraVisita.setCellValueFactory(new PropertyValueFactory<Paciente, Date>("fechaPrimeraVisita"));
    }   
    
    public void seleccionarElemento(){
        try{
            if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                txtCodigoPaciente.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
                txtNombresPaciente.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getNombresPaciente());
                txtApellidosPaciente.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getApellidosPaciente());
                txtSexo.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getSexo());
                fNacimiento.selectedDateProperty().set(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
                txtDireccionPaciente.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccionPaciente());
                txtTelefonoPersonal.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
                fPV.selectedDateProperty().set(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaPrimeraVisita());
                btnReporte.setText("Receta");
                imgReporte.setImage(new Image("/org/eduardocruz/image/RecetaMedica.png"));
            } else{
                JOptionPane.showMessageDialog(null, "¡No has seleccionado nada!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ObservableList<Paciente> getPaciente(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                        resultado.getString("nombresPaciente"),
                        resultado.getString("apellidosPaciente"),
                        resultado.getString("sexo"),
                        resultado.getDate("fechaNacimiento"),
                        resultado.getString("direccionPaciente"),
                        resultado.getString("telefonoPersonal"),
                        resultado.getDate("fechaPrimeraVisita")));
            }
        }catch(Exception e){
           e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableArrayList(lista);
    }
    
    public void estadoOriginal(){
        limpiarControles();
        desactivarControles();
        tblPacientes.getSelectionModel().clearSelection();
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
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/eduardocruz/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/eduardocruz/image/cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if((txtNombresPaciente.getText().length()!=0)&(txtApellidosPaciente.getText().length()!=0)&(txtSexo.getText().length()!=0)&
                        (fNacimiento.getSelectedDate()!=null)&(txtDireccionPaciente.getText().length()!=0)&(txtTelefonoPersonal.getText().length()!=0)&
                        (fPV.getSelectedDate()!=null)){
                    guardar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos.");
                }
                break;
            case ELIMINAR:
                if(txtCodigoPaciente.getText().length()!=0){
                    borrar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "¡No has seleccionado ningún paciente!");
                }
                break;
            case EDITAR:
                if((txtCodigoPaciente.getText().length()!=0)&(txtNombresPaciente.getText().length()!=0)&(txtApellidosPaciente.getText().length()!=0)&(txtSexo.getText().length()!=0)&
                        (fNacimiento.getSelectedDate()!=null)&(txtDireccionPaciente.getText().length()!=0)&(txtTelefonoPersonal.getText().length()!=0)&
                        (fPV.getSelectedDate()!=null)){
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
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/eduardocruz/image/borrar.png"));
                imgEliminar.setImage(new Image("/org/eduardocruz/image/cancelar.png"));
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
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/eduardocruz/image/actualizar.png"));
                imgEliminar.setImage(new Image("/org/eduardocruz/image/cancelar.png"));
                tipoDeOperacion = operaciones.EDITAR;
                break;
        }
            
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                break;
        }
    }
    
    public void guardar(){
        Paciente registro = new Paciente();
        registro.setCodigoPaciente(Integer.parseInt(txtCodigoPaciente.getText()));
        registro.setNombresPaciente(txtNombresPaciente.getText());
        registro.setApellidosPaciente(txtApellidosPaciente.getText());
        registro.setSexo(txtSexo.getText());
        registro.setFechaNacimiento(fNacimiento.getSelectedDate());
        registro.setDireccionPaciente(txtDireccionPaciente.getText());
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setFechaPrimeraVisita(fPV.getSelectedDate());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPaciente(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getNombresPaciente());
            procedimiento.setString(3, registro.getApellidosPaciente());
            procedimiento.setString(4, registro.getSexo());
            procedimiento.setDate(5, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(6, registro.getDireccionPaciente());
            procedimiento.setString(7, registro.getTelefonoPersonal());
            procedimiento.setDate(8, new java.sql.Date(registro.getFechaPrimeraVisita().getTime()));
            procedimiento.execute();
            listaPaciente.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void borrar(){
        Paciente registro = new Paciente();
        registro.setCodigoPaciente(Integer.parseInt(txtCodigoPaciente.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPaciente(?)}");
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarPaciente(?, ?, ?, ?, ?, ?, ?, ?)}");
            Paciente registro = (Paciente)tblPacientes.getSelectionModel().getSelectedItem();
            registro.setNombresPaciente(txtNombresPaciente.getText());
            registro.setApellidosPaciente(txtApellidosPaciente.getText());
            registro.setSexo(txtSexo.getText());
            registro.setFechaNacimiento(fNacimiento.getSelectedDate());
            registro.setDireccionPaciente(txtDireccionPaciente.getText());
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setFechaPrimeraVisita(fPV.getSelectedDate());
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getNombresPaciente());
            procedimiento.setString(3, registro.getApellidosPaciente());
            procedimiento.setString(4, registro.getSexo());
            procedimiento.setDate(5, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(6, registro.getDireccionPaciente());
            procedimiento.setString(7, registro.getTelefonoPersonal());
            procedimiento.setDate(8, new java.sql.Date(registro.getFechaPrimeraVisita().getTime()));
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private final String fondoReporte="/org/eduardocruz/report/FondoReporteHorizontal.jpg";
    private final String icono = "/org/eduardocruz/report/Pacientes.png";
    private final String firmaRecetaMedica = "/org/eduardocruz/report/background/firma.png";
    private final String logoRecetaMedica = "/org/eduardocruz/report/background/Simbolo.png";
    public void imprimirReporte(){
        Map parametros = new HashMap();
        if(tblPacientes.getSelectionModel().getSelectedItem() != null){
            try{
                parametros.put("Fondo", this.getClass().getResourceAsStream(fondoReporte));
                parametros.put("Firma", this.getClass().getResourceAsStream(firmaRecetaMedica));
                parametros.put("Logo", this.getClass().getResourceAsStream(logoRecetaMedica));
                parametros.put("codPaciente", ((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                GenerarReporte.mostrarReporte("RecetaMedica.jasper", "Receta Medica", parametros);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            try{
            parametros.put("fondo", this.getClass().getResourceAsStream(fondoReporte));
            parametros.put("logo", this.getClass().getResourceAsStream(icono));
            GenerarReporte.mostrarReporte("ReportePacientes.jasper", "Reporte de Pacientes", parametros);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void desactivarControles(){
        txtCodigoPaciente.setEditable(false);
        txtNombresPaciente.setEditable(false);
        txtApellidosPaciente.setEditable(false);
        txtSexo.setEditable(false);
        txtDireccionPaciente.setEditable(false);
        txtTelefonoPersonal.setEditable(false);
        fPV.setDisable(true);
        fNacimiento.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoPaciente.setEditable(true);
        txtNombresPaciente.setEditable(true);
        txtApellidosPaciente.setEditable(true);
        txtSexo.setEditable(true);
        txtDireccionPaciente.setEditable(true);
        txtTelefonoPersonal.setEditable(true);
        fPV.setDisable(false);
        fNacimiento.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoPaciente.clear();
        txtNombresPaciente.clear();
        txtApellidosPaciente.clear();
        txtSexo.clear();
        txtDireccionPaciente.clear();
        txtTelefonoPersonal.clear();
        fNacimiento.setSelectedDate(null);
        fPV.setSelectedDate(null);
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
}
