package com.example.demo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Receiver;
import com.example.demo.model.ReceiverId;
@Repository
public interface ReceiverRepository extends CrudRepository<Receiver, ReceiverId>    {

}
