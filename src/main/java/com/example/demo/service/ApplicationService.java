package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Application;
import com.example.demo.model.PayloadLog;
import com.example.demo.repo.ApplicationRepository;

@Service
public class ApplicationService {
    @Autowired
    ApplicationRepository appRepository;
    public void saveOrUpdate(Application app) {
    	System.out.println( app.getAppName());
    	appRepository.save(app);
    }
    
    public List<Application> getAllApp() {
        List<Application> apps = new ArrayList<Application>();
        appRepository.findAll().forEach(app -> apps.add(app));
        return apps;
    }
    
    public Application getApplicationById(int id) {
        return appRepository.findById(id).get();
    }
}
