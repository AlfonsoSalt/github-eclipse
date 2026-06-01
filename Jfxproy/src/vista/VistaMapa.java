package vista;

import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;
import modelo.*;
import logica.*;

public class VistaMapa extends Pane {

    private static final int    W                = 1920;
    private static final int    H                = 1000;
    private static final int    PANEL_X          = 1580;
    private static final int    PANEL_W          = W - PANEL_X;
    private static final int    BARRA_Y          = 860;
    private static final int    BARRA_H          = H - BARRA_Y;
    private static final int    INFO_H           = 260;
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

    private static final String COLOR_FONDO       = "#000000";
    private static final String COLOR_FONDO_PANEL = "#080808";
    private static final String COLOR_ACENTO      = "#00ff88";
    private static final String COLOR_TEXTO       = "#ffffff";
    private static final String COLOR_APAGADO     = "#444444";
    private static final String COLOR_BORDE       = "#2a2a2a";
    private static final String COLOR_POSITIVO    = "#001500";
    private static final String COLOR_NEGATIVO    = "#150000";
    private static final String COLOR_ACTIVO      = "#003300";
    private static final String COLOR_CONEXION    = "#222222";
    private static final String COLOR_GRIS        = "#888888";
    private static final String COLOR_SEPARADOR   = "#333333";

    // ── FIX: instancia de SistemaEconomia (ya no es estático) ────────────
    private final SistemaEconomia eco = new SistemaEconomia();

    private Image imgCaudillo, imgFentnyahu, imgAgente, imgSierra, imgElite, imgTanque;

    private Canvas          canvas;
    private GraphicsContext gc;
    private EstadoJuego     estado;

    private int              territorioClickeado = -1;
    private Territorio       territorioPanel     = null;
    private String           mensajeHUD          = "";
    private String           panelActivo         = "INFO";
    private ResultadoCombate ultimoResultado     = null;
    private Territorio       ultimoOrigen        = null;
    private Territorio       ultimoDestino       = null;

    private double[][] posiciones = {
            {110,310}, {830,310}, {870,220}, {850,390}, // Cloacan, Israel, Iranuke, La Bomba
            {270,230}, {160,240}, {120,150}, {200,290}, {320,110}, {420,40},  // Norteamérica
            {260,430}, {340,500}, {250,530}, {300,600}, {260,680}, {350,700}, // LATAM
            {460,130}, {490,260}, {570,160}, {600,280}, {670,130},            // Europa (¡Más espaciados!)
            {520,390}, {590,470}, {710,440}, {680,540}, {750,610}, {670,380}, // África
            {750,170}, {850,120}, {940,280}, {1020,380}, {1080,300}, {1100,150}, // Asia 1 (Más espaciados)
            {1200,200}, {1250,110}, {1150,380}, {1100,450},                   // Asia 2
            {1350,650}, {1400,450}, {1350,350}, {1420,280},                   // Pacífico/Oceanía
            {280,740}, {660,740}                                              // Antártida
        };

    public VistaMapa(EstadoJuego estado) {
        this.estado = estado;
        canvas = new Canvas(W, H);
        gc     = canvas.getGraphicsContext2D();
        getChildren().add(canvas);
        canvas.setOnMouseClicked(e -> manejarClick(e.getX(), e.getY()));

        imgCaudillo  = cargarImagen("/agentes/Calliacan.png");
        imgFentnyahu = cargarImagen("/agentes/Fentnyahu.png");
        imgAgente    = cargarImagen("/agentes/Agente.png");
        imgSierra    = cargarImagen("/agentes/Sierra.png");
        imgElite     = cargarImagen("/tropas/Elite.png");
        imgTanque    = cargarImagen("/tropas/Tanque.png");

        aplicarRefuerzo();
        dibujar();
    }

    private Image cargarImagen(String path) {
        try {
            var url = getClass().getResource(path);
            if (url == null) return null;
            return new Image(url.toExternalForm());
        } catch (Exception e) { return null; }
    }

    private Image imagenJugador(Jugador j) {
        if (j == null) return null;
        if (j.titulo.contains("Caudillo"))  return imgCaudillo;
        if (j.titulo.contains("Fentnyahu")) return imgFentnyahu;
        if (j.titulo.contains("Агент"))     return imgAgente;
        if (j.titulo.contains("Sierra"))    return imgSierra;
        return null;
    }

    public void dibujar() {
        gc.setFill(Color.web(COLOR_FONDO));
        gc.fillRect(0, 0, W, H);
        dibujarMapa();
        dibujarPanelDerecho();
        dibujarBarraInferior();
        dibujarHUDCombate();
        verificarVictoria();
    }

    
    //  MAPA @calli corrige las posiciones xfa
    // ─────────────────────────────────────────
    private void dibujarMapa() {
        gc.setStroke(Color.web(COLOR_SEPARADOR));
        gc.setLineWidth(1);
        gc.strokeRect(0, 0, PANEL_X, BARRA_Y);
        dibujarConexiones();
        dibujarTerritorios();
        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, TAM_HUD));
        String hud = mensajeHUD;

        if (hud.length() > 110)
            hud = hud.substring(0, 110) + "...";

        gc.fillText(hud, 10, BARRA_Y - 10);
    }

    private void dibujarConexiones() {
        List<Territorio> lista = estado.territorios;
        
        //líneas base
        for (int i = 0; i < lista.size(); i++) {
            for (Territorio ady : lista.get(i).adyacentes) {
                int j = lista.indexOf(ady);
                if (j > i) {
                    if (territorioClickeado != -1) {
                        gc.setStroke(Color.web(COLOR_CONEXION, 0.2)); // Atenuado
                    } else {
                        gc.setStroke(Color.web(COLOR_CONEXION));      // Normal
                    }
                    gc.setLineWidth(1.5);
                    gc.strokeLine(posiciones[i][0], posiciones[i][1], posiciones[j][0], posiciones[j][1]);
                }
            }
        }
        
        // resaltadas 
        if (territorioClickeado != -1) {
            gc.setStroke(Color.web(COLOR_ACENTO)); // Color de la interfaz activo
            gc.setLineWidth(3.0);                  // Línea más gruesa
            Territorio origen = lista.get(territorioClickeado);
            for (Territorio ady : origen.adyacentes) {
                int j = lista.indexOf(ady);
                gc.strokeLine(posiciones[territorioClickeado][0], posiciones[territorioClickeado][1],
                              posiciones[j][0], posiciones[j][1]);
            }
        }
    }

    private void dibujarTerritorios() {
        List<Territorio> lista = estado.territorios;
        for (int i = 0; i < lista.size(); i++) {
            Territorio t = lista.get(i);
            double x = posiciones[i][0], y = posiciones[i][1];
            Color c = t.dueno != null ? t.dueno.color : Color.GRAY;

            gc.setLineWidth(t.dueno != null && t.dueno.esNPC ? 0.5 : 1);

            if (t.dueno != null && t.dueno.esNPC) {
                gc.setFill(Color.web("#444444"));
                gc.setFont(Font.font(FUENTE, 9));
                String tag = switch (t.dueno.titulo) {
                    case "El Mossad"   -> "[MOSSAD]";
                    case "Los Masones" -> "[MASONES]";
                    case "BlackRock"   -> "[BLACKROCK]";
                    default            -> "[NPC]";
                };
                gc.fillText(tag, x - 22, y + RADIO + 28);
            }

            if (t.esCapital) {
                gc.setFill(Color.web("#ffff00", 0.8));
                gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 10));
                gc.fillText("Capital ★", x - 5, y - RADIO - 4);
            }

            if (i == territorioClickeado) {
                gc.setFill(Color.web(COLOR_ACENTO, 0.2));
                gc.fillOval(x - RADIO - 10, y - RADIO - 10, (RADIO + 10) * 2, (RADIO + 10) * 2);
                gc.setStroke(Color.web(COLOR_ACENTO));
                gc.setLineWidth(2);
                gc.strokeOval(x - RADIO - 10, y - RADIO - 10, (RADIO + 10) * 2, (RADIO + 10) * 2);
            }

            if (t == territorioPanel) {
                gc.setFill(Color.color(c.getRed(), c.getGreen(), c.getBlue(), 0.25));
                gc.fillOval(x - RADIO - 6, y - RADIO - 6, (RADIO + 6) * 2, (RADIO + 6) * 2);
            }

            gc.setFill(c);
            gc.fillOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);
            gc.setStroke(Color.web(COLOR_ACENTO));
            gc.setLineWidth(1);
            gc.strokeOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);

            gc.setFill(Color.BLACK);
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_MAPA_TROPAS));
            gc.fillText(String.valueOf(t.tropas), x - (t.tropas > 9 ? 9 : 5), y + 5);

            gc.setFill(Color.web(COLOR_GRIS));
            gc.setFont(Font.font(FUENTE, TAM_MAPA_NOMBRE));
            gc.fillText(t.nombre, x - 30, y + RADIO + 16);

            if (!t.edificios.isEmpty()) {
                double ex = x - (t.edificios.size() * 9) / 2.0;
                for (Edificio e : t.edificios) {
                    Color ec = switch (e.tipo) {
                        case PLANTA_ENERGETICA       -> Color.YELLOW;
                        case FABRICA_SEMICONDUCTORES -> Color.CYAN;
                        case REACTOR_NUCLEAR         -> Color.web("#ff6600");
                        case BANCO_CENTRAL           -> Color.LIME;
                        case CUARTEL                 -> Color.WHITE;
                        case FABRICA_TANQUES         -> Color.web("#888888");
                        case BASE_DRONES             -> Color.web("#00ccff");
                        case SILO_MISILES            -> Color.web("#ff0000");
                        case AGENCIA                 -> Color.web("#aa00ff");
                    };
                    gc.setFill(ec);
                    gc.fillRect(ex, y + RADIO + 20, 7, 7);
                    ex += 10;
                }
            }

            if (!t.unidades.isEmpty()) {
                double ux = x - (t.unidades.size() * 18) / 2.0;
                for (Unidad u : t.unidades) {
                    Image uImg = switch (u.tipo) {
                        case ELITЕ  -> imgElite;
                        case TANQUE -> imgTanque;
                        default     -> null;
                    };
                    if (uImg != null && !uImg.isError()) {
                        gc.drawImage(uImg, ux, y + RADIO + 18, 24, 24);
                    } else {
                        gc.setFill(Color.web("#ffffff"));
                        gc.fillRect(ux, y + RADIO + 20, 14, 14);
                    }
                    ux += 18;
                }
            }
        }
    }
    
    //ANIMACIONES DE COMBATE

    private void dibujarDado(double x, double y, int valor, Color color) {
        int s = 70;
        gc.setFill(Color.web("#000000", 0.6));
        gc.fillRoundRect(x + 4, y + 4, s, s, 12, 12);
        gc.setFill(Color.web("#111111"));
        gc.fillRoundRect(x, y, s, s, 12, 12);
        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.strokeRoundRect(x, y, s, s, 12, 12);

        double cx = x + s / 2.0, cy = y + s / 2.0, r = 7, off = 18;
        gc.setFill(color);
        int[][] puntos = {
            {}, {0,0}, {-1,-1,1,1}, {-1,-1,0,0,1,1},
            {-1,-1,1,-1,-1,1,1,1}, {-1,-1,1,-1,0,0,-1,1,1,1},
            {-1,-1,1,-1,-1,0,1,0,-1,1,1,1}
        };
        int[] p = puntos[Math.min(valor, 6)];
        for (int i = 0; i < p.length; i += 2)
            gc.fillOval(cx + p[i] * off - r, cy + p[i+1] * off - r, r * 2, r * 2);
        gc.setFill(Color.web("#ffffff", 0.4));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 11));
        gc.fillText(String.valueOf(valor), x + 4, y + 12);
    }
    private void dibujarHUDCombate() {

        if (ultimoResultado == null || ultimoOrigen == null || ultimoDestino == null)
            return;

        int x = 980;
        int y = 860;
        int w = 520;
        int h = 120;

        gc.setFill(Color.web("#050505", 0.92));
        gc.fillRoundRect(x, y, w, h, 12, 12);

        gc.setStroke(Color.web(COLOR_ACENTO));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x, y, w, h, 12, 12);

        // TITULO
        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 20));
        gc.fillText("COMBATE", x + 18, y + 28);

        // LINEA CENTRAL
        gc.setStroke(Color.web(COLOR_SEPARADOR));
        gc.strokeLine(x + w / 2, y + 15, x + w / 2, y + h - 15);

        // ATACANTE
        gc.setFill(ultimoOrigen.dueno.color);
        gc.fillOval(x + 28, y + 45, 46, 46);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 28));
        gc.fillText(String.valueOf(ultimoOrigen.tropas), x + 95, y + 78);

        gc.setFont(Font.font(FUENTE, 14));
        gc.fillText(ultimoOrigen.nombre, x + 95, y + 100);

        // DEFENSOR
        gc.setFill(ultimoDestino.dueno.color);
        gc.fillOval(x + 290, y + 45, 46, 46);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 28));
        gc.fillText(String.valueOf(ultimoDestino.tropas), x + 357, y + 78);

        gc.setFont(Font.font(FUENTE, 14));
        gc.fillText(ultimoDestino.nombre, x + 357, y + 100);

        // DADOS
        dibujarDado(x + 200, y + 12, ultimoResultado.dadoAtacante,
                ultimoOrigen.dueno.color);

        dibujarDado(x + 430, y + 12, ultimoResultado.dadoDefensor,
                ultimoDestino.dueno.color);

        // RESULTADO
        gc.setFill(ultimoResultado.atacanteGano
                ? Color.LIME
                : Color.web("#ff4444"));

        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 16));

        String resultado = ultimoResultado.atacanteGano
                ? "EL ATACANTE CONQUISTA"
                : "EL DEFENSOR RESISTE";

        gc.fillText(resultado, x + 140, y + 85);
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
        Jugador j = estado.jugadorActual();
        int px = PANEL_X + 13;
     
        Image img = imagenJugador(j);
        if (img != null && !img.isError()) {
            gc.save(); gc.beginPath();
            gc.arc(px+36, 12+36, 36, 36, 0, 360); gc.closePath(); gc.clip();
            gc.drawImage(img, px, 12, 72, 72); gc.restore();
            gc.setStroke(j.color); gc.setLineWidth(2); gc.strokeOval(px, 12, 72, 72);
        } else {
            gc.setFill(Color.web("#111111")); gc.fillRoundRect(px, 12, 72, 72, 10, 10);
            gc.setStroke(j.color); gc.setLineWidth(2); gc.strokeRoundRect(px, 12, 72, 72, 10, 10);
        }
     
        int dx = px + 82;
        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_TITULO));
        gc.fillText(j.titulo.toUpperCase(), dx, 30);
     
        // FIX: muestra liquidez y reservas por separado
        gc.setFill(Color.web(COLOR_TEXTO));
        gc.setFont(Font.font(FUENTE, TAM_NORMAL));
        gc.fillText("LIQ $" + j.liquidez, dx, 50);
     
        gc.setFill(Color.web(j.reservas > 0 ? "#ffaa00" : COLOR_APAGADO));
        gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
        gc.fillText("RES $" + j.reservas, dx, 68);
     
        gc.setFill(Color.web(COLOR_APAGADO));
        gc.fillText("+" + j.ultimoIngreso + "/turno", dx, 84);
     
        // Indicador de inflación junto al ingreso
        if (SistemaEconomia.tasaInflacion > 0) {
            gc.setFill(Color.web("#ff4444"));
            gc.fillText(String.format("⚠INF%.0f%%", SistemaEconomia.tasaInflacion * 100), dx + 130, 84);
        }
     
        gc.setFill(Color.web(COLOR_APAGADO));
        gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
        gc.fillText("TERRITORIOS : " + j.territorios.size(),    px, 100);
        gc.fillText("TROPAS DISP : " + j.tropasDisponibles,     px, 116);
        gc.fillText("TURNO #"         + (estado.indiceTurno+1), px, 132);
     
        double pct = (double) j.territorios.size() / estado.territorios.size();
        gc.setFill(Color.web("#111111")); gc.fillRect(px, 142, PANEL_W - 24, 10);
        gc.setFill(j.color);             gc.fillRect(px, 142, (PANEL_W - 24) * pct, 10);
        gc.setStroke(Color.web(COLOR_BORDE)); gc.strokeRect(px, 142, PANEL_W - 24, 10);
     
        if (territorioPanel != null) {
            gc.setFill(Color.web("#111111")); gc.fillRect(px, 162, PANEL_W - 24, 110);
            gc.setStroke(Color.web(COLOR_BORDE)); gc.strokeRect(px, 162, PANEL_W - 24, 110);
            gc.setFill(Color.web(COLOR_ACENTO)); gc.setFont(Font.font(FUENTE, TAM_NORMAL));
            gc.fillText("// " + territorioPanel.nombre.toUpperCase(), px+8, 180);
            gc.setFill(Color.web(COLOR_TEXTO)); gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText("Tropas    : " + territorioPanel.tropas,              px+8, 198);
            gc.fillText("Ingreso   : $" + territorioPanel.calcularIngreso(),   px+8, 214);
            gc.fillText("Edificios : " + territorioPanel.edificios.size(),     px+8, 230);
            gc.fillText("Unidades  : " + territorioPanel.unidades.size(),      px+8, 246);
            gc.fillText("ATK bonus : +" + territorioPanel.bonusAtaqueTotal(),  px+8, 262);
            gc.fillText("DEF bonus : +" + territorioPanel.bonusDefensaTotal(), px+8, 278);
        } else {
            gc.setFill(Color.web(COLOR_APAGADO)); gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText("Selecciona un territorio", px, 185);
        }
    }

    // ─────────────────────────────────────────
    //  SUBPANELES
    // ─────────────────────────────────────────
    private void dibujarPanelConstruir() {
        if (territorioPanel == null) { dibujarMensajePanel("Selecciona territorio primero"); return; }
        int    px = PANEL_X + 13;
        double y  = INFO_H + 60;

        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_TITULO));
        gc.fillText("[ CONSTRUIR ]", px, y); y += 14;

        for (TipoEdificio tipo : TipoEdificio.values()) {
            boolean construido = territorioPanel.tieneEdificio(tipo);
            boolean puedePagar = estado.jugadorActual().puedeGastar(tipo.costo);

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

    private void dibujarPanelUnidades() {//ablancer pq es´tan rotos (Op) (no de que no funcionrn)
        if (territorioPanel == null) { dibujarMensajePanel("Selecciona territorio primero"); return; }
        int     px          = PANEL_X + 13;
        double  y           = INFO_H + 60;
        boolean tienePlanta = estado.jugadorActual().tienePlanta();
        boolean tieneTanque = estado.jugadorActual().tieneTanque();
        boolean tieneChip = estado.jugadorActual().tieneChips();
        

        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_TITULO));
        gc.fillText("[ UNIDADES ]", px, y); y += 14;
        
        
        for (TipoUnidad tipo : TipoUnidad.values()) {
            boolean puedePagar = estado.jugadorActual().liquidez >= tipo.costo;
            
            boolean tieneReq = switch (tipo) {
                case SOLDADO -> estado.jugadorActual().tienePlanta();
                case TANQUE  -> estado.jugadorActual().tieneTanque() && estado.jugadorActual().tienePlanta();
                case ELITЕ    -> estado.jugadorActual().tienePlanta()  && estado.jugadorActual().tieneChips(); 
                default -> false;
            };

            boolean disponible = puedePagar && tieneReq;
            gc.setFill(Color.web(disponible ? COLOR_POSITIVO : COLOR_NEGATIVO));
            gc.fillRect(px, y, PANEL_W - 24, ROW_H_UNIDAD - 4);
            gc.setStroke(Color.web(COLOR_BORDE));
            gc.strokeRect(px, y, PANEL_W - 24, ROW_H_UNIDAD - 4);
            gc.setFill(Color.web(disponible ? COLOR_TEXTO : COLOR_APAGADO));
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_PEQUEÑO));
            gc.fillText(tipo.nombre + "   $" + tipo.costo, px + 8, y + 16);
            
            gc.setFill(Color.web(COLOR_ACENTO));
            gc.setFont(Font.font(FUENTE, 14));
            
            String descReq = switch (tipo) {
                case SOLDADO -> tieneReq ? tipo.descripcion : "Requiere Planta";
                case TANQUE  -> tieneReq ? tipo.descripcion : "Req.Planta, Fca Tanques";
                case ELITЕ    -> tieneReq ? tipo.descripcion : "Req.Planta, Fca Chips";
                default -> "";
            };
            
            gc.fillText("ATK +" + tipo.bonusAtaque + "   DEF +" + tipo.bonusDefensa + " | " + descReq, px + 2, y + 36);
            y += ROW_H_UNIDAD;
        }
        gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
        
      //  gc.fillText(tienePlanta ? TipoUnidad.SOLDADO.descripcion : "Requiere Planta Energética", px + 8, y-130 );
        //gc.fillText(tieneTanque ? TipoUnidad.TANQUE.descripcion : "Requiere Fabrica de Tanque", px + 8, y -70);

        
    }

    private void dibujarPanelEconomia() {
        int    px = PANEL_X + 12;
        double y  = INFO_H + 60;
        Jugador j = estado.jugadorActual();
     
        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_TITULO));
        gc.fillText("[ ECONOMÍA ]", px, y); y += 22;
     
        // Stats financieros
        gc.setFill(Color.web(COLOR_TEXTO)); gc.setFont(Font.font(FUENTE, TAM_NORMAL));
        gc.fillText("LIQUIDEZ      : $" + j.liquidez, px, y); y += 18;
        gc.setFill(Color.web(j.reservas > 0 ? "#ffaa00" : COLOR_APAGADO));
        gc.fillText("RESERVAS      : $" + j.reservas, px, y); y += 18;
        gc.setFill(Color.web(COLOR_TEXTO));
        gc.fillText("RIQUEZA TOTAL : $" + j.riquezaTotal(), px, y); y += 18;
        gc.fillText("Ingreso/turno : $" + j.ultimoIngreso, px, y); y += 18;
        gc.setFill(Color.web(COLOR_APAGADO)); gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
        gc.fillText("Territorios   : " + j.territorios.size(), px, y); y += 24;
     
        // FIX: caja fija de inversiones activas (altura siempre 50px → click handler no se desincroniza)
        gc.setFill(Color.web("#0a0a0a")); gc.fillRect(px, y, PANEL_W - 24, 46);
        gc.setStroke(Color.web(j.inversionesActivas.isEmpty() ? COLOR_BORDE : "#ffaa00"));
        gc.strokeRect(px, y, PANEL_W - 24, 46);
        gc.setFont(Font.font(FUENTE, 11));
        if (j.inversionesActivas.isEmpty()) {
            gc.setFill(Color.web(COLOR_APAGADO));
            gc.fillText("  sin inversiones activas", px+6, y+18);
        } else {
            for (int i = 0; i < Math.min(3, j.inversionesActivas.size()); i++) {
                InversionReserva inv = j.inversionesActivas.get(i);
                gc.setFill(Color.web("#ffaa00"));
                gc.fillText(String.format("▣ %s  +$%d  [%dT]",
                    inv.getTipo().nombre,
                    (int)(inv.getCapital() * inv.getTipo().retorno),
                    inv.getTurnosRestantes()), px+6, y + 14 + i * 14);
            }
        }
        y += 50;
     
        if (j.prestamoPendiente > 0) {
            gc.setFill(Color.web("#ff4444")); gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText("⚠ DEUDA: $" + j.prestamoPendiente + " | Turnos: " + j.turnosPrestamo, px, y);
            y += 18;
        }
     
        gc.setStroke(Color.web(COLOR_SEPARADOR)); gc.strokeLine(px, y, px+PANEL_W-24, y); y += 14;
        gc.setFill(Color.web(COLOR_ACENTO)); gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_PEQUEÑO));
        gc.fillText("[ PRÉSTAMOS ]", px, y); y += 14;
     
        Object[][] prestamos = {
            {"Préstamo Pequeño", 500,  "Devuelves $750 en 3 turnos"},
            {"Préstamo Grande",  2000, "Devuelves $3000 en 3 turnos"},
            {"Pagar Deuda",      0,    "Liquidar préstamo activo"}
        };
        for (Object[] p : prestamos) {
            String txt = (String) p[0]; int monto = (int) p[1]; String desc = (String) p[2];
            boolean puede = monto == 0
                ? j.prestamoPendiente > 0 && j.liquidez >= j.prestamoPendiente
                : j.prestamoPendiente == 0;
            gc.setFill(Color.web(puede ? COLOR_POSITIVO : COLOR_NEGATIVO));
            gc.fillRect(px, y, PANEL_W-24, ROW_H_EDIFICIO-4);
            gc.setStroke(Color.web(puede ? COLOR_ACENTO : COLOR_BORDE)); gc.setLineWidth(1);
            gc.strokeRect(px, y, PANEL_W-24, ROW_H_EDIFICIO-4);
            gc.setFill(Color.web(puede ? COLOR_TEXTO : COLOR_APAGADO));
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_PEQUEÑO));
            gc.fillText(txt + (monto > 0 ? "  $" + monto : ""), px+8, y+16);
            gc.setFill(Color.web(COLOR_APAGADO)); gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText(desc, px+8, y+30); y += ROW_H_EDIFICIO;
        }
     
       
        
        
     
        // MERCADO DE COMMODITIES + INFLACIÓN
        y += 8; gc.setStroke(Color.web(COLOR_SEPARADOR)); gc.strokeLine(px, y, px+PANEL_W-24, y); y += 14;
        gc.setFill(Color.web(COLOR_ACENTO)); gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_PEQUEÑO));
        gc.fillText("[ MERCADO DE BIENES ]", px, y); y += 14;
     
        if (SistemaEconomia.tasaInflacion > 0) {
            gc.setFill(Color.web("#ff4444")); gc.setFont(Font.font(FUENTE, 11));
            gc.fillText(String.format("⚠ INFLACIÓN %.0f%% — ingresos reducidos", SistemaEconomia.tasaInflacion * 100), px, y);
            y += 16;
        }
     
        int midX = PANEL_X + (PANEL_W) / 2;
        for (SistemaBienes.Bien c : SistemaBienes.Bien.values()) {
            int p = SistemaBienes.precio(c), d = SistemaBienes.delta(c);
            int stock = SistemaBienes.stock(j, c);
            gc.setFill(Color.web("#0d0d0d")); gc.fillRect(px, y, PANEL_W-24, ROW_H_EDIFICIO-4);
            gc.setStroke(Color.web(COLOR_BORDE)); gc.strokeRect(px, y, PANEL_W-24, ROW_H_EDIFICIO-4);
            gc.strokeLine(midX, y, midX, y+ROW_H_EDIFICIO-4); // divisor comprar|vender
     
            gc.setFill(Color.web(COLOR_TEXTO)); gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 11));
            gc.fillText(c.nombre, px+6, y+14);
            gc.setFill(d >= 0 ? Color.LIME : Color.web("#ff4444"));
            gc.fillText("$" + p + (d >= 0 ? " ▲" : " ▼"), px+6, y+28);
            gc.setFill(Color.web(j.puedeGastar(p) ? COLOR_TEXTO : COLOR_APAGADO));
            gc.fillText("[COMPRAR]", px+6, y+42);
     
            gc.setFill(Color.web(COLOR_APAGADO)); gc.fillText("STOCK:" + stock, midX+6, y+14);
            gc.setFill(Color.web(stock > 0 ? COLOR_ACENTO : COLOR_APAGADO));
            gc.fillText("[VENDER]", midX+6, y+42);
            y += ROW_H_EDIFICIO;
        }
    }

    private void dibujarPanelIntel() {
        int    px = PANEL_X + 12;
        double y  = INFO_H + 60;
        Jugador j = estado.jugadorActual();

        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_TITULO));
        gc.fillText("[ INTELIGENCIA ]", px, y); y += 14;

        String[] ops  = {"Sabotaje ($900)", "Infiltración ($1200)", "Ataque Dron ($800)", "Ataque Misil ($1500)", "Ataque Nuclear ($4000)"};
        int[]    costos = {900, 1200, 800, 1500, 4000};
        
        boolean tieneAgencia = j.tieneAgencia(); // Requisito universal para este panel
        
        for (int i = 0; i < ops.length; i++) {
            boolean reqEdificio = false;
            String reqFalta = "";
            
            switch (i) {
                case 0, 1 -> { 
                    reqEdificio = tieneAgencia; 
                    reqFalta = "Requiere Agencia"; 
                }
                case 2 -> { 
                    reqEdificio = tieneAgencia && j.tieneBaseDrones(); 
                    reqFalta = "Req. Agencia y B. Drones"; 
                }
                case 3 -> { 
                    reqEdificio = tieneAgencia && j.tieneSilo(); 
                    reqFalta = "Req. Agencia y Silo"; 
                }
                case 4 -> { 
                    reqEdificio = tieneAgencia && j.tieneSilo() && j.tieneReactor(); 
                    reqFalta = "Agencia, Silo, Reactor"; 
                }
            }
            
            boolean puede = reqEdificio && j.liquidez >= costos[i];
            
            gc.setFill(Color.web(puede ? COLOR_POSITIVO : COLOR_NEGATIVO));
            gc.fillRect(px, y, PANEL_W - 24, ROW_H_EDIFICIO - 4);
            gc.setStroke(Color.web(COLOR_BORDE));
            gc.strokeRect(px, y, PANEL_W - 24, ROW_H_EDIFICIO - 4);
            gc.setFill(Color.web(puede ? COLOR_TEXTO : COLOR_APAGADO));
            gc.setFont(Font.font(FUENTE, TAM_PEQUEÑO));
            gc.fillText(reqEdificio ? ops[i] : reqFalta, px + 8, y + 22);
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
        gc.setStroke(Color.web(COLOR_ACENTO)); gc.setLineWidth(1);
        gc.strokeLine(0, BARRA_Y, W, BARRA_Y);

        int      btnY  = BARRA_Y + (BARRA_H - BTN_H) / 2;
        String[] menus = {"CONSTRUIR","UNIDADES","ECONOMIA","INTEL","MOVER"};
        for (int i = 0; i < menus.length; i++) {
            int     bx     = BTN_START_X + i * (BTN_W + BTN_GAP);
            boolean activo = panelActivo.equals(menus[i]);
            gc.setFill(Color.web(activo ? COLOR_ACENTO : COLOR_FONDO_PANEL));
            gc.fillRect(bx, btnY, BTN_W, BTN_H);
            gc.setStroke(Color.web(COLOR_ACENTO)); gc.setLineWidth(1);
            gc.strokeRect(bx, btnY, BTN_W, BTN_H);
            gc.setFill(Color.web(activo ? COLOR_FONDO : COLOR_ACENTO));
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_NORMAL));
            gc.fillText(menus[i], bx + 28, btnY + 27);
        }

        int ftX = W - 220;
        gc.setFill(Color.web(COLOR_ACTIVO));
        gc.fillRect(ftX, btnY, 200, BTN_H);
        gc.setStroke(Color.web(COLOR_ACENTO));
        gc.strokeRect(ftX, btnY, 200, BTN_H);
        gc.setFill(Color.web(COLOR_ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, TAM_NORMAL));
        gc.fillText("FIN DE TURNO", ftX + 8, btnY + 27);
    }

    // ─────────────────────────────────────────
    //  CLICKS
    // ─────────────────────────────────────────
    private void manejarClick(double mouseX, double mouseY) {
      /*  if (ultimoResultado != null) {
            ultimoResultado = null; ultimoOrigen = null; ultimoDestino = null;*/
            dibujar(); 
        

        if (mouseY >= BARRA_Y) {
            int btnY = BARRA_Y + (BARRA_H - BTN_H) / 2;
            if (mouseX >= W - 220 && mouseX <= W - 20 && mouseY >= btnY && mouseY <= btnY + BTN_H) {
                estado.siguienteTurno();
                aplicarRefuerzo();
                territorioPanel = null; territorioClickeado = -1; panelActivo = "INFO";
                dibujar(); return;
            }
            String[] menus = {"CONSTRUIR","UNIDADES","ECONOMIA","INTEL","MOVER"};
            for (int i = 0; i < menus.length; i++) {
                int bx = BTN_START_X + i * (BTN_W + BTN_GAP);
                if (mouseX >= bx && mouseX <= bx + BTN_W && mouseY >= btnY && mouseY <= btnY + BTN_H) {
                    panelActivo = menus[i]; dibujar(); return;
                }
            }
            return;
        }

        if (mouseX >= PANEL_X) { manejarClickPanel(mouseX, mouseY); return; }

        List<Territorio> lista = estado.territorios;
        for (int i = 0; i < lista.size(); i++) {
            double dx = mouseX - posiciones[i][0], dy = mouseY - posiciones[i][1];
            if (Math.sqrt(dx*dx + dy*dy) <= RADIO) { seleccionarTerritorio(i); return; }
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
                        // FIX: eco.construir (era SistemaEconomia.construir estático)
                        boolean ok = eco.construir(estado.jugadorActual(), territorioPanel, tipo);
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
                        // FIX: eco.comprarUnidad (era SistemaEconomia.comprarUnidad estático)
                        boolean ok = eco.comprarUnidad(estado.jugadorActual(), territorioPanel, tipo);
                        // FIX: j.liquidez (era j.dinero)
                        mensajeHUD = ok
                            ? "COMPRADO: " + tipo.nombre + " | $" + estado.jugadorActual().liquidez
                            : !estado.jugadorActual().tienePlanta() ? "REQUIERE PLANTA ENERGÉTICA" : "FONDOS INSUFICIENTES";
                        dibujar(); return;
                    }
                    y += ROW_H_UNIDAD;
                }
            }
            case "ECONOMIA" -> {
                Jugador j = estado.jugadorActual();
                // Offsets sincronizados con dibujarPanelEconomia (altura fija por sección)
                double dy = INFO_H + 20;
                dy += 22;  // título
                dy += 20;  // LIQUIDEZ
                dy += 20;  // RESERVAS
                dy += 20;  // RIQUEZA TOTAL
                dy += 20;  // Ingreso/turno
                dy += 24;  // Territorios
                dy += 50;  // caja inversiones (FIJA)
                if (j.prestamoPendiente > 0) dy += 18;
                dy += 16;  // separator
                dy += 14;  // título préstamos
             
                // Préstamos
                if (mouseY >= dy && mouseY <= dy+ROW_H_EDIFICIO-4) { mensajeHUD = SistemaFinanzas.tomarPrestamo(j, 500);  dibujar(); return; } dy += ROW_H_EDIFICIO;
                if (mouseY >= dy && mouseY <= dy+ROW_H_EDIFICIO-4) { mensajeHUD = SistemaFinanzas.tomarPrestamo(j, 2000); dibujar(); return; } dy += ROW_H_EDIFICIO;
                if (mouseY >= dy && mouseY <= dy+ROW_H_EDIFICIO-4) { mensajeHUD = SistemaFinanzas.pagarPrestamo(j);       dibujar(); return; } dy += ROW_H_EDIFICIO;
             
                dy += 8 + 14 + 14; // sep + título bienes
             
                
                if (SistemaEconomia.tasaInflacion > 0) dy += 16;
             
                // Commodities — izquierda = comprar, derecha = vender
                int midX = PANEL_X + PANEL_W / 2;
                for (SistemaBienes.Bien c : SistemaBienes.Bien.values()) {
                    if (mouseY >= dy && mouseY <= dy+ROW_H_EDIFICIO-4) {
                        mensajeHUD = mouseX < midX ? SistemaBienes.comprar(j, c) : SistemaBienes.vender(j, c);
                        dibujar(); return;
                    }
                    dy += ROW_H_EDIFICIO;
                }
            }
            case "INTEL" -> {
 //// / / / 
            	int[] costos = {900, 1200, 800, 1500, 2500}; // FIX: Ahora reconoce los 5 botones
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

    private void manejarIntel(int tipo, int costo) {
        Jugador j = estado.jugadorActual();
        
        // 1. Verificación de infraestructura (Filtro universal de Agencia)
        if (!j.tieneAgencia()) { 
             mensajeHUD = "TODA OPERACIÓN REQUIERE AGENCIA DE INTELIGENCIA"; 
             dibujar(); return; 
         }
         
        // reqs
        if (tipo == 2 && !j.tieneBaseDrones()) { mensajeHUD = "REQUIERE BASE DE DRONES"; dibujar(); return; }
        if (tipo == 3 && !j.tieneSilo()) { mensajeHUD = "REQUIERE SILO DE MISILES"; dibujar(); return; }
        if (tipo == 4 && (!j.tieneSilo() || !j.tieneReactor())) { mensajeHUD = "REQUIERE SILO Y REACTOR NUCLEAR"; dibujar(); return; }
        
        // 2. Verificación de fondos
        if (!j.puedeGastar(costo)) { mensajeHUD = "FONDOS INSUFICIENTES"; dibujar(); return; }
        
        // 3. Verificación de Objetivo elegido en el mapa
        if (tipo >= 2 && tipo <= 4) { // Dron, Misil y Nuclear requieren objetivo
            if (territorioPanel == null || territorioPanel.dueno == j) {
                mensajeHUD = "SELECCIONA UN TERRITORIO ENEMIGO EN EL MAPA PRIMERO";
                dibujar(); return;
            }
        }
        
        j.gastar(costo);
        
        /// ATAQUE INTELIGENCIA
        switch (tipo) {
            case 0 -> { // Sabotaje (Robar Dinero)
                Jugador rival = estado.jugadores.get((estado.indiceTurno + 1) % estado.jugadores.size());
                int robo = Math.min(500, rival.liquidez);
                rival.liquidez -= robo;
                j.recibirLiquidez(robo);
                mensajeHUD = "SABOTAJE: Has robado $" + robo + " de los fondos enemigos.";
            }
            case 1 -> { // Infiltración (1 unidad aleatoria)
                List<Jugador> enemigos = estado.jugadores.stream()
                        .filter(jug -> jug != j && !jug.territorios.isEmpty())
                        .toList();
                if (!enemigos.isEmpty()) {
                    Jugador rival = enemigos.get((int)(Math.random() * enemigos.size()));
                    List<Territorio> vulnerables = rival.territorios.stream()
                            .filter(t -> t.tropas > 1)
                            .toList();
                    if (!vulnerables.isEmpty()) {
                        Territorio tRival = vulnerables.get((int)(Math.random() * vulnerables.size()));
                        tRival.tropas--;
                        if (!tRival.unidades.isEmpty()) tRival.unidades.remove(0); 
                        mensajeHUD = "INFILTRACIÓN EXITOSA: 1 unidad eliminada en " + tRival.nombre;
                    } else {
                        mensajeHUD = "INFILTRACIÓN FALLIDA: El enemigo no tiene tropas suficientes para eliminar.";
                    }
                } else {
                    mensajeHUD = "INFILTRACIÓN FALLIDA: No hay enemigos válidos.";
                }
            }
            case 2 -> { // Ataque Dron (1 unidad seleccionada, no importa adyacencia)
                if (territorioPanel.tropas > 1) {
                    territorioPanel.tropas--;
                    if (!territorioPanel.unidades.isEmpty()) territorioPanel.unidades.remove(0);
                    mensajeHUD = "ATAQUE DRON: 1 unidad eliminada en " + territorioPanel.nombre;
                } else {
                    mensajeHUD = "ATAQUE DRON DESPERDICIADO: Territorio enemigo ya está a 1 tropa.";
                }
            }
            case 3 -> { // Ataque Misil (Dirigido y destruye 1 edificio)
                territorioPanel.tropas = 1;
                territorioPanel.unidades.clear();
                
                String extra = "";
                if (!territorioPanel.edificios.isEmpty()) {
                    territorioPanel.edificios.remove(0);
                    extra = " y 1 edificio destruido";
                }
                mensajeHUD = "IMPACTO MISIL: " + territorioPanel.nombre + " reducido a 1 tropa" + extra + ".";
            }
            case 4 -> { // Ataque Nuclear (Seleccionado, destruye TODOS los edificios, deja 1 unidad)
                territorioPanel.tropas = 1;
                territorioPanel.edificios.clear();
                territorioPanel.unidades.clear();
                mensajeHUD = "IMPACTO NUCLEAR: " + territorioPanel.nombre + " HA SIDO DEVASTADO.";
            }
        }
        dibujar();
    }

    //  LÓGICA DE JUEGO no lo rompan xfa
    // ─────────────────────────────────────────
    private void seleccionarTerritorio(int indice) {
        Territorio seleccionado = estado.territorios.get(indice);

        // Fase de colocar tropas
        if (seleccionado.dueno == estado.jugadorActual() && estado.jugadorActual().tropasDisponibles > 0) {
            seleccionado.tropas++;
            estado.jugadorActual().tropasDisponibles--;
            territorioPanel = seleccionado;
            mensajeHUD = "TROPA COLOCADA | Restantes: " + estado.jugadorActual().tropasDisponibles;
            dibujar(); return;
        }

        // Movimiento
        if (panelActivo.equals("MOVER")) {
            if (territorioClickeado == -1) {
                if (seleccionado.dueno != estado.jugadorActual()) { mensajeHUD = "SELECCIONA TU TERRITORIO ORIGEN"; dibujar(); return; }
                if (seleccionado.tropas <= 1) { mensajeHUD = "MÍNIMO 2 TROPAS PARA MOVER"; dibujar(); return; }
                territorioClickeado = indice;
                territorioPanel = seleccionado;
                mensajeHUD = "MOVER DESDE: " + seleccionado.nombre + " | Selecciona destino amigo"; dibujar();
            } else {
                Territorio origen = estado.territorios.get(territorioClickeado), destino = seleccionado;
                if (destino.dueno != estado.jugadorActual()) { mensajeHUD = "SOLO PUEDES MOVER A TERRITORIO AMIGO"; territorioClickeado = -1; dibujar(); return; }
                if (!origen.adyacentes.contains(destino))    { mensajeHUD = "NO SON ADYACENTES"; territorioClickeado = -1; dibujar(); return; }
                origen.tropas--;
                destino.tropas++;
                mensajeHUD = "TROPA MOVIDA: " + origen.nombre + " -> " + destino.nombre;
                territorioClickeado = -1; dibujar();
            }
            return;
        }

        // marcar objetivos de Inteligencia
        if (panelActivo.equals("INTEL")) {
            territorioPanel = seleccionado;
            mensajeHUD = "OBJETIVO MARCADO: " + seleccionado.nombre;
            dibujar();
            return;
        }

        // Combate normal
        if (territorioClickeado == -1) { 
            /*AQUI NO ROMPER PARA ACTIVAR INTELIGENCIA calli por favor basta*/
            if (seleccionado.dueno != estado.jugadorActual()) { 
                mensajeHUD = "TERRITORIO ENEMIGO   selecciona el tuyo";
                dibujar(); return; 
            } 
            territorioClickeado = indice;
            territorioPanel = seleccionado;
            mensajeHUD = "ORIGEN: " + seleccionado.nombre + " | Selecciona objetivo"; dibujar();
        } else {
            Territorio origen = estado.territorios.get(territorioClickeado), destino = seleccionado;
            if (origen == destino)                       { territorioClickeado = -1; dibujar(); return; }
            if (destino.dueno == estado.jugadorActual()) { mensajeHUD = "ES TU TERRITORIO"; territorioClickeado = -1; dibujar(); return; }
            if (!origen.adyacentes.contains(destino))    { mensajeHUD = "NO SON ADYACENTES"; territorioClickeado = -1; dibujar(); return; }
            if (origen.tropas <= 1)                      { mensajeHUD = "TROPAS INSUFICIENTES"; territorioClickeado = -1; dibujar(); return; }
            MotorCombate     motor = new MotorCombate();
            ResultadoCombate r     = motor.atacar(origen, destino);
            territorioPanel = destino;
            ultimoResultado = r; ultimoOrigen = origen; ultimoDestino = destino;
            mensajeHUD = (r.atacanteGano ? "   CONQUISTA: " : "   FALLA: ")
                       + destino.nombre + "  |  " + r.dadoAtacante + " vs " + r.dadoDefensor;
            territorioClickeado = -1;
            dibujar();
        }
    }

    private void aplicarRefuerzo() {
        while (estado.jugadorActual().esNPC) estado.siguienteTurno();
        SistemaBienes.fluctuar();

        List<String> invLogs = eco.procesarInversionesMaduras(estado);
        if (!invLogs.isEmpty()) mensajeHUD = invLogs.get(invLogs.size() - 1);

        String alertaBanco = eco.cobrarIngresos(estado);
        Jugador actual = estado.jugadorActual();
        
        // Refuerzos base (Se corrige el bug de "size() / 1")
        int refuerzo = Math.max(1, actual.territorios.size() / 3);
        
        // +1 Tropa extra por cada Cuartel          
        long cuarteles = actual.territorios.stream().filter(t -> t.tieneEdificio(TipoEdificio.CUARTEL)).count();
        refuerzo += (int) cuarteles;
        
        actual.tropasDisponibles = refuerzo;
        
        if (alertaBanco != null) {
            mensajeHUD = alertaBanco; // Muestra el castigo o embargo
        } else {
            mensajeHUD = actual.titulo
                + "  |  LIQ $" + actual.liquidez
                + (actual.reservas > 0 ? "  RES $" + actual.reservas : "")
                + (SistemaEconomia.tasaInflacion > 0 ? String.format("   INF%.0f%%", SistemaEconomia.tasaInflacion * 100) : "")
                + "  (+$" + actual.ultimoIngreso + ")  |  Coloca " + refuerzo + " tropas";
        }
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