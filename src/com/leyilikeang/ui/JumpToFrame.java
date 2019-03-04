package com.leyilikeang.ui;

import com.leyilikeang.common.util.ConstantUtils;
import com.leyilikeang.common.util.PacketUtils;
import org.jnetstream.protocol.ProtocolInfo;
import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.PortUnreachableException;
import java.util.ArrayList;

/**
 * @author likang
 * @date 2019/3/4 8:36
 */
public class JumpToFrame {
    private JPanel contentPane;
    private JComboBox protocolComboBox;
    private JTextField jumpToTextField;
    private JButton previousButton;
    private JButton jumpToButton;
    private JButton nextButton;

    public static boolean isOpen = false;

    private MainFrame mainFrame;

    public JumpToFrame(final MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        mainFrame.setJumpToFrame(this);
        jumpToButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String protocol = (String) protocolComboBox.getSelectedItem();
                int index = new Integer(jumpToTextField.getText().trim());
                JTable packetTable = mainFrame.getPacketTable();
                ArrayList<String> protocols = new ArrayList<String>();
                protocols.add(protocol);
                if (protocol.equalsIgnoreCase("ALL")) {
                    if (index > PacketUtils.allPackets.size()) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        packetTable.setRowSelectionInterval(index - 1, index - 1);
                        Rectangle rectangle = packetTable.getCellRect(index - 1, 0, true);
                        packetTable.scrollRectToVisible(rectangle);
                    }
                } else if (protocol.equalsIgnoreCase("ARP")) {
                    if (index > PacketUtils.arpAmount) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出ARP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        select(protocols, index);
                    }
                } else if (protocol.equalsIgnoreCase("ICMP")) {
                    if (index > PacketUtils.icmpAmount) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出ICMP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        select(protocols, index);
                    }
                } else if (protocol.equalsIgnoreCase("TCP")) {
                    if (index > PacketUtils.tcpAmount) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出TCP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        protocols.add(ConstantUtils.Protocol.HTTP.getValue());
                        select(protocols, index);
                    }
                } else if (protocol.equalsIgnoreCase("UDP")) {
                    if (index > PacketUtils.udpAmount) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出UDP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        protocols.add(ConstantUtils.Protocol.DNS.getValue());
                        protocols.add(ConstantUtils.Protocol.SIP.getValue());
                        protocols.add(ConstantUtils.Protocol.SDP.getValue());
                        select(protocols, index);
                    }
                } else if (protocol.equalsIgnoreCase("HTTP")) {
                    if (index > PacketUtils.httpAmount) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出HTTP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        select(protocols, index);
                    }
                } else if (protocol.equalsIgnoreCase("DNS")) {
                    if (index > PacketUtils.dnsAmount) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出DNS包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        select(protocols, index);
                    }
                }
            }
        });

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String protocol = (String) protocolComboBox.getSelectedItem();
                int index = new Integer(jumpToTextField.getText().trim());
                if (index > 1) {
                    jumpToTextField.setText(Integer.toString(--index));
                    JTable packetTable = mainFrame.getPacketTable();
                    ArrayList<String> protocols = new ArrayList<String>();
                    protocols.add(protocol);
                    if (protocol.equalsIgnoreCase("ALL")) {
                        if (index > PacketUtils.allPackets.size()) {
                            JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                    "超出包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                        } else {
                            packetTable.setRowSelectionInterval(index - 1, index - 1);
                            Rectangle rectangle = packetTable.getCellRect(index - 1, 0, true);
                            packetTable.scrollRectToVisible(rectangle);
                        }
                    } else if (protocol.equalsIgnoreCase("ARP")) {
                        if (index > PacketUtils.arpAmount) {
                            JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                    "超出ARP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                        } else {
                            select(protocols, index);
                        }
                    } else if (protocol.equalsIgnoreCase("ICMP")) {
                        if (index > PacketUtils.icmpAmount) {
                            JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                    "超出ICMP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                        } else {
                            select(protocols, index);
                        }
                    } else if (protocol.equalsIgnoreCase("TCP")) {
                        if (index > PacketUtils.tcpAmount) {
                            JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                    "超出TCP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                        } else {
                            protocols.add(ConstantUtils.Protocol.HTTP.getValue());
                            select(protocols, index);
                        }
                    } else if (protocol.equalsIgnoreCase("UDP")) {
                        if (index > PacketUtils.udpAmount) {
                            JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                    "超出UDP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                        } else {
                            protocols.add(ConstantUtils.Protocol.DNS.getValue());
                            protocols.add(ConstantUtils.Protocol.SIP.getValue());
                            protocols.add(ConstantUtils.Protocol.SDP.getValue());
                            select(protocols, index);
                        }
                    } else if (protocol.equalsIgnoreCase("HTTP")) {
                        if (index > PacketUtils.httpAmount) {
                            JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                    "超出HTTP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                        } else {
                            select(protocols, index);
                        }
                    } else if (protocol.equalsIgnoreCase("DNS")) {
                        if (index > PacketUtils.dnsAmount) {
                            JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                    "超出DNS包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                        } else {
                            select(protocols, index);
                        }
                    }
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String protocol = (String) protocolComboBox.getSelectedItem();
                int index = new Integer(jumpToTextField.getText().trim());
                jumpToTextField.setText(Integer.toString(++index));
                JTable packetTable = mainFrame.getPacketTable();
                ArrayList<String> protocols = new ArrayList<String>();
                protocols.add(protocol);
                if (protocol.equalsIgnoreCase("ALL")) {
                    if (index > PacketUtils.allPackets.size()) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        packetTable.setRowSelectionInterval(index - 1, index - 1);
                        Rectangle rectangle = packetTable.getCellRect(index - 1, 0, true);
                        packetTable.scrollRectToVisible(rectangle);
                    }
                } else if (protocol.equalsIgnoreCase("ARP")) {
                    if (index > PacketUtils.arpAmount) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出ARP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        select(protocols, index);
                    }
                } else if (protocol.equalsIgnoreCase("ICMP")) {
                    if (index > PacketUtils.icmpAmount) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出ICMP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        select(protocols, index);
                    }
                } else if (protocol.equalsIgnoreCase("TCP")) {
                    if (index > PacketUtils.tcpAmount) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出TCP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        protocols.add(ConstantUtils.Protocol.HTTP.getValue());
                        select(protocols, index);
                    }
                } else if (protocol.equalsIgnoreCase("UDP")) {
                    if (index > PacketUtils.udpAmount) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出UDP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        protocols.add(ConstantUtils.Protocol.DNS.getValue());
                        protocols.add(ConstantUtils.Protocol.SIP.getValue());
                        protocols.add(ConstantUtils.Protocol.SDP.getValue());
                        select(protocols, index);
                    }
                } else if (protocol.equalsIgnoreCase("HTTP")) {
                    if (index > PacketUtils.httpAmount) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出HTTP包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        select(protocols, index);
                    }
                } else if (protocol.equalsIgnoreCase("DNS")) {
                    if (index > PacketUtils.dnsAmount) {
                        JOptionPane.showMessageDialog(mainFrame.getJumpToDialog(),
                                "超出DNS包总数量", "提示", JOptionPane.WARNING_MESSAGE);
                    } else {
                        select(protocols, index);
                    }
                }

            }
        });
    }

    public void select(ArrayList<String> protocols, int index) {
        int record = 0;
        int select = 0;
        for (int i = 0; i < PacketUtils.allPackets.size(); i++) {
            for (String protocol : protocols) {
                if (protocol.equalsIgnoreCase((String) mainFrame.getDefaultTableModel().getValueAt(i, 5))) {
                    record++;
                    break;
                }
            }
            if (record == index) {
                select = i;
                break;
            }
        }
        JTable packetTable = mainFrame.getPacketTable();
        packetTable.setRowSelectionInterval(select, select);
        Rectangle rectangle = packetTable.getCellRect(select, 0, true);
        packetTable.scrollRectToVisible(rectangle);
    }

    public void reset() {
        jumpToTextField.setText("");
        protocolComboBox.setSelectedIndex(0);
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
