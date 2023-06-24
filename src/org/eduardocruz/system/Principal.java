/*
Nombre: Eduardo Alexander Cruz Sanchez
Código Técnico:  IN5AM
Carné: 2021122
Fecha de Creación: 5/04/2022
Modificaciones: 
    5/04/2022
    20/04/2022
    26/04/2022
    3/05/2022
    4/05/2022
    6/05/2022
    11/05/2022
    18/05/2022
    25/05/2022
    1/06/2022
    8/06/2022
 */
package org.eduardocruz.system;


import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.eduardocruz.controller.CitaController;
import org.eduardocruz.controller.DetalleRecetaController;
import org.eduardocruz.controller.DoctorController;
import org.eduardocruz.controller.EspecialidadController;
import org.eduardocruz.controller.LoginController;
import org.eduardocruz.controller.MedicamentoController;
import org.eduardocruz.controller.MenuPrincipalController;
import org.eduardocruz.controller.PacienteController;
import org.eduardocruz.controller.ProgramadorController;
import org.eduardocruz.controller.RecetaController;
import org.eduardocruz.controller.UsuarioController;

/**
 *
 * @author Mixco
 */
public class Principal extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VISTA = "/org/eduardocruz/view/";
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Healthy Mouths");
        escenarioPrincipal.getIcons().add(new Image("/org/eduardocruz/image/favicon.png"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/eduardocruz/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        
        ventanaLogin();
        // menuPrincipal();
        escenarioPrincipal.show();
    }
    
    public void ventanaLogin(){
        try{
            LoginController login = (LoginController)cambiarEscena("LoginView.fxml", 481, 535);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try{
            UsuarioController usuario = (UsuarioController)cambiarEscena("UsuariosView.fxml", 533, 350);
            usuario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController ventanaMenu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 732, 479);
            ventanaMenu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try{
            ProgramadorController vistaProgramador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 481, 535);
            vistaProgramador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPacientes(){
        try{
            PacienteController vistaPaciente = (PacienteController) cambiarEscena("PacientesView.fxml", 1100, 439);
            vistaPaciente.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEspecialidades(){
        try{
            EspecialidadController vistaEspecialidad = (EspecialidadController) cambiarEscena("EspecialidadesView.fxml", 718, 463);
            vistaEspecialidad.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaMedicamentos(){
        try{
            MedicamentoController vistaMedicamento = (MedicamentoController) cambiarEscena("MedicamentosView.fxml", 651, 463);
            vistaMedicamento.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDoctores(){
        try{
            DoctorController vistaDoctor = (DoctorController) cambiarEscena("DoctoresView.fxml", 951, 463);
            vistaDoctor.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaRecetas(){
        try{
            RecetaController vistaReceta = (RecetaController) cambiarEscena("RecetasView.fxml", 816, 463);
            vistaReceta.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCitas(){
        try{
            CitaController vistaCita = (CitaController) cambiarEscena("CitasView.fxml", 1184, 463);
            vistaCita.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDetalleRecetas(){
        try{
            DetalleRecetaController vistaDetalleReceta = (DetalleRecetaController) cambiarEscena("DetalleRecetasView.fxml", 947, 463);
            vistaDetalleReceta.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        
        return resultado;
    }
}
