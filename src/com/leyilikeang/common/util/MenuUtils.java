package com.leyilikeang.common.util;

import com.leyilikeang.ui.DevsFrame;

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

    /**
     * TODO : 文件菜单分为打开选项，另存为选项，选择另存为选项和退出选项
     * TODO : 打开选项用于打开后缀名为.pcap的数据包文件，并且读取数据包中的数据显示在列表中
     * TODO : 另存为选项用于保存列表中所有的数据包信息到后缀名为.pcap的数据包文件中，可用Wireshark打开查看更多信息
     * TODO : 选择另存为选项用于选择指定的数据包进行另存为操作
     * TODO : 退出选项点击后退出程序，后续添加退出时询问是否保存对话框
     * @return 文件菜单
     */
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

    /**
     * TODO : 查看菜单暂时分为跳转功能和统计功能
     * TODO : 跳转功能实现在列表上跳转到指定序号的包
     * TODO : 统计功能实现各数据包数量的统计
     *
     * @return 查看菜单
     */
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

    /**
     * TODO : 捕获菜单暂时分为切换网卡和捕获规则两种，传参问题待考虑
     *
     * @param frame 切换网卡时，弹出的网卡设备选择对话框将居中与此frame
     * @return 捕获菜单
     */
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

    /**
     * TODO : 工具菜单暂时分为扫描功能和ARP欺骗功能
     * TODO : 扫描功能实现获取同网段内所有在线主机IP地址和对应的MAC地址，方便用于ARP欺骗
     * TODO : ARP欺骗实现单向欺骗和双向欺骗
     *
     * @return 工具菜单
     */
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
