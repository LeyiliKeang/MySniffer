package com.leyilikeang.ui;

import com.leyilikeang.common.packethandler.MyPacketHandler;
import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.Pcap;
import sun.applet.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author likang
 * @date 2019/2/27 12:22
 */
public class FilterFrame {
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JCheckBox arpCheckBox;
    private JCheckBox ipCheckBox;
    private JCheckBox tcpCheckBox;
    private JCheckBox udpCheckBox;
    private JCheckBox httpCheckBox;
    private JTextField sourceIpTextField;
    private JTextField destinationIpTextField;
    private JTextField sourcePortTextField;
    private JTextField destinationPortTextField;
    private JButton startButton;
    private JButton cancelButton;
    private JComboBox expressionComboBox;

    public FilterFrame(final MainFrame mainFrame, final JDialog dialog) {
        expressionComboBox.setSelectedIndex(-1);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.dispose();

                PacketUtils.capClear();
                mainFrame.getDefaultTableModel().setRowCount(0);
                mainFrame.getCaptureService().capture(mainFrame);

                if (tabbedPane.getSelectedIndex() == 0) {
                    doSimpleFilter();
                }

                if (tabbedPane.getSelectedIndex() == 1) {
                    doExpressionFilter();
                }

                final long time = System.currentTimeMillis();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        String str = String.format("%1$tM:%1$tS:%1$1tL", System.currentTimeMillis() - time);
                        mainFrame.getTimeLabel().setText("用时：" + str);
                    }
                };
                TimerTask task1 = new TimerTask() {
                    @Override
                    public void run() {
                        String str = String.format("%.2f", (double) MyPacketHandler.per / 1024);
                        mainFrame.getRateLabel().setText("流量：" + str + "Kb/s");
                        MyPacketHandler.per = 0;
                    }
                };
                // 使用ScheduledExecutorService代替Timer
                Timer timer = new Timer();
                timer.schedule(task, 1, 1);
                timer.schedule(task1, 1, 1000);
                mainFrame.setTimer(timer);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                dialog.setVisible(false);
            }
        });
    }

    private void doSimpleFilter() {
        String sourceIp = sourceIpTextField.getText().trim();
        String sourcePort = sourcePortTextField.getText().trim();
        String sourceExpression = "";
        if (sourceIp.equals("") && !sourcePort.equals("")) {
            sourceExpression = "src port " + sourcePort;
        } else if (!sourceIp.equals("") && sourcePort.equals("")) {
            sourceExpression = "src " + sourceIp;
        } else if (!sourceIp.equals("") && !sourcePort.equals("")) {
            sourceExpression = "src " + sourceIp + " and src port " + sourcePort;
        }

        String destinationIp = destinationIpTextField.getText().trim();
        String destinationPort = destinationPortTextField.getText().trim();
        String destinationExpression = "";
        if (destinationIp.equals("") && !destinationPort.equals("")) {
            destinationExpression = "dst port " + destinationPort;
        } else if (!destinationIp.equals("") && destinationPort.equals("")) {
            destinationExpression = "dst " + destinationIp;
        } else if (!destinationIp.equals("") && !destinationPort.equals("")) {
            destinationExpression = "dst " + destinationIp + " and dst port " + destinationPort;
        }

        StringBuilder protocolExpression = new StringBuilder();
        String protocol = "";
        if (arpCheckBox.isSelected()) {
            protocol = "arp";
            protocolExpression.append(protocol);
            protocol = "";
        }
        if (ipCheckBox.isSelected()) {
            protocol = "ip";
            if (!protocolExpression.toString().equals("")) {
                protocolExpression.append(" and ").append(protocol);
                protocol = "";
            } else {
                protocolExpression.append(protocol);
                protocol = "";
            }
        }
        if (tcpCheckBox.isSelected()) {
            protocol = "tcp";
            if (!protocolExpression.toString().equals("")) {
                protocolExpression.append(" and ").append(protocol);
                protocol = "";
            } else {
                protocolExpression.append(protocol);
                protocol = "";
            }
        }
        if (udpCheckBox.isSelected()) {
            protocol = "udp";
            if (!protocolExpression.toString().equals("")) {
                protocolExpression.append(" and ").append(protocol);
                protocol = "";
            } else {
                protocolExpression.append(protocol);
                protocol = "";
            }
        }
        if (httpCheckBox.isSelected()) {
            protocol = "http";
            if (!protocolExpression.toString().equals("")) {
                protocolExpression.append(" and ").append(protocol);
            } else {
                protocolExpression.append(protocol);
            }
        }
        System.out.println(protocolExpression);

        String expression = "";
        if (sourceExpression.equals("") && !destinationExpression.equals("")) {
            expression = destinationExpression;
        } else if (!sourceExpression.equals("") && destinationExpression.equals("")) {
            expression = sourceExpression;
        } else if (!sourceExpression.equals("") && !destinationExpression.equals("")) {
            expression = sourceExpression + " and " + destinationExpression;
        }
        if (!protocolExpression.toString().equals("")) {
            if (expression.equals("")) {
                expression = protocolExpression.toString();
            } else {
                expression = expression + " and " + protocolExpression.toString();
            }
        }
        System.out.println(expression);
        if (PcapUtils.filter(expression, PcapUtils.pcap)) {
            System.out.println("过滤器加载成功");
        } else {
            System.out.println("过滤器加载失败");
        }
    }

    private void doExpressionFilter() {
        String expression = expressionComboBox.getSelectedItem().toString().trim();
        if (PcapUtils.filter(expression, PcapUtils.pcap)) {
            System.out.println("过滤器加载成功");
        } else {
            System.out.println("过滤器加载失败");
        }
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
