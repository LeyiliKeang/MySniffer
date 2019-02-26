package com.leyilikeang.common.example;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;

/**
 * @author likang
 * @date 2018/9/8 15:11
 * <p>
 * 示例：遍历pcap并输出数据包header信息
 */
public class OutputHeaderInfo {

    public static void main(String[] args) {
        final String FILE_NAME = "tmp-capture-file.cap";
        StringBuilder errbuf = new StringBuilder(); // For any error msgs

        /***************************************************************************
         * 打开文件
         **************************************************************************/
        Pcap pcap = Pcap.openOffline(FILE_NAME, errbuf);

        if (pcap == null) {
            System.err.printf("Error while opening file for capture: "
                    + errbuf.toString());
            return;
        }

        /***************************************************************************
         *创建一些再loop中会使用和重用的对象
         **************************************************************************/
        Ip4 ip = new Ip4();
        Ethernet eth = new Ethernet();
        PcapHeader hdr = new PcapHeader(JMemory.POINTER);
        JBuffer buf = new JBuffer(JMemory.POINTER);

        /***************************************************************************
         * 我们必须将pcap’s data-link-type 映射到 jnetPcap‘s 协议id，scanner需要这个id数据用来判断packet的第一个header是什么
         **************************************************************************/
        int id = JRegistry.mapDLTToId(pcap.datalink());
        // JRegistry 是协议的注册表，包括它们的类，运行时id，和相关的绑定，这个全局的注册表包括 绑定表，header scanner表和每个header的数字化id表。同时也提供一些查找和转化功能，比如吧header class 映射为 数字化id

        /***************************************************************************
         * 我们同步header 和 buffer 不是copy的，而是如同指针的
         **************************************************************************/
        while (pcap.nextEx(hdr, buf) == Pcap.NEXT_EX_OK) {

            /*************************************************************************
             * 我们吧header和buffer复制（指向）到新的packet对象中
             ************************************************************************/
            PcapPacket packet = new PcapPacket(hdr, buf);

            /*************************************************************************
             * 扫描packet
             ************************************************************************/
            packet.scan(id);

            /*
             * 使用 格式化工具吧源数据变为容易看懂的数据
             */
            if (packet.hasHeader(eth)) {  // 如果packet有ether头部
                String str = FormatUtils.mac(eth.source());
                System.out.printf("#%d: eth.src=%s\n", packet.getFrameNumber(), str);
            }
            if (packet.hasHeader(ip)) {  // 如果packet有ip头部
                String str = FormatUtils.ip(ip.source());
                System.out.printf("#%d: ip.src=%s\n", packet.getFrameNumber(), str);
                // getFrameNumber()是帧号
            }
        }

        /*************************************************************************
         * 关闭pcap
         ************************************************************************/
        pcap.close();
    }
}
