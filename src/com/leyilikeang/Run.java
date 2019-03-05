package com.leyilikeang;

import com.leyilikeang.ui.DevsFrame;
import com.leyilikeang.ui.MainFrame;
import sun.awt.image.ToolkitImage;

import javax.swing.*;
import java.awt.*;

/**
 * @author likang
 * @date 2018/9/7 21:55
 */
public class Run {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.print("最大内存：");
                    System.out.println(Runtime.getRuntime().maxMemory() / Math.pow(2, 20));
                    System.out.print("可用内存");
                    System.out.println(Runtime.getRuntime().freeMemory() / Math.pow(2, 20));
                    System.out.print("现在的内存");
                    System.out.println(Runtime.getRuntime().totalMemory() / Math.pow(2, 20));
                    System.out.println();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
