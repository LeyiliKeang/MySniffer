package com.leyilikeang.common.example.my;

import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import java.util.Date;

/**
 * @author likang
 * @date 2019/2/21 11:57
 */
public class Send {

    public static void main(String[] args) {
        /***************************************************************************
         * 首先创建一个用来表示错误信息的字符串，和文件名字符串
         **************************************************************************/
        final StringBuilder errbuf = new StringBuilder(); // For any error msgs
        final String file = "D://message1.cap";
        System.out.printf("Opening file for reading: %s%n", file);


        /***************************************************************************
         * 用openOffline()方法打开选中的文件
         **************************************************************************/
        Pcap pcap = Pcap.openOffline(file, errbuf);
        PcapUtils.getAllDevs();
        PcapUtils.index = 2;
        PcapUtils.useDev();

        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }

        /***************************************************************************
         * 创建用来接收数据包handler
         **************************************************************************/
        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {

            @Override
            public void nextPacket(PcapPacket packet, String user) {

//                System.out.printf("Received at %s caplen=%-4d len=%-4d %s\n",
//                        new Date(packet.getCaptureHeader().timestampInMillis()),
//                        packet.getCaptureHeader().caplen(), // Length actually captured
//                        packet.getCaptureHeader().wirelen(), // Original length
//                        user // User supplied object
//                );

                System.out.println("sent");
                if (PcapUtils.pcap.sendPacket(packet) != Pcap.OK) {
                    System.out.println(PcapUtils.pcap.getErr());
                }
            }
        };

        /***************************************************************************
         * 抓取10个数据包
         **************************************************************************/
        try {
            pcap.loop(-1, jpacketHandler, "jNetPcap rocks!");
        } finally {
            /***************************************************************************
             * 最后要关闭pcap
             **************************************************************************/
            pcap.close();
        }
    }
}
