package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Message;
import com.example.demo.repo.MessageRepository;

@Service
public class MessageService {
	
	@Autowired
    MessageRepository messageRepository;

    public List<Message> getAllPersons() {
        List<Message> persons = new ArrayList<Message>();
        messageRepository.findAll().forEach(person -> persons.add(person));
        return persons;
    }

    public Message getPersonById(int id) {
        return messageRepository.findById(id).get();
    }

    public Integer saveOrUpdate(Message person) {
    	Message a = messageRepository.save(person);
    	return a.getMsgId();
    }

    public void delete(int id) {
    	messageRepository.deleteById(id);
    }
}
