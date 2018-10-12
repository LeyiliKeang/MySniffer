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

        ARP("ARP", "地址解析协议"),
        ICMP("ICMP", "互联网控制消息协议"),
        IP("IP", "互联网协议"),
        TCP("TCP", "传输控制协议"),
        UDP("UDP", "用户数据报协议");

        private String value;
        private String name;

        private Protocol() {
        }

        private Protocol(String value, String name) {
            this.value = value;
            this.name = name;
        }

        public static String getNameByValue(String value) {
            for (Protocol t : Protocol.values()) {
                if (t.getValue().equals(value)) {
                    return t.getName();
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
