package com.example.demo.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer>{
	@Query(value="SELECT t.* FROM push_notif.message t WHERE t.is_queue = true  and t.message_status = 'N' " , nativeQuery = true)
    List<Message> findMessageByIsQueueAndStatusN();
	@Query(value="SELECT t.* FROM push_notif.message t WHERE t.is_queue = ?1 and t.message_status = ?2" , nativeQuery = true)
    List<Message> findMessageByQueue(boolean isQueue , String msgStatus);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value="UPDATE push_notif.message   set message_status = ?1  WHERE  msg_id = ?2" , nativeQuery = true)
    int updateMessageByMsgId(String msgStatus ,Integer msgId);
}
