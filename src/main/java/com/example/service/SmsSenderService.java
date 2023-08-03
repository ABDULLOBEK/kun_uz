package com.example.service;

import com.example.util.RandomUtil;
import org.springframework.stereotype.Service;

@Service
public class SmsSenderService {

    public void send(String phone, String message){

    }

    public void sendRegistrationSms(String phone){
        String smsCode = RandomUtil.getRandomSmsCode();
        String text = "Kun uz sms: \n"+ smsCode;
//        sendMessange(phone, text, smsCode);
    }
}
