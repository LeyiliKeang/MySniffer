package com.leyilikeang.common.packethandler;

import com.leyilikeang.common.util.PacketUtils;
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

import javax.swing.table.DefaultTableModel;

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

    public MyPacketHandler(DefaultTableModel defaultTableModel) {
        this.defaultTableModel = defaultTableModel;
    }

    @Override
    public void nextPacket(JPacket jPacket, T user) {
        PacketUtils.count++;
        PacketUtils.allMap.put(PacketUtils.count, jPacket);

        if (jPacket.hasHeader(ethernet)) {
            String sourceMac = FormatUtils.mac(ethernet.source());
            String destinationMac = FormatUtils.mac(ethernet.destination());
            if (jPacket.hasHeader(arp)) {
                PacketUtils.arpMap.put(PacketUtils.count, jPacket);
                defaultTableModel.addRow(new Object[]{PacketUtils.count, sourceMac, null, destinationMac, null, "arp", jPacket.getPacketWirelen()});
            }
            if (jPacket.hasHeader(ip4)) {
                String sourceIp = FormatUtils.ip(ip4.source());
                String destinationIp = FormatUtils.ip(ip4.destination());
                if (jPacket.hasHeader(icmp)) {
                    PacketUtils.icmpMap.put(PacketUtils.count, jPacket);
                } else if (jPacket.hasHeader(tcp)) {
                    int sourcePort = tcp.source();
                    int destinationPort = tcp.destination();
                    if (jPacket.hasHeader(http)) {
                        PacketUtils.httpMap.put(PacketUtils.count, jPacket);
                        defaultTableModel.addRow(new Object[]{PacketUtils.count, sourceIp, sourcePort, destinationIp, destinationPort, "http", jPacket.getPacketWirelen()});
                    } else {
                        PacketUtils.tcpMap.put(PacketUtils.count, jPacket);
                        defaultTableModel.addRow(new Object[]{PacketUtils.count, sourceIp, sourcePort, destinationIp, destinationPort, "tcp", jPacket.getPacketWirelen()});
                    }
                } else if (jPacket.hasHeader(udp)) {
                    int sourcePort = udp.source();
                    int destinationPort = udp.destination();
                    if (jPacket.hasHeader(sip)) {
                        PacketUtils.sipMap.put(PacketUtils.count, jPacket);
                        defaultTableModel.addRow(new Object[]{PacketUtils.count, sourceIp, sourcePort, destinationIp, destinationPort, "sip", jPacket.getPacketWirelen()});
                    } else if (jPacket.hasHeader(sdp)) {
                        PacketUtils.sdpMap.put(PacketUtils.count, jPacket);
                        defaultTableModel.addRow(new Object[]{PacketUtils.count, sourceIp, sourcePort, destinationIp, destinationPort, "sdp", jPacket.getPacketWirelen()});
                    } else {
                        PacketUtils.udpMap.put(PacketUtils.count, jPacket);
                        defaultTableModel.addRow(new Object[]{PacketUtils.count, sourceIp, sourcePort, destinationIp, destinationPort, "udp", jPacket.getPacketWirelen()});
                    }
                } else {
                    PacketUtils.ipMap.put(PacketUtils.count, jPacket);
                }
            }
        }
    }
}
