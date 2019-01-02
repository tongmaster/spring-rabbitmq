package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.TokenDevice;

public interface TokenDeviceRepository extends CrudRepository<TokenDevice, Integer> {
		@Query(value="SELECT t.* FROM push_notif.token t WHERE t.user_ref = ?1" , nativeQuery = true)
	    List<TokenDevice> findByUserRef(String userRef);
}
