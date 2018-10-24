package com.leyilikeang.common.packethandler;

import com.leyilikeang.common.util.ConstantUtils;
import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.ui.MainFrame;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.lan.IEEE802dot2;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
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
public class MyPacketHandler<T> implements PcapPacketHandler<T> {

    private Ethernet ethernet = new Ethernet();

    private IEEE802dot2 llc2 = new IEEE802dot2();

    private Arp arp = new Arp();

    private Icmp icmp = new Icmp();

    private Ip4 ip4 = new Ip4();

    private Ip6 ip6 = new Ip6();

    private Tcp tcp = new Tcp();

    private Http http = new Http();

    private Udp udp = new Udp();

    private Sip sip = new Sip();

    private Sdp sdp = new Sdp();

    private DefaultTableModel defaultTableModel;

    private JScrollPane scrollPane;

    private String sourceMac;
    private String destinationMac;
    private String sourceIp;
    private String destinationIp;
    private Integer sourcePort;
    private Integer destinationPort;
    private String protocol;

    public MyPacketHandler(MainFrame mainFrame) {
        this.defaultTableModel = mainFrame.getDefaultTableModel();
        this.scrollPane = mainFrame.getPacketTableScrollPane();
    }

    @Override
    public void nextPacket(PcapPacket pcapPacket, T t) {
        if (t instanceof PcapDumper) {
            ((PcapDumper) t).dump(pcapPacket);
        }
        sourceMac = null;
        destinationMac = null;
        sourceIp = null;
        destinationIp = null;
        sourcePort = null;
        destinationPort = null;
        protocol = null;

        PcapPacket packet = new PcapPacket(pcapPacket);
        PacketUtils.allPackets.add(packet);

        if (pcapPacket.hasHeader(ethernet)) {
            protocol = ConstantUtils.Protocol.ETH.getValue();
            sourceMac = FormatUtils.mac(ethernet.source());
            destinationMac = FormatUtils.mac(ethernet.destination());
            if (pcapPacket.hasHeader(arp)) {
                PacketUtils.arpPackets.add(packet);
                protocol = ConstantUtils.Protocol.ARP.getValue();
            }
            if (pcapPacket.hasHeader(ip4)) {
                sourceIp = FormatUtils.ip(ip4.source());
                destinationIp = FormatUtils.ip(ip4.destination());
                protocol = ConstantUtils.Protocol.IPv4.getValue();
            }
            if (pcapPacket.hasHeader(ip6)) {
                sourceIp = FormatUtils.ip(ip6.source());
                destinationIp = FormatUtils.ip(ip6.destination());
                protocol = ConstantUtils.Protocol.IPv6.getValue();
            }
            if (pcapPacket.hasHeader(icmp)) {
                PacketUtils.icmpPackets.add(packet);
                protocol = ConstantUtils.Protocol.ICMP.getValue();
            } else if (pcapPacket.hasHeader(tcp)) {
                sourcePort = tcp.source();
                destinationPort = tcp.destination();
                if (pcapPacket.hasHeader(http)) {
                    PacketUtils.httpPackets.add(packet);
                    protocol = ConstantUtils.Protocol.HTTP.getValue();
                } else {
                    PacketUtils.tcpPackets.add(packet);
                    protocol = ConstantUtils.Protocol.TCP.getValue();
                }
            } else if (pcapPacket.hasHeader(udp)) {
                sourcePort = udp.source();
                destinationPort = udp.destination();
                if (pcapPacket.hasHeader(sip)) {
                    PacketUtils.sipPackets.add(packet);
                    protocol = ConstantUtils.Protocol.SIP.getValue();
                } else if (pcapPacket.hasHeader(sdp)) {
                    PacketUtils.sdpPackets.add(packet);
                    protocol = ConstantUtils.Protocol.SDP.getValue();
                } else {
                    PacketUtils.udpPackets.add(packet);
                    protocol = ConstantUtils.Protocol.UDP.getValue();
                }
            }
        }
        if (pcapPacket.hasHeader(llc2)) {
            PacketUtils.llc2Packets.add(packet);
            protocol = ConstantUtils.Protocol.LLC.getValue();
        }
        if (ConstantUtils.Protocol.ETH.getValue().equals(protocol)) {
            PacketUtils.ethernetPackets.add(packet);
        }
        if (ConstantUtils.Protocol.IPv4.getValue().equals(protocol)) {
            PacketUtils.ip4Packets.add(packet);
        }
        if (ConstantUtils.Protocol.IPv6.getValue().equals(protocol)) {
            PacketUtils.ip6Packets.add(packet);
        }
        if (null != sourceIp && null != destinationIp) {
            defaultTableModel.addRow(new Object[]{PacketUtils.allPackets.size(), sourceIp, sourcePort,
                    destinationIp, destinationPort, protocol, pcapPacket.getPacketWirelen()});
        } else {
            defaultTableModel.addRow(new Object[]{PacketUtils.allPackets.size(), sourceMac, sourcePort,
                    destinationMac, destinationPort, protocol, pcapPacket.getPacketWirelen()});
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int maximum = scrollPane.getVerticalScrollBar().getMaximum();
                scrollPane.getViewport().setViewPosition(new Point(0, maximum));
            }
        });
    }
}
