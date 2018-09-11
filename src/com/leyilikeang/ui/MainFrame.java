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

        toolButton.setMnemonic('T');
        toolButton.setDisplayedMnemonicIndex(3);
        toolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu toolsMenu = MenuUtils.getToolsMenu();
                toolsMenu.show(toolButton, 0, toolButton.getHeight());
            }
        });

    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
