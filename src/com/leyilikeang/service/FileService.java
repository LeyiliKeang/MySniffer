package com.leyilikeang.service;

import com.leyilikeang.common.packethandler.MyPacketHandler;
import com.leyilikeang.common.util.FileUtils;
import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.common.util.PcapUtils;
import com.leyilikeang.ui.MainFrame;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.packet.PcapPacket;
import org.jnetstream.capture.CapturePacket;
import sun.applet.Main;

import javax.swing.*;
import java.io.File;

/**
 * @author likang
 * @date 2018/10/26 14:10
 */
public class FileService {

    public void save(String path) {
        Pcap pcap = PcapUtils.saveOffline();
        PcapDumper dumper = pcap.dumpOpen(path);
        for (PcapPacket packet : PacketUtils.allPackets) {
            dumper.dump(packet);
        }
        File file = new File(path);
        dumper.close();
        pcap.close();
    }

    // TODO : 打开时检查是否有过滤，赋值expression
    public void open(MainFrame mainFrame, String expression) {
        if (MainFrame.isDevs) {
            mainFrame.ready();
        }
        final Pcap pcap = PcapUtils.readOffline(FileUtils.openFile);
        if (!expression.equals("")) {
            if (PcapUtils.filter(expression, pcap)) {
                System.out.println("筛选器加载成功");
                MainFrame.isFilter = true;
            } else {
                System.out.println("筛选器加载失败");
                JOptionPane.showMessageDialog(mainFrame.getContentPane(), "筛选器加载失败", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        mainFrame.getDefaultTableModel().setRowCount(0);
        PacketUtils.capClear();
        if (MainFrame.isFilter) {
            mainFrame.getApplyButton().setText("取消");
            mainFrame.getExpressionComboBox().setEnabled(false);
        }
        final MyPacketHandler packetHandler = new MyPacketHandler(mainFrame);
        new Thread(new Runnable() {
            @Override
            public void run() {
                pcap.loop(-1, packetHandler, "likang");
                pcap.close();
                PacketUtils.protocolType = null;
            }
        }).start();
    }
}
