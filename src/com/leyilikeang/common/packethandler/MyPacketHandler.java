package com.leyilikeang.common.packethandler;

import com.leyilikeang.common.util.PacketUtils;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.packet.format.JFormatter;
import org.jnetpcap.packet.format.TextFormatter;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.protocol.voip.Sdp;
import org.jnetpcap.protocol.voip.Sip;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;

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

    private String type;

    public MyPacketHandler(DefaultTableModel defaultTableModel) {
        this.defaultTableModel = defaultTableModel;
    }

    @Override
    public void nextPacket(JPacket jPacket, T user) {
        PacketUtils.allCount++;
        PacketUtils.allMap.put(PacketUtils.allCount, jPacket);

        if (jPacket.hasHeader(ethernet)) {
            String sourceMac = FormatUtils.mac(ethernet.source());
            String destinationMac = FormatUtils.mac(ethernet.destination());
            if (jPacket.hasHeader(arp)) {
                PacketUtils.arpCount++;
                if (type == null) {
                    PacketUtils.arpMap.put(PacketUtils.allCount, jPacket);
                    defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceMac, null, destinationMac, null, "ARP", jPacket.getPacketWirelen()});
                } else if ("ARP".equals(type)) {
                    PacketUtils.arpMap.put(PacketUtils.arpCount, jPacket);
                    defaultTableModel.addRow(new Object[]{PacketUtils.arpCount, sourceMac, null, destinationMac, null, "ARP", jPacket.getPacketWirelen()});
                }
            }
            if (jPacket.hasHeader(ip4)) {
                String sourceIp = FormatUtils.ip(ip4.source());
                String destinationIp = FormatUtils.ip(ip4.destination());
                if (jPacket.hasHeader(icmp)) {
                    PacketUtils.icmpCount++;
                    if (type == null) {
                        PacketUtils.icmpMap.put(PacketUtils.allCount, jPacket);
                        defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceIp, null, destinationIp, null, "ICMP", jPacket.getPacketWirelen()});
                    } else if ("ICMP".equals(type)) {
                        PacketUtils.icmpMap.put(PacketUtils.icmpCount, jPacket);
                        defaultTableModel.addRow(new Object[]{PacketUtils.icmpCount, sourceIp, null, destinationIp, null, "ICMP", jPacket.getPacketWirelen()});
                    }
                } else if (jPacket.hasHeader(tcp)) {
                    int sourcePort = tcp.source();
                    int destinationPort = tcp.destination();
                    if (jPacket.hasHeader(http)) {
                        PacketUtils.httpCount++;
                        if (type == null) {
                            PacketUtils.httpMap.put(PacketUtils.allCount, jPacket);
                            defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceIp, sourcePort, destinationIp, destinationPort, "HTTP", jPacket.getPacketWirelen()});
                        } else if ("HTTP".equals(type)) {
                            PacketUtils.httpMap.put(PacketUtils.httpCount, jPacket);
                            defaultTableModel.addRow(new Object[]{PacketUtils.httpCount, sourceIp, sourcePort, destinationIp, destinationPort, "HTTP", jPacket.getPacketWirelen()});
                        }
                    } else {
                        PacketUtils.tcpCount++;
                        if (type == null) {
                            PacketUtils.tcpMap.put(PacketUtils.allCount, jPacket);
                            defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceIp, sourcePort, destinationIp, destinationPort, "TCP", jPacket.getPacketWirelen()});
                        } else if ("TCP".equals(type)) {
                            PacketUtils.tcpMap.put(PacketUtils.tcpCount, jPacket);
                            defaultTableModel.addRow(new Object[]{PacketUtils.tcpCount, sourceIp, sourcePort, destinationIp, destinationPort, "TCP", jPacket.getPacketWirelen()});
                        }
                    }
                } else if (jPacket.hasHeader(udp)) {
                    int sourcePort = udp.source();
                    int destinationPort = udp.destination();
                    if (jPacket.hasHeader(sip)) {
                        PacketUtils.sipCount++;
                        if (type == null) {
                            PacketUtils.sipMap.put(PacketUtils.allCount, jPacket);
                            defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceIp, sourcePort, destinationIp, destinationPort, "SIP", jPacket.getPacketWirelen()});
                        } else if ("SIP".equals(type)) {
                            PacketUtils.sipMap.put(PacketUtils.sipCount, jPacket);
                            defaultTableModel.addRow(new Object[]{PacketUtils.sipCount, sourceIp, sourcePort, destinationIp, destinationPort, "SIP", jPacket.getPacketWirelen()});
                        }
                    } else if (jPacket.hasHeader(sdp)) {
                        PacketUtils.sdpCount++;
                        if (type == null) {
                            PacketUtils.sdpMap.put(PacketUtils.allCount, jPacket);
                            defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceIp, sourcePort, destinationIp, destinationPort, "SDP", jPacket.getPacketWirelen()});
                        } else if ("SDP".equals(type)) {
                            PacketUtils.sdpMap.put(PacketUtils.sdpCount, jPacket);
                            defaultTableModel.addRow(new Object[]{PacketUtils.sdpCount, sourceIp, sourcePort, destinationIp, destinationPort, "SDP", jPacket.getPacketWirelen()});
                        }
                    } else {
                        PacketUtils.udpCount++;
                        if (type == null) {
                            PacketUtils.udpMap.put(PacketUtils.allCount, jPacket);
                            defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceIp, sourcePort, destinationIp, destinationPort, "UDP", jPacket.getPacketWirelen()});
                        } else if ("UDP".equals(type)) {
                            PacketUtils.udpMap.put(PacketUtils.udpCount, jPacket);
                            defaultTableModel.addRow(new Object[]{PacketUtils.udpCount, sourceIp, sourcePort, destinationIp, destinationPort, "UDP", jPacket.getPacketWirelen()});
                        }
                    }
                } else {
                    PacketUtils.ipMap.put(PacketUtils.allCount, jPacket);
                }
            }
        }
    }
}
