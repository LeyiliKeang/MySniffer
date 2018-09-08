package com.leyilikeang.common.example;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author likang
 * @date 2018/9/7 22:05
 */
public class ClassicPcapExample {

    public static void main(String[] args) {
        List<PcapIf> alldevs = new ArrayList<PcapIf>();
        StringBuilder errbuf = new StringBuilder();

        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
            return;
        }

        System.out.println("Network devices found:");

        int i = 0;
        for (PcapIf device : alldevs) {
            String description =
                    (device.getDescription() != null) ? device.getDescription() : "No description available";
            System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
        }

        PcapIf device = alldevs.get(5);
        System.out.printf("\nChoosing '%s' on your behalf:\n",
                (device.getDescription() != null) ? device.getDescription()
                        : device.getName());


        int snaplen = 64 * 1024;
        int flags = Pcap.MODE_PROMISCUOUS;
        int timeout = 10 * 1000;
        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }

        PcapPacketHandler packetHandler = new PcapPacketHandler() {
            @Override
            public void nextPacket(PcapPacket pcapPacket, Object o) {
                System.out.printf("Received packet at %s caplen = %-4d len = %-4d %s\n",
                        new Date(pcapPacket.getCaptureHeader().timestampInMillis()),
                        pcapPacket.getCaptureHeader().wirelen(),
                        pcapPacket.getCaptureHeader().wirelen(),
                        o);
            }
        };

        pcap.loop(-1, packetHandler, "jNetPcap rocks!");

        pcap.close();
    }
}
