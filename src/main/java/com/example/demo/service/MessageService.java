package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Message;
import com.example.demo.model.TokenDevice;
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
    	//return messageRepository.save(person);
    	Message a = messageRepository.save(person);
    	return a.getMsgId();
    }
    
    public int  updateByMsgId(Message msg) {
    	//return messageRepository.save(person);
    	System.out.println(msg.getMessageStatus()  + "  "+msg.getMsgId());
    	return messageRepository.updateMessageByMsgId(msg.getMessageStatus() , msg.getMsgId());
    }

    public void delete(int id) {
    	messageRepository.deleteById(id);
    }
    
    public List<Message> findMessageByIsQueueAndStatusN() {
    	return messageRepository.findMessageByIsQueueAndStatusN();
    }
    
    public List<Message> findMessageByQueue(boolean isQueue , String msgStatus) {
    	return messageRepository.findMessageByQueue(isQueue, msgStatus);
    }
    
    
    
    
}
