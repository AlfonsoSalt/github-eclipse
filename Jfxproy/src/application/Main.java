package application;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.ConfigJugador;
import modelo.EstadoJuego;
import vista.MenuInicio;
import vista.VistaMapa;
import java.util.List;


public class Main extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("CAPITAL");
        mostrarMenu();
        stage.show();
        
        
    }

    private void mostrarMenu() {
        MenuInicio menu  = new MenuInicio(this::iniciarJuego);
        Scene      scene = new Scene(menu, (double)1920, (double)1000,false);
        stage.setScene(scene);
        
      
    }

    private void iniciarJuego(List<ConfigJugador> configs) {
        EstadoJuego estado = new EstadoJuego(configs);
        VistaMapa   vista  = new VistaMapa(estado);
        Scene       scene  = new Scene(vista, 1920, 1000);
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}