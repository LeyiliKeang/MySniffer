package com.leyilikeang;

import com.leyilikeang.common.util.PcapUtils;
import com.leyilikeang.ui.MainFrame;
import net.sf.image4j.codec.ico.ICODecoder;
import sun.awt.image.ToolkitImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

//                List<BufferedImage> images = null;
//                try {
//                    images = ICODecoder.read(new File("imgs/icon.ico"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (images != null) {
//                    mainFrame.setIconImage(images.get(0));
//                }

                mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("imgs/icon.png"));
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
