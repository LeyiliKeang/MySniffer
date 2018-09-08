package com.leyilikeang.common.example;

import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

/**
 * @author likang
 * @date 2018/9/8 14:18
 * <p>
 * 示例：创建一个TCP数据包
 */
public class CreateTcpPacketExample {

    public static void main(String[] args) {
        JPacket packet = new JMemoryPacket(JProtocol.ETHERNET_ID,
                " 001801bf 6adc0025 4bb7afec 08004500 "
                        + " 0041a983 40004006 d69ac0a8 00342f8c "
                        + " ca30c3ef 008f2e80 11f52ea8 4b578018 "
                        + " ffffa6ea 00000101 080a152e ef03002a "
                        + " 2c943538 322e3430 204e4f4f 500d0a");

        Ip4 ip = packet.getHeader(new Ip4());
        Tcp tcp = packet.getHeader(new Tcp());

        tcp.destination(80);
        ip.checksum(ip.calculateChecksum());
        tcp.checksum(tcp.calculateChecksum());
        packet.scan(Ethernet.ID);

        System.out.println(packet.toString());
        System.out.println(packet.toHexdump());
    }
}
