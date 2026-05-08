package vista;

import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;
import modelo.*;
import logica.*;

public class VistaMapa extends Pane {

    // ═══════════════════════════════════════════
    //  CONSTANTES DE DISEÑO — modifica aquí
    // ═══════════════════════════════════════════

    private static final int    W                = 1920;
    private static final int    H                = 1000;

    private static final int    PANEL_X          = 1580;
    private static final int    PANEL_W          = W - PANEL_X;
    private static final int    BARRA_Y          = 920;
    private static final int    BARRA_H          = H - BARRA_Y;
    private static final int    INFO_H           = 300;

    private static final int    BTN_W            = 180;
    private static final int    BTN_H            = 44;
    private static final int    BTN_GAP          = 10;
    private static final int    BTN_START_X      = 10;

    private static final int    RADIO            = 24;
    private static final int    ROW_H_EDIFICIO   = 50;
    private static final int    ROW_H_UNIDAD     = 58;

   
   private static final String FUENTE           = "OCR A Extended";
    private static final int    TAM_TITULO       = 25;
    private static final int    TAM_NORMAL       = 25;
    private static final int    TAM_PEQUEÑO      = 20;
    private static final int    TAM_HUD          = 25;
    private static final int    TAM_MAPA_NOMBRE  = 20;
    private static final int    TAM_MAPA_TROPAS  = 25;
    private static final int    TAM_VICTORIA     = 80;
    private static final int    TAM_SUBTITULO    = 28;
    private static final int    TAM_AVATAR       = 30;

    private static final String COLOR_FONDO      = "#000000";
    private static final String COLOR_FONDO_PANEL= "#080808";
    private static final String COLOR_ACENTO     = "#00ff88";
    private static final String COLOR_TEXTO      = "#ffffff";
    private static final String COLOR_APAGADO    = "#444444";
    private static final String COLOR_BORDE      = "#2a2a2a";
    private static final String COLOR_POSITIVO   = "#001500";
    private static final String COLOR_NEGATIVO   = "#150000";
    private static final String COLOR_ACTIVO     = "#003300";
    private static final String COLOR_CONEXION   = "#222222";
    private static final String COLOR_GRIS       = "#888888";
    private static final String COLOR_SEPARADOR  = "#333333";

    // ═══════════════════════════════════════════

    private Canvas     canvas;
    private GraphicsContext gc;
    private EstadoJuego estado;

    private int        territorioClickeado = -1;
    private Territorio territorioPanel     = null;
    private String     mensajeHUD          = "";
    private String     panelActivo         = "INFO";

    private double[][] posiciones = {
        {200,  180},  // Sector Norte
        {200,  500},  // Frente Oeste
        {520,  270},  // Zona Gris
        {720,  130},  // Dominio Ártico
        {980,  240},  // Bloque Oriental
        {860,  430},  // Campo Rojo
        {650,  570},  // Territorio Libre
        {340,  640},  // Frente Sur
        {1200, 300},  // Isla Quemada
        {1080, 510},  // Zona Nuclear
        {460,  90},   // Corredor Norte
        {860,  640}  , // Cinturón de Acero
        {200,  840}   // Cinturón de Acero
    };

    // ─────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────
    public VistaMapa(EstadoJuego estado) {
        this.estado = estado;
        canvas = new Canvas(W, H);
        gc = canvas.getGraphicsContext2D();
        getChildren().add(canvas);
        canvas.setOnMouseClicked(e -> manejarClick(e.getX(), e.getY()));
        aplicarRefuerzo();
        dibujar();
    }

    // ─────────────────────────────────────────
    //  DIBUJO PRINCIPAL
    // ─────────────────────────────────────────
    public void dibujar() {
        gc.setFill(Color.web(COLOR_FONDO));
        gc.fillRect(0, 0, W, H);
        dibujarMapa();
        dibujarPanelDerecho();
        dibujarBarraInferior();
        verificarVictoria();
    }

    // ─────────────────────────────────────────
    //  MAPA
    // ─────────────────────────────────────────
    private void dibujarMapa() {
        gc.setStroke(Color.web(COLOR_SEPARADOR));
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, PANEL_X, BARRA_Y);

        dibujarConexiones();
        dibujarTerritorios();

        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, TAM_HUD));
        gc.fillText(mensajeHUD, 10, BARRA_Y - 10);
    }

    private void dibujarConexiones() {
        gc.setStroke(Color.web(COLOR_CONEXION));
        gc.setLineWidth(1.5);
        List<Territorio> lista = estado.territorios;
        for (int i = 0; i < lista.size(); i++) {
            for (Territorio ady : lista.get(i).adyacentes) {
                int j = lista.indexOf(ady);
                if (j > i) {
                    gc.strokeLine(posiciones[i][0], posiciones[i][1],
                                  posiciones[j][0], posiciones[j][1]);
                }
            }
        }
    }

    private void dibujarTerritorios() {
        List<Territorio> lista = estado.territorios;
        for (int i = 0; i < lista.size(); i++) {
            Territorio t = lista.get(i);
            double x = posiciones[i][0];
            double y = posiciones[i][1];
            Color  c = t.dueno != null ? t.dueno.color : Color.GRAY;
            
            //Capital
            if (t.esCapital) {
                gc.setFill(Color.web("#ffff00", 0.8));
                gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 10));
                gc.fillText("Capital ★", x - 5, y - RADIO - 4);
            }

            // Resaltado seleccionado
            if (i == territorioClickeado) {
                gc.setFill(Color.web(COLOR_ACENTO, 0.2));
                gc.fillOval(x - RADIO - 10, y - RADIO - 10, (RADIO + 10) * 2, (RADIO + 10) * 2);
                gc.setStroke(Color.web(COLOR_ACENTO));
                gc.setLineWidth(2);
                gc.strokeOval(x - RADIO - 10, y - RADIO - 10, (RADIO + 10) * 2, (RADIO + 10) * 2);
            }

            // Resaltado panel abierto
            if (t == territorioPanel) {
                gc.setFill(Color.color(c.getRed(), c.getGreen(), c.getBlue(), 0.25));
                gc.fillOval(x - RADIO - 6, y - RADIO - 6, (RADIO + 6) * 2, (RADIO + 6) * 2);
            }

            // Círculo territorio
            gc.setFill(c);
            gc.fillOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);
            gc.setStroke(Color.web(COLOR_ACENTO));
            gc.setLineWidth(1);
            gc.strokeOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);

            // Tropas
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_MAPA_TROPAS));
            String tropasStr = String.valueOf(t.tropas);
            gc.fillText(tropasStr, x - (t.tropas > 9 ? 9 : 5), y + 5);

            // Nombre
            gc.setFill(Color.web(COLOR_GRIS));
            gc.setFont(Font.font(FUENTE, TAM_MAPA_NOMBRE));
            gc.fillText(t.nombre, x - 30, y + RADIO + 16);

            // Indicador edificios
            if (!t.edificios.isEmpty()) {
                gc.setFill(Color.web(COLOR_ACENTO));
                gc.fillRect(x + RADIO - 4, y - RADIO - 2, 8, 8);
            }
        }
    }

    // ─────────────────────────────────────────
    //  PANEL DERECHO
    // ─────────────────────────────────────────
    private void dibujarPanelDerecho() {
        gc.setFill(Color.web(COLOR_FONDO_PANEL));
        gc.fillRect(PANEL_X, 0, PANEL_W, BARRA_Y);
        gc.setStroke(Color.web(COLOR_ACENTO));
        gc.setLineWidth(1);
        gc.strokeLine(PANEL_X, 0, PANEL_X, BARRA_Y);

        dibujarInfoJugador();

        gc.setStroke(Color.web(COLOR_SEPARADOR));
        gc.strokeLine(PANEL_X + 10, INFO_H, PANEL_X + PANEL_W - 10, INFO_H);

        switch (panelActivo) {
            case "CONSTRUIR" -> dibujarPanelConstruir();
            case "UNIDADES"  -> dibujarPanelUnidades();
            case "ECONOMIA"  -> dibujarPanelEconomia();
            case "INTEL"     -> dibujarPanelIntel();
            default          -> dibujarMensajePanel("Selecciona un menú");
        }
    }

    private void dibujarInfoJugador() {
        Jugador j  = estado.jugadorActual();
        int px     = PANEL_X + 13;
        int avatarS= 70;

        // Avatar
        gc.setFill(Color.web("#111111"));
        gc.fillRect(px, 12, avatarS, avatarS);
        gc.setStroke(j.color);
        gc.setLineWidth(2);
        gc.strokeRect(px, 12, avatarS, avatarS);
        gc.setFill(j.color);
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_AVATAR));
        gc.fillText(j.titulo.substring(3, 4), px + 22, 58);

        // Datos jugador
        int dx = px + avatarS + 10;
        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_TITULO));
        gc.fillText(j.titulo.toUpperCase(), dx, 30);

        gc.setFill(Color.web(COLOR_TEXTO));
        gc.setFont(Font.font(FUENTE, TAM_NORMAL));
        gc.fillText("$  " + j.dinero,                        dx, 50);
        gc.fillText("+" + estado.ultimoIngreso + " / turno", dx, 68);

        // Stats
        gc.setFill(Color.web(COLOR_APAGADO));
        gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
        gc.fillText("TERRITORIOS : " + j.territorios.size(),  px, 100);
        gc.fillText("TROPAS DISP : " + j.tropasDisponibles,   px, 116);
        gc.fillText("TURNO #"       + (estado.indiceTurno + 1), px, 132);

        // Barra de dominio
        double pct = (double) j.territorios.size() / estado.territorios.size();
        gc.setFill(Color.web("#111111"));
        gc.fillRect(px, 142, PANEL_W - 24, 10);
        gc.setFill(j.color);
        gc.fillRect(px, 142, (PANEL_W - 24) * pct, 10);
        gc.setStroke(Color.web(COLOR_BORDE));
        gc.strokeRect(px, 142, PANEL_W - 24, 10);

        // Info territorio seleccionado
        if (territorioPanel != null) {
            gc.setFill(Color.web("#111111"));
            gc.fillRect(px, 162, PANEL_W - 24, 128);
            gc.setStroke(Color.web(COLOR_BORDE));
            gc.strokeRect(px, 162, PANEL_W - 24, 128);

            gc.setFill(Color.web(COLOR_ACENTO));
            gc.setFont(Font.font(FUENTE, TAM_NORMAL));
            gc.fillText("// " + territorioPanel.nombre.toUpperCase(), px + 8, 180);

            gc.setFill(Color.web(COLOR_TEXTO));
            gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText("Tropas    : " + territorioPanel.tropas,                    px + 8, 198);
            gc.fillText("Ingreso   : $" + territorioPanel.calcularIngreso(),         px + 8, 214);
            gc.fillText("Edificios : " + territorioPanel.edificios.size(),           px + 8, 230);
            gc.fillText("Unidades  : " + territorioPanel.unidades.size(),            px + 8, 246);
            gc.fillText("ATK bonus : +" + territorioPanel.bonusAtaqueTotal(),        px + 8, 262);
            gc.fillText("DEF bonus : +" + territorioPanel.bonusDefensaTotal(),       px + 8, 278);
        } else {
            gc.setFill(Color.web(COLOR_APAGADO));
            gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText("Selecciona un territorio", px, 185);
        }
    }

    // ─────────────────────────────────────────
    //  SUBPANELES
    // ─────────────────────────────────────────
    private void dibujarPanelConstruir() {
        if (territorioPanel == null) { dibujarMensajePanel("Selecciona territorio primero"); return; }
        int    px = PANEL_X + 13;
        double y  = INFO_H + 20;

        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_TITULO));
        gc.fillText("[ CONSTRUIR ]", px, y);
        y += 14;

        for (TipoEdificio tipo : TipoEdificio.values()) {
            boolean construido = territorioPanel.tieneEdificio(tipo);
            boolean puedePagar = estado.jugadorActual().dinero >= tipo.costo;

            gc.setFill(Color.web(construido ? COLOR_ACTIVO : puedePagar ? COLOR_POSITIVO : COLOR_NEGATIVO));
            gc.fillRect(px, y, PANEL_W - 24, ROW_H_EDIFICIO - 4);
            gc.setStroke(Color.web(construido ? COLOR_ACENTO : COLOR_BORDE));
            gc.strokeRect(px, y, PANEL_W - 24, ROW_H_EDIFICIO - 4);

            gc.setFill(Color.web(construido ? COLOR_ACENTO : puedePagar ? COLOR_TEXTO : COLOR_APAGADO));
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_PEQUEÑO));
            gc.fillText(tipo.nombre + (construido ? "  ✓" : "  $" + tipo.costo), px + 8, y + 16);

            gc.setFill(Color.web(COLOR_APAGADO));
            gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText(tipo.descripcion, px + 8, y + 32);
            y += ROW_H_EDIFICIO;
        }
    }

    private void dibujarPanelUnidades() {
        if (territorioPanel == null) { dibujarMensajePanel("Selecciona territorio primero"); return; }
        int    px          = PANEL_X + 13;
        double y           = INFO_H + 20;
        boolean tienePlanta = estado.jugadorActual().tienePlanta();

        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_TITULO));
        gc.fillText("[ UNIDADES ]", px, y);
        y += 14;

        for (TipoUnidad tipo : TipoUnidad.values()) {
            boolean puedePagar = estado.jugadorActual().dinero >= tipo.costo;
            boolean disponible = puedePagar && tienePlanta;

            gc.setFill(Color.web(disponible ? COLOR_POSITIVO : COLOR_NEGATIVO));
            gc.fillRect(px, y, PANEL_W - 24, ROW_H_UNIDAD - 4);
            gc.setStroke(Color.web(COLOR_BORDE));
            gc.strokeRect(px, y, PANEL_W - 24, ROW_H_UNIDAD - 4);

            gc.setFill(Color.web(disponible ? COLOR_TEXTO : COLOR_APAGADO));
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_PEQUEÑO));
            gc.fillText(tipo.nombre + "   $" + tipo.costo, px + 8, y + 16);

            gc.setFill(Color.web(COLOR_ACENTO));
            gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText("ATK +" + tipo.bonusAtaque + "   DEF +" + tipo.bonusDefensa, px + 8, y + 30);

            gc.setFill(Color.web(COLOR_APAGADO));
            gc.fillText(tienePlanta ? tipo.descripcion : "Requiere Planta Energética", px + 8, y + 44);
            y += ROW_H_UNIDAD;
        }
    }

    private void dibujarPanelEconomia() {
        int px   = PANEL_X + 12;
        double y = INFO_H + 20;
        Jugador j = estado.jugadorActual();

        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_TITULO));
        gc.fillText("[ ECONOMÍA ]", px, y); y += 22;

        gc.setFill(Color.web(COLOR_TEXTO));
        gc.setFont(Font.font(FUENTE, TAM_NORMAL));
        gc.fillText("Capital       : $" + j.dinero,            px, y); y += 18;
        gc.fillText("Ingreso/turno : $" + estado.ultimoIngreso, px, y); y += 18;
        gc.fillText("Territorios   : "  + j.territorios.size(), px, y); y += 24;

        // Préstamo activo
        if (j.prestamoPendiente > 0) {
            gc.setFill(Color.web("#ff4444"));
            gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText("⚠ DEUDA: $" + j.prestamoPendiente + " | Turnos: " + j.turnosPrestamo, px, y); y += 18;
        }

        // Separador
        gc.setStroke(Color.web(COLOR_SEPARADOR));
        gc.strokeLine(px, y, px + PANEL_W - 24, y); y += 14;

        // ── PRÉSTAMOS ──
        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_PEQUEÑO));
        gc.fillText("[ PRÉSTAMOS ]", px, y); y += 14;

        Object[][] prestamos = {
            {"Préstamo Pequeño",  500,  "Devuelves $750 en 3 turnos"},
            {"Préstamo Grande",   2000, "Devuelves $3000 en 3 turnos"},
            {"Pagar Deuda",       0,    "Liquidar préstamo activo"}
        };

        for (Object[] p : prestamos) {
            String txt   = (String) p[0];
            int    monto = (int)    p[1];
            String desc  = (String) p[2];
            boolean puede = monto == 0
                ? j.prestamoPendiente > 0 && j.dinero >= j.prestamoPendiente
                : j.prestamoPendiente == 0;

            gc.setFill(Color.web(puede ? COLOR_POSITIVO : COLOR_NEGATIVO));
            gc.fillRect(px, y, PANEL_W - 24, ROW_H_EDIFICIO - 4);
            gc.setStroke(Color.web(puede ? COLOR_ACENTO : COLOR_BORDE));
            gc.setLineWidth(1);
            gc.strokeRect(px, y, PANEL_W - 24, ROW_H_EDIFICIO - 4);

            gc.setFill(Color.web(puede ? COLOR_TEXTO : COLOR_APAGADO));
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_PEQUEÑO));
            gc.fillText(txt + (monto > 0 ? "  $" + monto : ""), px + 8, y + 16);
            gc.setFill(Color.web(COLOR_APAGADO));
            gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText(desc, px + 8, y + 30);
            y += ROW_H_EDIFICIO;
        }

        y += 8;
        gc.setStroke(Color.web(COLOR_SEPARADOR));
        gc.strokeLine(px, y, px + PANEL_W - 24, y); y += 14;

        // ── CASINO ──
        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_PEQUEÑO));
        gc.fillText("[ CASINO DE GUERRA ]", px, y); y += 14;

        boolean tieneCasino = j.tieneCasino();
        boolean yaApostoó   = j.apostaronEsteTurno;

        Object[][] apuestas = {
            {"Apuesta Simple",   "$500  — par=x2, non=pierde"},
            {"Contrato Riesgo",  "$1000 — gana ataque=x3"},
            {"Mercado Negro",    "$800  — evento aleatorio"}
        };

        for (Object[] ap : apuestas) {
            boolean puede = tieneCasino && !yaApostoó;
            gc.setFill(Color.web(puede ? COLOR_POSITIVO : COLOR_NEGATIVO));
            gc.fillRect(px, y, PANEL_W - 24, ROW_H_EDIFICIO - 4);
            gc.setStroke(Color.web(puede ? COLOR_BORDE : COLOR_BORDE));
            gc.setLineWidth(1);
            gc.strokeRect(px, y, PANEL_W - 24, ROW_H_EDIFICIO - 4);

            gc.setFill(Color.web(puede ? COLOR_TEXTO : COLOR_APAGADO));
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_PEQUEÑO));
            gc.fillText((String) ap[0], px + 8, y + 16);
            gc.setFill(Color.web(COLOR_APAGADO));
            gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText(tieneCasino ? (String) ap[1] : "Requiere Casino de Guerra", px + 8, y + 30);
            y += ROW_H_EDIFICIO;
        }
    }

    private void dibujarPanelIntel() {
        int    px = PANEL_X + 12;
        double y  = INFO_H + 20;
        Jugador j = estado.jugadorActual();

        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_TITULO));
        gc.fillText("[ INTELIGENCIA ]", px, y);
        y += 14;

        boolean  tieneAgencia = j.tieneAgencia();
        String[] ops          = {"Espionaje      $600", "Sabotaje       $900", "Infiltración  $1200"};
        int[]    costos       = {600, 900, 1200};

        for (int i = 0; i < ops.length; i++) {
            boolean puede = tieneAgencia && j.dinero >= costos[i];
            gc.setFill(Color.web(puede ? COLOR_POSITIVO : COLOR_NEGATIVO));
            gc.fillRect(px, y, PANEL_W - 24, ROW_H_EDIFICIO - 4);
            gc.setStroke(Color.web(COLOR_BORDE));
            gc.strokeRect(px, y, PANEL_W - 24, ROW_H_EDIFICIO - 4);
            gc.setFill(Color.web(puede ? COLOR_TEXTO : COLOR_APAGADO));
            gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText(tieneAgencia ? ops[i] : "Requiere Agencia de Inteligencia", px + 8, y + 22);
            y += ROW_H_EDIFICIO;
        }
    }

    private void dibujarMensajePanel(String msg) {
        gc.setFill(Color.web(COLOR_APAGADO));
        gc.setFont(Font.font(FUENTE, TAM_NORMAL));
        gc.fillText(msg, PANEL_X + 13, INFO_H + 30);
    }

    // ─────────────────────────────────────────
    //  BARRA INFERIOR
    // ─────────────────────────────────────────
    private void dibujarBarraInferior() {
        gc.setFill(Color.web("#050505"));
        gc.fillRect(0, BARRA_Y, W, BARRA_H);
        gc.setStroke(Color.web(COLOR_ACENTO));
        gc.setLineWidth(1);
        gc.strokeLine(0, BARRA_Y, W, BARRA_H > 0 ? BARRA_Y : BARRA_Y);
        gc.strokeLine(0, BARRA_Y, W, BARRA_Y);

        int    btnY    = BARRA_Y + (BARRA_H - BTN_H) / 2;
        String[] menus = {"CONSTRUIR", "UNIDADES", "ECONOMIA", "INTEL", "MOVER"};

        for (int i = 0; i < menus.length; i++) {
            int     bx     = BTN_START_X + i * (BTN_W + BTN_GAP);
            boolean activo = panelActivo.equals(menus[i]);

            gc.setFill(Color.web(activo ? COLOR_ACENTO : COLOR_FONDO_PANEL));
            gc.fillRect(bx, btnY, BTN_W, BTN_H);
            gc.setStroke(Color.web(COLOR_ACENTO));
            gc.setLineWidth(1);
            gc.strokeRect(bx, btnY, BTN_W, BTN_H);
            gc.setFill(Color.web(activo ? COLOR_FONDO : COLOR_ACENTO));
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_NORMAL));
            gc.fillText(menus[i], bx + 28, btnY + 27);
        }

        // Botón fin de turno
        int ftX = W - 220;
        gc.setFill(Color.web(COLOR_ACTIVO));
        gc.fillRect(ftX, btnY, 200, BTN_H);
        gc.setStroke(Color.web(COLOR_ACENTO));
        gc.strokeRect(ftX, btnY, 200, BTN_H);
        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_NORMAL));
        gc.fillText("[ FIN DE TURNO ]", ftX + 28, btnY + 27);
    }

    // ─────────────────────────────────────────
    //  CLICKS
    // ─────────────────────────────────────────
    private void manejarClick(double mouseX, double mouseY) {

        // Barra inferior
        if (mouseY >= BARRA_Y) {
            int btnY = BARRA_Y + (BARRA_H - BTN_H) / 2;

            // Fin de turno
            if (mouseX >= W - 220 && mouseX <= W - 20 && mouseY >= btnY && mouseY <= btnY + BTN_H) {
                estado.siguienteTurno();
                aplicarRefuerzo();
                territorioPanel     = null;
                territorioClickeado = -1;
                panelActivo         = "INFO";
                dibujar();
                return;
            }

            // Menús
            String[] menus = {"CONSTRUIR", "UNIDADES", "ECONOMIA", "INTEL","MOVER"};
            for (int i = 0; i < menus.length; i++) {
                int bx = BTN_START_X + i * (BTN_W + BTN_GAP);
                if (mouseX >= bx && mouseX <= bx + BTN_W && mouseY >= btnY && mouseY <= btnY + BTN_H) {
                    panelActivo = menus[i];
                    dibujar();
                    return;
                }
            }
            return;
        }

        // Panel derecho
        if (mouseX >= PANEL_X) {
            manejarClickPanel(mouseX, mouseY);
            return;
        }

        // Mapa
        List<Territorio> lista = estado.territorios;
        for (int i = 0; i < lista.size(); i++) {
            double dx = mouseX - posiciones[i][0];
            double dy = mouseY - posiciones[i][1];
            if (Math.sqrt(dx * dx + dy * dy) <= RADIO) {
                seleccionarTerritorio(i);
                return;
            }
        }
    }

    private void manejarClickPanel(double mouseX, double mouseY) {
        if (territorioPanel == null) return;
        int    px = PANEL_X + 13;
        double y  = INFO_H + 34;

        switch (panelActivo) {
            case "CONSTRUIR" -> {
                for (TipoEdificio tipo : TipoEdificio.values()) {
                    if (mouseY >= y && mouseY <= y + ROW_H_EDIFICIO - 4) {
                        boolean ok = SistemaEconomia.construir(estado.jugadorActual(), territorioPanel, tipo);
                        mensajeHUD = ok
                            ? "CONSTRUIDO: " + tipo.nombre
                            : territorioPanel.tieneEdificio(tipo) ? "YA EXISTE" : "FONDOS INSUFICIENTES";
                        dibujar(); return;
                    }
                    y += ROW_H_EDIFICIO;
                }
            }
            case "UNIDADES" -> {
                for (TipoUnidad tipo : TipoUnidad.values()) {
                    if (mouseY >= y && mouseY <= y + ROW_H_UNIDAD - 4) {
                        boolean ok = SistemaEconomia.comprarUnidad(estado.jugadorActual(), territorioPanel, tipo);
                        mensajeHUD = ok
                            ? "COMPRADO: " + tipo.nombre + " | $" + estado.jugadorActual().dinero
                            : !estado.jugadorActual().tienePlanta() ? "REQUIERE PLANTA ENERGÉTICA" : "FONDOS INSUFICIENTES";
                        dibujar(); return;
                    }
                    y += ROW_H_UNIDAD;
                }
            }
            case "ECONOMIA" -> {
                Jugador j = estado.jugadorActual();

                // Replicar exactamente el mismo y que dibujarPanelEconomia
                double dy = INFO_H + 20;
                dy += 22;  // título
                dy += 18;  // capital
                dy += 18;  // ingreso
                dy += 24;  // territorios
                if (j.prestamoPendiente > 0) dy += 18; // deuda activa
                dy += 14;  // separador

                // Título préstamos
                dy += 14;

                // ── PRÉSTAMOS ──
                if (mouseY >= dy && mouseY <= dy + ROW_H_EDIFICIO - 4) {
                    mensajeHUD = SistemaFinanzas.tomarPrestamo(j, 500);
                    dibujar(); return;
                }
                dy += ROW_H_EDIFICIO;

                if (mouseY >= dy && mouseY <= dy + ROW_H_EDIFICIO - 4) {
                    mensajeHUD = SistemaFinanzas.tomarPrestamo(j, 2000);
                    dibujar(); return;
                }
                dy += ROW_H_EDIFICIO;

                if (mouseY >= dy && mouseY <= dy + ROW_H_EDIFICIO - 4) {
                    mensajeHUD = SistemaFinanzas.pagarPrestamo(j);
                    dibujar(); return;
                }
                dy += ROW_H_EDIFICIO;

                // Separador + título casino
                dy += 8 + 14 + 14;

                // ── CASINO ──
                if (mouseY >= dy && mouseY <= dy + ROW_H_EDIFICIO - 4) {
                    mensajeHUD = SistemaFinanzas.apuestaSimple(j);
                    dibujar(); return;
                }
                dy += ROW_H_EDIFICIO;

                if (mouseY >= dy && mouseY <= dy + ROW_H_EDIFICIO - 4) {
                    mensajeHUD = SistemaFinanzas.contratoRiesgo(j, false);
                    dibujar(); return;
                }
                dy += ROW_H_EDIFICIO;

                if (mouseY >= dy && mouseY <= dy + ROW_H_EDIFICIO - 4) {
                    mensajeHUD = SistemaFinanzas.mercadoNegro(j);
                    dibujar(); return;
                }
            }
            
            case "INTEL" -> {
                int[] costos = {600, 900, 1200};
                for (int i = 0; i < costos.length; i++) {
                    if (mouseY >= y && mouseY <= y + ROW_H_EDIFICIO - 4) {
                        manejarIntel(i, costos[i]);
                        return;
                    }
                    y += ROW_H_EDIFICIO;
                }
            }
        }
    }

    private void manejarApuesta(int tipo, int costo) {
        Jugador j = estado.jugadorActual();
        if (!j.tieneCasino())      { mensajeHUD = "REQUIERE CASINO DE GUERRA"; dibujar(); return; }
        if (j.dinero < costo)      { mensajeHUD = "FONDOS INSUFICIENTES";      dibujar(); return; }
        j.dinero -= costo;
        int dado = (int)(Math.random() * 6) + 1;
        switch (tipo) {
            case 0 -> { // Apuesta simple
                if (dado % 2 == 0) { j.dinero += costo * 2; mensajeHUD = "APUESTA: GANASTE $" + costo * 2 + " (dado " + dado + ")"; }
                else               { mensajeHUD = "APUESTA: PERDISTE $" + costo + " (dado " + dado + ")"; }
            }
            case 1 -> { // Contrato riesgo
                if (dado >= 4)  { j.dinero += costo * 3; mensajeHUD = "CONTRATO: GANASTE $" + costo * 3 + " (dado " + dado + ")"; }
                else            { j.dinero -= 500;        mensajeHUD = "CONTRATO: PERDISTE + $500 EXTRA (dado " + dado + ")"; }
            }
            case 2 -> { // Mercado negro
                int[] efectos = {-1000, -500, 0, 500, 1000, 2000};
                int   efecto  = efectos[dado - 1];
                j.dinero += efecto;
                mensajeHUD = "MERCADO NEGRO: " + (efecto >= 0 ? "+" : "") + efecto + " (dado " + dado + ")";
            }
        }
        dibujar();
    }

    private void manejarIntel(int tipo, int costo) {
        Jugador j = estado.jugadorActual();
        if (!j.tieneAgencia()) { mensajeHUD = "REQUIERE AGENCIA DE INTELIGENCIA"; dibujar(); return; }
        if (j.dinero < costo)  { mensajeHUD = "FONDOS INSUFICIENTES";             dibujar(); return; }
        j.dinero -= costo;
        switch (tipo) {
            case 0 -> mensajeHUD = "ESPIONAJE: Rival tiene $" + estado.jugadores.get((estado.indiceTurno + 1) % estado.jugadores.size()).dinero;
            case 1 -> {
                Jugador rival = estado.jugadores.get((estado.indiceTurno + 1) % estado.jugadores.size());
                if (!rival.territorios.isEmpty()) {
                    Territorio objetivo = rival.territorios.get(0);
                    if (!objetivo.edificios.isEmpty()) {
                        objetivo.edificios.remove(0);
                        mensajeHUD = "SABOTAJE: Destruido edificio en " + objetivo.nombre;
                    } else mensajeHUD = "SABOTAJE: Sin edificios que destruir";
                }
            }
            case 2 -> {
                Jugador rival = estado.jugadores.get((estado.indiceTurno + 1) % estado.jugadores.size());
                int robo = Math.min(500, rival.dinero);
                rival.dinero -= robo;
                j.dinero     += robo;
                mensajeHUD = "INFILTRACIÓN: Robados $" + robo;
            }
        }
        dibujar();
    }

    // ─────────────────────────────────────────
    //  LÓGICA DE JUEGO
    // ─────────────────────────────────────────
    private void seleccionarTerritorio(int indice) {
        Territorio seleccionado = estado.territorios.get(indice);

        // Refuerzo
        if (seleccionado.dueno == estado.jugadorActual() && estado.jugadorActual().tropasDisponibles > 0) {
            seleccionado.tropas++;
            estado.jugadorActual().tropasDisponibles--;
            territorioPanel = seleccionado;
            mensajeHUD = "TROPA COLOCADA | Restantes: " + estado.jugadorActual().tropasDisponibles;
            dibujar(); return;
        }
        
     // MODO MOVER
        if (panelActivo.equals("MOVER")) {
            if (territorioClickeado == -1) {
                if (seleccionado.dueno != estado.jugadorActual()) {
                    mensajeHUD = "SELECCIONA TU TERRITORIO ORIGEN";
                    dibujar(); return;
                }
                if (seleccionado.tropas <= 1) {
                    mensajeHUD = "MÍNIMO 2 TROPAS PARA MOVER";
                    dibujar(); return;
                }
                territorioClickeado = indice;
                territorioPanel     = seleccionado;
                mensajeHUD = "MOVER DESDE: " + seleccionado.nombre + " | Selecciona destino amigo";
                dibujar();
            } else {
                Territorio origen  = estado.territorios.get(territorioClickeado);
                Territorio destino = seleccionado;
                if (destino.dueno != estado.jugadorActual()) {
                    mensajeHUD = "SOLO PUEDES MOVER A TERRITORIO AMIGO";
                    territorioClickeado = -1; dibujar(); return;
                }
                if (!origen.adyacentes.contains(destino)) {
                    mensajeHUD = "NO SON ADYACENTES";
                    territorioClickeado = -1; dibujar(); return;
                }
                origen.tropas--;
                destino.tropas++;
                mensajeHUD = "TROPA MOVIDA: " + origen.nombre + " → " + destino.nombre;
                territorioClickeado = -1;
                dibujar();
            }
            return;
        }

        if (territorioClickeado == -1) {
            if (seleccionado.dueno != estado.jugadorActual()) {
                mensajeHUD = "TERRITORIO ENEMIGO — selecciona el tuyo";
                dibujar(); return;
            }
            territorioClickeado = indice;
            territorioPanel     = seleccionado;
            mensajeHUD = "ORIGEN: " + seleccionado.nombre + " | Selecciona objetivo";
            dibujar();
        } else {
            Territorio origen  = estado.territorios.get(territorioClickeado);
            Territorio destino = seleccionado;

            if (origen == destino)                        { territorioClickeado = -1; dibujar(); return; }
            if (destino.dueno == estado.jugadorActual())  { mensajeHUD = "ES TU TERRITORIO";    territorioClickeado = -1; dibujar(); return; }
            if (!origen.adyacentes.contains(destino))     { mensajeHUD = "NO SON ADYACENTES";   territorioClickeado = -1; dibujar(); return; }
            if (origen.tropas <= 1)                       { mensajeHUD = "TROPAS INSUFICIENTES"; territorioClickeado = -1; dibujar(); return; }

            MotorCombate     motor = new MotorCombate();
            ResultadoCombate r     = motor.atacar(origen, destino);

            mensajeHUD = (r.atacanteGano
                ? "⚔ CONQUISTA: " + destino.nombre
                : "⚔ FALLA: "    + origen.nombre)
                + "  |  " + r.dadoAtacante + " vs " + r.dadoDefensor
                + "  |  " + r.evento;

            territorioClickeado = -1;
            dibujar();
        }
    }

    private void aplicarRefuerzo() {
        SistemaEconomia.cobrarIngresos(estado);
        Jugador actual   = estado.jugadorActual();
        int     refuerzo = Math.max(3, actual.territorios.size() / 3);
        actual.tropasDisponibles = refuerzo;
        mensajeHUD = actual.titulo + " | $" + actual.dinero
            + " (+$" + estado.ultimoIngreso + ") | Coloca " + refuerzo + " tropas";
    }

    private void verificarVictoria() {
        Jugador primero      = estado.territorios.get(0).dueno;
        boolean todosIguales = estado.territorios.stream().allMatch(t -> t.dueno == primero);
        if (!todosIguales) return;

        gc.setFill(Color.web("#000000", 0.92));
        gc.fillRect(0, 0, W, H);
        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_VICTORIA));
        gc.fillText("CAPITAL TOMADA", W / 2 - 380, H / 2 - 40);
        gc.setFont(Font.font(FUENTE, TAM_SUBTITULO));
        gc.fillText(primero.titulo.toUpperCase() + " CONTROLA EL MUNDO", W / 2 - 300, H / 2 + 40);
    }
}