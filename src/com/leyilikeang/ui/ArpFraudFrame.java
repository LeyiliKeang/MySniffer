package com.leyilikeang.ui;

import com.leyilikeang.common.util.ConvertUtils;
import com.leyilikeang.common.util.PcapUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author likang
 * @date 2019/3/4 16:10
 */
public class ArpFraudFrame {
    private JPanel contentPane;
    private JTable arpTable;
    private JCheckBox localCheckBox;
    private JButton refreshButton;
    private JButton spoofButton;
    private JTextField ipTextField;
    private JTextField macTextField;
    private JScrollPane arpTableScrollPane;

    private DefaultTableModel defaultTableModel;

    public ArpFraudFrame() {
        defaultTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        defaultTableModel.setColumnIdentifiers(new Object[]{"IP", "MAC"});
        arpTable.setModel(defaultTableModel);
        arpTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        arpTableScrollPane.setViewportView(arpTable);
        load();

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
                refreshButton.setEnabled(false);
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 5; i++) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            load();
                        }
                        refreshButton.setEnabled(true);
                    }
                });

            }
        });

        spoofButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String destinationIp;
                String destinationMac;
                if (arpTable.getSelectedRow() != -1) {
                    String ip = (String) arpTable.getValueAt(arpTable.getSelectedRow(), 0);
                    String mac = (String) arpTable.getValueAt(arpTable.getSelectedRow(), 1);
                    destinationIp = ConvertUtils.ipToHex(ip);
                    destinationMac = mac.replace("-", " ");
                } else {
                    return;
                }
                String sourceIp = ipTextField.getText().trim();

                String realMac = null;
                for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
                    if (sourceIp.equals((String) arpTable.getValueAt(i, 0))) {
                        String mac = (String) arpTable.getValueAt(i, 1);
                        realMac = mac.replace("-", " ");
                        break;
                    }
                }

                sourceIp = ConvertUtils.ipToHex(sourceIp);
                String sourceMac = macTextField.getText().toLowerCase().trim().replace("-", " ");
                PcapUtils.arpResponse(sourceIp, sourceMac, destinationIp, destinationMac, realMac);
            }
        });

        localCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (localCheckBox.isSelected()) {
                    macTextField.setText(PcapUtils.ipMacMap.get(PcapUtils.index).get("MAC"));
                    macTextField.setEnabled(false);
                } else {
                    macTextField.setEnabled(true);
                }
            }
        });
    }

    private void load() {
        defaultTableModel.setRowCount(0);
        String cmdStr = "arp -a";
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec(cmdStr);
            InputStream in = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(in, "GBK");
            BufferedReader br = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String message;
            while ((message = br.readLine()) != null) {
                sb.append(message + "\n");
            }
            String str = sb.toString();
            String ip = PcapUtils.ipMacMap.get(PcapUtils.index).get("IP");
            int index = str.indexOf("接口: " + ip);
            int startIndex = str.indexOf("类型", index + 1);
            int endIndex = str.indexOf("接口", index + 1);
            String string;
            if (endIndex == -1) {
                string = str.substring(startIndex + 2);
            } else {
                string = str.substring(startIndex + 2, endIndex);
            }
            String[] splitString = string.split("\n");
            ArrayList<String> ipMacList = new ArrayList<String>();
            for (String s : splitString) {
                String prefixIp = ip.substring(0, ip.lastIndexOf(".") + 1);
                if (s.startsWith("  " + prefixIp) && !s.startsWith("  " + prefixIp + "255")) {
                    String[] s1 = s.split(" ");
                    for (String s2 : s1) {
                        if (s2.equals("")) {
                            continue;
                        }
                        ipMacList.add(s2);
                    }
                }
            }
            for (int i = 0; i < ipMacList.size(); i += 3) {
                this.defaultTableModel.addRow(new Object[]{ipMacList.get(i), ipMacList.get(i + 1)});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        PcapUtils.arpRequest();
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
