package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.CustomField;
import com.example.demo.model.CustomFieldId;
import com.example.demo.repo.CustomFieldRepository;
@Service
public class CustomService {
	 	@Autowired
	    CustomFieldRepository customRepository;
	    public void saveOrUpdate(CustomField app) {
	    	customRepository.save(app);
	    }
	    
	    public List<CustomField> getAllCustomField() {
	        List<CustomField> apps = new ArrayList<CustomField>();
	        customRepository.findAll().forEach(app -> apps.add(app));
	        return apps;
	    }
	    
	    public CustomField getCustomFieldById(CustomFieldId customFieldid) {
	        return customRepository.findById(customFieldid).get();
	    }
}
