package com.leyilikeang.common.util;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author likang
 * @date 2018/10/24 20:30
 */
public class MyFilter extends FileFilter {

    private String extName;
    private String description;

    public MyFilter(String extName, String description) {
        this.extName = extName;
        this.description = description;
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        if (this.getExtName(f).equalsIgnoreCase(this.extName)) {
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    private String getExtName(File file) {
        String name = file.getName();
        int index = name.lastIndexOf(".");
        if (index == -1) {
            return "";
        } else {
            return name.substring(index + 1);
        }
    }
}
