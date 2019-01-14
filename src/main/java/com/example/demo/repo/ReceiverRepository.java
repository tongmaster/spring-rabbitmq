package com.example.demo.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Message;
import com.example.demo.model.Receiver;
import com.example.demo.model.ReceiverId;
@Repository
public interface ReceiverRepository extends CrudRepository<Receiver, ReceiverId>    {
	@Query(value="SELECT t.* FROM push_notif.receiver t WHERE t.msg_id = ?1 " , nativeQuery = true)
    List<Receiver> findReceiverByMsgId(Integer msgId);
	
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value="UPDATE push_notif.receiver  SET notif_status = ?1  where  msg_id = ?2 and user_ref = ?3" , nativeQuery = true)
	public int updateByMsgId(String notifStatus ,Integer msgId , String useref);
}
