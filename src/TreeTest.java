import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JHeader;
import org.jnetpcap.packet.JHeaderPool;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.packet.format.JFormatter;
import org.jnetpcap.packet.format.TextFormatter;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author likang
 * @date 2018/10/2 19:04
 */
public class TreeTest {

    private JPanel contentPane;
    private JTree tree;
    private JScrollPane scrollPane;

    public TreeTest() {
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("1");
        node1.add(new DefaultMutableTreeNode("likang"));
        node1.add(new DefaultMutableTreeNode("hiwfowjfpow\ndhiuwhwof\n"));

        tree = new JTree(node1);
        scrollPane.setViewportView(tree);
    }

    public TreeTest(PcapPacket packet, List<PcapIf> alldevs, Integer num) {
        if (num == 1) {
            this.devAndPcaketTree(packet, alldevs);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TreeTest");
        frame.setContentPane(new TreeTest().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void devAndPcaketTree(PcapPacket packet, List<PcapIf> alldevs){
        DefaultMutableTreeNode top = new DefaultMutableTreeNode();
        DefaultMutableTreeNode frame = new DefaultMutableTreeNode("Frame");
        top.add(frame);
        DefaultTreeModel defaultTreeModel = new DefaultTreeModel(top);
        tree.setCellRenderer(new DefaultTreeCellRenderer() {

            @Override
            public Icon getClosedIcon() {
                return null;
            }

            @Override
            public Icon getOpenIcon() {
                return null;
            }

            @Override
            public Icon getLeafIcon() {
                return null;
            }
        });

        final Map<DefaultMutableTreeNode, Integer> treeNodeMap = new HashMap<DefaultMutableTreeNode, Integer>();

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (node.isLeaf()) {
                    System.out.println(treeNodeMap.get(node.getParent()));
                } else {
                    System.out.println(treeNodeMap.get(node));
                }
            }
        });

        StringBuilder sb = new StringBuilder();
        JFormatter formatter = new TextFormatter(sb);
        try {
            formatter.packetBefore(packet, JFormatter.Detail.MULTI_LINE_FULL_DETAIL);
            System.out.println(sb);
            String[] strings = sb.toString().split("\n");

            for (int i = 2; i < strings.length; i++) {
                if (i == strings.length - 1) {
                    continue;
                }
                frame.add(new DefaultMutableTreeNode(strings[i]));
            }

            int var3 = packet.getHeaderCount();
            final JHeaderPool headers = new JHeaderPool();
            sb.delete(0, sb.length());


            for (int i = 0; i < var3; ++i) {
                int var5 = packet.getHeaderIdByIndex(i);
                JHeader var6 = headers.getHeader(var5);
                packet.getHeaderByIndex(i, var6);
                if (var6.getLength() != 0) {
                    formatter.format(var6, JFormatter.Detail.MULTI_LINE_FULL_DETAIL);
                }
                System.out.print("------");
                System.out.println(sb);
                System.out.print("------");
                String[] string = sb.toString().split("\n");
                String s = string[1].replace("*******", "\t");
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(string[1]);
                top.add(node);
                for (int j = 3; j < string.length; j++) {
                    if (string[j].isEmpty() || j == string.length - 1 && i != var3 - 1) {
                        continue;
                    }
                    node.add(new DefaultMutableTreeNode(string[j]));
                }
                sb.delete(0, sb.length());
            }

            for (PcapIf dev : alldevs) {
                System.out.println(dev.getAddresses());
                System.out.println(dev.getDescription());
                System.out.println(dev.getName());
                System.out.println(dev.getFlags());
                System.out.println(FormatUtils.mac(dev.getHardwareAddress()));
            }

            Integer i = 0;
            for (PcapIf dev : alldevs) {
                List<PcapAddr> addrs = dev.getAddresses();
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(dev.getDescription());
                treeNodeMap.put(node, i++);
                top.add(node);
                for (PcapAddr addr : addrs) {
                    if (addr.getAddr().toString().startsWith("[INET4") || addr.getAddr().toString().startsWith("[INET6")) {
                        String ip = addr.getAddr().toString().replace("INET4", "Ip4");
                        ip = ip.replace("INET6", "Ip6");
                        node.add(new DefaultMutableTreeNode(ip));
                    }
                    if (addr.getNetmask().toString().startsWith("[INET4") || addr.getNetmask().toString().startsWith("[INET6")) {
                        String mask = addr.getNetmask().toString().replace("INET4", "Mask");
                        mask = mask.replace("INET6", "Mask");
                        node.add(new DefaultMutableTreeNode(mask));
                    }
                    if (addr.getBroadaddr().toString().startsWith("[INET4") || addr.getBroadaddr().toString().startsWith("[INET6")) {
                        String broad = addr.getBroadaddr().toString().replace("INET4", "Broad");
                        broad = broad.replace("INET6", "Broad");
                        node.add(new DefaultMutableTreeNode(broad));
                    }
                }
                String mac = FormatUtils.mac(dev.getHardwareAddress()).replace(":", "-");
                node.add(new DefaultMutableTreeNode("[MAC:" + mac + "]"));
            }

            tree.setModel(defaultTreeModel);
            scrollPane.setViewportView(tree);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JPanel getContentPane() {
        return contentPane;
    }

}
