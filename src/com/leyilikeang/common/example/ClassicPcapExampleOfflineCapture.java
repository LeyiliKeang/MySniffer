package com.leyilikeang.common.example;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import java.util.Date;

/**
 * @author likang
 * @date 2018/9/8 15:04
 * <p>
 * 示例：从离线文件捕获数据包
 */
public class ClassicPcapExampleOfflineCapture {

    public static void main(String[] args) {
        /***************************************************************************
         * 首先创建一个用来表示错误信息的字符串，和文件名字符串
         **************************************************************************/
        final StringBuilder errbuf = new StringBuilder(); // For any error msgs
        final String file = "tests/test-l2tp.pcap";

        System.out.printf("Opening file for reading: %s%n", file);

        /***************************************************************************
         * 用openOffline()方法打开选中的文件
         **************************************************************************/
        Pcap pcap = Pcap.openOffline(file, errbuf);

        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }

        /***************************************************************************
         * 创建用来接收数据包handler
         **************************************************************************/
        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {

            public void nextPacket(PcapPacket packet, String user) {

                System.out.printf("Received at %s caplen=%-4d len=%-4d %s\n",
                        new Date(packet.getCaptureHeader().timestampInMillis()),
                        packet.getCaptureHeader().caplen(), // Length actually captured
                        packet.getCaptureHeader().wirelen(), // Original length
                        user // User supplied object
                );
            }
        };

        /***************************************************************************
         * 抓取10个数据包
         **************************************************************************/
        try {
            pcap.loop(10, jpacketHandler, "jNetPcap rocks!");
        } finally {
            /***************************************************************************
             * 最后要关闭pcap
             **************************************************************************/
            pcap.close();
        }
    }
}
