package com.leyilikeang.common.util;

/**
 * @author likang
 * @date 2018/9/14 19:52
 */
public class ConstantUtils {

    /**
     * 协议信息
     */
    public static enum Protocol {

        ETH("ETH", "以太帧"),
        LLC("LLC", "逻辑链路控制协议"),
        ARP("ARP", "地址解析协议"),
        ICMP("ICMP", "互联网控制消息协议"),
        IPv4("IPv4", "互联网协议"),
        IPv6("IPv6", "互联网协议第6版"),
        TCP("TCP", "传输控制协议"),
        HTTP("HTTP", "超文本传输协议"),
        UDP("UDP", "用户数据报协议"),
        SIP("SIP", "会话发起协议"),
        SDP("SDP", "会话描述协议");

        private String value;
        private String name;

        private Protocol() {
        }

        private Protocol(String value, String name) {
            this.value = value;
            this.name = name;
        }

        public static String getNameByValue(String value) {
            for (Protocol protocol : Protocol.values()) {
                if (protocol.getValue().equals(value)) {
                    return protocol.getName();
                }
            }
            return null;
        }

        public String getValue() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum Ethernet {
        DESTINATION_MAC(),
        SOURCE_MAC(),
        ETHER_TYPE_ARP("0806", "以太类型-ARP");

        private String value;
        private String name;

        private Ethernet() {
        }

        private Ethernet(String value, String name) {
            this.value = value;
            this.name = name;
        }

        public static String getNameByValue(String value) {
            for (Ethernet ethernet : Ethernet.values()) {
                if (ethernet.getValue().equals(value)) {
                    return ethernet.getName();
                }
            }
            return null;
        }

        public String getValue() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum Arp {
        HARDWARE_TYPE_ETHER("0001", "硬件类型-以太网"),
        UPPER_PROTOCOL_TYPE_IP("0800", "上层协议类型-IP协议"),
        MAC_LENGTH("06", "MAC地址长度"),
        IP_LENGTH("04", "IP地址长度"),
        OPCODE_REQUEST("0001", "操作码-请求包"),
        OPCODE_RESPONSE("0002", "操作码-应答包"),
        SOURCE_MAC(),
        SOURCE_IP(),
        DESTINATION_MAC(),
        DESTINATION_IP();

        private String value;
        private String name;

        private Arp() {
        }

        private Arp(String value, String name) {
            this.value = value;
            this.name = name;
        }

        public static String getNameByValue(String value) {
            for (Arp arp : Arp.values()) {
                if (arp.getValue().equals(value)) {
                    return arp.getName();
                }
            }
            return null;
        }

        public String getValue() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }
    }
}
