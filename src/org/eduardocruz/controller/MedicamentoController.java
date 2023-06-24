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
import org.eduardocruz.bean.Medicamento;
import org.eduardocruz.db.Conexion;
import org.eduardocruz.report.GenerarReporte;
import org.eduardocruz.system.Principal;

public class MedicamentoController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Medicamento> listaMedicamento;
    
    @FXML private TextField txtCodigoMedicamento;
    @FXML private TextField txtNombreMedicamento;
    @FXML private TableView tblMedicamentos;
    @FXML private TableColumn colCodigoMedicamento;
    @FXML private TableColumn colNombreMedicamento;
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
    
    //obtener los datos conectando con MySQL
    public ObservableList<Medicamento> getMedicamento(){
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medicamento(resultado.getInt("codigoMedicamento"), resultado.getString("nombreMedicamento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaMedicamento = FXCollections.observableArrayList(lista);
    }
    
    //cargar los datos obtenidos de MySQL en la tabla
    public void cargarDatos(){
        tblMedicamentos.setItems(getMedicamento());
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, Integer>("codigoMedicamento"));
        colNombreMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, String>("nombreMedicamento"));
    }
    
    
    //llevar los datos seleccionados a los textfield
    public void seleccionarElemento(){
        try{
            if(tblMedicamentos.getSelectionModel().getSelectedItem() != null){
                txtCodigoMedicamento.setText(String.valueOf(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
                txtNombreMedicamento.setText(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getNombreMedicamento());
                
                // Cambio de texto e imagen para cuando esté seleccionado algún elemento
                btnReporte.setText("Receta");
                imgReporte.setImage(new Image("/org/eduardocruz/image/RecetaMedica.png"));
            }else{
                JOptionPane.showMessageDialog(null, "¡No has seleccionado nada!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //control de los textfield
    public void desactivarControles(){
        txtCodigoMedicamento.setEditable(false);
        txtNombreMedicamento.setEditable(false);
    }
    public void activarControles(){
        txtNombreMedicamento.setEditable(true);
    }
    public void limpiarControles(){
        txtCodigoMedicamento.clear();
        txtNombreMedicamento.clear();
    }
    
    //procedimientos para los botones
    public void guardar(){
        Medicamento registro = new Medicamento();
        registro.setNombreMedicamento(txtNombreMedicamento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarMedicamento(?)}");
            procedimiento.setString(1, registro.getNombreMedicamento());
            procedimiento.execute();
            listaMedicamento.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void borrar(){
        Medicamento registro = new Medicamento();
        registro.setCodigoMedicamento(Integer.parseInt(txtCodigoMedicamento.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarMedicamento(?)}");
            procedimiento.setInt(1, registro.getCodigoMedicamento());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        Medicamento registro = (Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem();
        registro.setNombreMedicamento(txtNombreMedicamento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarMedicamento(?, ?)}");
            procedimiento.setInt(1, registro.getCodigoMedicamento());
            procedimiento.setString(2, registro.getNombreMedicamento());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //Estado original de los botones para no reescribir código innecesariamente
    public void estadoOriginal(){
        limpiarControles();
        desactivarControles();
        tblMedicamentos.getSelectionModel().clearSelection();
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
    
    //interacción con los botones
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/eduardocruz/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/eduardocruz/image/cancelar.png"));
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(txtNombreMedicamento.getText().length()!=0){
                    guardar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos.");
                }
                break;
            case ELIMINAR:
                if(txtCodigoMedicamento.getText().length()!=0){
                    borrar();
                    cargarDatos();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "¡No has seleccionado ningún medicamento!");
                }
                break;
            case ACTUALIZAR:
                if((txtCodigoMedicamento.getText().length()!=0)&(txtNombreMedicamento.getText().length()!=0)){
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
                estadoOriginal();;
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
    
    // declaramos las direcciones de las imágenes del reporte como variables de tipo String
    private final String fondoReceta = "/org/eduardocruz/report/background/FondoReporteHorizontal.jpg";
    private final String fondoReporte = "/org/eduardocruz/report/background/FondoReporte.jpg";
    private final String icono = "/org/eduardocruz/report/background/Medicamentos.png";
    private final String firmaRecetaMedica = "/org/eduardocruz/report/background/firma.png";
    private final String logoRecetaMedica = "/org/eduardocruz/report/background/Simbolo.png";
    
    // Método para mostrar los reportes en pantalla
    public void imprimirReporte(){
        Map parametros = new HashMap();
        try{
            if(tblMedicamentos.getSelectionModel().getSelectedItem() != null){
                parametros.put("Fondo", this.getClass().getResourceAsStream(fondoReceta));
                parametros.put("Firma", this.getClass().getResourceAsStream(firmaRecetaMedica));
                parametros.put("Logo", this.getClass().getResourceAsStream(logoRecetaMedica));
                parametros.put("codMedicamento", ((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
                GenerarReporte.mostrarReporte("RecetaMedica.jasper", "Receta Médica", parametros);
            }else{
                parametros.put("fondo", this.getClass().getResourceAsStream(fondoReporte));
                parametros.put("icono", this.getClass().getResourceAsStream(icono));
                GenerarReporte.mostrarReporte("ReporteMedicamentos.jasper", "Reporte de medicamentos", parametros);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    // Método para el botón
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                break;
        }
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
