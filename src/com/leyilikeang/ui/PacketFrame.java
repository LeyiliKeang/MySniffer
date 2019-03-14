package com.leyilikeang.ui;

import com.leyilikeang.common.util.MyFileFilter;
import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.service.FileService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author likang
 * @date 2019/3/3 14:17
 */
public class PacketFrame {
    private JPanel contentPane;
    private JTable packetTable;
    private JScrollPane packetTableScrollPane;
    private JButton saveButton;
    private JButton cancelButton;

    public PacketFrame(MainFrame mainFrame, JDialog dialog) {
        packetTable.setModel(mainFrame.getDefaultTableModel());
        packetTableScrollPane.setViewportView(packetTable);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int[] count = packetTable.getSelectedRows();
                        JFileChooser fileChooser = new JFileChooser();
//                fileChooser.setCurrentDirectory(new File("."));
                        fileChooser.setSelectedFile(new File("packet.cap"));
                        fileChooser.setMultiSelectionEnabled(false);
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fileChooser.setFileHidingEnabled(true);
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        fileChooser.setFileFilter(new MyFileFilter("cap", "MySniffer(*.cap)"));
                        int result = fileChooser.showSaveDialog(mainFrame.getContentPane());
                        if (JFileChooser.APPROVE_OPTION == result) {
                            for (int index : count) {
                                PacketUtils.savePackets.add(PacketUtils.allPackets.get(index));
                            }
                            String path = fileChooser.getSelectedFile().getPath();
                            FileService fileService = new FileService();
                            fileService.save(path, false);    // 将数据包文件保存到选定路径
                            dialog.dispose();
                        }
                    }
                });
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
