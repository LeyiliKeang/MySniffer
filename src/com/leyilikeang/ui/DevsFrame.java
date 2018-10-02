package com.leyilikeang.ui;

import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.format.FormatUtils;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author likang
 * @date 2018/9/8 10:07
 */
public class DevsFrame {

    private JPanel contentPane;
    private JButton useDevButton;
    private JTree devTree;
    private JScrollPane devTreeScrollPane;

    private Window devsFrame;

    private Integer index;

    public DevsFrame(Window devsFrame) {
        this();
        this.devsFrame = devsFrame;
    }

    public DevsFrame() {
        final List<PcapIf> devs = PcapUtils.getAllDevs();
        DefaultMutableTreeNode top = new DefaultMutableTreeNode();
        DefaultTreeModel defaultTreeModel = new DefaultTreeModel(top);
        devTree.setCellRenderer(new DefaultTreeCellRenderer() {

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

        devTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) devTree.getLastSelectedPathComponent();
                if (node.isLeaf()) {
                    index = treeNodeMap.get(node.getParent());
                } else {
                    index = treeNodeMap.get(node);
                }
            }
        });
        Integer i = 0;
        for (PcapIf dev : devs) {
            List<PcapAddr> addrs = dev.getAddresses();
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(dev.getDescription() + i);
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
            String mac = null;
            try {
                mac = FormatUtils.mac(dev.getHardwareAddress()).replace(":", "-");
            } catch (IOException e) {
                e.printStackTrace();
            }
            node.add(new DefaultMutableTreeNode("[MAC:" + mac + "]"));
        }

        devTree.setModel(defaultTreeModel);
        devTreeScrollPane.setViewportView(devTree);

        useDevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (PcapUtils.index == null) {
                    PcapUtils.index = index;
                    PcapUtils.useDev();
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JFrame mainFrame = new JFrame("MySniffer");
                            mainFrame.setContentPane(new MainFrame(mainFrame).getContentPane());
                            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            mainFrame.pack();
                            mainFrame.setLocationRelativeTo(null);
                            devsFrame.setVisible(false);
                            mainFrame.setVisible(true);
                        }
                    });
                } else {
                    PcapUtils.index = index;
                    PcapUtils.useDev();
                    devsFrame.setVisible(false);
                }
            }
        });
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
