package com.leyilikeang.ui;

import com.leyilikeang.common.util.FileUtils;
import com.leyilikeang.common.util.MyFileFilter;
import com.leyilikeang.common.util.PacketUtils;
import com.leyilikeang.service.FileService;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import com.sun.xml.internal.fastinfoset.tools.StAX2SAXReader;
import javafx.scene.control.Tab;
import jdk.nashorn.internal.scripts.JD;
import sun.security.krb5.internal.PAForUserEnc;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PipedReader;

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

    public PacketFrame(final MainFrame mainFrame, final JDialog dialog) {
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
                            fileService.save(path, false);
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
