package com.leyilikeang.common.packethandler;

import com.leyilikeang.common.util.ConstantUtils;
import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.ui.MainFrame;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
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
public class ExpressionPacketHandler<T> implements JPacketHandler<T> {

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

    private JLabel countLabel;

    private JScrollPane scrollPane;

    private String sourceMac;
    private String destinationMac;
    private String sourceIp;
    private String destinationIp;
    private Integer sourcePort;
    private Integer destinationPort;
    private String protocol;

    public ExpressionPacketHandler(MainFrame mainFrame) {
        this.defaultTableModel = mainFrame.getDefaultTableModel();
        this.countLabel = mainFrame.getCountLabel();
        this.scrollPane = mainFrame.getPacketTableScrollPane();
    }

    @Override
    public void nextPacket(final JPacket jPacket, T user) {
        sourceMac = null;
        destinationMac = null;
        sourceIp = null;
        destinationIp = null;
        sourcePort = null;
        destinationPort = null;
        protocol = null;

        PacketUtils.allCount++;
        PacketUtils.allMap.put(PacketUtils.allCount, jPacket);

        if (jPacket.hasHeader(ethernet)) {
            protocol = ConstantUtils.Protocol.ETH.getValue();
            sourceMac = FormatUtils.mac(ethernet.source());
            destinationMac = FormatUtils.mac(ethernet.destination());
            if (jPacket.hasHeader(arp)) {
                PacketUtils.arpCount++;
                PacketUtils.arpMap.put(PacketUtils.arpCount, jPacket);
                protocol = ConstantUtils.Protocol.ARP.getValue();
            }
            if (jPacket.hasHeader(ip4)) {
                sourceIp = FormatUtils.ip(ip4.source());
                destinationIp = FormatUtils.ip(ip4.destination());
                protocol = ConstantUtils.Protocol.IPv4.getValue();
            }
            if (jPacket.hasHeader(ip6)) {
                sourceIp = FormatUtils.ip(ip6.source());
                destinationIp = FormatUtils.ip(ip6.destination());
                protocol = ConstantUtils.Protocol.IPv6.getValue();
            }
            if (jPacket.hasHeader(icmp)) {
                PacketUtils.icmpCount++;
                PacketUtils.icmpMap.put(PacketUtils.icmpCount, jPacket);
                protocol = ConstantUtils.Protocol.ICMP.getValue();
            } else if (jPacket.hasHeader(tcp)) {
                sourcePort = tcp.source();
                destinationPort = tcp.destination();
                if (jPacket.hasHeader(http)) {
                    PacketUtils.httpCount++;
                    PacketUtils.httpMap.put(PacketUtils.httpCount, jPacket);
                    protocol = ConstantUtils.Protocol.HTTP.getValue();
                } else {
                    PacketUtils.tcpCount++;
                    PacketUtils.tcpMap.put(PacketUtils.tcpCount, jPacket);
                    protocol = ConstantUtils.Protocol.TCP.getValue();
                }
            } else if (jPacket.hasHeader(udp)) {
                sourcePort = udp.source();
                destinationPort = udp.destination();
                if (jPacket.hasHeader(sip)) {
                    PacketUtils.sipCount++;
                    PacketUtils.sipMap.put(PacketUtils.sipCount, jPacket);
                    protocol = ConstantUtils.Protocol.SIP.getValue();
                } else if (jPacket.hasHeader(sdp)) {
                    PacketUtils.sdpCount++;
                    PacketUtils.sdpMap.put(PacketUtils.sdpCount, jPacket);
                    protocol = ConstantUtils.Protocol.SDP.getValue();
                } else {
                    PacketUtils.udpCount++;
                    PacketUtils.udpMap.put(PacketUtils.udpCount, jPacket);
                    protocol = ConstantUtils.Protocol.UDP.getValue();
                }
            }
        }
        if (jPacket.hasHeader(llc2)) {
            protocol = ConstantUtils.Protocol.LLC.getValue();
        }

        if (null != sourceIp && null != destinationIp) {
            defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceIp, sourcePort,
                    destinationIp, destinationPort, protocol, jPacket.getPacketWirelen()});
        } else {
            defaultTableModel.addRow(new Object[]{PacketUtils.allCount, sourceMac, sourcePort,
                    destinationMac, destinationPort, protocol, jPacket.getPacketWirelen()});
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
