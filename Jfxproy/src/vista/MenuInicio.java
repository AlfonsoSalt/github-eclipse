package vista;

import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import modelo.ConfigJugador;
import java.util.*;
import java.util.function.Consumer;

public class MenuInicio extends Pane {

    // ══════════════════════════════════
    //  CONSTANTES
    // ══════════════════════════════════
    private static final int    W      = 1920;
    private static final int    H      = 1000;
    private static final String FUENTE = "OCR A Extended";
    private static final String ACENTO = "#00ff88";

    // ══════════════════════════════════
    //  DATOS FIJOS
    // ══════════════════════════════════
    private static final String[] TODOS_TITULOS = {
        "Caudillo de Culiacán", "Fentnyahu", "Агент", "Sierra7"
    };
    private static final Color[] TODOS_COLORES = {
        Color.LIME, Color.RED, Color.CYAN, Color.YELLOW
    };
    private static final String[] TODOS_TERRITORIOS = {
        "Tilted Towers", "Parangaricutirimicuaro", "Viltrum", "Nuevo Tlaxcala",
        "Bloque Oriental", "Campo Rojo", "Territorio Libre", "Frente Sur",
        "Israel", "Iranuclear", "La Bomba", "Cinturón de Acero"
    };

    // ══════════════════════════════════
    //  ESTADO DEL MENÚ
    // ══════════════════════════════════
    private int  fase          = 0; // 0=numJugadores 1=configurar 2=resumen
    private int  numJugadores  = 0;
    private int  jugadorActual = 0;

    private List<String>        titulosDisponibles = new ArrayList<>(Arrays.asList(TODOS_TITULOS));
    private List<String>        capitalesUsadas    = new ArrayList<>();
    private List<ConfigJugador> configs            = new ArrayList<>();

    private int tituloSel  = -1;
    private int capitalSel = -1;

    private Canvas          canvas;
    private GraphicsContext gc;
    private Consumer<List<ConfigJugador>> onIniciar;

    public MenuInicio(Consumer<List<ConfigJugador>> onIniciar) {
        this.onIniciar = onIniciar;
        canvas = new Canvas(W, H);
        gc     = canvas.getGraphicsContext2D();
        getChildren().add(canvas);
        canvas.setOnMouseClicked(e -> manejarClick(e.getX(), e.getY()));
        dibujar();
    }

    // ══════════════════════════════════
    //  DIBUJO
    // ══════════════════════════════════
    private void dibujar() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, W, H);
        switch (fase) {
            case 0 -> dibujarFase0();
            case 1 -> dibujarFase1();
            case 2 -> dibujarFase2();
        }
    }

    private void dibujarFase0() {
        gc.setFill(Color.web(ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 100));
        gc.fillText("CAPITAL", W / 2 - 240, 260);

        gc.setFill(Color.web("#444444"));
        gc.setFont(Font.font(FUENTE, 18));
        gc.fillText("PROTOCOLO DE TERCERA GUERRA MUNDIAL — SELECCIONA AGENTES", W / 2 - 340, 320);

        gc.setFill(Color.web(ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 22));
        gc.fillText("NÚMERO DE AGENTES", W / 2 - 140, 460);

        int[] opciones = {2, 3, 4};
        for (int i = 0; i < opciones.length; i++) {
            int bx = W / 2 - 220 + i * 200;
            int by = 490;
            gc.setFill(Color.web("#001500"));
            gc.fillRect(bx, by, 140, 90);
            gc.setStroke(Color.web(ACENTO));
            gc.setLineWidth(1);
            gc.strokeRect(bx, by, 140, 90);
            gc.setFill(Color.web(ACENTO));
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 56));
            gc.fillText(String.valueOf(opciones[i]), bx + 44, by + 62);
        }
    }

    private void dibujarFase1() {
        // Encabezado
        gc.setFill(Color.web(ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 26));
        gc.fillText("AGENTE " + (jugadorActual + 1) + " de " + numJugadores + " — ELIGE IDENTIDAD Y CAPITAL", 80, 70);

        gc.setFill(Color.web("#333333"));
        gc.setFont(Font.font(FUENTE, 13));
        gc.fillText("IDENTIDAD", 80, 115);

        // Títulos
        for (int i = 0; i < titulosDisponibles.size(); i++) {
            String titulo   = titulosDisponibles.get(i);
            int    colorIdx = Arrays.asList(TODOS_TITULOS).indexOf(titulo);
            Color  c        = TODOS_COLORES[colorIdx];
            boolean sel     = (i == tituloSel);

            int bx = 80, by = 130 + i * 76;
            gc.setFill(Color.web(sel ? "#002200" : "#0a0a0a"));
            gc.fillRect(bx, by, 540, 62);
            gc.setStroke(sel ? Color.web(ACENTO) : c);
            gc.setLineWidth(sel ? 2 : 1);
            gc.strokeRect(bx, by, 540, 62);

            gc.setFill(c);
            gc.fillOval(bx + 14, by + 18, 26, 26);

            gc.setFill(sel ? Color.web(ACENTO) : Color.WHITE);
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 17));
            gc.fillText(titulo, bx + 54, by + 36);

            gc.setFill(Color.web("#444444"));
            gc.setFont(Font.font(FUENTE, 11));
            gc.fillText("COLOR: " + colorNombre(c), bx + 54, by + 52);
        }

        // Capitales
        gc.setFill(Color.web("#333333"));
        gc.setFont(Font.font(FUENTE, 13));
        gc.fillText("CAPITAL DE INICIO  //  +$200 ingreso por turno", 700, 115);

        int cols = 3;
        for (int i = 0; i < TODOS_TERRITORIOS.length; i++) {
            String  terr   = TODOS_TERRITORIOS[i];
            boolean usada  = capitalesUsadas.contains(terr);
            boolean sel    = (i == capitalSel);
            int col = i % cols, row = i / cols;
            int bx  = 700 + col * 400, by = 130 + row * 90;

            gc.setFill(Color.web(usada ? "#1a0000" : sel ? "#002200" : "#0a0a0a"));
            gc.fillRect(bx, by, 380, 70);
            gc.setStroke(Color.web(usada ? "#330000" : sel ? ACENTO : "#2a2a2a"));
            gc.setLineWidth(sel ? 2 : 1);
            gc.strokeRect(bx, by, 380, 70);

            gc.setFill(Color.web(usada ? "#333333" : sel ? ACENTO : Color.WHITE.toString()));
            gc.setFont(Font.font(FUENTE, usada ? FontWeight.NORMAL : FontWeight.BOLD, 14));
            gc.fillText(terr + (usada ? "  [OCUPADA]" : ""), bx + 14, by + 38);

            if (!usada) {
                gc.setFill(Color.web("#444444"));
                gc.setFont(Font.font(FUENTE, 11));
                gc.fillText(sel ? "✓ SELECCIONADA" : "Click para elegir", bx + 14, by + 56);
            }
        }

        // Botón confirmar
        boolean puede = tituloSel >= 0 && capitalSel >= 0;
        int ftX = W - 340, ftY = H - 110;
        gc.setFill(Color.web(puede ? "#003300" : "#111111"));
        gc.fillRect(ftX, ftY, 300, 68);
        gc.setStroke(Color.web(puede ? ACENTO : "#333333"));
        gc.setLineWidth(puede ? 2 : 1);
        gc.strokeRect(ftX, ftY, 300, 68);
        gc.setFill(Color.web(puede ? ACENTO : "#333333"));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 18));
        gc.fillText("[ CONFIRMAR AGENTE ]", ftX + 28, ftY + 42);
    }

    private void dibujarFase2() {
        gc.setFill(Color.web(ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 44));
        gc.fillText("// AGENTES CONFIRMADOS", W / 2 - 360, 120);

        for (int i = 0; i < configs.size(); i++) {
            ConfigJugador c  = configs.get(i);
            int bx = W / 2 - 500, by = 180 + i * 140;

            gc.setFill(Color.web("#0a0a0a"));
            gc.fillRect(bx, by, 1000, 120);
            gc.setStroke(c.color);
            gc.setLineWidth(2);
            gc.strokeRect(bx, by, 1000, 120);

            // Avatar
            gc.setFill(c.color);
            gc.fillOval(bx + 20, by + 34, 52, 52);

            gc.setFill(c.color);
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 24));
            gc.fillText(c.titulo, bx + 92, by + 54);

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(FUENTE, 15));
            gc.fillText("CAPITAL: " + c.nombreCapital, bx + 92, by + 80);

            gc.setFill(Color.web("#444444"));
            gc.setFont(Font.font(FUENTE, 13));
            gc.fillText("+$200 ingreso/turno  //  5 tropas iniciales", bx + 92, by + 102);
        }

        // Botón iniciar
        int bx = W / 2 - 180, by = H - 180;
        gc.setFill(Color.web("#003300"));
        gc.fillRect(bx, by, 360, 90);
        gc.setStroke(Color.web(ACENTO));
        gc.setLineWidth(2);
        gc.strokeRect(bx, by, 360, 90);
        gc.setFill(Color.web(ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 34));
        gc.fillText("[ INICIAR ]", bx + 68, by + 58);
    }

    // ══════════════════════════════════
    //  CLICKS
    // ══════════════════════════════════
    private void manejarClick(double x, double y) {
        switch (fase) {
            case 0 -> clickFase0(x, y);
            case 1 -> clickFase1(x, y);
            case 2 -> clickFase2(x, y);
        }
    }

    private void clickFase0(double x, double y) {
        int[] opciones = {2, 3, 4};
        for (int i = 0; i < opciones.length; i++) {
            int bx = W / 2 - 220 + i * 200, by = 490;
            if (x >= bx && x <= bx + 140 && y >= by && y <= by + 90) {
                numJugadores = opciones[i];
                fase = 1;
                dibujar();
                return;
            }
        }
    }

    private void clickFase1(double x, double y) {
        // Título
        for (int i = 0; i < titulosDisponibles.size(); i++) {
            int bx = 80, by = 130 + i * 76;
            if (x >= bx && x <= bx + 540 && y >= by && y <= by + 62) {
                tituloSel = i;
                dibujar(); return;
            }
        }

        // Capital
        int cols = 3;
        for (int i = 0; i < TODOS_TERRITORIOS.length; i++) {
            if (capitalesUsadas.contains(TODOS_TERRITORIOS[i])) continue;
            int col = i % cols, row = i / cols;
            int bx = 700 + col * 400, by = 130 + row * 90;
            if (x >= bx && x <= bx + 380 && y >= by && y <= by + 70) {
                capitalSel = i;
                dibujar(); return;
            }
        }

        // Confirmar
        if (tituloSel >= 0 && capitalSel >= 0) {
            int ftX = W - 340, ftY = H - 110;
            if (x >= ftX && x <= ftX + 300 && y >= ftY && y <= ftY + 68) {
                confirmarJugador();
            }
        }
    }

    private void clickFase2(double x, double y) {
        int bx = W / 2 - 180, by = H - 180;
        if (x >= bx && x <= bx + 360 && y >= by && y <= by + 90) {
            onIniciar.accept(configs);
        }
    }

    private void confirmarJugador() {
        String titulo   = titulosDisponibles.get(tituloSel);
        int    colorIdx = Arrays.asList(TODOS_TITULOS).indexOf(titulo);
        Color  color    = TODOS_COLORES[colorIdx];
        String capital  = TODOS_TERRITORIOS[capitalSel];

        configs.add(new ConfigJugador(titulo, color, capital));
        capitalesUsadas.add(capital);
        titulosDisponibles.remove(tituloSel);
        tituloSel  = -1;
        capitalSel = -1;
        jugadorActual++;

        if (jugadorActual >= numJugadores) fase = 2;
        dibujar();
    }

    private String colorNombre(Color c) {
        if (c == Color.LIME)   return "VERDE NEÓN";
        if (c == Color.RED)    return "ROJO";
        if (c == Color.CYAN)   return "CIAN";
        if (c == Color.YELLOW) return "AMARILLO";
        return "—";
    }
}