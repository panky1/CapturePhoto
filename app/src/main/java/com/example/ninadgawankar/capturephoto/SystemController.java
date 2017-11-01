package com.example.ninadgawankar.capturephoto;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ninad Gawankar on 04/10/2017.
 */

public class SystemController {

    public static String getDate() {
        try {
            Date dNow = new Date();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat ft = new SimpleDateFormat("dd-MMM-yyyy");
            return ft.format(dNow);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTime() {
        try {
            Date dNow = new Date();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat ft = new SimpleDateFormat("dd-MMM-yyyy");
            return ft.format(dNow);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDateTime() {
        try {
            Date dNow = new Date();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat ft = new SimpleDateFormat("dd-MMM-yyyy");
            return ft.format(dNow);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDateTimeProvider() {
        try {
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd_HHmmss");
            return ft.format(dNow);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDateTimeProvider2() {
        try {
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd_HHMMss");
            return ft.format(dNow);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
