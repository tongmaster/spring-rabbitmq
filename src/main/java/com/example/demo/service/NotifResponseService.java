package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.NotifResponse;
import com.example.demo.repo.NotifResponseRepository;

@Service
public class NotifResponseService {
    @Autowired
    NotifResponseRepository notifRespRepository;
    
    public void saveOrUpdate(NotifResponse app) {
    	notifRespRepository.save(app);
    }
    
    public List<NotifResponse> getAllNotifResp() {
        List<NotifResponse> apps = new ArrayList<NotifResponse>();
        notifRespRepository.findAll().forEach(app -> apps.add(app));
        return apps;
    }
    
    public NotifResponse getNotifRespById(int id) {
        return notifRespRepository.findById(id).get();
    }
}
