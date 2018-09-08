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

    private CaptureService captureService = new CaptureService();

    private DefaultTableModel defaultTableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public MainFrame() {
        stopButton.setEnabled(false);
        defaultTableModel.setColumnIdentifiers(new Object[]{"序号", "源地址", "目的地址", "源端口", "目的端口", "协议", "长度"});
        packetTable.setModel(defaultTableModel);
        packetTable.getTableHeader().setReorderingAllowed(false);
        packetTable.getTableHeader().setFont(new Font("宋体", 0, 16));
        packetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        packetTableScrollPane.setViewportView(packetTable);
        packetTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = packetTable.getSelectedRow();
                final String hexdump = PacketUtils.map.get(index).toHexdump();
                final String toString = PacketUtils.map.get(index).toString();
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
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
