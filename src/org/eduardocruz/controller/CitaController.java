package org.eduardocruz.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.eduardocruz.bean.Cita;
import org.eduardocruz.bean.Doctor;
import org.eduardocruz.bean.Paciente;
import org.eduardocruz.db.Conexion;
import org.eduardocruz.report.GenerarReporte;
import org.eduardocruz.system.Principal;

public class CitaController implements Initializable{
    private Principal escenarioPrincipal;
    
    private enum operaciones{NINGUNO, GUARDAR, ELIMINAR, EDITAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<Cita> listaCita;
    private ObservableList<Paciente> listaPacientes;
    private ObservableList<Doctor> listaDoctores;
    
    private DatePicker fCita;
    @FXML private TextField txtCodigoCita;
    @FXML private TextField txtTratamiento;
    @FXML private TextField txtDescCActual;
    @FXML private TextField txtHoraCita;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private ComboBox cmbNumeroColegiado;
    @FXML private GridPane grpFechas;
    @FXML private TableView tblCitas;
    @FXML private TableColumn colCodigoCita;
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colHoraCita;
    @FXML private TableColumn colTratamiento;
    @FXML private TableColumn colDescCActual;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private TableColumn colNumeroColegiado;
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
        cmbCodigoPaciente.setItems(getPaciente());
        cmbNumeroColegiado.setItems(getDoctor());
        cmbCodigoPaciente.setDisable(true);
        cmbNumeroColegiado.setDisable(true);
        
        fCita = new DatePicker(Locale.ENGLISH);
        fCita.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fCita.getCalendarView().todayButtonTextProperty().set("Today");
        fCita.getCalendarView().setShowWeeks(false);
        grpFechas.add(fCita, 3, 0);
        fCita.getStylesheets().add("/org/eduardocruz/resource/DatePicker.css");
        fCita.setAlignment(Pos.CENTER);
        fCita.setDisable(true);
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
    
    public ObservableList<Cita>getCita(){
        ArrayList<Cita> lista = new ArrayList<Cita>();       
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCitas()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Cita(resultado.getInt("codigoCita"), resultado.getDate("fechaCita"), resultado.getTime("horaCita"),
                                    resultado.getString("tratamiento"), resultado.getString("descCActual"), resultado.getInt("codigoPaciente"), resultado.getInt("numeroColegiado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCita = FXCollections.observableArrayList(lista);
    }
    
    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Paciente(registro.getInt("codigoPaciente"), registro.getString("nombresPaciente"),
                                        registro.getString("apellidosPaciente"), registro.getString("sexo"),
                                        registro.getDate("fechaNacimiento"), registro.getString("direccionPaciente"),
                                        registro.getString("telefonoPersonal"), registro.getDate("fechaPrimeraVisita"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    public Doctor buscarDoctor(int numeroColegiado){
        Doctor resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDoctor(?)}");
            procedimiento.setInt(1, numeroColegiado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Doctor(registro.getInt("numeroColegiado"), registro.getString("nombresDoctor"),
                                registro.getString("apellidosDoctor"), registro.getString("telefonoContacto"), registro.getInt("codigoEspecialidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Paciente> getPaciente(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPacientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"), resultado.getString("nombresPaciente"),
                                        resultado.getString("apellidosPaciente"), resultado.getString("sexo"),
                                        resultado.getDate("fechaNacimiento"), resultado.getString("direccionPaciente"),
                                        resultado.getString("telefonoPersonal"), resultado.getDate("fechaPrimeraVisita")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPacientes = FXCollections.observableArrayList(lista);
    }
    public ObservableList<Doctor> getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Doctor(resultado.getInt("numeroColegiado"), resultado.getString("nombresDoctor"), resultado.getString("apellidosDoctor"),
                        resultado.getString("telefonoContacto"), resultado.getInt("codigoEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctores = FXCollections.observableArrayList(lista);
    }
    
    public void cargarDatos(){
        tblCitas.setItems(getCita());
        colCodigoCita.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("codigoCita"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<Cita, Date>("fechaCita"));
        colHoraCita.setCellValueFactory(new PropertyValueFactory<Cita, Time>("horaCita"));
        colTratamiento.setCellValueFactory(new PropertyValueFactory<Cita, String>("tratamiento"));
        colDescCActual.setCellValueFactory(new PropertyValueFactory<Cita, String>("descCActual"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("codigoPaciente"));
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("numeroColegiado"));
    }
    
    public void seleccionarElemento(){
        try{
            if(tblCitas.getSelectionModel().getSelectedItem() != null){
                txtCodigoCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita()));
                fCita.selectedDateProperty().set(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getFechaCita());
                txtHoraCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita()));
                txtTratamiento.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getTratamiento());
                cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
                cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
                txtDescCActual.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getDescCActual());
                btnReporte.setText("Receta");
                imgReporte.setImage(new Image("/org/eduardocruz/image/RecetaMedica.png"));
            }else{
                JOptionPane.showMessageDialog(null, "¡No has seleccionado nada!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtCodigoCita.setEditable(false);
        fCita.setDisable(true);
        txtHoraCita.setEditable(false);
        txtTratamiento.setEditable(false);
        cmbNumeroColegiado.setDisable(true);
        cmbCodigoPaciente.setDisable(true);
        txtDescCActual.setEditable(false);
    }
    public void activarControles(){
        fCita.setDisable(false);
        txtHoraCita.setEditable(true);
        txtTratamiento.setEditable(true);
        cmbNumeroColegiado.setDisable(false);
        cmbCodigoPaciente.setDisable(false);
        txtDescCActual.setEditable(true);
    }
    public void limpiarControles(){
        txtCodigoCita.clear();
        fCita.setSelectedDate(null);
        txtHoraCita.clear();
        txtTratamiento.clear();
        cmbNumeroColegiado.getSelectionModel().clearSelection();
        cmbCodigoPaciente.getSelectionModel().clearSelection();
        txtDescCActual.clear();
    }
    
    public void guardar(){
        Cita registro = new Cita();
        registro.setFechaCita(fCita.getSelectedDate());
        registro.setHoraCita(Time.valueOf(txtHoraCita.getText()));
        registro.setTratamiento(txtTratamiento.getText());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        registro.setDescCActual(txtDescCActual.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCita(?, ?, ?, ?, ?, ?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(2, new java.sql.Time(registro.getHoraCita().getTime()));
            procedimiento.setString(3, registro.getTratamiento());
            procedimiento.setString(4, registro.getDescCActual());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.setInt(6, registro.getNumeroColegiado());
            procedimiento.execute();
            listaCita.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void borrar(){
        Cita registro = new Cita();
        registro.setCodigoCita(Integer.parseInt(txtCodigoCita.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCita(?)}");
            procedimiento.setInt(1, registro.getCodigoCita());
            procedimiento.execute();
            listaCita.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void actualizar(){
        Cita registro = (Cita)tblCitas.getSelectionModel().getSelectedItem();
        registro.setCodigoCita(Integer.parseInt(txtCodigoCita.getText()));
        registro.setFechaCita(fCita.getSelectedDate());
        registro.setHoraCita(Time.valueOf(txtHoraCita.getText()));
        registro.setTratamiento(txtTratamiento.getText());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        registro.setDescCActual(txtDescCActual.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCita(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoCita());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(3, new java.sql.Time(registro.getHoraCita().getTime()));
            procedimiento.setString(4, registro.getTratamiento());
            procedimiento.setString(5, registro.getDescCActual());
            procedimiento.setInt(6, registro.getCodigoPaciente());
            procedimiento.setInt(7, registro.getNumeroColegiado());
            procedimiento.execute();
            listaCita.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void estadoOriginal(){
        desactivarControles();
        limpiarControles();
        tblCitas.getSelectionModel().clearSelection();
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
                if((fCita.getSelectedDate()!=null)&(txtHoraCita.getText().length()!=0)&(txtTratamiento.getText().length()!=0)&
                        (cmbNumeroColegiado.getSelectionModel().getSelectedItem()!=null)&(cmbCodigoPaciente.getSelectionModel().getSelectedItem()!=null)&
                        (txtDescCActual.getText().length()!=0)){
                    guardar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos.");
                }
                break;
            case ELIMINAR:
                if(txtCodigoCita.getText().length()!=0){
                    borrar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "¡No has seleccionado ninguna Cita!");
                }
                break;
            case EDITAR:
                if((txtCodigoCita.getText().length()!=0)&(fCita.getSelectedDate()!=null)&(txtHoraCita.getText().length()!=0)&(txtTratamiento.getText().length()!=0)&
                        (cmbNumeroColegiado.getSelectionModel().getSelectedItem()!=null)&(cmbCodigoPaciente.getSelectionModel().getSelectedItem()!=null)&
                        (txtDescCActual.getText().length()!=0)){
                    actualizar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "¡No has seleccionado ninguna Cita!");
                }
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
    
    private final String fondoReporte="/org/eduardocruz/report/background/FondoReporteHorizontal.jpg";
    private final String icono = "/org/eduardocruz/report/background/Citas.png";
    private final String firmaRecetaMedica = "/org/eduardocruz/report/background/firma.png";
    private final String logoRecetaMedica = "/org/eduardocruz/report/background/Simbolo.png";
    public void imprimirReporte(){
        Map parametros = new HashMap();
        if(tblCitas.getSelectionModel().getSelectedItem() != null){
            try{
                parametros.put("Fondo", this.getClass().getResourceAsStream(fondoReporte));
                parametros.put("Firma", this.getClass().getResourceAsStream(firmaRecetaMedica));
                parametros.put("Logo", this.getClass().getResourceAsStream(logoRecetaMedica));
                parametros.put("codCita", ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita());
                GenerarReporte.mostrarReporte("RecetaMedica.jasper", "Receta Médica", parametros);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            try{
                parametros.put("fondo", this.getClass().getResourceAsStream(fondoReporte));
                parametros.put("icono", this.getClass().getResourceAsStream(icono));
                GenerarReporte.mostrarReporte("ReporteCitas.jasper", "Reporte de Citas", parametros);
            }catch(Exception e){
                e.printStackTrace();
            }
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
