package com.leyilikeang.common.packethandler;

import com.leyilikeang.common.util.ConstantUtils;
import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.ui.MainFrame;
import com.leyilikeang.ui.StatisticsFrame;
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

    private JLabel countLabel;

    private StatisticsFrame statisticsFrame;

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
        this.countLabel = mainFrame.getCountLabel();
        this.statisticsFrame = mainFrame.getStatisticsFrame();
    }

    @Override
    public void nextPacket(PcapPacket pcapPacket, T t) {
        this.sourceMac = null;
        this.destinationMac = null;
        this.sourceIp = null;
        this.destinationIp = null;
        this.sourcePort = null;
        this.destinationPort = null;
        this.protocol = null;

//        final PcapPacket packet = new PcapPacket(pcapPacket);

        if (pcapPacket.hasHeader(this.ethernet)) {
            this.protocol = ConstantUtils.Protocol.ETH.getValue();
            this.sourceMac = FormatUtils.mac(this.ethernet.source());
            this.destinationMac = FormatUtils.mac(this.ethernet.destination());
            if (pcapPacket.hasHeader(this.arp)) {
                PacketUtils.arpAmount++;
                this.protocol = ConstantUtils.Protocol.ARP.getValue();
            }
            if (pcapPacket.hasHeader(this.ip4)) {
                PacketUtils.ip4Amount++;
                this.sourceIp = FormatUtils.ip(this.ip4.source());
                this.destinationIp = FormatUtils.ip(this.ip4.destination());
                this.protocol = ConstantUtils.Protocol.IPv4.getValue();
            }
            if (pcapPacket.hasHeader(this.ip6)) {
                PacketUtils.ip6Amount++;
                this.sourceIp = FormatUtils.ip(this.ip6.source());
                this.destinationIp = FormatUtils.ip(this.ip6.destination());
                this.protocol = ConstantUtils.Protocol.IPv6.getValue();
            }
            if (pcapPacket.hasHeader(this.icmp)) {
                PacketUtils.icmpAmount++;
                this.protocol = ConstantUtils.Protocol.ICMP.getValue();
            } else if (pcapPacket.hasHeader(this.tcp)) {
                this.sourcePort = this.tcp.source();
                this.destinationPort = this.tcp.destination();
                PacketUtils.tcpAmount++;
                if (pcapPacket.hasHeader(this.http)) {
                    PacketUtils.httpAmount++;
                    this.protocol = ConstantUtils.Protocol.HTTP.getValue();
                } else {
                    this.protocol = ConstantUtils.Protocol.TCP.getValue();
                }
            } else if (pcapPacket.hasHeader(this.udp)) {
                this.sourcePort = this.udp.source();
                this.destinationPort = this.udp.destination();
                PacketUtils.udpAmount++;
                if (53 == this.sourcePort || 53 == this.destinationPort) {
                    PacketUtils.dnsAmount++;
                    this.protocol = ConstantUtils.Protocol.DNS.getValue();
                } else if (pcapPacket.hasHeader(this.sip)) {
                    PacketUtils.sipAmount++;
                    this.protocol = ConstantUtils.Protocol.SIP.getValue();
                } else if (pcapPacket.hasHeader(this.sdp)) {
                    PacketUtils.sdpAmount++;
                    this.protocol = ConstantUtils.Protocol.SDP.getValue();
                } else {
                    this.protocol = ConstantUtils.Protocol.UDP.getValue();
                }
            }
        }
        if (pcapPacket.hasHeader(this.llc2)) {
            PacketUtils.llc2Amount++;
            this.protocol = ConstantUtils.Protocol.LLC.getValue();
        }
        if (ConstantUtils.Protocol.ETH.getValue().equals(this.protocol)) {
            PacketUtils.ethernetAmount++;
        }
        if (ConstantUtils.Protocol.IPv4.getValue().equals(this.protocol)) {
            PacketUtils.ip4Amount++;
        }
        if (ConstantUtils.Protocol.IPv6.getValue().equals(this.protocol)) {
            PacketUtils.ip6Amount++;
        }

        if (null == PacketUtils.protocolType) {
                PacketUtils.allPackets.add(pcapPacket);
        } else if (this.protocol.equals(PacketUtils.protocolType)) {
                PacketUtils.allPackets.add(pcapPacket);
        } else {
            return;
        }

        if (t instanceof PcapDumper) {
            ((PcapDumper) t).dump(pcapPacket);
        }

        if (null != this.sourceIp && null != this.destinationIp) {
            this.defaultTableModel.addRow(new Object[]{PacketUtils.allPackets.size(), this.sourceIp, this.sourcePort,
                    this.destinationIp, this.destinationPort, this.protocol, pcapPacket.getPacketWirelen()});
        } else {
            this.defaultTableModel.addRow(new Object[]{PacketUtils.allPackets.size(), this.sourceMac, this.sourcePort,
                    this.destinationMac, this.destinationPort, this.protocol, pcapPacket.getPacketWirelen()});
        }

        PacketUtils.per = PacketUtils.per + pcapPacket.getPacketWirelen();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int maximum = scrollPane.getVerticalScrollBar().getMaximum();
                int extent = scrollPane.getVerticalScrollBar().getModel().getExtent();
                scrollPane.getVerticalScrollBar().setValue(maximum - extent);
                countLabel.setText("数量：" + PacketUtils.allPackets.size());
                statisticsFrame.getArpLabel().setText("ARP：" + PacketUtils.arpAmount);
                statisticsFrame.getIcmpLabel().setText("ICMP：" + PacketUtils.icmpAmount);
                statisticsFrame.getIpv4Label().setText("IPv4：" + PacketUtils.ip4Amount);
                statisticsFrame.getIpv6Label().setText("IPv6：" + PacketUtils.ip6Amount);
                statisticsFrame.getTcpLabel().setText("TCP：" + PacketUtils.tcpAmount);
                statisticsFrame.getUdpLabel().setText("UDP：" + PacketUtils.udpAmount);
                statisticsFrame.getHttpLabel().setText("HTTP：" + PacketUtils.httpAmount);
                statisticsFrame.getDnsLabel().setText("DNS：" + PacketUtils.dnsAmount);
            }
        });
    }
}
