package com.leyilikeang.common.util;

import com.leyilikeang.ui.DevsFrame;
import com.sun.org.apache.bcel.internal.generic.POP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author likang
 * @date 2018/9/11 22:43
 */
public class MenuUtils {

    private MenuUtils() {

    }

    public static JPopupMenu getFileMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem openMenuItem = new JMenuItem("打开");
        // 设置快捷键为 Ctrl + O
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("打开");
            }
        });
        popupMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("另存为");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("另存为");
            }
        });
        popupMenu.add(saveMenuItem);

        JMenuItem selectSaveMenuItem = new JMenuItem("选择另存为");
        selectSaveMenuItem.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
        selectSaveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("选择另存为");
            }
        });
        popupMenu.add(selectSaveMenuItem);

        JMenuItem exitMenuItem = new JMenuItem("退出");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke("control Q"));
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        popupMenu.add(exitMenuItem);
        return popupMenu;
    }

    public static JPopupMenu getLookOverMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem jumpToMenuItem = new JMenuItem("跳转");
        jumpToMenuItem.setAccelerator(KeyStroke.getKeyStroke("control J"));
        jumpToMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("跳转");
            }
        });
        popupMenu.add(jumpToMenuItem);

        JMenuItem statisticsMenuItem = new JMenuItem("统计");
        statisticsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("统计");
            }
        });
        popupMenu.add(statisticsMenuItem);
        return popupMenu;
    }

    // TODO ： 暂时分为切换网卡和捕获规则两种，传参问题待考虑
    public static JPopupMenu getCapMenu(final Window frame) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem ruleMenuItem = new JMenuItem("规则");
        ruleMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog((JFrame) frame, true);
                dialog.setContentPane(new DevsFrame(dialog).getContentPane());
                dialog.pack();
                dialog.setLocationRelativeTo(frame);
                dialog.setVisible(true);
            }
        });
        popupMenu.add(ruleMenuItem);
        return popupMenu;
    }

    public static JPopupMenu getToolsMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem scanMenuItem = new JMenuItem("扫描");
        scanMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("扫描");
            }
        });
        popupMenu.add(scanMenuItem);

        JMenuItem arpMenuItem = new JMenuItem("ARP欺骗");
        arpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ARP欺骗");
            }
        });
        popupMenu.add(arpMenuItem);
        return popupMenu;
    }
}
