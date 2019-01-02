package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Payload;
import com.example.demo.model.PayloadLog;
import com.example.demo.repo.PayloadRepository;


@Service
public class PayloadService {

    @Autowired
    PayloadRepository payloadRepository;

    public List<PayloadLog> getAllPersons() {
        List<PayloadLog> persons = new ArrayList<PayloadLog>();
        payloadRepository.findAll().forEach(person -> persons.add(person));
        return persons;
    }

    public PayloadLog getPersonById(int id) {
        return payloadRepository.findById(id).get();
    }

    public void saveOrUpdate(PayloadLog person) {
    	System.out.println(person.getMessage());
    	payloadRepository.save(person);
    }

    public void delete(int id) {
    	payloadRepository.deleteById(id);
    }
}
