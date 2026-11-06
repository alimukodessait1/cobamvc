/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.cobamvc;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Cobamvc {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Tampilan modern mengikuti tema Windows
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new NewJFrame().setVisible(true);
        });
    }
}
