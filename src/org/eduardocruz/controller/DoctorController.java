package org.eduardocruz.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.eduardocruz.bean.Doctor;
import org.eduardocruz.bean.Especialidad;
import org.eduardocruz.db.Conexion;
import org.eduardocruz.report.GenerarReporte;
import org.eduardocruz.system.Principal;

public class DoctorController implements Initializable{
    private Principal escenarioPrincipal;
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Doctor> listaDoctor;
    private ObservableList <Especialidad> listaEspecialidad;
    
    @FXML private TextField txtNumeroColegiado;
    @FXML private TextField txtNombresDoctor;
    @FXML private TextField txtApellidosDoctor;
    @FXML private TextField txtTelefonoContacto;
    @FXML private ComboBox cmbCodigoEspecialidad;
    @FXML private TableView tblDoctores;
    @FXML private TableColumn colNumeroColegiado;
    @FXML private TableColumn colNombresDoctor;
    @FXML private TableColumn colApellidosDoctor;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEspecialidad;
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
        cmbCodigoEspecialidad.setItems(getEspecialidad());
        cmbCodigoEspecialidad.setDisable(true);
    }
    
    public void cargarDatos(){
        tblDoctores.setItems(getDoctor());
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("numeroColegiado"));
        colNombresDoctor.setCellValueFactory(new PropertyValueFactory<Doctor, String>("nombresDoctor"));
        colApellidosDoctor.setCellValueFactory(new PropertyValueFactory<Doctor, String>("apellidosDoctor"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Doctor, String>("telefonoContacto"));
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("codigoEspecialidad"));
    }
    
    public void seleccionarElemento(){
        try{
            if(tblDoctores.getSelectionModel().getSelectedItem() != null){
                txtNumeroColegiado.setText(String.valueOf(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
                txtNombresDoctor.setText(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getNombresDoctor());
                txtApellidosDoctor.setText(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getApellidosDoctor());
                txtTelefonoContacto.setText(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getTelefonoContacto());
                cmbCodigoEspecialidad.getSelectionModel().select(buscarEspecialidad(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
                btnReporte.setText("Receta");
                imgReporte.setImage(new Image("/org/eduardocruz/image/RecetaMedica.png"));
            }else{
                JOptionPane.showMessageDialog(null, "¡No has seleccionado nada!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public Especialidad buscarEspecialidad(int codigoEspecialidad){
        Especialidad resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEspecialidad(?)}");
            procedimiento.setInt(1, codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
		resultado = new Especialidad(registro.getInt("codigoEspecialidad"), registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
	return resultado;
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
        
        return listaDoctor = FXCollections.observableList(lista);
    }
    
    public ObservableList<Especialidad> getEspecialidad(){
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEspecialidades()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                                          resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEspecialidad = FXCollections.observableArrayList(lista);
    }
    
    public void estadoOriginal(){
        limpiarControles();
        desactivarControles();
        tblDoctores.getSelectionModel().clearSelection();
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
                if((txtNombresDoctor.getText().length()!=0)&(txtApellidosDoctor.getText().length()!=0)&
                        (txtTelefonoContacto.getText().length()!=0)&(cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()!=null)){
                    guardar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos.");
                }
                break;
            case ELIMINAR:
                if(txtNumeroColegiado.getText().length()!=0){
                    borrar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "¡No has seleccionado ningún doctor!");
                }
                break;
            case ACTUALIZAR:
                if((txtNumeroColegiado.getText().length()!=0)&(txtNombresDoctor.getText().length()!=0)&(txtApellidosDoctor.getText().length()!=0)&
                        (txtTelefonoContacto.getText().length()!=0)&(cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()!=null)){
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
            case ACTUALIZAR:
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
                tipoDeOperacion = operaciones.ACTUALIZAR;
                break;
        }
    }
    
    public void guardar(){
        Doctor registro = new Doctor();
        registro.setNumeroColegiado(Integer.parseInt(txtNumeroColegiado.getText()));
        registro.setNombresDoctor(txtNombresDoctor.getText());
        registro.setApellidosDoctor(txtApellidosDoctor.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEspecialidad(((Especialidad)cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDoctor(?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getNumeroColegiado());
            procedimiento.setString(2, registro.getNombresDoctor());
            procedimiento.setString(3, registro.getApellidosDoctor());
            procedimiento.setString(4, registro.getTelefonoContacto());
            procedimiento.setInt(5, registro.getCodigoEspecialidad());
            procedimiento.execute();
            listaDoctor.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void borrar(){
        Doctor registro = new Doctor();
        registro.setNumeroColegiado(Integer.parseInt(txtNumeroColegiado.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDoctor(?)}");
            procedimiento.setInt(1, registro.getNumeroColegiado());
            procedimiento.execute();
            listaDoctor.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void actualizar(){
        Doctor registro = (Doctor)tblDoctores.getSelectionModel().getSelectedItem();
        registro.setNumeroColegiado(Integer.parseInt(txtNumeroColegiado.getText()));
        registro.setNombresDoctor(txtNombresDoctor.getText());
        registro.setApellidosDoctor(txtApellidosDoctor.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEspecialidad(((Especialidad)cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDoctor(?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getNumeroColegiado());
            procedimiento.setString(2, registro.getNombresDoctor());
            procedimiento.setString(3, registro.getApellidosDoctor());
            procedimiento.setString(4, registro.getTelefonoContacto());
            procedimiento.setInt(5, registro.getCodigoEspecialidad());
            procedimiento.execute();
            listaDoctor.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtNumeroColegiado.setEditable(false);
        txtNombresDoctor.setEditable(false);
        txtApellidosDoctor.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        cmbCodigoEspecialidad.setDisable(true);
    }
    
    public void activarControles(){
        txtNumeroColegiado.setEditable(true);
        txtNombresDoctor.setEditable(true);
        txtApellidosDoctor.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        cmbCodigoEspecialidad.setDisable(false);
    }
    
    public void limpiarControles(){
        txtNumeroColegiado.clear();
        txtNombresDoctor.clear();
        txtApellidosDoctor.clear();
        txtTelefonoContacto.clear();
        tblDoctores.getSelectionModel().clearSelection();
        cmbCodigoEspecialidad.getSelectionModel().clearSelection();
    }
    
    private final String fondoReceta = "/org/eduardocruz/report/background/FondoReporteHorizontal.jpg";
    private final String fondoReporte = "/org/eduardocruz/report/background/FondoReporte.jpg";
    private final String icono = "/org/eduardocruz/report/background/Doctores.png";
    private final String firmaRecetaMedica = "/org/eduardocruz/report/background/firma.png";
    private final String logoRecetaMedica = "/org/eduardocruz/report/background/Simbolo.png";
    public void imprimirReporte(){
        Map parametros = new HashMap();
        try{
            if(tblDoctores.getSelectionModel().getSelectedItem() != null){
                parametros.put("Fondo", this.getClass().getResourceAsStream(fondoReceta));
                parametros.put("Firma", this.getClass().getResourceAsStream(firmaRecetaMedica));
                parametros.put("Logo", this.getClass().getResourceAsStream(logoRecetaMedica));
                parametros.put("numColegiado", ((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getNumeroColegiado());
                GenerarReporte.mostrarReporte("RecetaMedica.jasper", "Receta Médica", parametros);
            }else{
                parametros.put("fondo", this.getClass().getResourceAsStream(fondoReporte));
                parametros.put("icono", this.getClass().getResourceAsStream(icono));
                GenerarReporte.mostrarReporte("ReporteDoctores.jasper", "Reporte de doctores", parametros);
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
