package com.leyilikeang.ui;

import com.leyilikeang.common.util.PcapUtils;
import com.sun.deploy.security.DeployNTLMAuthCallback;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import sun.rmi.server.UnicastServerRef;

import javax.security.auth.kerberos.KerberosPrincipal;
import javax.sql.rowset.serial.SQLOutputImpl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

/**
 * @author likang
 * @date 2019/3/6 9:22
 */
public class ArpFraudInfoFrame {
    private JPanel contentPane;
    private JCheckBox forwardCheckBox;
    private JTextField destinationIpTextField;
    private JTextField destinationMacTextField;
    private JTextField usingIpTextField;
    private JTextField usingMacTextField;
    private JLabel infoLabel;
    private JPanel infoPane;
    private JPanel detailsPane;
    private JButton closeButton;

    private static Point location = new Point();

    private Pcap forwardPcap;

    public ArpFraudInfoFrame(JDialog dialog, Pcap pcap, Map<String, String> map) {
        String destinationIp = map.get("destinationIp");
        String destinationMac = map.get("destinationMac");
        String usingIp = map.get("sourceIp");
        String usingMac = map.get("sourceMac");

        destinationIpTextField.setText(destinationIp);
        destinationMacTextField.setText(destinationMac);
        usingIpTextField.setText(usingIp);
        usingMacTextField.setText(usingMac);

        infoLabel.setText("正在ARP欺骗" + destinationIp);
        detailsPane.setVisible(false);
        dialog.setUndecorated(true);

        dialog.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                location.x = e.getX();
                location.y = e.getY();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (detailsPane.isVisible()) {
                    detailsPane.setVisible(false);
                    dialog.pack();
                } else {
                    detailsPane.setVisible(true);
                    dialog.pack();
                }
            }
        });

        dialog.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = dialog.getLocation();
                dialog.setLocation(p.x + e.getX() - location.x, p.y + e.getY() - location.y);
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pcap.close();
                if (forwardPcap != null) {
                    forwardPcap.close();
                }
                dialog.dispose();
            }
        });

        PcapIf device = PcapUtils.alldevs.get(PcapUtils.index);
        int snaplen = 64 * 1024;
        int flags = Pcap.MODE_PROMISCUOUS;
        int timeout = 10 * 1000;
        forwardCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (forwardCheckBox.isSelected()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            forwardPcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, PcapUtils.errbuf);
                            PcapUtils.forward(forwardPcap, destinationIp, destinationMac.replace("-", " "));
                        }
                    }).start();
                } else {
                    forwardPcap.close();
                }
            }
        });
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
