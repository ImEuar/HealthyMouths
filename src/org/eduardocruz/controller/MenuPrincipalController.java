package org.eduardocruz.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.eduardocruz.system.Principal;

public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    @Override
    public void initialize(URL Location, ResourceBundle resources){
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
    }
    
    public void ventanaEspecialidades(){
        escenarioPrincipal.ventanaEspecialidades();
    }
    
    public void ventanaMedicamentos(){
        escenarioPrincipal.ventanaMedicamentos();
    }
    
    public void ventanaDoctores(){
        escenarioPrincipal.ventanaDoctores();
    }
    
    public void ventanaRecetas(){
        escenarioPrincipal.ventanaRecetas();
    }
    
    public void ventanaCitas(){
        escenarioPrincipal.ventanaCitas();
    }
    
    public void ventanaDetalleReceta(){
        escenarioPrincipal.ventanaDetalleRecetas();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
}