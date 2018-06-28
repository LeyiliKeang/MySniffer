package com.lk;

import com.lk.packethandler.FtpPacketHandler;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainFrame extends JFrame {
    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JTextArea toHexdumpTextArea;
    private JTextArea toStringTextArea;
    private JTextField sourceIpTextField;
    private JTextField sourcePortTextField;
    private JTextField destinationIpTextField;
    private JTextField destinationPortTextField;
    private JButton stopButton;
    private JButton startButton;
    private JComboBox protocolComboBox;
    private JTable packetTable;
    private JScrollPane packetTableScrollPane;

    private DefaultTableModel defaultTableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private List<PcapPacket> packetList = new ArrayList<PcapPacket>();
    private Pcap pcap;

    public MainFrame() {
        stopButton.setEnabled(false);
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        defaultTableModel.setColumnIdentifiers(new Object[]{"序号", "源地址", "目的地址", "源端口", "目的端口", "协议", "长度", "信息"});
        packetTable.setModel(defaultTableModel);
        packetTable.getTableHeader().setReorderingAllowed(false);
        packetTable.getTableHeader().setFont(new Font("宋体", 0, 16));
        packetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        packetTableScrollPane.setViewportView(packetTable);
        packetTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                final String hexdump = packetList.get(packetTable.getSelectedRow()).toHexdump();
                final String toString = packetList.get(packetTable.getSelectedRow()).toString();
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        toHexdumpTextArea.setText(hexdump);
                        toStringTextArea.setText(toString);
                    }
                });
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        stopButton.setEnabled(true);
                        startButton.setEnabled(false);
                        sourceIpTextField.setEnabled(false);
                        sourcePortTextField.setEnabled(false);
                        destinationIpTextField.setEnabled(false);
                        destinationPortTextField.setEnabled(false);
                    }
                });

                new Thread() {
                    @Override
                    public void run() {
                        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
                        StringBuilder errbuf = new StringBuilder(); // For any error msgs

                        int r = Pcap.findAllDevs(alldevs, errbuf);
                        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
                            System.err.printf("Can't read list of devices, error is %s", errbuf
                                    .toString());
                            return;
                        }

                        System.out.println("Network devices found:");

                        int i = 0;
                        for (PcapIf device : alldevs) {
                            String description =
                                    (device.getDescription() != null) ? device.getDescription()
                                            : "No description available";
                            System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
                        }

                        PcapIf device = alldevs.get(0); // We know we have atleast 1 device
                        System.out
                                .printf("\nChoosing '%s' on your behalf:\n",
                                        (device.getDescription() != null) ? device.getDescription()
                                                : device.getName());

                        int snaplen = 64 * 1024;           // Capture all packets, no trucation
                        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
                        int timeout = 10 * 1000;           // 10 seconds in millis
                        pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

                        if (pcap == null) {
                            System.err.printf("Error while opening device for capture: "
                                    + errbuf.toString());
                            return;
                        }

                        String sourceIp = sourceIpTextField.getText().trim();
                        String sourcePort = sourcePortTextField.getText().trim();
                        String destinationIp = destinationIpTextField.getText().trim();
                        String destinationPort = destinationPortTextField.getText().trim();

                        FtpPacketHandler ftpPacketHandler = new FtpPacketHandler(defaultTableModel, packetList);

                        if (!sourceIp.equals("")) {
                            ftpPacketHandler.setSourceIp(sourceIp);
                        }
                        if (!sourcePort.equals("")) {
                            ftpPacketHandler.setSourcePort(sourcePort);
                        }
                        if (!destinationIp.equals("")) {
                            ftpPacketHandler.setDestinationIp(destinationIp);
                        }
                        if (!destinationPort.equals("")) {
                            ftpPacketHandler.setDestinationPort(destinationPort);
                        }

                        pcap.loop(-1, ftpPacketHandler, "jNetPcap rocks!");
                    }
                }.start();

            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        stopButton.setEnabled(false);
                        startButton.setEnabled(true);
                        sourceIpTextField.setEnabled(true);
                        sourcePortTextField.setEnabled(true);
                        destinationIpTextField.setEnabled(true);
                        destinationPortTextField.setEnabled(true);
                    }
                });
                pcap.close();
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new MainFrame().setVisible(true);
    }

}
