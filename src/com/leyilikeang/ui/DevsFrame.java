package com.leyilikeang.ui;

import com.leyilikeang.common.util.FileUtils;
import com.leyilikeang.common.util.PcapUtils;
import com.leyilikeang.service.FileService;
import com.sun.org.apache.xml.internal.resolver.readers.TR9401CatalogReader;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetstream.protocol.ProtocolInfo;
import sun.nio.cs.ext.ISCII91;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author likang
 * @date 2018/9/8 10:07
 */
public class DevsFrame {

    private JPanel contentPane;
    private JButton useDevButton;
    private JTree devTree;
    private JScrollPane devTreeScrollPane;
    private JList recentList;
    private JButton openButton;
    private JScrollPane listScrollPane;
    private JButton clearButton;

    private DefaultListModel defaultListModel = new DefaultListModel();
    private DefaultMutableTreeNode top = new DefaultMutableTreeNode();
    private DefaultTreeModel defaultTreeModel = new DefaultTreeModel(top);

    private Window devsFrame;
    private MainFrame mainFrame;

    private Integer index;

    public DevsFrame(Window devsFrame) {
        this();
        this.devsFrame = devsFrame;
    }

    public DevsFrame() {
        recentList.setModel(defaultListModel);
        listScrollPane.setViewportView(recentList);
        recent();

        final List<PcapIf> devs = PcapUtils.getAllDevs();
        DefaultMutableTreeNode top = new DefaultMutableTreeNode();
        final DefaultTreeModel defaultTreeModel = new DefaultTreeModel(top);
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
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            treeNodeMap.put(node, i);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            for (PcapAddr addr : addrs) {
                if (addr.getAddr().toString().startsWith("[INET6")) {
                    continue;
                }
                if (addr.getAddr().toString().startsWith("[INET4")) {
                    String ip = addr.getAddr().toString().replace("INET4", "Ip4");
                    hashMap.put("IP", ip.substring(5, ip.length() - 1));
                    node.add(new DefaultMutableTreeNode(ip));
                }
                if (addr.getNetmask().toString().startsWith("[INET4")) {
                    String mask = addr.getNetmask().toString().replace("INET4", "Mask");
                    node.add(new DefaultMutableTreeNode(mask));
                }
                if (addr.getBroadaddr().toString().startsWith("[INET4")) {
                    String broad = addr.getBroadaddr().toString().replace("INET4", "Broad");
                    node.add(new DefaultMutableTreeNode(broad));
                }
            }
            String mac = null;
            try {
                mac = FormatUtils.mac(dev.getHardwareAddress()).replace(":", "-");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String description = dev.getDescription();
            if (description.equalsIgnoreCase("VMware Virtual Ethernet Adapter")) {
                description += " VMnet" + mac.substring(mac.length() - 1);
            }
            if (!node.isLeaf()) {
                node.setUserObject(description);
                top.add(node);
            }
            node.add(new DefaultMutableTreeNode("[MAC:" + mac + "]"));
            hashMap.put("MAC", mac);
            PcapUtils.ipMacMap.put(i++, hashMap);
        }

        devTree.setModel(defaultTreeModel);
        devTreeScrollPane.setViewportView(devTree);

        useDevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PcapUtils.index = index;
                PcapUtils.useDev();
                mainFrame.ready();
                mainFrame.getCaptureButton().setEnabled(true);
                if (JumpToFrame.isOpen) {
                    mainFrame.getJumpToDialog().setVisible(true);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new FileUtils().writeRecent("", true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                recent();
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileUtils.openFile = (String) recentList.getSelectedValue();
                try {
                    FileUtils.openFile = FileUtils.openFile.substring(0, FileUtils.openFile.lastIndexOf("("));
                    new FileUtils().writeRecent(FileUtils.openFile, false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                File file = new File(FileUtils.openFile);
                if (file.exists()) {
                    FileService fileService = new FileService();
                    fileService.open(mainFrame, "");
                } else {
                    JOptionPane.showMessageDialog(mainFrame.getMainFrame(),
                            "文件已不存在", "提示", JOptionPane.WARNING_MESSAGE);
                    try {
                        new FileUtils().deleteRecent(FileUtils.openFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    recent();
                }
            }
        });
    }

    public void recent() {
        defaultListModel.removeAllElements();
        try {
            ArrayList<String> arrayList = new FileUtils().readRecent();
            for (int i = arrayList.size() - 1; i >= 0; i--) {
                String path = arrayList.get(i);
                File file = new File(path);
                if (file.exists()) {
                    String end = "";
                    long size = file.length();
                    if (size < Math.pow(2, 11)) {
                        end = " Bytes";
                    } else if (size < Math.pow(2, 20)) {
                        size /= Math.pow(2, 10);
                        end = " KB";
                    } else if (size < Math.pow(2, 30)) {
                        size /= Math.pow(2, 20);
                        end = " MB";
                    } else if (size < Math.pow(2, 40)) {
                        size /= Math.pow(2, 30);
                        end = " GB";
                    }
                    path += "(" + Long.toString(size) + end + ")";
                } else {
                    path += "(未找到)";
                }
                defaultListModel.addElement(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshDevs() {
//        final List<PcapIf> devs = PcapUtils.getAllDevs();
//        DefaultMutableTreeNode top = new DefaultMutableTreeNode();
//        DefaultTreeModel defaultTreeModel = new DefaultTreeModel(top);
//        devTree.setCellRenderer(new DefaultTreeCellRenderer() {
//
//            @Override
//            public Icon getClosedIcon() {
//                return null;
//            }
//
//            @Override
//            public Icon getOpenIcon() {
//                return null;
//            }
//
//            @Override
//            public Icon getLeafIcon() {
//                return null;
//            }
//        });
//
//        final Map<DefaultMutableTreeNode, Integer> treeNodeMap = new HashMap<DefaultMutableTreeNode, Integer>();
//
//        devTree.addTreeSelectionListener(new TreeSelectionListener() {
//            @Override
//            public void valueChanged(TreeSelectionEvent e) {
//
//                DefaultMutableTreeNode node = (DefaultMutableTreeNode) devTree.getLastSelectedPathComponent();
//                if (node.isLeaf()) {
//                    index = treeNodeMap.get(node.getParent());
//                } else {
//                    index = treeNodeMap.get(node);
//                }
//            }
//        });
//        Integer i = 0;
//        for (PcapIf dev : devs) {
//            List<PcapAddr> addrs = dev.getAddresses();
//            DefaultMutableTreeNode node = new DefaultMutableTreeNode(dev.getDescription());
//            treeNodeMap.put(node, i++);
//            top.add(node);
//            for (PcapAddr addr : addrs) {
//                if (addr.getAddr().toString().startsWith("[INET4")
//                        || addr.getAddr().toString().startsWith("[INET6")) {
//                    String ip = addr.getAddr().toString().replace("INET4", "Ip4");
//                    ip = ip.replace("INET6", "Ip6");
//                    node.add(new DefaultMutableTreeNode(ip));
//                }
//                if (addr.getNetmask().toString().startsWith("[INET4")
//                        || addr.getNetmask().toString().startsWith("[INET6")) {
//                    String mask = addr.getNetmask().toString().replace("INET4", "Mask");
//                    mask = mask.replace("INET6", "Mask");
//                    node.add(new DefaultMutableTreeNode(mask));
//                }
//                if (addr.getBroadaddr().toString().startsWith("[INET4")
//                        || addr.getBroadaddr().toString().startsWith("[INET6")) {
//                    String broad = addr.getBroadaddr().toString().replace("INET4", "Broad");
//                    broad = broad.replace("INET6", "Broad");
//                    node.add(new DefaultMutableTreeNode(broad));
//                }
//            }
//            String mac = null;
//            try {
//                mac = FormatUtils.mac(dev.getHardwareAddress()).replace(":", "-");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            node.add(new DefaultMutableTreeNode("[MAC:" + mac + "]"));
//        }
//
//        devTree.setModel(defaultTreeModel);
//        devTreeScrollPane.setViewportView(devTree);
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
}
