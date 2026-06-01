package pck;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.MatteBorder;

public class GuiCandy extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable table, table_1, table_2, table_3, table_4, table_5, table_6;

    // ── Estado ────────────────────────────────────────────────────
    private CashRegister cashRegister = new CashRegister();
    private Dispenser candy   = new Dispenser(2, 5);
    private Dispenser chips   = new Dispenser(10, 20);
    private Dispenser gum     = new Dispenser(3,  2);
    private Dispenser cookies = new Dispenser(20, 8);
    private int dineroInsertado = 0;

    // ── Labels que se actualizan ──────────────────────────────────
    private JLabel lblDisponible;
    private JLabel lblCandyPrc, lblChipsPrc, lblGumPrc, lblCookiesPrc;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new GuiCandy().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GuiCandy() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 406, 230);
        getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        table   = new JTable(); getContentPane().add(table);
        table_1 = new JTable(); getContentPane().add(table_1);
        table_4 = new JTable(); getContentPane().add(table_4);
        table_5 = new JTable(); getContentPane().add(table_5);

        // ── panel_1 ───────────────────────────────────────────────
        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
        getContentPane().add(panel_1);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[]{1, 89, 1, 1, 0};
        gbl_panel_1.rowHeights   = new int[]{0, 0, 0, 23, 0, 26, 33, 0};
        gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panel_1.rowWeights    = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        panel_1.setLayout(gbl_panel_1);

        JLabel lblBienvenido = new JLabel("Bienvenido a la Tienda de dulces.\r\n\r\n");
        GridBagConstraints gbc_lblBienvenido = new GridBagConstraints();
        gbc_lblBienvenido.insets = new Insets(0,0,5,5);
        gbc_lblBienvenido.gridx = 1; gbc_lblBienvenido.gridy = 0;
        panel_1.add(lblBienvenido, gbc_lblBienvenido);
        lblBienvenido.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblNewLabel = new JLabel("Seleccione el producto a comprar\r\n");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.insets = new Insets(0,0,5,5);
        gbc_lblNewLabel.gridx = 1; gbc_lblNewLabel.gridy = 1;
        panel_1.add(lblNewLabel, gbc_lblNewLabel);

        table_2 = new JTable();
        GridBagConstraints gbc_table_2 = new GridBagConstraints();
        gbc_table_2.anchor = GridBagConstraints.WEST;
        gbc_table_2.insets = new Insets(0,0,5,5);
        gbc_table_2.gridx = 0; gbc_table_2.gridy = 3;
        panel_1.add(table_2, gbc_table_2);

        table_6 = new JTable();
        GridBagConstraints gbc_table_6 = new GridBagConstraints();
        gbc_table_6.anchor = GridBagConstraints.WEST;
        gbc_table_6.insets = new Insets(0,0,5,5);
        gbc_table_6.gridx = 2; gbc_table_6.gridy = 3;
        panel_1.add(table_6, gbc_table_6);

        table_3 = new JTable();
        GridBagConstraints gbc_table_3 = new GridBagConstraints();
        gbc_table_3.insets = new Insets(0,0,5,0);
        gbc_table_3.anchor = GridBagConstraints.WEST;
        gbc_table_3.gridx = 3; gbc_table_3.gridy = 3;
        panel_1.add(table_3, gbc_table_3);

        JLabel lbldin = new JLabel("Dinero disponible");
        GridBagConstraints gbc_lbldin = new GridBagConstraints();
        gbc_lbldin.insets = new Insets(0,0,5,5);
        gbc_lbldin.gridx = 1; gbc_lbldin.gridy = 4;
        panel_1.add(lbldin, gbc_lbldin);

        // ── Label de dinero (instancia) ───────────────────────────
        lblDisponible = new JLabel("0");
        GridBagConstraints gbc_lblDisponible = new GridBagConstraints();
        gbc_lblDisponible.insets = new Insets(0,0,5,5);
        gbc_lblDisponible.gridx = 1; gbc_lblDisponible.gridy = 5;
        panel_1.add(lblDisponible, gbc_lblDisponible);

        JPanel panel_2 = new JPanel();
        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.insets = new Insets(0,0,0,5);
        gbc_panel_2.fill = GridBagConstraints.BOTH;
        gbc_panel_2.gridx = 1; gbc_panel_2.gridy = 6;
        panel_1.add(panel_2, gbc_panel_2);
        GridBagLayout gbl_panel_2 = new GridBagLayout();
        gbl_panel_2.columnWidths = new int[]{0, 0, 0, 0};
        gbl_panel_2.rowHeights   = new int[]{31, 0};
        gbl_panel_2.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panel_2.rowWeights    = new double[]{0.0, Double.MIN_VALUE};
        panel_2.setLayout(gbl_panel_2);

        JLabel lblNewLabel_1 = new JLabel("Inserte moneda:");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_1.insets = new Insets(0,0,0,5);
        gbc_lblNewLabel_1.gridx = 0; gbc_lblNewLabel_1.gridy = 0;
        panel_2.add(lblNewLabel_1, gbc_lblNewLabel_1);

        // ── Botón +1 ──────────────────────────────────────────────
        JButton btnadd1 = new JButton("+1");
        btnadd1.addActionListener(e -> {
            dineroInsertado += 1;
            lblDisponible.setText(String.valueOf(dineroInsertado));
        });
        GridBagConstraints gbc_btnadd1 = new GridBagConstraints();
        gbc_btnadd1.insets = new Insets(0,0,0,5);
        gbc_btnadd1.gridx = 1; gbc_btnadd1.gridy = 0;
        panel_2.add(btnadd1, gbc_btnadd1);

        // ── Botón +5 ──────────────────────────────────────────────
        JButton btnadd5 = new JButton("+5");
        btnadd5.addActionListener(e -> {
            dineroInsertado += 5;
            lblDisponible.setText(String.valueOf(dineroInsertado));
        });
        GridBagConstraints gbc_btnadd5 = new GridBagConstraints();
        gbc_btnadd5.gridx = 2; gbc_btnadd5.gridy = 0;
        panel_2.add(btnadd5, gbc_btnadd5);

        // ── panel de productos ────────────────────────────────────
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths  = new int[]{0, 0, 0};
        gbl_panel.rowHeights    = new int[]{0, 0, 0, 0, 0, 23, 0};
        gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panel.rowWeights    = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);

        JLabel lblNewLabel_3 = new JLabel("PRECIO");
        GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
        gbc_lblNewLabel_3.insets = new Insets(0,0,5,5);
        gbc_lblNewLabel_3.gridx = 0; gbc_lblNewLabel_3.gridy = 0;
        panel.add(lblNewLabel_3, gbc_lblNewLabel_3);

        // ── Candy ─────────────────────────────────────────────────
        lblCandyPrc = new JLabel(String.valueOf(candy.getProductprecio()));
        GridBagConstraints gbc_lblCandyPrc = new GridBagConstraints();
        gbc_lblCandyPrc.insets = new Insets(0,0,5,5);
        gbc_lblCandyPrc.gridx = 0; gbc_lblCandyPrc.gridy = 1;
        panel.add(lblCandyPrc, gbc_lblCandyPrc);

        JButton btnCandy = new JButton("Candy");
        btnCandy.addActionListener(e -> intentarCompra(candy));
        GridBagConstraints gbc_btnCandy = new GridBagConstraints();
        gbc_btnCandy.insets = new Insets(0,0,5,0);
        gbc_btnCandy.gridx = 1; gbc_btnCandy.gridy = 1;
        panel.add(btnCandy, gbc_btnCandy);

        // ── Chips ─────────────────────────────────────────────────
        lblChipsPrc = new JLabel(String.valueOf(chips.getProductprecio()));
        GridBagConstraints gbc_lblChipsPrc = new GridBagConstraints();
        gbc_lblChipsPrc.insets = new Insets(0,0,5,5);
        gbc_lblChipsPrc.gridx = 0; gbc_lblChipsPrc.gridy = 2;
        panel.add(lblChipsPrc, gbc_lblChipsPrc);

        JButton btnChips = new JButton("Chips");
        btnChips.addActionListener(e -> intentarCompra(chips));
        GridBagConstraints gbc_btnChips = new GridBagConstraints();
        gbc_btnChips.insets = new Insets(0,0,5,0);
        gbc_btnChips.gridx = 1; gbc_btnChips.gridy = 2;
        panel.add(btnChips, gbc_btnChips);

        // ── Gum ───────────────────────────────────────────────────
        lblGumPrc = new JLabel(String.valueOf(gum.getProductprecio()));
        GridBagConstraints gbc_lblGumPrc = new GridBagConstraints();
        gbc_lblGumPrc.insets = new Insets(0,0,5,5);
        gbc_lblGumPrc.gridx = 0; gbc_lblGumPrc.gridy = 3;
        panel.add(lblGumPrc, gbc_lblGumPrc);

        JButton btnGum = new JButton("Gum ");
        btnGum.addActionListener(e -> intentarCompra(gum));
        GridBagConstraints gbc_btnGum = new GridBagConstraints();
        gbc_btnGum.insets = new Insets(0,0,5,0);
        gbc_btnGum.gridx = 1; gbc_btnGum.gridy = 3;
        panel.add(btnGum, gbc_btnGum);

        // ── Cookies ───────────────────────────────────────────────
        lblCookiesPrc = new JLabel(String.valueOf(cookies.getProductprecio()));
        GridBagConstraints gbc_lblCookiesPrc = new GridBagConstraints();
        gbc_lblCookiesPrc.insets = new Insets(0,0,5,5);
        gbc_lblCookiesPrc.gridx = 0; gbc_lblCookiesPrc.gridy = 4;
        panel.add(lblCookiesPrc, gbc_lblCookiesPrc);

        JButton btnCookies = new JButton("Cookies");
        btnCookies.addActionListener(e -> intentarCompra(cookies));
        GridBagConstraints gbc_btnCookies = new GridBagConstraints();
        gbc_btnCookies.fill = GridBagConstraints.BOTH;
        gbc_btnCookies.insets = new Insets(0,0,5,0);
        gbc_btnCookies.gridx = 1; gbc_btnCookies.gridy = 4;
        panel.add(btnCookies, gbc_btnCookies);

        // ── Exit ──────────────────────────────────────────────────
        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(e -> System.exit(0));
        GridBagConstraints gbc_btnExit = new GridBagConstraints();
        gbc_btnExit.gridx = 1; gbc_btnExit.gridy = 5;
        panel.add(btnExit, gbc_btnExit);
    }

    // ── Lógica de compra ──────────────────────────────────────────
    private void intentarCompra(Dispenser producto) {
        if (producto.contarInv() <= 0) {
            JOptionPane.showMessageDialog(this, "Sin inventario.");
            if(producto==candy) {
            lblCandyPrc.setText("Sin stock");
            }
            return;
        }
        int precio = producto.getProductprecio();
        if (dineroInsertado < precio) {
            JOptionPane.showMessageDialog(this,
                "Dinero insuficiente. Necesitas " + (precio - dineroInsertado) + " más.");
            return;
        }
        dineroInsertado -= precio;
        cashRegister.recibirDinero(precio);
        producto.makeSale();
        lblDisponible.setText(String.valueOf(dineroInsertado));
        JOptionPane.showMessageDialog(this, "¡Producto dispensado! Cambio: " + dineroInsertado);
        //dineroInsertado = 0;
        lblDisponible.setText(String.valueOf(dineroInsertado));
    }
}