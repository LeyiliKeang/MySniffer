package com.leyilikeang.common.util;

import com.leyilikeang.service.CaptureService;
import com.leyilikeang.service.FileService;
import com.leyilikeang.ui.*;
import com.sun.corba.se.impl.protocol.JIDLLocalCRDImpl;
import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
     *
     * @return 文件菜单
     */
    public static JPopupMenu getFileMenu(final MainFrame mainFrame) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem openMenuItem = new JMenuItem("打开");
        if (CaptureService.isStart) {
            openMenuItem.setEnabled(false);
        }
        // 设置快捷键为 Ctrl + O
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("打开");
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JFileChooser fileChooser = new JFileChooser();
//                        fileChooser.setCurrentDirectory(new File("."));
//                        fileChooser.setSelectedFile(new File("packet.cap"));
                        fileChooser.setMultiSelectionEnabled(false);
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fileChooser.setFileHidingEnabled(true);
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        fileChooser.setFileFilter(new MyFileFilter("cap", "MySniffer(*.cap)"));
                        fileChooser.setFileFilter(new MyFileFilter("pcap", "MySniffer(*.pcap)"));
                        int result = fileChooser.showOpenDialog(mainFrame.getContentPane());
                        if (JFileChooser.APPROVE_OPTION == result) {
                            System.out.println(fileChooser.getSelectedFile().getPath());
                            FileUtils.openFile = fileChooser.getSelectedFile().getPath();
                            try {
                                new FileUtils().writeRecent(FileUtils.openFile, false);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            FileService fileService = new FileService();
                            fileService.open(mainFrame, "");
                        }
                    }
                });
            }
        });
        popupMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("另存为");
        if (CaptureService.isStart || MainFrame.isDevs) {
            saveMenuItem.setEnabled(false);
        }
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JFileChooser fileChooser = new JFileChooser();
//                        fileChooser.setCurrentDirectory(new File("."));
                        fileChooser.setSelectedFile(new File("packet.cap"));
                        fileChooser.setMultiSelectionEnabled(false);
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fileChooser.setFileHidingEnabled(true);
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        fileChooser.setFileFilter(new MyFileFilter("cap", "MySniffer(*.cap)"));
                        int result = fileChooser.showSaveDialog(mainFrame.getContentPane());
                        if (JFileChooser.APPROVE_OPTION == result) {
                            String path = fileChooser.getSelectedFile().getPath();
                            try {
                                new FileUtils().writeRecent(path, false);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            FileService fileService = new FileService();
                            fileService.save(path, true);
                        }
                    }
                });
            }
        });
        popupMenu.add(saveMenuItem);

        JMenuItem selectSaveMenuItem = new JMenuItem("选择另存为");
        if (CaptureService.isStart || MainFrame.isDevs) {
            selectSaveMenuItem.setEnabled(false);
        }
        selectSaveMenuItem.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
        selectSaveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("选择另存为");
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JDialog dialog = new JDialog(mainFrame.getMainFrame(), "选择另存为", true);
                        dialog.setContentPane(new PacketFrame(mainFrame, dialog).getContentPane());
                        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        Rectangle rectangle = mainFrame.getContentPane().getBounds();
                        dialog.setBounds(rectangle);
                        dialog.setLocationRelativeTo(mainFrame.getContentPane());
                        dialog.setVisible(true);
                    }
                });
            }
        });
        popupMenu.add(selectSaveMenuItem);

        JMenuItem closeMenuItem = new JMenuItem("关闭");
        if (CaptureService.isStart || mainFrame.isDevs) {
            closeMenuItem.setEnabled(false);
        }
        closeMenuItem.setAccelerator(KeyStroke.getKeyStroke("control W"));
        closeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PacketUtils.capClear();
                mainFrame.getDefaultTableModel().setRowCount(0);
                mainFrame.devs();
            }
        });
        popupMenu.add(closeMenuItem);

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
    public static JPopupMenu getLookOverMenu(final MainFrame mainFrame) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem jumpToMenuItem = new JMenuItem("跳转");
        if (CaptureService.isStart || MainFrame.isDevs || mainFrame.getJumpToDialog().isVisible()) {
            jumpToMenuItem.setEnabled(false);
        }
        jumpToMenuItem.setAccelerator(KeyStroke.getKeyStroke("control J"));
        jumpToMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jumpToMenuItem.setEnabled(false);
                JumpToFrame.isOpen = true;
                JDialog dialog = mainFrame.getJumpToDialog();
                dialog.setResizable(false);
                dialog.pack();
                Rectangle rectangle = mainFrame.getMainFrame().getBounds();
                dialog.setBounds(rectangle.x + rectangle.width - dialog.getWidth() - 13,
                        rectangle.y + rectangle.height - dialog.getHeight() - 279,
                        dialog.getWidth(), dialog.getHeight());
                dialog.setVisible(true);
            }
        });
        popupMenu.add(jumpToMenuItem);

        final JMenuItem statisticsMenuItem = new JMenuItem("统计");
        if (MainFrame.isDevs || mainFrame.getStatisticsDialog().isVisible()) {
            statisticsMenuItem.setEnabled(false);
        }
        statisticsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        statisticsMenuItem.setEnabled(false);
                        StatisticsFrame.isOpen = true;
                        JDialog dialog = mainFrame.getStatisticsDialog();
                        dialog.setResizable(false);
                        dialog.pack();
                        Rectangle rectangle = mainFrame.getMainFrame().getBounds();
                        dialog.setBounds(rectangle.x + rectangle.width - dialog.getWidth() - 13,
                                rectangle.y + rectangle.height - dialog.getHeight() - 40,
                                dialog.getWidth(), dialog.getHeight());
                        dialog.setVisible(true);
                    }
                });
            }
        });
        popupMenu.add(statisticsMenuItem);
        return popupMenu;
    }

    /**
     * TODO : 捕获菜单暂时分为切换网卡和捕获规则两种，传参问题待考虑
     * TODO : 弹出的网卡设备选择对话框未居中于frame
     *
     * @param mainFrame 切换网卡时，弹出的网卡设备选择对话框将居中于此frame
     * @return 捕获菜单
     */
    public static JPopupMenu getCapMenu(final MainFrame mainFrame) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem ruleMenuItem = new JMenuItem("切换网卡");
        if (CaptureService.isStart || MainFrame.isDevs) {
            ruleMenuItem.setEnabled(false);
        }
        ruleMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        PacketUtils.capClear();
                        mainFrame.getDefaultTableModel().setRowCount(0);
                        mainFrame.devs();
                    }
                });
            }
        });
        popupMenu.add(ruleMenuItem);

        JMenuItem capture = new JMenuItem("捕获");
        capture.setEnabled(false);
        if (PcapUtils.index != null && MainFrame.isReady) {
            capture.setEnabled(true);
        }
        if (CaptureService.isStart) {
            capture.setEnabled(false);
        }
        capture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getCaptureButton().doClick();
            }
        });
        popupMenu.add(capture);

        JMenuItem stopCapture = new JMenuItem("停止");
        stopCapture.setEnabled(false);
        if (CaptureService.isStart && !CaptureService.isOpen) {
            stopCapture.setEnabled(true);
        }
        stopCapture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getStopButton().doClick();
            }
        });
        popupMenu.add(stopCapture);

        return popupMenu;
    }

    /**
     * TODO : 工具菜单暂时分为扫描功能和ARP欺骗功能
     * TODO : 扫描功能实现获取同网段内所有在线主机IP地址和对应的MAC地址，方便用于ARP欺骗
     * TODO : ARP欺骗实现单向欺骗和双向欺骗
     *
     * @return 工具菜单
     */
    public static JPopupMenu getToolsMenu(final MainFrame mainFrame) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem scanMenuItem = new JMenuItem("扫描");
        scanMenuItem.setEnabled(false);
        if (PcapUtils.index != null && MainFrame.isReady) {
            scanMenuItem.setEnabled(true);
        }
        scanMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("扫描");
            }
        });
        popupMenu.add(scanMenuItem);

        JMenuItem arpMenuItem = new JMenuItem("ARP欺骗");
        arpMenuItem.setEnabled(false);
        if (PcapUtils.index != null && MainFrame.isReady && !mainFrame.getArpFraudDialog().isVisible()) {
            arpMenuItem.setEnabled(true);

        }
        arpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                arpMenuItem.setEnabled(false);
                ArpFraudFrame.isOpen = true;
                Dialog dialog = mainFrame.getArpFraudDialog();
                dialog.setResizable(false);
                dialog.pack();
                mainFrame.getArpFraudFrame().load();
                Rectangle rectangle = mainFrame.getMainFrame().getBounds();
                dialog.setBounds(rectangle.x + 13, rectangle.y + rectangle.height - dialog.getHeight() - 40,
                        dialog.getWidth(), dialog.getHeight());
                dialog.setVisible(true);
            }
        });
        popupMenu.add(arpMenuItem);
        return popupMenu;
    }
}
