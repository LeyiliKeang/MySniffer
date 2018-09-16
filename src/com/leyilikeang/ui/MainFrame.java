package com.leyilikeang.ui;

import com.leyilikeang.common.util.MenuUtils;
import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.service.CaptureService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author likang
 * @date 2018/9/7 20:30
 */
public class MainFrame {

    private JPanel contentPane;
    private JTextArea toHexDumpTextArea;
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
    private JButton capButton;

    private JFrame mainFrame;

    private CaptureService captureService = new CaptureService();

    private DefaultTableModel defaultTableModel;

    public MainFrame(JFrame mainFrame) {
        this();
        this.mainFrame = mainFrame;
    }

    public MainFrame() {
        stopButton.setEnabled(false);
        defaultTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        defaultTableModel.setColumnIdentifiers(new Object[]{"序号", "源地址", "源端口", "目的地址", "目的端口", "协议", "长度"});
        packetTable.setModel(defaultTableModel);
        packetTable.getTableHeader().setReorderingAllowed(false);
        packetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        packetTableScrollPane.setViewportView(packetTable);
        packetTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (packetTable.getSelectedRow() != -1) {
                    Integer index = Integer.parseInt(packetTable.getValueAt(packetTable.getSelectedRow(), 0).toString());
                    final String hexDump = PacketUtils.allMap.get(index).toHexdump();
                    final String toString = PacketUtils.allMap.get(index).toString();
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            toHexDumpTextArea.setText(hexDump);
                            toStringTextArea.setText(toString);
                        }
                    });
                } else {
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            toHexDumpTextArea.setText("");
                            toStringTextArea.setText("");
                        }
                    });
                }
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

                PacketUtils.sourceIpAddress = sourceIpTextField.getText().trim().equals("")
                        ? null : sourceIpTextField.getText().trim();
                PacketUtils.destinationIpAddress = destinationIpTextField.getText().trim().equals("")
                        ? null : destinationIpTextField.getText().trim();
                PacketUtils.sourcePort = sourcePortTextField.getText().trim().equals("") ? null : Integer.parseInt(sourcePortTextField.getText().trim());
                PacketUtils.destinationPort = destinationPortTextField.getText().trim().equals("") ? null : Integer.parseInt(destinationPortTextField.getText().trim());
                PacketUtils.clear();
                defaultTableModel.setRowCount(0);
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
                JPopupMenu fileMenu = MenuUtils.getFileMenu();
                fileMenu.show(fileButton, 0, fileButton.getHeight());
            }
        });

        lookOverButton.setMnemonic('L');
        lookOverButton.setDisplayedMnemonicIndex(3);
        lookOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu lookOverMenu = MenuUtils.getLookOverMenu();
                lookOverMenu.show(lookOverButton, 0, lookOverButton.getHeight());
            }
        });

        capButton.setMnemonic('C');
        capButton.setDisplayedMnemonicIndex(3);
        capButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu capMenu = MenuUtils.getCapMenu(mainFrame);
                capMenu.show(capButton, 0, capButton.getHeight());
            }
        });

        toolButton.setMnemonic('T');
        toolButton.setDisplayedMnemonicIndex(3);
        toolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu toolsMenu = MenuUtils.getToolsMenu();
                toolsMenu.show(toolButton, 0, toolButton.getHeight());
            }
        });

        fileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JPopupMenu fileMenu = MenuUtils.getFileMenu();
                fileMenu.show(fileButton, 0, fileButton.getHeight());
            }
        });
        lookOverButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JPopupMenu lookOverMenu = MenuUtils.getLookOverMenu();
                lookOverMenu.show(lookOverButton, 0, lookOverButton.getHeight());
            }
        });
        capButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JPopupMenu capMenu = MenuUtils.getCapMenu(mainFrame);
                capMenu.show(capButton, 0, capButton.getHeight());
            }
        });
        toolButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JPopupMenu toolsMenu = MenuUtils.getToolsMenu();
                toolsMenu.show(toolButton, 0, toolButton.getHeight());
            }
        });
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
