package com.example.util;

import com.example.exp.TimeRunOutException;

public class EmailUtil {
    public static final int tokenLiveTime = 1000*60; // 1 min

    public static Boolean timeController(Long sendMillis){
        if(sendMillis+tokenLiveTime<System.currentTimeMillis()){
            throw new TimeRunOutException("Time run out");
        }
        return true;
    }
}
