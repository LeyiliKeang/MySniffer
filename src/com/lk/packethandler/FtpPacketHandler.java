package com.lk.packethandler;

import com.lk.Tools;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class FtpPacketHandler<T> implements PcapPacketHandler<T> {
    private DefaultTableModel defaultTableModel;
    private List<PcapPacket> packetList;
    String sourceIp;
    String sourcePort;
    String destinationIp;
    String destinationPort;

    public FtpPacketHandler(DefaultTableModel defaultTableModel, List<PcapPacket> packetList) {
        this.defaultTableModel = defaultTableModel;
        this.packetList = packetList;
    }

    @Override
    public void nextPacket(final PcapPacket packet, T t) {
        final Tcp tcp = new Tcp();
        final Ip4 ip4 = new Ip4();
        if (packet.hasHeader(tcp) && packet.hasHeader(ip4)) {
            byte[] sipBytes = null;
            if (sourceIp != null) {
                sipBytes = new byte[4];
                String[] sip = sourceIp.split(".");
                for (int j = 0; j < sip.length; j++) {
                    int i = Integer.parseInt(sip[j]);
                    if (i > 127) {
                        sipBytes[j] = (byte) (i - 256);
                    } else {
                        sipBytes[j] = (byte) i;
                    }
                }
            }
            byte[] dipBytes = null;
            if (destinationIp != null) {
                dipBytes = new byte[4];
                String[] dip = destinationIp.split("\\.");
                for (int j = 0; j < dip.length; j++) {
                    int i = Integer.parseInt(dip[j]);
                    if (i > 127) {
                        dipBytes[j] = (byte) (i - 256);
                    } else {
                        dipBytes[j] = (byte) i;
                    }
                }
            }

            if (sipBytes != null && dipBytes != null) {
                if ((Arrays.equals(ip4.source(), sipBytes) && Arrays.equals(ip4.destination(), dipBytes))
                        || (Arrays.equals(ip4.destination(), sipBytes) && Arrays.equals(ip4.source(), dipBytes))) {
                    System.out.println(packet.toHexdump());
                    System.out.println(packet.toString());
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            packetList.add(packet);
                            StringBuilder source = new StringBuilder();
                            StringBuilder destination = new StringBuilder();
                            for (int i = 0; i < 4; i++) {
                                int temp;
                                if (ip4.source()[i] < 0) {
                                    temp = 256 + ip4.source()[i];
                                } else {
                                    temp = ip4.source()[i];
                                }
                                if (i != 3) {
                                    source.append(temp).append(".");
                                } else {
                                    source.append(temp);
                                }
                            }
                            for (int i = 0; i < 4; i++) {
                                int temp;
                                if (ip4.destination()[i] < 0) {
                                    temp = 256 + ip4.destination()[i];
                                } else {
                                    temp = ip4.destination()[i];
                                }
                                if (i != 3) {
                                    destination.append(temp).append(".");
                                } else {
                                    destination.append(temp);
                                }
                            }
                            String protocol = "tcp";
                            String ftpHexContent = new Tools().subString(packet.toHexdump());
                            if (ftpHexContent.trim().endsWith("0d0a")) {
                                protocol = "ftp";
                                String ftpContent = new Tools().hexStringToString(ftpHexContent.trim());
                                defaultTableModel.addRow(new Object[]{packetList.size(), source, destination, tcp.source(), tcp.destination(), protocol, packet.getPacketWirelen(), ftpContent});
                            } else {
                                defaultTableModel.addRow(new Object[]{packetList.size(), source, destination, tcp.source(), tcp.destination(), protocol, packet.getPacketWirelen()});
                            }
                        }
                    });
                }
            } else {
                System.out.println(packet.toHexdump());
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        packetList.add(packet);
                        StringBuilder source = new StringBuilder();
                        StringBuilder destination = new StringBuilder();
                        for (int i = 0; i < 4; i++) {
                            int temp;
                            if (ip4.source()[i] < 0) {
                                temp = 256 + ip4.source()[i];
                            } else {
                                temp = ip4.source()[i];
                            }
                            if (i != 3) {
                                source.append(temp).append(".");
                            } else {
                                source.append(temp);
                            }
                        }
                        for (int i = 0; i < 4; i++) {
                            int temp;
                            if (ip4.destination()[i] < 0) {
                                temp = 256 + ip4.destination()[i];
                            } else {
                                temp = ip4.destination()[i];
                            }
                            if (i != 3) {
                                destination.append(temp).append(".");
                            } else {
                                destination.append(temp);
                            }
                        }
                        String protocol = "tcp";
                        String ftpHexContent = new Tools().subString(packet.toHexdump());
                        if (ftpHexContent.endsWith("0d0a")) {
                            protocol = "ftp";
                            String ftpContent = new Tools().hexStringToString(ftpHexContent.trim());
                            defaultTableModel.addRow(new Object[]{packetList.size(), source, destination, tcp.source(), tcp.destination(), protocol, packet.getPacketWirelen(), ftpContent});
                        } else {
                            defaultTableModel.addRow(new Object[]{packetList.size(), source, destination, tcp.source(), tcp.destination(), protocol, packet.getPacketWirelen()});
                        }
                    }
                });
            }
        }
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }
}
