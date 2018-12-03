import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CapIP {
    public static void main(String[] args){
        StringBuilder errbuf = new StringBuilder();
        List<PcapIf> ifs = new ArrayList<PcapIf>(); // Will hold list of devices
        int statusCode = Pcap.findAllDevs(ifs, errbuf);
        if (statusCode != Pcap.OK) {
            System.out.println("Error occurred: " + errbuf.toString());
            return;
        }
        else{
            for(int i=0; i<ifs.size(); ++i){
                System.out.println(ifs.get(i).getDescription());//输出所有网络接口的描述
                System.out.println(ifs.get(i).getName());
                System.out.println(ifs.get(i).getFlags());
                try {
                    System.out.println(ifs.get(i).getHardwareAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
