package com.leyilikeang.common.util;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author likang
 * @date 2018/9/7 22:19
 */
public class PcapUtils {

    public static Pcap pcap;
    public static Integer index;
    private static List<PcapIf> alldevs;
    private static StringBuilder errbuf = new StringBuilder();

    public static List<PcapIf> getAllDevs() {
        alldevs = new ArrayList<PcapIf>();

        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
            return null;
        }

        return alldevs;
    }

    public static void useDev() {
        PcapIf device = alldevs.get(index);

        int snaplen = 64 * 1024;
        int flags = Pcap.MODE_PROMISCUOUS;
        int timeout = 10 * 1000;
        pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }
    }

    public static boolean filter(String expression) {
        PcapBpfProgram filter = new PcapBpfProgram();
        int optimize = 0;
        int netmask = 0;
        if (expression.equalsIgnoreCase(ConstantUtils.Protocol.LLC.getValue())) {
            PacketUtils.protocolType = ConstantUtils.Protocol.LLC.getValue();
            return true;
        } else if (expression.equalsIgnoreCase(ConstantUtils.Protocol.HTTP.getValue())) {
            PacketUtils.protocolType = ConstantUtils.Protocol.HTTP.getValue();
            return true;
        } else if (expression.equalsIgnoreCase(ConstantUtils.Protocol.SIP.getValue())) {
            PacketUtils.protocolType = ConstantUtils.Protocol.SIP.getValue();
            return true;
        } else if (expression.equalsIgnoreCase(ConstantUtils.Protocol.SDP.getValue())) {
            PacketUtils.protocolType = ConstantUtils.Protocol.SDP.getValue();
            return true;
        } else if (expression.equalsIgnoreCase(ConstantUtils.Protocol.DNS.getValue())) {
            expression = "port 53";
        }
        int r = pcap.compile(filter, expression, optimize, netmask);
        if (r != Pcap.OK) {
            return false;
        }
        pcap.setFilter(filter);
        return true;
    }

    public static Pcap readOffline(String file) {
        Pcap offlinePcap = Pcap.openOffline(file, errbuf);
        if (offlinePcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
        }
        return offlinePcap;
    }

    public static Pcap saveOffline() {
        PcapIf device = alldevs.get(index);
        int snaplen = 64 * 1024;
        int flags = Pcap.MODE_PROMISCUOUS;
        int timeout = 10 * 1000;
        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return null;
        }
        return pcap;
    }
}
