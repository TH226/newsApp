package com.example.asus.newsapp.DoMain;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * Created by asus on 2016/8/12.
 */
public class PhotoData {

    public int retcode;
    public PhptosInfo data;

    public class PhptosInfo{
        public String title;
        public ArrayList<PhotoInfo> news;
    }
    public class PhotoInfo{
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }
}
