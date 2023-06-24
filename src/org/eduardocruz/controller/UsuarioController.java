package org.eduardocruz.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.eduardocruz.bean.Usuario;
import org.eduardocruz.db.Conexion;
import org.eduardocruz.system.Principal;

public class UsuarioController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO, GUARDAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    @FXML TextField txtCodigoUsuario;
    @FXML TextField txtNombreUsuario;
    @FXML TextField txtApellidoUsuario;
    @FXML TextField txtUsuario;
    @FXML TextField txtPassword;
    @FXML Button btnNuevo;
    @FXML Button btnEliminar;
    @FXML ImageView imgNuevo;
    @FXML ImageView imgEliminar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEliminar.setDisable(true);
    }
    
    public void desactivarControles(){
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtPassword.setEditable(false);
        btnEliminar.setDisable(true);
    }
    public void activarControles(){
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtPassword.setEditable(true);
        btnEliminar.setDisable(false);
    }
    public void limpiarControles(){
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtPassword.clear();
    }
    
    public void guardar(){
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setContrasena(txtPassword.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getApellidoUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getContrasena());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null, "¡Datos agregados correctamente!");
            ventanaLogin();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void estadoOriginal(){
        limpiarControles();
        desactivarControles();
        btnNuevo.setText("Nuevo");
        imgNuevo.setImage(new Image("/org/eduardocruz/image/Nuevo.png"));
        tipoDeOperacion = operaciones.NINGUNO;
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/eduardocruz/image/guardar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if((txtNombreUsuario.getText().length()!=0)&(txtApellidoUsuario.getText().length()!=0)&
                        (txtUsuario.getText().length()!=0)&(txtPassword.getText().length()!=0)){
                    guardar();
                    estadoOriginal();
                }else{
                    JOptionPane.showMessageDialog(null, "Por favor, llena todos los campos.");
                }
                break;
        }
    }
    
    public void cancelar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                estadoOriginal();
                break;
        }
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
}
