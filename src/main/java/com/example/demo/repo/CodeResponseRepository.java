package com.example.demo.repo;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Application;
import com.example.demo.model.CodeResponse;
import com.example.demo.model.TokenDevice;


@Repository
public interface CodeResponseRepository extends CrudRepository<CodeResponse, Integer>  {
	@Query(value="SELECT t.* FROM push_notif.fcm_response t WHERE t.response_code = ?1" , nativeQuery = true)
	CodeResponse findByResponseCode(String responseCode);
	
	@Query(value="SELECT t.* FROM push_notif.fcm_response t order by t.response_id desc " , nativeQuery = true)
	List<CodeResponse> findLastId();
}
