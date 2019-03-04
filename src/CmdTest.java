import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author likang
 * @date 2018/10/1 11:57
 */
public class CmdTest {

    public static void main(String[] args) {
        String cmdStr = "arp -a";
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec(cmdStr);
            InputStream in = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(in, "GBK");
            BufferedReader br = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String message;
            while ((message = br.readLine()) != null) {
                sb.append(message + "\n");
            }
//            System.out.println(sb.toString());
            String str = sb.toString();
            int index = str.indexOf("接口: 192.168.0.7");
            int startIndex = str.indexOf("类型", index + 1);
            int endIndex = str.indexOf("接口", index + 1);
//            System.out.println(index);
//            System.out.println(endIndex);
            String string = str.substring(startIndex + 2, endIndex);
//            System.out.println(string);
            String[] splitString = string.split("\n");
            for (String s : splitString) {
                if (s.startsWith("  192.168.0.") && !s.startsWith("  192.168.0.255")) {
                    String[] s1 = s.split(" ");
                    for (String s2 : s1) {
                        if (s2.equals("")) {
                            continue;
                        }
                        System.out.println(s2);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
