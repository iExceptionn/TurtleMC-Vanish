package me.flame.vanish.donators.utils;

import me.flame.vanish.utils.FileManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDaysUtils {

    public static Date addDays(){
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(calendar.DATE, FileManager.get("donator.yml").getInt("config.prefix-cooldown"));

        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.printf(sf.format(calendar.getTime()));

        return calendar.getTime();
    }
}
