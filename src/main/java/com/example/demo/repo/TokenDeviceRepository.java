package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.TokenDevice;

public interface TokenDeviceRepository extends CrudRepository<TokenDevice, Integer> {
		@Query(value="SELECT t.* FROM push_notif.token t WHERE t.user_ref = ?1" , nativeQuery = true)
	    List<TokenDevice> findByUserRef(String userRef);
		@Query(value="SELECT t.* FROM push_notif.token t WHERE t.user_ref = ?1 and t.app_id = ?2 and t.device_det = ?3 " , nativeQuery = true)
	    TokenDevice findTokenByDeviceDet(String userRef, int appId, String deviceDet);
		@Modifying
		@Query(value="UPDATE  push_notif.token set token = ?1  WHERE user_ref = ?2 and app_id = ?3 and device_det = ?4 " , nativeQuery = true)
	    int updateTokenByDeviceDet(String token ,String userRef, int appId, String deviceDet);
}
