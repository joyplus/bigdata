package tv.joyplus.backend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatTool {
	/**
	 * 格式化时间显示
	 * @param format
	 * @param date
	 * @return
	 */
	public static String date(String format, Date date) {
		if(date==null) return "";
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(date);
	}

    public static boolean isEmpty(String string){
        return (string==null||string.trim().length()<=0);
    }

}
