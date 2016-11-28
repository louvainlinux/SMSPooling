package org.kap.louvainlinux.ludovic.smspooling;

import java.util.Calendar;

/**
 * Created by Ludovic Zipsin on 27/11/16.
 * Mail: ludovic.j.r.zipsin@gmail.com
 * Github: https://github.com/LudoZipsin
 */
public class Pool implements Comparable<Pool>{

    protected String name;
    protected String date;

    public Pool(){
        this.name = "";
        this.date = currentTime();
    }

    public Pool(String name){
        this.name = name;
        this.date = currentTime();
    }

    public Pool(String name, String date){
        this.name = name;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String currentTime(){
        Calendar calendar = Calendar.getInstance();
        return Integer.toString(calendar.get(Calendar.YEAR)) + "." +
                Integer.toString(calendar.get(Calendar.MONTH)) + "." +
                Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                Integer.toString(calendar.get(Calendar.MINUTE));
    }

    public int compareTo(Pool pool){
        return (-1)*this.getDate().compareTo(pool.getDate());
    }

}
