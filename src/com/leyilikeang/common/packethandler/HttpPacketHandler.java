package com.leyilikeang.common.packethandler;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;

/**
 * @author likang
 * @date 2018/9/8 12:30
 */
public class HttpPacketHandler<T> implements JPacketHandler<T> {

    @Override
    public void nextPacket(JPacket jPacket, T user) {

    }
}
