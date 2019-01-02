package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Notif;
import com.example.demo.repo.NotifLogRepository;

@Service
public class NotifService {
    @Autowired
    NotifLogRepository notifLogRepository;
    
    public void saveOrUpdate(Notif app) {
    	notifLogRepository.save(app);
    }
    
    public List<Notif> getAllNotifLog() {
        List<Notif> apps = new ArrayList<Notif>();
        notifLogRepository.findAll().forEach(app -> apps.add(app));
        return apps;
    }
    
    public Notif getNotifById(int id) {
        return notifLogRepository.findById(id).get();
    }
}
