package com.leyilikeang.common.packethandler;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.protocol.network.Arp;

/**
 * @author likang
 * @date 2018/9/16 12:33
 */
public class ArpPacketHandler<T> implements JPacketHandler<T> {

    private Arp arp = new Arp();

    @Override
    public void nextPacket(JPacket jPacket, T user) {

    }
}
