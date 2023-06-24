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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.eduardocruz.bean.Especialidad;
import org.eduardocruz.db.Conexion;
import org.eduardocruz.report.GenerarReporte;
import org.eduardocruz.system.Principal;

public class EspecialidadController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Especialidad> listaEspecialidad;
    
    @FXML private TextField txtCodigoEspecialidad;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblEspecialidades;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colDescripcion;
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
    }
    
    public void cargarDatos(){
        tblEspecialidades.setItems(getEspecialidad());
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidad, Integer>("codigoEspecialidad"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Especialidad, String>("descripcion"));
    }
    
    public void seleccionarElemento(){
        try{
            if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                txtCodigoEspecialidad.setText(String.valueOf(((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
                txtDescripcion.setText(((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getDescripcion());
                btnReporte.setText("Receta");
                imgReporte.setImage(new Image("/org/eduardocruz/image/RecetaMedica.png"));
            }else{
                JOptionPane.showMessageDialog(null, "¡No has seleccionado nada!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ObservableList<Especialidad> getEspecialidad(){
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEspecialidades}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"), resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEspecialidad = FXCollections.observableArrayList(lista);
    }
    
    
    
    public void desactivarControles(){
        txtCodigoEspecialidad.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    
    public void activarControles(){
        txtDescripcion.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoEspecialidad.clear();
        txtDescripcion.clear();
    }
    
    public void estadoOriginal(){
        limpiarControles();
        desactivarControles();
        tblEspecialidades.getSelectionModel().clearSelection();
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
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/eduardocruz/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/eduardocruz/image/cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if((txtDescripcion.getText().length()!=0)){
                    guardar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos.");
                }
                break;
            case ELIMINAR:
                if((txtCodigoEspecialidad.getText().length()!=0)){
                    borrar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "¡No seleccionaste ninguna especialidad!");
                }
                break;
            case EDITAR:
                if((txtCodigoEspecialidad.getText().length()!=0)&(txtDescripcion.getText().length()!=0)){
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
                imgNuevo.setImage(new Image("/org/eduardocruz/image/ActualizarPlantilla.png"));
                imgEliminar.setImage(new Image("/org/eduardocruz/image/cancelar.png"));
                tipoDeOperacion = operaciones.EDITAR;
                break;
        }
    }
    
    public void guardar(){
        Especialidad registro = new Especialidad();
        registro.setDescripcion(txtDescripcion.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEspecialidad(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaEspecialidad.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void borrar(){
        Especialidad registro = new Especialidad();
        registro.setCodigoEspecialidad(Integer.parseInt(txtCodigoEspecialidad.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEspecialidad(?)}");
            procedimiento.setInt(1, registro.getCodigoEspecialidad());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEspecialidad(?, ?)}");
            Especialidad registro = (Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoEspecialidad());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private final String fondoReceta = "/org/eduardocruz/report/background/FondoReporteHorizontal.jpg";
    private final String fondoReporte = "/org/eduardocruz/report/background/FondoReporte.jpg";
    private final String icono = "/org/eduardocruz/report/background/Especialidades.png";
    private final String firmaRecetaMedica = "/org/eduardocruz/report/background/firma.png";
    private final String logoRecetaMedica = "/org/eduardocruz/report/background/Simbolo.png";
    public void imprimirReporte(){
        Map parametros = new HashMap();
        try{
            if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                parametros.put("Fondo", this.getClass().getResourceAsStream(fondoReceta));
                parametros.put("Firma", this.getClass().getResourceAsStream(firmaRecetaMedica));
                parametros.put("Logo", this.getClass().getResourceAsStream(logoRecetaMedica));
                parametros.put("codEspecialidad", ((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                GenerarReporte.mostrarReporte("RecetaMedica.jasper", "Receta Médica", parametros);
            }else{
                parametros.put("fondo", this.getClass().getResourceAsStream(fondoReporte));
                parametros.put("icono", this.getClass().getResourceAsStream(icono));
                GenerarReporte.mostrarReporte("ReporteEspecialidades.jasper", "Reporte de especialidades", parametros);
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
