package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserDevice;
import com.example.demo.repo.UserDeviceRepository;

@Service
public class UserDeviceService {
    @Autowired
    UserDeviceRepository UDRepository;
    public void saveOrUpdate(UserDevice userDevice) {
    	System.out.println( userDevice.getDeviceToken());
    	UDRepository.save(userDevice);
    }
}
