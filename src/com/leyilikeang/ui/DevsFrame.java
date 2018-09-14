package com.leyilikeang.ui;

import com.leyilikeang.common.util.PcapUtils;
import org.jnetpcap.PcapIf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author likang
 * @date 2018/9/8 10:07
 */
public class DevsFrame {

    private JPanel contentPane;
    private JComboBox devsComboBox;
    private JButton useDevButton;

    private Window devsFrame;

    public DevsFrame(Window devsFrame) {
        this();
        this.devsFrame = devsFrame;
    }

    public DevsFrame() {
        final List<PcapIf> devs = PcapUtils.getAllDevs();
        for (PcapIf dev : devs) {
            devsComboBox.addItem(dev.getDescription() + "[" + dev.getName() + "]");
        }
        useDevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (PcapUtils.index == null) {
                    PcapUtils.index = devsComboBox.getSelectedIndex();
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
                    PcapUtils.index = devsComboBox.getSelectedIndex();
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
