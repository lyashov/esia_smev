package com.rtk.mdm.esia.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getDateNow(){
        //2020.10.08 22:30:52 +0300
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss Z");
        return sdf.format(new Date());
    }
}
