package com.leyilikeang.common.example;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.PcapHandler;
import org.jnetpcap.PcapIf;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author likang
 * @date 2018/9/8 15:05
 * <p>
 * 示例：将捕获的数据包写入文件
 */
public class PcapDumperExample {

    public static void main(String[] args) {
        List<PcapIf> alldevs = new ArrayList<PcapIf>();
        StringBuilder errbuf = new StringBuilder();

        /***************************************************************************
         * 获取设备列表
         **************************************************************************/
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s\n",
                    errbuf.toString());
            return;
        }
        PcapIf device = alldevs.get(1); // We know we have atleast 1 device

        /***************************************************************************
         * 打开选中设备
         **************************************************************************/
        int snaplen = 64 * 1024;           // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10 * 1000;           // 10 seconds in millis
        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
        if (pcap == null) {
            System.err.printf("Error while opening device for capture: %s\n",
                    errbuf.toString());
            return;
        }

        /***************************************************************************
         * 创建一个和pcap关联的pcapdumper
         ***************************************************************************/
        String ofile = "tmp-capture-file.cap";
        PcapDumper dumper = pcap.dumpOpen(ofile); // output file

        /***************************************************************************
         * 创建一个接收数据包的handler并且告诉dumper将数据包写入输出文件中
         **************************************************************************/
        PcapHandler<PcapDumper> dumpHandler = new PcapHandler<PcapDumper>() {

            @Override
            public void nextPacket(PcapDumper dumper, long seconds, int useconds,
                                   int caplen, int len, ByteBuffer buffer) {

                dumper.dump(seconds, useconds, caplen, len, buffer);
            }
        };

        /***************************************************************************
         * 告诉loop捕获10个数据包，并传入我们第三步创建的dumper
         **************************************************************************/
        pcap.loop(10, dumpHandler, dumper);

        File file = new File(ofile);
        System.out.printf("%s file has %d bytes in it!\n", ofile, file.length());


        /***************************************************************************
         * 最后dumper和handler都要关闭
         **************************************************************************/
        dumper.close(); // 如果dumper不关闭，那么输出文件是没法删除的
        pcap.close();

        if (file.exists()) {
            file.delete();
            // 删除输出文件，当然你也可以不删除，使用wireshark打开可以看到更多的信息
        }
    }
}
