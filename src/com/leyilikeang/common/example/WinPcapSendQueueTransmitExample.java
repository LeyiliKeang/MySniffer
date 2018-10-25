package com.leyilikeang.common.example;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapPktHdr;
import org.jnetpcap.winpcap.WinPcap;
import org.jnetpcap.winpcap.WinPcapSendQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author likang
 * @date 2018/10/25 10:21
 * <p>
 * 示例：发送数据包队列
 */
public class WinPcapSendQueueTransmitExample {

    public static void main(String[] args) {
        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        StringBuilder errbuf = new StringBuilder(); // For any error msgs

        /***************************************************************************
         * 首先获取设备列表
         **************************************************************************/
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
            return;
        }
        PcapIf device = alldevs.get(0); // We know we have atleast 1 device

        /***************************************************************************
         * 打开一个接口
         **************************************************************************/
        int snaplen = 64 * 1024; // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10 * 1000; // 10 seconds in millis
        WinPcap pcap = WinPcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

        /***************************************************************************
         * 创建一个数据包队列
         **************************************************************************/
        WinPcapSendQueue queue = WinPcap.sendQueueAlloc(512);
        PcapPktHdr hdr = new PcapPktHdr(128, 128);
        byte[] pkt = new byte[128];

        Arrays.fill(pkt, (byte) 255); // Broadcast
        queue.queue(hdr, pkt); // Packet #1
        queue.queue(hdr, pkt); // Packet #2

        Arrays.fill(pkt, (byte) 0x11); // Junk packet
        queue.queue(hdr, pkt); // Packet #3

        /***************************************************************************
         * 发送我们的数据包队列
         **************************************************************************/
        r = pcap.sendQueueTransmit(queue, WinPcap.TRANSMIT_SYNCH_ASAP);
        if (r != queue.getLen()) {
            System.err.println(pcap.getErr());
            return;
        }
        /***************************************************************************
         * 最后关闭
         **************************************************************************/
        pcap.close();
    }
}
