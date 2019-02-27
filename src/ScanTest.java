import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author likang
 * @date 2018/10/1 11:59
 */
public class ScanTest {

    private static int i;

    public static void main(String[] args) {
        final String ip = "192.168.0.";
        for (i = 1; i < 255; i++) {
            new Thread(new Runnable() {
                final int j = i;
                @Override
                public void run() {
                    String host = ip + j;
                    try {
                        InetAddress address = InetAddress.getByName(host);
                        if (address.isReachable(1500)) {
                            System.out.println(host);
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
