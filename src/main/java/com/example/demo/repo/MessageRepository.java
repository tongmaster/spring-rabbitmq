package com.example.demo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer>{

}
