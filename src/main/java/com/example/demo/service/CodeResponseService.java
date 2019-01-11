package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Application;
import com.example.demo.model.CodeResponse;
import com.example.demo.model.PayloadLog;
import com.example.demo.repo.ApplicationRepository;
import com.example.demo.repo.CodeResponseRepository;

@Service
public class CodeResponseService {
    @Autowired
    CodeResponseRepository CodeRespRepository;
    public CodeResponse saveOrUpdate(CodeResponse app) {
    	return CodeRespRepository.save(app);
    }
    
    public List<CodeResponse> getAllCodeResp() {
        List<CodeResponse> apps = new ArrayList<CodeResponse>();
        CodeRespRepository.findAll().forEach(app -> apps.add(app));
        return apps;
    }
    
    public CodeResponse getCodeRespById(int id) {
        return CodeRespRepository.findById(id).get();
    }
    
    public CodeResponse getCodeRespByName(String responseCode) {
        return CodeRespRepository.findByResponseCode(responseCode);
    }
    
    public List<CodeResponse> findLastId() {
		// TODO Auto-generated method stub
    	 return CodeRespRepository.findLastId();

	}
}
