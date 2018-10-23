package com.leyilikeang.common.packethandler;

import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.ui.MainFrame;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.protocol.voip.Sdp;
import org.jnetpcap.protocol.voip.Sip;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * @author likang
 * @date 2018/9/9 10:57
 */
public class MyPacketHandler<T> implements JPacketHandler<T> {

    private Ethernet ethernet = new Ethernet();

    private Arp arp = new Arp();

    private Icmp icmp = new Icmp();

    private Ip4 ip4 = new Ip4();

    private Tcp tcp = new Tcp();

    private Http http = new Http();

    private Udp udp = new Udp();

    private Sip sip = new Sip();

    private Sdp sdp = new Sdp();

    private DefaultTableModel defaultTableModel;

    private JScrollPane scrollPane;

    private JLabel countLabel;

    private String type;

    public MyPacketHandler(MainFrame mainFrame) {
        this.defaultTableModel = mainFrame.getDefaultTableModel();
        this.scrollPane = mainFrame.getPacketTableScrollPane();
        this.countLabel = mainFrame.getCountLabel();
    }

    @Override
    public void nextPacket(JPacket jPacket, T user) {
        PacketUtils.allCount++;
        PacketUtils.allMap.put(PacketUtils.allCount, jPacket);

        if (jPacket.hasHeader(ethernet)) {
            String sourceMac = FormatUtils.mac(ethernet.source());
            String destinationMac = FormatUtils.mac(ethernet.destination());
            if (PacketUtils.sourceIpAddress == null && PacketUtils.destinationIpAddress == null
                    && PacketUtils.sourcePort == null && PacketUtils.destinationPort == null) {
                if (jPacket.hasHeader(arp)) {
                    PacketUtils.arpCount++;
                    PacketUtils.arpMap.put(PacketUtils.arpCount, jPacket);
                    if (type == null) {
                        defaultTableModel.addRow(new Object[]{PacketUtils.allCount,
                                sourceMac, null, destinationMac, null, "ARP", jPacket.getPacketWirelen()});
                    } else if ("ARP".equals(type)) {
                        defaultTableModel.addRow(new Object[]{PacketUtils.arpCount,
                                sourceMac, null, destinationMac, null, "ARP", jPacket.getPacketWirelen()});
                    }
                }
            }
            if (jPacket.hasHeader(ip4)) {
                String sourceIp = PacketUtils.sourceIpAddress == null
                        ? FormatUtils.ip(ip4.source()) : PacketUtils.sourceIpAddress;
                String destinationIp = PacketUtils.destinationIpAddress == null
                        ? FormatUtils.ip(ip4.destination()) : PacketUtils.destinationIpAddress;

                if (!FormatUtils.ip(ip4.source()).equals(sourceIp)
                        || !FormatUtils.ip(ip4.destination()).equals(destinationIp)) {
                    return;
                }
                destinationIp = FormatUtils.ip(ip4.destination());
                if (jPacket.hasHeader(icmp)) {
                    PacketUtils.icmpCount++;
                    PacketUtils.icmpMap.put(PacketUtils.icmpCount, jPacket);
                    if (type == null) {
                        defaultTableModel.addRow(new Object[]{PacketUtils.allCount,
                                sourceIp, null, destinationIp, null, "ICMP", jPacket.getPacketWirelen()});
                    } else if ("ICMP".equals(type)) {
                        defaultTableModel.addRow(new Object[]{PacketUtils.icmpCount,
                                sourceIp, null, destinationIp, null, "ICMP", jPacket.getPacketWirelen()});
                    }
                } else if (jPacket.hasHeader(tcp)) {
                    Integer sourcePort = PacketUtils.sourcePort == null ? tcp.source() : PacketUtils.sourcePort;
                    Integer destinationPort = PacketUtils.destinationPort == null
                            ? tcp.destination() : PacketUtils.destinationPort;
                    if (tcp.source() != sourcePort || tcp.destination() != destinationPort) {
                        return;
                    }
                    if (jPacket.hasHeader(http)) {
                        PacketUtils.httpCount++;
                        PacketUtils.httpMap.put(PacketUtils.httpCount, jPacket);
                        if (type == null) {
                            defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceIp, sourcePort,
                                    destinationIp, destinationPort, "HTTP", jPacket.getPacketWirelen()});
                        } else if ("HTTP".equals(type)) {
                            defaultTableModel.addRow(new Object[]{PacketUtils.httpCount, sourceIp, sourcePort,
                                    destinationIp, destinationPort, "HTTP", jPacket.getPacketWirelen()});
                        }
                    } else {
                        PacketUtils.tcpCount++;
                        PacketUtils.tcpMap.put(PacketUtils.tcpCount, jPacket);
                        if (type == null) {
                            defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceIp, sourcePort,
                                    destinationIp, destinationPort, "TCP", jPacket.getPacketWirelen()});
                        } else if ("TCP".equals(type)) {
                            defaultTableModel.addRow(new Object[]{PacketUtils.tcpCount, sourceIp, sourcePort,
                                    destinationIp, destinationPort, "TCP", jPacket.getPacketWirelen()});
                        }
                    }
                } else if (jPacket.hasHeader(udp)) {
                    int sourcePort = PacketUtils.sourcePort == null ? udp.source() : PacketUtils.sourcePort;
                    int destinationPort = PacketUtils.destinationPort == null
                            ? udp.destination() : PacketUtils.destinationPort;
                    if (udp.source() != sourcePort || udp.destination() != destinationPort) {
                        return;
                    }
                    if (jPacket.hasHeader(sip)) {
                        PacketUtils.sipCount++;
                        PacketUtils.sipMap.put(PacketUtils.sipCount, jPacket);
                        if (type == null) {
                            defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceIp, sourcePort,
                                    destinationIp, destinationPort, "SIP", jPacket.getPacketWirelen()});
                        } else if ("SIP".equals(type)) {
                            defaultTableModel.addRow(new Object[]{PacketUtils.sipCount, sourceIp, sourcePort,
                                    destinationIp, destinationPort, "SIP", jPacket.getPacketWirelen()});
                        }
                    } else if (jPacket.hasHeader(sdp)) {
                        PacketUtils.sdpCount++;
                        PacketUtils.sdpMap.put(PacketUtils.sdpCount, jPacket);
                        if (type == null) {
                            defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceIp, sourcePort,
                                    destinationIp, destinationPort, "SDP", jPacket.getPacketWirelen()});
                        } else if ("SDP".equals(type)) {
                            defaultTableModel.addRow(new Object[]{PacketUtils.sdpCount, sourceIp, sourcePort,
                                    destinationIp, destinationPort, "SDP", jPacket.getPacketWirelen()});
                        }
                    } else {
                        PacketUtils.udpCount++;
                        PacketUtils.udpMap.put(PacketUtils.udpCount, jPacket);
                        if (type == null) {
                            defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceIp, sourcePort,
                                    destinationIp, destinationPort, "UDP", jPacket.getPacketWirelen()});
                        } else if ("UDP".equals(type)) {
                            defaultTableModel.addRow(new Object[]{PacketUtils.udpCount, sourceIp, sourcePort,
                                    destinationIp, destinationPort, "UDP", jPacket.getPacketWirelen()});
                        }
                    }
                } else {
                    PacketUtils.ipMap.put(PacketUtils.allCount, jPacket);
                }
            }
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int maximum = scrollPane.getVerticalScrollBar().getMaximum();
                scrollPane.getViewport().setViewPosition(new Point(0, maximum));
                countLabel.setText("数量：" + PacketUtils.allMap.size());
            }
        });
    }
}
