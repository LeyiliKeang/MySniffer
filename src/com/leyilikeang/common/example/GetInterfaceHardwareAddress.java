package com.leyilikeang.common.example;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author likang
 * @date 2018/9/8 15:10
 * <p>
 * 示例：获取设备的MAC地址
 */
public class GetInterfaceHardwareAddress {

    public static void main(String[] args) throws IOException {

        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        StringBuilder errbuf = new StringBuilder(); // For any error msgs

        /***************************************************************************
         * 获取设备列表
         **************************************************************************/
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf
                    .toString());
            return;
        }

        /***************************************************************************
         * 获取硬件地址
         **************************************************************************/
        for (final PcapIf i : alldevs) {
            final byte[] mac = i.getHardwareAddress();
            if (mac == null) {
                continue; // 当接口不含有硬件地址的时候
            }
            System.out.printf("%s=%s\n", i.getName(), asString(mac));
        }
    }

    private static String asString(final byte[] mac) {  // 此方法格式化mac地址
        final StringBuilder buf = new StringBuilder();
        for (byte b : mac) {
            if (buf.length() != 0) {
                buf.append(':');
            }
            if (b >= 0 && b < 16) {
                buf.append('0');
            }
            buf.append(Integer.toHexString((b < 0) ? b + 256 : b).toUpperCase());
        }

        return buf.toString();
    }
}
