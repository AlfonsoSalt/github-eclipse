package vista;

import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
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
    private static final int    W           = 1920;
    private static final int    H           = 1000;
    private static final String FUENTE      = "OCR A Extended";
    private static final String ACENTO      = "#00ff88";
    private static final int    PANEL_W     = 440;
    private static final int    MAPA_X      = PANEL_W;
    private static final int    MAPA_W      = W - PANEL_W;

    // ══════════════════════════════════
    //  DATOS AGENTES
    // ══════════════════════════════════
    
    private Image imgCaudillo;
    private Image imgFentnyahu;
    private Image imgAgente;
    private Image imgSierra;
    
    private static final String[] TODOS_TITULOS = {
        "Caudillo de Culiacán", "Fentnyahu", "Агент", "Sierra7"
    };
    private static final Color[] TODOS_COLORES = {
        Color.LIME, Color.RED, Color.CYAN, Color.YELLOW
    };
    private static final String[] DESCRIPCIONES = {
        "Señor de la guerra. Controla rutas y\nlealtades a sangre fría. Sin piedad.",
        "Estratega sin escrúpulos. Para él,\nla guerra es solo otro mercado.",
        "Fantasma del estado profundo.\nOpera donde nadie lo ve venir.",
        "Operativo de élite clasificado.\nEntrenado para lo imposible."
    };
    private static final String[] BONUS_TEXTO = {
        "+2 tropas extra en capital",
        "+$500 ingreso adicional / turno",
        "Agencia de Inteligencia gratis",
        "+$500 capital inicial"
    };

    // ══════════════════════════════════
    //  TERRITORIOS Y MAPA
    // ══════════════════════════════════
    private static final String[] TODOS_TERRITORIOS = {
            "Cloacan", "Israel", "Iranuke", "La Bomba",
            "Tilted Tower", "New Vegas", "El Oso", "El Crudo", "El Maple", "Agroenlandia",
            "El Capo", "Amazon°", "Terraseca", "La Marina", "Patagonia", "Nuevo Sol",
            "Reino Separado", "Ibailagos", "Hausefgaben", "Nueva Roma", "Droenladia",
            "Ivory", "Savahna", "Warlord", "Mar Sangre", "Julien", "Kampf",
            "Vodkistahn", "La Plaga", "Leiribo", "Nehong Kong", "Gran Astro", "XingXing", "Dosaka", "La Oz", "Neolatam", "Buenasia",
            "Neo Zelanda", "Los Polinesios", "Mar hostilsico", "Big St James", "El Origen", "BajoTerra"
        };

    private static final double[][] POS = {
            {470,320}, {1190,320}, {1270,270}, {1210,400}, 
            {630,240}, {520,250}, {480,160}, {560,300}, {680,120}, {780,50},
            {620,440}, {700,510}, {610,540}, {660,610}, {620,690}, {710,710},
            {880,160}, {880,250}, {940,200}, {990,260}, {1010,170},
            {880,400}, {950,480}, {1070,450}, {1040,550}, {1110,620}, {1030,390},
            {1090,230}, {1180,190}, {1280,360}, {1380,390}, {1440,310}, {1460,210},
            {1540,250}, {1610,120}, {1510,390}, {1460,460},
            {1710,660}, {1760,460}, {1710,360}, {1780,290},
            {640,750}, {1020,750} 
        };

        private static final int[][] ADY = {
            {0,7}, {0,10}, {1,19}, {1,25}, {1,2}, {1,3}, {2,1}, {2,3}, {2,29}, {2,27}, 
            {3,21}, {3,25}, {3,1}, {3,23}, {3,2}, {4,7}, {4,5}, {4,8}, {4,16}, 
            {5,6}, {5,7}, {5,4}, {5,8}, {6,5}, {6,40}, {6,34}, {7,0}, {7,5}, {7,4}, {7,10}, 
            {8,5}, {8,4}, {8,9}, {9,8}, {9,16}, {10,0}, {10,7}, {10,11}, {10,12}, 
            {11,10}, {11,13}, {11,21}, {12,10}, {12,13}, {12,14}, {13,11}, {13,12}, {13,14}, {13,15}, 
            {14,12}, {14,13}, {14,15}, {14,41}, {15,13}, {15,14}, {15,26}, {16,4}, {16,9}, {16,18}, {16,17}, 
            {17,16}, {17,18}, {17,21}, {17,19}, {18,16}, {18,17}, {18,19}, {18,20}, 
            {19,18}, {19,20}, {19,27}, {19,25}, {19,1}, {19,17}, {20,18}, {20,19}, {20,27}, 
            {21,11}, {21,17}, {21,22}, {21,3}, {22,21}, {22,24}, {22,23}, {23,22}, {23,24}, {23,25}, {23,3}, 
            {24,22}, {24,23}, {24,26}, {25,19}, {25,1}, {25,23}, {25,3}, {26,24}, {26,15}, {26,42}, 
            {27,19}, {27,20}, {27,28}, {27,2}, {28,27}, {28,29}, {28,32}, {29,2}, {29,28}, {29,30}, 
            {30,29}, {30,31}, {30,36}, {31,30}, {31,32}, {31,33}, {32,28}, {32,34}, {32,33}, {32,31}, 
            {33,31}, {33,32}, {33,34}, {33,35}, {34,32}, {34,33}, {34,6}, {35,33}, {35,36}, {35,39}, 
            {36,30}, {36,35}, {36,37}, {37,36}, {37,38}, {37,42}, {38,37}, {38,39}, {38,40}, 
            {39,35}, {39,38}, {39,40}, {40,6}, {40,39}, {40,38}, {41,14}, {41,42}, {42,41}, {42,26}, {42,37}
        };

    // ══════════════════════════════════
    //  ESTADO
    // ══════════════════════════════════
    private int  fase          = 0;
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
        
        //imgs
        
        imgCaudillo  = cargarImagen("/agentes/Calliacan.png");
        imgFentnyahu = cargarImagen("/agentes/Fentnyahu.png");
        imgAgente    = cargarImagen("/agentes/Agente.png");
        imgSierra    = cargarImagen("/agentes/Sierra.png");
        
        
        dibujar();
    }

    // ══════════════════════════════════
    //  DIBUJO
    // ══════════════════════════════════
    
    private Image cargarImagen(String path) {
        try {
            var url = getClass().getResource(path);
            if (url == null) { System.out.println("NULL: " + path); return null; }
            return new Image(url.toExternalForm());
        } catch (Exception e) {
            System.out.println("ERROR: " + path + " — " + e.getMessage());
            return null;
        }
    }
    private Image imagenAgente(int idx) {
        return switch (idx) {
            case 0 -> imgCaudillo;
            case 1 -> imgFentnyahu;
            case 2 -> imgAgente;
            case 3 -> imgSierra;
            default -> null;
        };
    }
    
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
        // Título centrado
        gc.setFill(Color.web(ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 180));
        gc.fillText("CAPITAL", W / 2 - 310, 400);

        gc.setFill(Color.web("#333333"));
        gc.setFont(Font.font(FUENTE, 25));
        gc.fillText("Finanzas y estrategia — SELECCIONA NÚMERO DE AGENTES", W / 2 - 400, 470);

        int[] opciones = {2, 3, 4};
        for (int i = 0; i < opciones.length; i++) {
            int bx = W / 2 - 260 + i * 240;
            int by = 540;
            gc.setFill(Color.web("#001500"));
            gc.fillRect(bx, by, 180, 110);
            gc.setStroke(Color.web(ACENTO));
            gc.setLineWidth(1.5);
            gc.strokeRect(bx, by, 180, 110);
            gc.setFill(Color.web(ACENTO));
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 90));
            gc.fillText(String.valueOf(opciones[i]), bx + 54, by + 82);
            
          
        }

      
    }

    private void dibujarFase1() {
        // ── PANEL IZQUIERDO ──
        gc.setFill(Color.web("#060606"));
        gc.fillRect(0, 0, PANEL_W, H);
        gc.setStroke(Color.web("#1a1a1a"));
        gc.setLineWidth(1);
        gc.strokeLine(PANEL_W, 0, PANEL_W, H);

        // Encabezado panel
        gc.setFill(Color.web(ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 25));
        gc.fillText("// AGENTE " + (jugadorActual + 1) + " DE " + numJugadores, 20, 30);

        gc.setFill(Color.web("#333333"));
        gc.setFont(Font.font(FUENTE, 25));
        gc.fillText("SELECCIONA IDENTIDAD", 20, 70);

        // Lista de títulos
        for (int i = 0; i < titulosDisponibles.size(); i++) {
            String titulo   = titulosDisponibles.get(i);
            int    idx      = Arrays.asList(TODOS_TITULOS).indexOf(titulo);
            Color  c        = TODOS_COLORES[idx];
            boolean sel     = (i == tituloSel);

            int bx = 14, by = 80 + i * 68;
            gc.setFill(Color.web(sel ? "#002200" : "#0d0d0d"));
            gc.fillRect(bx, by, PANEL_W - 28, 80);
            gc.setStroke(sel ? Color.web(ACENTO) : c.deriveColor(0, 1, 0.4, 1));
            gc.setLineWidth(sel ? 2 : 1);
            gc.strokeRect(bx, by, PANEL_W - 28, 80);

            // Color dot
            gc.setFill(c);
            gc.fillOval(bx + 12, by + 20, 20, 20);

            gc.setFill(sel ? Color.web(ACENTO) : Color.WHITE);
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 25));
            gc.fillText(titulo, bx + 42, by + 26);
            gc.setFill(Color.web("#444444"));
            gc.setFont(Font.font(FUENTE, 25));

            if (sel) {
                gc.setFill(Color.web(ACENTO));
                gc.setFont(Font.font(FUENTE, 9));
                gc.fillText("SELECCIONADO", bx + 42, by + 55);
            }
        }

        // Separador
        double sepY = 80 + titulosDisponibles.size() * 68 + 10;
        gc.setStroke(Color.web("#1a1a1a"));
        gc.strokeLine(14, sepY, PANEL_W - 14, sepY);

        // Perfil del agente seleccionado
        if (tituloSel >= 0) {
            String titulo = titulosDisponibles.get(tituloSel);
            int    idx    = Arrays.asList(TODOS_TITULOS).indexOf(titulo);
            Color  c      = TODOS_COLORES[idx];
            dibujarPerfilAgente(idx, c, titulo, sepY + 32);
        } else {
            gc.setFill(Color.web("#222222"));
            gc.setFont(Font.font(FUENTE, 25));
            gc.fillText("Selecciona un agente", 20, (int) sepY + 60);
        }

        // Capital seleccionada
        double capY = H - 130;
        gc.setFill(Color.web("#0a0a0a"));
        gc.fillRect(14, (int) capY, PANEL_W - 28, 50);
        gc.setStroke(Color.web(capitalSel >= 0 ? ACENTO : "#222222"));
        gc.setLineWidth(1);
        gc.strokeRect(14, (int) capY, PANEL_W - 28, 50);
        gc.setFill(Color.web(capitalSel >= 0 ? ACENTO : "#333333"));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 25));
        gc.fillText("CAPITAL:", 24, (int) capY + 18);
        gc.setFill(Color.web(capitalSel >= 0 ? Color.WHITE.toString() : "#333333"));
        gc.setFont(Font.font(FUENTE, 25));
        gc.fillText(capitalSel >= 0 ? TODOS_TERRITORIOS[capitalSel] : " Haz click en el mapa ",
                    24, (int) capY + 38);

        // Botón confirmar
        boolean puede = tituloSel >= 0 && capitalSel >= 0;
        gc.setFill(Color.web(puede ? "#003300" : "#0d0d0d"));
        gc.fillRect(14, H - 70, PANEL_W - 28, 54);
        gc.setStroke(Color.web(puede ? ACENTO : "#222222"));
        gc.setLineWidth(puede ? 2 : 1);
        gc.strokeRect(14, H - 70, PANEL_W - 28, 54);
        gc.setFill(Color.web(puede ? ACENTO : "#333333"));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 25));
        gc.fillText("[ CONFIRMAR AGENTE ]", 60, H - 36);

        // ── MAPA DERECHO ──
        dibujarMapaFase1();
    }

    private void dibujarPerfilAgente(int idx, Color c, String titulo, double startY) {
        int px = 14;
        int pw = PANEL_W - 28;

        // Avatar geométrico
        int avSize = 80;
        int avX    = px + pw / 2 - avSize / 2;
        int avY    = (int) startY + 16;

        Image img = imagenAgente(idx);
        if (img != null && !img.isError()) {
            // Clip circular — dibuja fondo y recorte
            gc.save();
            gc.beginPath();
            gc.arc(avX + avSize / 2.0, avY + avSize / 2.0, avSize / 2.0, avSize / 2.0, 0, 360);
            gc.closePath();
            gc.clip();
            gc.drawImage(img, avX, avY, avSize, avSize);
            gc.restore();
        } else {
            // Fallback geométrico
            gc.setFill(Color.web("#111111"));
            gc.fillRoundRect(avX, avY, avSize, avSize, 10, 10);
            gc.setFill(c.deriveColor(0, 1, 0.3, 1));
            gc.fillOval(avX + 14, avY + 14, 52, 52);
        }
        // Borde siempre
        gc.setStroke(c);
        gc.setLineWidth(2);
        gc.strokeOval(avX, avY, avSize, avSize);
        gc.setStroke(c);
        gc.setLineWidth(2);
        gc.strokeRoundRect(avX, avY, avSize, avSize, 10, 10);

        // Inicial
        gc.setFill(c);
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 25));
        gc.fillText(titulo.substring(titulo.lastIndexOf(' ') + 1, titulo.lastIndexOf(' ') + 2),
                    avX + avSize / 2 - 6, avY + avSize - 8);

        // Nombre
        double ty = startY + avSize + 22;
        gc.setFill(c);
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 25));
        gc.fillText(titulo.toUpperCase(), px, ty); ty += 18;

        // Descripción
        String desc = DESCRIPCIONES[idx];
        gc.setFill(Color.web("#888888"));
        gc.setFont(Font.font(FUENTE, 28));
        for (String linea : desc.split("\n")) {
            gc.fillText(linea, px, ty); ty += 24;
        }
        ty += 8;

        // Bonus
        gc.setFill(Color.web("#111111"));
        gc.fillRect(px, (int) ty, pw, 36);
        gc.setStroke(Color.web(ACENTO));
        gc.setLineWidth(1);
        gc.strokeRect(px, (int) ty, pw, 36);
        gc.setFill(Color.web(ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 25));
        gc.fillText("BONUS", px + 8, (int) ty + 32);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(FUENTE, 25));
        gc.fillText(BONUS_TEXTO[idx], px + 8, (int) ty + 70);
    }

    private void dibujarMapaFase1() {
        // Fondo mapa
        gc.setFill(Color.web("#040404"));
        gc.fillRect(MAPA_X, 0, MAPA_W, H);

        // Título
        gc.setFill(Color.web("#222222"));
        gc.setFont(Font.font(FUENTE, 30));
        gc.fillText("// MAPA MUNDIAL — HAZ CLICK PARA ELEGIR TU CAPITAL", MAPA_X + 20, 24);

        // Conexiones
        gc.setStroke(Color.web("#161616"));
        gc.setLineWidth(1);
        for (int[] adj : ADY) {
            gc.strokeLine(POS[adj[0]][0], POS[adj[0]][1],
                          POS[adj[1]][0], POS[adj[1]][1]);
        }

        // Territorios
        for (int i = 0; i < TODOS_TERRITORIOS.length; i++) {
            double x      = POS[i][0];
            double y      = POS[i][1];
            boolean usada = capitalesUsadas.contains(TODOS_TERRITORIOS[i]);
            boolean sel   = (i == capitalSel);

            // Halo selección
            if (sel) {
                gc.setFill(Color.web(ACENTO, 0.15));
                gc.fillOval(x - 26, y - 26, 52, 52);
                gc.setStroke(Color.web(ACENTO));
                gc.setLineWidth(2);
                gc.strokeOval(x - 26, y - 26, 52, 52);
            }

            // Círculo
            gc.setFill(Color.web(usada ? "#2a0000" : sel ? "#002800" : "#141414"));
            gc.fillOval(x - 14, y - 14, 28, 28);
            gc.setStroke(Color.web(usada ? "#440000" : sel ? ACENTO : "#2a2a2a"));
            gc.setLineWidth(sel ? 2 : 1);
            gc.strokeOval(x - 14, y - 14, 28, 28);

            // Nombre
            String nombre = TODOS_TERRITORIOS[i];
            String corto  = nombre.length() > 16 ? nombre.substring(0, 15) + "." : nombre;
            gc.setFill(Color.web(usada ? "#3a1a1a" : sel ? ACENTO : "#666666"));
            gc.setFont(Font.font(FUENTE, sel ? FontWeight.BOLD : FontWeight.NORMAL, 25));
            gc.fillText(corto, x - 22, y + 24);

            if (usada) {
                gc.setFill(Color.web("#550000"));
                gc.setFont(Font.font(FUENTE, 20));
                gc.fillText("[OCUPADA]", x - 18, y + 34);
            }
        }

        // Etiquetas continentes
        gc.setFill(Color.web("#1a1a1a"));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 25));
        gc.fillText("NORTEAMÉRICA",  530,  140);
        gc.fillText("SUDAMÉRICA",    620,  590);
        gc.fillText("EUROPA",        980,  140);
        gc.fillText("ÁFRICA",        930,  560);
        gc.fillText("MEDIO ORIENTE", 1150, 500);
        gc.fillText("RUSIA",         1290, 140);
        gc.fillText("ASIA",          1540, 260);
        gc.fillText("OCEANÍA",       1580, 700);
    }

    private void dibujarFase2() {
        gc.setFill(Color.web(ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 46));
        gc.fillText("// AGENTES CONFIRMADOS", W / 2 - 400, 110);

        for (int i = 0; i < configs.size(); i++) {
            ConfigJugador c  = configs.get(i);
            int idx = Arrays.asList(TODOS_TITULOS).indexOf(c.titulo);
            int bx = W / 2 - 560, by = 160 + i * 150;

            gc.setFill(Color.web("#0a0a0a"));
            gc.fillRect(bx, by, 1120, 130);
            gc.setStroke(c.color);
            gc.setLineWidth(2);
            gc.strokeRect(bx, by, 1120, 130);

            gc.setFill(c.color.deriveColor(0, 1, 0.3, 1));
            gc.fillOval(bx + 20, by + 38, 54, 54);
            gc.setStroke(c.color);
            gc.setLineWidth(1);
            gc.strokeOval(bx + 20, by + 38, 54, 54);

            gc.setFill(c.color);
            gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 26));
            gc.fillText(c.titulo.toUpperCase(), bx + 90, by + 46);

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(FUENTE, 25));
            gc.fillText("CAPITAL : " + c.nombreCapital, bx + 90, by + 72);

            gc.setFill(Color.web("#444444"));
            gc.setFont(Font.font(FUENTE, 25));
            gc.fillText("BONUS   : " + (idx >= 0 ? BONUS_TEXTO[idx] : "—"), bx + 90, by + 94);
            gc.fillText("+$200 ingreso/turno  //  5 tropas iniciales", bx + 90, by + 114);
        }

        // Botón iniciar
        int bx = W / 2 - 200, by = H - 180;
        gc.setFill(Color.web("#003300"));
        gc.fillRect(bx, by, 400, 100);
        gc.setStroke(Color.web(ACENTO));
        gc.setLineWidth(2);
        gc.strokeRect(bx, by, 400, 100);
        gc.setFill(Color.web(ACENTO));
        gc.setFont(Font.font(FUENTE, FontWeight.BOLD, 38));
        gc.fillText("[ INICIAR ]", bx + 78, by + 66);
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
            int bx = W / 2 - 260 + i * 240, by = 440;
            if (x >= bx && x <= bx + 180 && y >= by && y <= by + 110) {
                numJugadores = opciones[i];
                fase = 1;
                dibujar();
                return;
            }
        }
    }

    private void clickFase1(double x, double y) {
        // Click en botón confirmar
        if (tituloSel >= 0 && capitalSel >= 0 &&
            x >= 14 && x <= PANEL_W - 14 && y >= H - 70 && y <= H - 16) {
            confirmarJugador(); return;
        }

        // Click en panel izquierdo — títulos
        if (x < PANEL_W) {
            for (int i = 0; i < titulosDisponibles.size(); i++) {
                int by = 64 + i * 68;
                if (y >= by && y <= by + 60) {
                    tituloSel = (tituloSel == i) ? -1 : i;
                    dibujar(); return;
                }
            }
            return;
        }

        // Click en mapa — territorios
        for (int i = 0; i < TODOS_TERRITORIOS.length; i++) {
            if (capitalesUsadas.contains(TODOS_TERRITORIOS[i])) continue;
            double dx = x - POS[i][0];
            double dy = y - POS[i][1];
            if (Math.sqrt(dx * dx + dy * dy) <= 16) {
                capitalSel = (capitalSel == i) ? -1 : i;
                dibujar(); return;
            }
        }
    }

    private void clickFase2(double x, double y) {
        int bx = W / 2 - 200, by = H - 180;
        if (x >= bx && x <= bx + 400 && y >= by && y <= by + 100) {
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

    
}