package com.lk;

public class Tools {
    public Tools() {
    }

    public String subString(String hexString) {
        StringBuilder sb = new StringBuilder();
        String[] splitHexString = hexString.trim().split("\\n");
        for (int i = 0; i < splitHexString.length; i++) {
            String[] splitContent = splitHexString[i].split("    ");
            for (int j = 0; j < splitContent.length; j += 2) {
//                if (splitContent[j].trim() != null) {
                    String[] splitData = splitContent[j].split(":");
                    for (int z = 1; z < splitData.length; z += 2) {
                        sb.append(splitData[z]);
                    }
//                }
            }
        }
        String[] p = sb.toString().split("\\*");
        String ftpContent = p[p.length - 1].replace(" ", "");
        return ftpContent;
    }

    public String hexStringToString(String s) {
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
            s = new String(baKeyword, "utf-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}
