import org.jnetpcap.Pcap;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UnknownFormatConversionException;

public class ClassicPcapExample {
    public static void main(String[] args) {
        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        StringBuilder errbuf = new StringBuilder(); // For any error msgs

        /***************************************************************************
         * First get a list of devices on this system
         **************************************************************************/
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf
                    .toString());
            return;
        }

        System.out.println("Network devices found:");

        int i = 0;
        for (PcapIf device : alldevs) {
            String description =
                    (device.getDescription() != null) ? device.getDescription()
                            : "No description available";
            System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
        }

        PcapIf device = alldevs.get(0); // We know we have atleast 1 device
        System.out
                .printf("\nChoosing '%s' on your behalf:\n",
                        (device.getDescription() != null) ? device.getDescription()
                                : device.getName());

        /***************************************************************************
         * Second we open up the selected device
         **************************************************************************/
        int snaplen = 64 * 1024;           // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10 * 1000;           // 10 seconds in millis
        final Pcap pcap =
                Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }

        /***************************************************************************
         * Third we create a packet handler which will receive packets from the
         * libpcap loop.
         **************************************************************************/
        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {

            public void nextPacket(PcapPacket packet, String user) {
//                Http http = new Http();
//                Tcp tcp = new Tcp();
//                Udp udp = new Udp();
//                Ip4 ip4 = new Ip4();
//                Icmp icmp = new Icmp();
//                Ip6 ip6 = new Ip6();
//                if (packet.hasHeader(tcp) && packet.hasHeader(ip4)) {
//                    if (((ip4.source()[0] == -64 && ip4.source()[1] == -88 && ip4.source()[2] == 0 && ip4.source()[3] == 6) && (ip4.destination()[0] == -64 && ip4.destination()[1] == -88 && ip4.destination()[2] == 0 && ip4.destination()[3] == 24)) ||
//                            ((ip4.source()[0] == -64 && ip4.source()[1] == -88 && ip4.source()[2] == 0 && ip4.source()[3] == 24) && (ip4.destination()[0] == -64 && ip4.destination()[1] == -88 && ip4.destination()[2] == 0 && ip4.destination()[3] == 6))) {
////                        System.out.printf("Received packet at %s caplen=%-4d len=%-4d %s\n",
////                                new Date(packet.getCaptureHeader().timestampInMillis()),
////                                packet.getCaptureHeader().caplen(),  // Length actually captured
////                                packet.getCaptureHeader().wirelen(), // Original length
////                                user                                 // User supplied object
////                        );
//                        if (packet.toHexdump().contains("USER") || packet.toHexdump().contains("PASS")) {
//                            System.out.println(packet.toHexdump());
//                            String content = packet.toHexdump();
//                            String[] s = content.split("\\*");
//                            System.out.println(s[s.length - 1]);
//                        }
//                    }
//                }
                System.out.println(packet.toString());
                Ip4 ip4 = new Ip4();
                if (packet.hasHeader(ip4)) {
                    System.out.printf("IP.version = %d\n", ip4.version());
                    System.out.printf("%s -> %s\n", FormatUtils.ip(ip4.source()), FormatUtils.ip(ip4.destination()));
                }
            }
        };

        /***************************************************************************
         * Fourth we enter the loop and tell it to capture 10 packets. The loop
         * method does a mapping of pcap.datalink() DLT value to JProtocol ID, which
         * is needed by JScanner. The scanner scans the packet buffer and decodes
         * the headers. The mapping is done automatically, although a variation on
         * the loop method exists that allows the programmer to sepecify exactly
         * which protocol ID to use as the data link type for this pcap interface.
         **************************************************************************/
        pcap.loop(2, jpacketHandler, "jNetPcap rocks!");

        /***************************************************************************
         * Last thing to do is close the pcap handle
         **************************************************************************/
        pcap.close();
    }
}
