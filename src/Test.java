import java.io.ByteArrayOutputStream;

public class Test {

    public static void main(String[] args) {
        String hexString = "0000:*1c 2c 08 4c  5b 4b 00 0c  29 9d 9c 33  08 00*45 00    .,.L[K..)..3..E.\n" +
                "0010: 00 43 18 d3  40 00 80 06  60 73 c0 a8  00 06 c0 a8    .C..@...`s......\n" +
                "0020: 00 18*00 15  0f b9 f6 fa  8a 26 ce 09  2b 7f 50 18    .........&..+.P.\n" +
                "0030: 01 00 07 e1  00 00*32 32  30 20 4d 69  63 72 6f 73    ......220 Micros\n" +
                "0040: 6f 66 74 20  46 54 50 20  53 65 72 76  69 63 65 0d    oft FTP Service.\n" +
                "0050: 0a                                                    .               \n";
//        System.out.println(hexString);
//        System.out.println(hexString.trim());

        StringBuilder sb = new StringBuilder();

        String[] splitHexString = hexString.trim().split("\\n");
        for (int i = 0; i < splitHexString.length; i++) {
//            System.out.println("split["+i+"]:"+splitHexString[i]);
            String[] splitContent = splitHexString[i].split("    ");
            for (int j = 0; j < splitContent.length; j += 2) {
//                System.out.println(splitContent[j]);
                String[] splitData = splitContent[j].split(":");
                for (int z = 1; z < splitData.length; z += 2) {
//                    System.out.println(splitData[z]);
                    sb.append(splitData[z]);
                }
            }
        }
//        System.out.println(sb);
        String[] p = sb.toString().split("\\*");
//        System.out.println(p.length);
        System.out.println(p[p.length - 1].replace(" ", ""));

        String s = "323230204d6963726f736f66742046545020536572766963650d0a";
        System.out.println(hexStringToString(s));
    }

    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}