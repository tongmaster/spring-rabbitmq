package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TokenDevice;
import com.example.demo.repo.TokenDeviceRepository;

@Service
public class TokenDeviceService {
	  @Autowired
	    TokenDeviceRepository tokenDeviceRepository;

	    public List<TokenDevice> getAllToken() {
	        List<TokenDevice> persons = new ArrayList<TokenDevice>();
	        tokenDeviceRepository.findAll().forEach(person -> persons.add(person));
	        return persons;
	    }

	    public TokenDevice getTokenByUID(int id) {
	        return tokenDeviceRepository.findById(id).get();
	    }

	    public void saveOrUpdate(TokenDevice person) {
	    	System.out.println(person.getToken());
	    	tokenDeviceRepository.save(person);

	    }

	    public void delete(int id) {
	    	tokenDeviceRepository.deleteById(id);
	    }
	    
	    
	    public List<TokenDevice> getTokenByUserRef(String userRef) {
	        return tokenDeviceRepository.findByUserRef(userRef);
	    }
	    
	    public List<TokenDevice> findTokenByUserRefAndAppId(String userRef, int appId) {
	    	return tokenDeviceRepository.findTokenByUserRefAndAppId( userRef, appId);
	    }
	    
	    public TokenDevice findTokenByDeviceDet(String userRef, int appId, String deviceDet) {
	    	return tokenDeviceRepository.findTokenByDeviceDet( userRef, appId, deviceDet);
	    }
	    
	    
	    public int updateTokenByDeviceDet(String token ,String userRef, int appId, String deviceDet) {
	    	return tokenDeviceRepository.updateTokenByDeviceDet(token, userRef, appId, deviceDet);
	    }
}
