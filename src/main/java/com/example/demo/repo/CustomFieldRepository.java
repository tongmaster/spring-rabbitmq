package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CustomField;
import com.example.demo.model.CustomFieldId;
import com.example.demo.model.Message;
@Repository
public interface CustomFieldRepository extends CrudRepository<CustomField, CustomFieldId>   {
	@Query(value="SELECT t.* FROM push_notif.custom_field t WHERE t.msg_id = ?1" , nativeQuery = true)
    List<CustomField> findByMsgId(Integer msgId);
}
