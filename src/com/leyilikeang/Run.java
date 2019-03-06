package com.leyilikeang;

import com.leyilikeang.common.util.PcapUtils;
import com.leyilikeang.ui.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author likang
 * @date 2018/9/7 21:55
 */
public class Run {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Tree.paintLines", false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame mainFrame = new JFrame("MySniffer");
                mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("imgs/images.png"));
                MainFrame main = new MainFrame(mainFrame);
                main.getDevsFrame().setMainFrame(main);
                mainFrame.setContentPane(main.getContentPane());
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.pack();
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);
            }
        });
    }
}
