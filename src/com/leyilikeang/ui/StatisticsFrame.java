package com.leyilikeang.ui;

import com.leyilikeang.common.util.PacketUtils;
import sun.applet.Main;
import sun.plugin2.message.EventMessage;

import javax.sql.rowset.JdbcRowSet;
import javax.swing.*;
import java.awt.*;

/**
 * @author likang
 * @date 2019/3/3 19:33
 */
public class StatisticsFrame {
    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JLabel arpLabel;
    private JLabel ipv4Label;
    private JLabel tcpLabel;
    private JLabel httpLabel;
    private JLabel dnsLabel;
    private JLabel udpLabel;
    private JLabel ipv6Label;
    private JLabel icmpLabel;

    public StatisticsFrame(MainFrame mainFrame) {
        mainFrame.setStatisticsFrame(this);
    }

    public void reset() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                arpLabel.setText("ARP：0");
                icmpLabel.setText("ICMP：0");
                ipv4Label.setText("IPv4：0");
                ipv6Label.setText("IPv6：0");
                tcpLabel.setText("TCP：0");
                udpLabel.setText("UDP：0");
                httpLabel.setText("HTTP：0");
                dnsLabel.setText("DNS：0");
            }
        });
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public JLabel getArpLabel() {
        return arpLabel;
    }

    public JLabel getIpv4Label() {
        return ipv4Label;
    }

    public JLabel getTcpLabel() {
        return tcpLabel;
    }

    public JLabel getHttpLabel() {
        return httpLabel;
    }

    public JLabel getDnsLabel() {
        return dnsLabel;
    }

    public JLabel getUdpLabel() {
        return udpLabel;
    }

    public JLabel getIpv6Label() {
        return ipv6Label;
    }

    public JLabel getIcmpLabel() {
        return icmpLabel;
    }
}
