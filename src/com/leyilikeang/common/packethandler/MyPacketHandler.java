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

    private JLabel countLabel;

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

        final PcapPacket packet = new PcapPacket(pcapPacket);

        if (packet.hasHeader(this.ethernet)) {
            this.protocol = ConstantUtils.Protocol.ETH.getValue();
            this.sourceMac = FormatUtils.mac(this.ethernet.source());
            this.destinationMac = FormatUtils.mac(this.ethernet.destination());
            if (packet.hasHeader(this.arp)) {
                PacketUtils.arpPackets.add(packet);
                this.protocol = ConstantUtils.Protocol.ARP.getValue();
            }
            if (packet.hasHeader(this.ip4)) {
                this.sourceIp = FormatUtils.ip(this.ip4.source());
                this.destinationIp = FormatUtils.ip(this.ip4.destination());
                this.protocol = ConstantUtils.Protocol.IPv4.getValue();
            }
            if (packet.hasHeader(this.ip6)) {
                this.sourceIp = FormatUtils.ip(this.ip6.source());
                this.destinationIp = FormatUtils.ip(this.ip6.destination());
                this.protocol = ConstantUtils.Protocol.IPv6.getValue();
            }
            if (packet.hasHeader(this.icmp)) {
                PacketUtils.icmpPackets.add(packet);
                this.protocol = ConstantUtils.Protocol.ICMP.getValue();
            } else if (packet.hasHeader(this.tcp)) {
                this.sourcePort = this.tcp.source();
                this.destinationPort = this.tcp.destination();
                if (53 == this.sourcePort || 53 == this.destinationPort) {
                    PacketUtils.dnsPackets.add(packet);
                    this.protocol = ConstantUtils.Protocol.DNS.getValue();
                } else if (packet.hasHeader(this.http)) {
                    PacketUtils.httpPackets.add(packet);
                    this.protocol = ConstantUtils.Protocol.HTTP.getValue();
                } else {
                    PacketUtils.tcpPackets.add(packet);
                    this.protocol = ConstantUtils.Protocol.TCP.getValue();
                }
            } else if (packet.hasHeader(this.udp)) {
                this.sourcePort = this.udp.source();
                this.destinationPort = this.udp.destination();
                if (53 == this.sourcePort || 53 == this.destinationPort) {
                    PacketUtils.dnsPackets.add(packet);
                    this.protocol = ConstantUtils.Protocol.DNS.getValue();
                } else if (packet.hasHeader(this.sip)) {
                    PacketUtils.sipPackets.add(packet);
                    this.protocol = ConstantUtils.Protocol.SIP.getValue();
                } else if (packet.hasHeader(this.sdp)) {
                    PacketUtils.sdpPackets.add(packet);
                    this.protocol = ConstantUtils.Protocol.SDP.getValue();
                } else {
                    PacketUtils.udpPackets.add(packet);
                    this.protocol = ConstantUtils.Protocol.UDP.getValue();
                }
            }
        }
        if (packet.hasHeader(this.llc2)) {
            PacketUtils.llc2Packets.add(packet);
            this.protocol = ConstantUtils.Protocol.LLC.getValue();
        }
        if (ConstantUtils.Protocol.ETH.getValue().equals(this.protocol)) {
            PacketUtils.ethernetPackets.add(packet);
        }
        if (ConstantUtils.Protocol.IPv4.getValue().equals(this.protocol)) {
            PacketUtils.ip4Packets.add(packet);
        }
        if (ConstantUtils.Protocol.IPv6.getValue().equals(this.protocol)) {
            PacketUtils.ip6Packets.add(packet);
        }

        if (null == PacketUtils.protocolType) {
            PacketUtils.allPackets.add(packet);
        } else if (this.protocol.equals(PacketUtils.protocolType)) {
            PacketUtils.allPackets.add(packet);
        } else {
            return;
        }

        if (t instanceof PcapDumper) {
            ((PcapDumper) t).dump(packet);
        }

        if (null != this.sourceIp && null != this.destinationIp) {
            this.defaultTableModel.addRow(new Object[]{PacketUtils.allPackets.size(), this.sourceIp, this.sourcePort,
                    this.destinationIp, this.destinationPort, this.protocol, pcapPacket.getPacketWirelen()});
        } else {
            this.defaultTableModel.addRow(new Object[]{PacketUtils.allPackets.size(), this.sourceMac, this.sourcePort,
                    this.destinationMac, this.destinationPort, this.protocol, pcapPacket.getPacketWirelen()});
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int maximum = scrollPane.getVerticalScrollBar().getMaximum();
                int extent = scrollPane.getVerticalScrollBar().getModel().getExtent();
                scrollPane.getVerticalScrollBar().setValue(maximum - extent);
                countLabel.setText("数量：" + PacketUtils.allPackets.size());
            }
        });
    }
}
