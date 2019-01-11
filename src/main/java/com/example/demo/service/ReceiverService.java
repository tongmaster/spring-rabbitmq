package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Receiver;
import com.example.demo.model.ReceiverId;
import com.example.demo.repo.ReceiverRepository;
@Service
public class ReceiverService {
	 @Autowired
	    ReceiverRepository recRepository;
	    public void saveOrUpdate(Receiver app) {
	    	recRepository.save(app);
	    	
	    }
	    
	    public List<Receiver> getAllReceiver() {
	        List<Receiver> apps = new ArrayList<Receiver>();
	        recRepository.findAll().forEach(app -> apps.add(app));
	        return apps;
	    }
	    
	    public Receiver getReceiverById(ReceiverId receiverid) {
	        return recRepository.findById(receiverid).get();
	    }
}
