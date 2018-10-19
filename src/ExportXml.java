import jxl.format.Alignment;
import jxl.format.Border;
import jxl.write.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author likang
 * @date 2018/10/18 20:01
 */
public class ExportXml {
    public void writeToExcel(WritableSheet sheet, List<Map<String, Object>> data, List<String> keys,
                             Integer row, Integer column) throws WriteException {
        Integer rowIndex = row;
        Integer columnIndex = column;
        // 记录每列最长列长，保证单元格内容全部都能显示，默认12
        Map<Integer, Integer> columnLengthMap = new HashMap<Integer, Integer>();
        WritableFont font = new WritableFont(WritableFont.createFont("等线"), 11);
        WritableCellFormat format = new WritableCellFormat(font);
        format.setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);
        format.setAlignment(Alignment.CENTRE);
        format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        Integer columnSize = 0;
        Integer rowSize = 0;
        for (Map<String, Object> rowData : data) {
            if (rowData.get("columnSize") != null) {
                columnSize = (Integer) rowData.get("columnSize");
            }
            if (rowData.get("rowSize") != null) {
                rowSize = (Integer) rowData.get("rowSize");
            } else if (rowData.get("firstKeySize") != null) {
                rowSize = (Integer) rowData.get("firstKeySize");
            }
            columnSize = (columnSize == null || columnSize <= 0) ? 1 : columnSize;
            rowSize = (rowSize == null || rowSize <= 0) ? 1 : rowSize;
            for (String key : keys) {
                sheet.mergeCells(columnIndex, rowIndex, columnIndex + columnSize - 1, rowIndex + rowSize - 1);

                if (columnLengthMap.get(columnIndex) == null) {
                    columnLengthMap.put(columnIndex, 12);
                }
                String content = rowData.get(key) == null ? "" : rowData.get(key).toString();
                try {
                    if (content.getBytes("GBK").length > columnLengthMap.get(columnIndex)) {
                        columnLengthMap.put(columnIndex, rowData.get(key).toString().getBytes("GBK").length);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sheet.addCell(new Label(columnIndex, rowIndex, content, format));
                columnIndex += columnSize;
            }
            columnIndex = column;
            rowIndex += rowSize;
        }

        for (Map.Entry<Integer, Integer> entry : columnLengthMap.entrySet()) {
            sheet.setColumnView(entry.getKey(), entry.getValue());
        }
    }
}
