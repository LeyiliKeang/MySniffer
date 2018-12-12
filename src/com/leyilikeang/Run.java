package com.leyilikeang;

import com.leyilikeang.ui.DevsFrame;
import com.leyilikeang.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

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
//                JFrame devsFrame = new JFrame("选择网卡设备");
//                devsFrame.setContentPane(new DevsFrame(devsFrame).getContentPane());
//                devsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                devsFrame.pack();
//                devsFrame.setLocationRelativeTo(null);
//                devsFrame.setVisible(true);

                JFrame mainFrame = new JFrame("MySniffer");
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
