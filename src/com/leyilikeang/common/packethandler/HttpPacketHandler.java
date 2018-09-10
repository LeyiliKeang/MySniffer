package com.leyilikeang.common.packethandler;

import com.leyilikeang.common.util.PacketUtils;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.packet.format.JFormatter;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;

import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * @author likang
 * @date 2018/9/8 12:30
 */
public class HttpPacketHandler<T> implements JPacketHandler<T> {

    private DefaultTableModel defaultTableModel;

    public HttpPacketHandler(DefaultTableModel defaultTableModel) {
        this.defaultTableModel = defaultTableModel;
    }

    @Override
    public void nextPacket(final JPacket jPacket, T user) {
        Http http = new Http();
        Tcp tcp = new Tcp();
        Ip4 ip4 = new Ip4();
        if (jPacket.hasHeader(http) && jPacket.hasHeader(tcp) && jPacket.hasHeader(ip4)) {
            final int sourcePort = tcp.source();
            final int destinationPort = tcp.destination();
            final String sourceIp = FormatUtils.ip(ip4.source());
            final String destinationIp = FormatUtils.ip(ip4.destination());

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    PacketUtils.allMap.put(PacketUtils.count, jPacket);
                    defaultTableModel.addRow(new Object[]{++PacketUtils.count, sourceIp, sourcePort, destinationIp, destinationPort, "http", jPacket.getPacketWirelen()});
                }
            });
        }
    }
}
