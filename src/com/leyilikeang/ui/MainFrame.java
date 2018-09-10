package com.leyilikeang.ui;

import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.service.CaptureService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author likang
 * @date 2018/9/7 20:30
 */
public class MainFrame {

    private JPanel contentPane;
    private JTextArea toHexdumpTextArea;
    private JTextArea toStringTextArea;
    private JTable packetTable;
    private JTextField sourceIpTextField;
    private JTextField sourcePortTextField;
    private JTextField destinationIpTextField;
    private JTextField destinationPortTextField;
    private JButton captureButton;
    private JButton stopButton;
    private JScrollPane packetTableScrollPane;
    private JButton fileButton;
    private JButton lookOverButton;
    private JButton toolButton;

    private CaptureService captureService = new CaptureService();

    private DefaultTableModel defaultTableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public MainFrame() {
        stopButton.setEnabled(false);
        defaultTableModel.setColumnIdentifiers(new Object[]{"序号", "源地址", "源端口", "目的地址", "目的端口", "协议", "长度"});
        packetTable.setModel(defaultTableModel);
        packetTable.getTableHeader().setReorderingAllowed(false);
        packetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        packetTableScrollPane.setViewportView(packetTable);
        packetTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Integer index = Integer.parseInt(packetTable.getValueAt(packetTable.getSelectedRow(), 0).toString());
                final String hexdump = PacketUtils.allMap.get(index).toHexdump();
                final String toString = PacketUtils.allMap.get(index).toString();
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        toHexdumpTextArea.setText(hexdump);
                        toStringTextArea.setText(toString);
                    }
                });
            }
        });

        captureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        captureButton.setEnabled(false);
                        stopButton.setEnabled(true);
                    }
                });
                captureService.capture(defaultTableModel);
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        captureButton.setEnabled(true);
                        stopButton.setEnabled(false);
                    }
                });
                captureService.stop();
            }
        });

        // 设置快捷键为 Alt + F
        fileButton.setMnemonic('F');
        // 按下Alt键时第四个字符带有下划线
        fileButton.setDisplayedMnemonicIndex(3);
        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                popupMenu.show(fileButton, 0, fileButton.getHeight());
            }
        });

        lookOverButton.setMnemonic('L');
        lookOverButton.setDisplayedMnemonicIndex(3);
        lookOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                statisticsMenuItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
                statisticsMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("统计");
                    }
                });
                popupMenu.add(statisticsMenuItem);

                popupMenu.show(lookOverButton, 0, lookOverButton.getHeight());
            }
        });

        toolButton.setMnemonic('T');
        toolButton.setDisplayedMnemonicIndex(3);
        toolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popupMenu = new JPopupMenu();

                JMenuItem scanMenuItem = new JMenuItem("扫描");
                scanMenuItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
                scanMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("扫描");
                    }
                });
                popupMenu.add(scanMenuItem);

                JMenuItem arpMenuItem = new JMenuItem("ARP欺骗");
                arpMenuItem.setAccelerator(KeyStroke.getKeyStroke("control A"));
                arpMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("ARP欺骗");
                    }
                });
                popupMenu.add(arpMenuItem);

                popupMenu.show(toolButton, 0, toolButton.getHeight());
            }
        });

    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
