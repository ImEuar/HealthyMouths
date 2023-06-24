package org.eduardocruz.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.eduardocruz.bean.DetalleReceta;
import org.eduardocruz.bean.Medicamento;
import org.eduardocruz.bean.Receta;
import org.eduardocruz.db.Conexion;
import org.eduardocruz.report.GenerarReporte;
import org.eduardocruz.system.Principal;

public class DetalleRecetaController implements Initializable{

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoReceta.setItems(getReceta());
        cmbCodigoReceta.setDisable(true);
        cmbCodigoMedicamento.setItems(getMedicamento());
        cmbCodigoMedicamento.setDisable(true);
    }
    
    private Principal escenarioPrincipal;
    
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
    private ObservableList <DetalleReceta> listaDetalleReceta;
    private ObservableList <Receta> listaRecetas;
    private ObservableList <Medicamento> listaMedicamentos;
    
    @FXML private TextField txtCodigoDetalleReceta;
    @FXML private TextField txtDosis;
    @FXML private ComboBox cmbCodigoReceta;
    @FXML private ComboBox cmbCodigoMedicamento;
    @FXML private TableView tblDetalleRecetas;
    @FXML private TableColumn colCodigoDetalleReceta;
    @FXML private TableColumn colDosis;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colCodigoMedicamento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    public ObservableList<DetalleReceta> getDetalleReceta(){
        ArrayList<DetalleReceta> lista = new ArrayList<DetalleReceta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDetalleRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleReceta(resultado.getInt("codigoDetalleReceta"),
                                            resultado.getString("dosis"),
                                            resultado.getInt("codigoReceta"),
                                            resultado.getInt("codigoMedicamento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDetalleReceta = FXCollections.observableList(lista);
    }
    
    public void cargarDatos(){
        tblDetalleRecetas.setItems(getDetalleReceta());
        colCodigoDetalleReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoDetalleReceta"));
        colDosis.setCellValueFactory(new PropertyValueFactory<DetalleReceta, String>("dosis"));
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoReceta"));
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoMedicamento"));
    }
    
    public Receta buscarReceta(int codigoReceta){
        Receta resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarReceta(?)}");
            procedimiento.setInt(1, codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Receta(registro.getInt("codigoReceta"), registro.getDate("fechaReceta"), registro.getInt("numeroColegiado"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Receta> getReceta(){
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
        return listaRecetas = FXCollections.observableArrayList(lista);
    }
    public Medicamento buscarMedicamento(int codigoMedicamento){
        Medicamento resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarMedicamento(?)}");
            procedimiento.setInt(1, codigoMedicamento);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medicamento(registro.getInt("codigoMedicamento"), registro.getString("nombreMedicamento"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Medicamento> getMedicamento(){
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medicamento(resultado.getInt("codigoMedicamento"), resultado.getString("nombreMedicamento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedicamentos = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        try{
            if(tblDetalleRecetas.getSelectionModel().getSelectedItem() != null){
                txtCodigoDetalleReceta.setText(String.valueOf(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta()));
                txtDosis.setText(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getDosis());
                cmbCodigoReceta.getSelectionModel().select(buscarReceta(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
                cmbCodigoMedicamento.getSelectionModel().select(buscarMedicamento(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
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
        txtCodigoDetalleReceta.setEditable(false);
        txtDosis.setEditable(false);
        cmbCodigoReceta.setDisable(true);
        cmbCodigoMedicamento.setDisable(true);
    }
    public void activarControles(){
        txtDosis.setEditable(true);
        cmbCodigoReceta.setDisable(false);
        cmbCodigoMedicamento.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoDetalleReceta.clear();
        txtDosis.clear();
        cmbCodigoReceta.getSelectionModel().clearSelection();
        cmbCodigoMedicamento.getSelectionModel().clearSelection();
    }
    
    public void guardar(){
        DetalleReceta registro = new DetalleReceta();
        registro.setDosis(txtDosis.getText());
        registro.setCodigoReceta(((Receta)cmbCodigoReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
        registro.setCodigoMedicamento(((Medicamento)cmbCodigoMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDetalleReceta(?, ?, ?)}");
            procedimiento.setString(1, registro.getDosis());
            procedimiento.setInt(2, registro.getCodigoReceta());
            procedimiento.setInt(3, registro.getCodigoMedicamento());
            procedimiento.execute();
            listaDetalleReceta.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    } 
    public void borrar(){
        DetalleReceta registro = new DetalleReceta();
        registro.setCodigoDetalleReceta(Integer.parseInt(txtCodigoDetalleReceta.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDetalleReceta(?)}");
            procedimiento.setInt(1, registro.getCodigoDetalleReceta());
            procedimiento.execute();
            listaDetalleReceta.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void actualizar(){
        DetalleReceta registro = (DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem();
        registro.setCodigoDetalleReceta(Integer.parseInt(txtCodigoDetalleReceta.getText()));
        registro.setDosis(txtDosis.getText());
        registro.setCodigoReceta(((Receta)cmbCodigoReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
        registro.setCodigoMedicamento(((Medicamento)cmbCodigoMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDetalleReceta(?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoDetalleReceta());
            procedimiento.setString(2, registro.getDosis());
            procedimiento.setInt(3, registro.getCodigoReceta());
            procedimiento.setInt(4, registro.getCodigoMedicamento());
            procedimiento.execute();
            listaDetalleReceta.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void estadoOriginal(){
        limpiarControles();
        desactivarControles();
        tblDetalleRecetas.getSelectionModel().clearSelection();
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
                if((txtDosis.getText().length()!=0)&(cmbCodigoReceta.getSelectionModel().getSelectedItem()!=null)&
                        (cmbCodigoMedicamento.getSelectionModel().getSelectedItem()!=null)){
                    guardar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos.");
                }
                break;
            case ELIMINAR:
                if(txtCodigoDetalleReceta.getText().length()!=0){
                    borrar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "¡No has seleccionado ningún detalle de receta!");
                }
                break;
            case EDITAR:
                if((txtCodigoDetalleReceta.getText().length()!=0)&(txtDosis.getText().length()!=0)&(cmbCodigoReceta.getSelectionModel().getSelectedItem()!=null)&
                        (cmbCodigoMedicamento.getSelectionModel().getSelectedItem()!=null)){
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
    private final String icono = "/org/eduardocruz/report/background/DetalleRecetas.png";
    private final String firmaRecetaMedica = "/org/eduardocruz/report/background/firma.png";
    private final String logoRecetaMedica = "/org/eduardocruz/report/background/Simbolo.png";
    public void imprimirReporte(){
        Map parametros = new HashMap();
        try{
            if(tblDetalleRecetas.getSelectionModel().getSelectedItem() != null){
                parametros.put("Fondo", this.getClass().getResourceAsStream(fondoReceta));
                parametros.put("Firma", this.getClass().getResourceAsStream(firmaRecetaMedica));
                parametros.put("Logo", this.getClass().getResourceAsStream(logoRecetaMedica));
                parametros.put("codDetalleReceta", ((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta());
                GenerarReporte.mostrarReporte("RecetaMedica.jasper", "Receta Médica", parametros);
            }else{
                parametros.put("fondo", this.getClass().getResourceAsStream(fondoReporte));
                parametros.put("icono", this.getClass().getResourceAsStream(icono));
                GenerarReporte.mostrarReporte("ReporteDetalleRecetas.jasper", "Reporte Detalle de Recetas", parametros);
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
