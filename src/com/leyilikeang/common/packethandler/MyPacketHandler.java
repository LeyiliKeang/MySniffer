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

        if (pcapPacket.hasHeader(this.ethernet)) {
            this.protocol = ConstantUtils.Protocol.ETH.getValue();
            this.sourceMac = FormatUtils.mac(this.ethernet.source());             // 转换源MAC显示格式
            this.destinationMac = FormatUtils.mac(this.ethernet.destination());  // 转换目的MAC显示格式
            if (pcapPacket.hasHeader(this.arp)) {                                  // 如果数据帧封装ARP协议
                PacketUtils.arpAmount++;                                            // 更新ARP数据包数目
                this.protocol = ConstantUtils.Protocol.ARP.getValue();
            }
            if (pcapPacket.hasHeader(this.ip4)) {                                  // 如果数据帧有IPv4首部
                PacketUtils.ip4Amount++;                                            // 更新IPv4数据包数目
                this.sourceIp = FormatUtils.ip(this.ip4.source());                // 转换源IP显示格式
                this.destinationIp = FormatUtils.ip(this.ip4.destination());     // 转换目的IP显示格式
                this.protocol = ConstantUtils.Protocol.IPv4.getValue();           // 更新此时数据包所使用协议为IPv4
            }
            if (pcapPacket.hasHeader(this.ip6)) {                                  // 如果数据帧有IPv6首部
                PacketUtils.ip6Amount++;                                            // 更新IPv6数据包数目
                this.sourceIp = FormatUtils.ip(this.ip6.source());               // 转换IPv6源地址显示格式
                this.destinationIp = FormatUtils.ip(this.ip6.destination());     // 转换IPv6目的地址显示格式
                this.protocol = ConstantUtils.Protocol.IPv6.getValue();           // 更新此时数据包所使用协议为IPv6
            }
            if (pcapPacket.hasHeader(this.icmp)) {                                 // 如果数据帧有ICMP首部
                PacketUtils.icmpAmount++;                                           // 更新ICMP数据包数目
                this.protocol = ConstantUtils.Protocol.ICMP.getValue();           // 更新此时数据包所使用协议为ICMP
            } else if (pcapPacket.hasHeader(this.tcp)) {                          // 如果数据帧有TCP首部
                this.sourcePort = this.tcp.source();                             // 获取源端口
                this.destinationPort = this.tcp.destination();                   // 获取目的端口
                PacketUtils.tcpAmount++;                                            // 更新TCP数据包数目
                   if (pcapPacket.hasHeader(this.http)) {                          // 如果数据帧有HTTP头
                    PacketUtils.httpAmount++;                                       // 更新HTTP数据包数目
                    this.protocol = ConstantUtils.Protocol.HTTP.getValue();       // 更新此时数据包所使用协议为HTTP
                } else {
                    this.protocol = ConstantUtils.Protocol.TCP.getValue();
                }
            } else if (pcapPacket.hasHeader(this.udp)) {                          // 如果数据帧有UDP首部
                this.sourcePort = this.udp.source();                             // 获取源端口
                this.destinationPort = this.udp.destination();                  // 获取目的端口
                PacketUtils.udpAmount++;                                           // 更新UDP数据包数目
                if (53 == this.sourcePort || 53 == this.destinationPort) {     // 源端口或目的端口为53时为DNS协议
                    PacketUtils.dnsAmount++;                                       // 更新DNS数据包数目
                    this.protocol = ConstantUtils.Protocol.DNS.getValue();       // 更新此时数据包所使用协议为DNS
                } else if (pcapPacket.hasHeader(this.sip)) {                     // 如果数据帧有SIP头
                    PacketUtils.sipAmount++;                                       // 更新SIP数据包数目
                    this.protocol = ConstantUtils.Protocol.SIP.getValue();       // 更新此时数据包所使用协议为SIP
                } else if (pcapPacket.hasHeader(this.sdp)) {                     // 如果数据帧有SDP头
                    PacketUtils.sdpAmount++;                                       // 更新SDP数据包数目
                    this.protocol = ConstantUtils.Protocol.SDP.getValue();       // 更新此时数据包所使用协议为SDP
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

        if (null != this.sourceIp && null != this.destinationIp) {// 将分析的数据包信息添加到表中
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
                scrollPane.getVerticalScrollBar().setValue(maximum - extent);                 // 滚动条置于底部
                countLabel.setText("数量：" + PacketUtils.allPackets.size());                 // 更新数据包总数量
                statisticsFrame.getArpLabel().setText("ARP：" + PacketUtils.arpAmount);      // 更新ARP数据包数量
                statisticsFrame.getIcmpLabel().setText("ICMP：" + PacketUtils.icmpAmount);   // 更新ICMP数据包数量
                statisticsFrame.getIpv4Label().setText("IPv4：" + PacketUtils.ip4Amount);    // 更新IPv4数据包数量
                statisticsFrame.getIpv6Label().setText("IPv6：" + PacketUtils.ip6Amount);    // 更新IPv6数据包数量
                statisticsFrame.getTcpLabel().setText("TCP：" + PacketUtils.tcpAmount);      // 更新TCP数据包数量
                statisticsFrame.getUdpLabel().setText("UDP：" + PacketUtils.udpAmount);      // 更新UDP数据包数量
                statisticsFrame.getHttpLabel().setText("HTTP：" + PacketUtils.httpAmount);   // 更新HTTP数据包数量
                statisticsFrame.getDnsLabel().setText("DNS：" + PacketUtils.dnsAmount);      // 更新DNS数据包数量
            }
        });
    }
}
