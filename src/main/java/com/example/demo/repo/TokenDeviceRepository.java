package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.TokenDevice;

public interface TokenDeviceRepository extends CrudRepository<TokenDevice, Integer> {
		@Query(value="SELECT t.* FROM push_notif.token t WHERE t.user_ref = ?1" , nativeQuery = true)
	    List<TokenDevice> findByUserRef(String userRef);
		@Query(value="SELECT t.* FROM push_notif.token t WHERE t.user_ref = ?1 and t.app_id = ?2 and t.device_uuid = ?3 " , nativeQuery = true)
	    TokenDevice findTokenByDeviceDet(String userRef, int appId, String deviceDet);
		@Modifying
		@Query(value="UPDATE  push_notif.token set token = ?1 , last_login = now() WHERE user_ref = ?2 and app_id = ?3 and device_uuid = ?4 " , nativeQuery = true)
	    int updateTokenByDeviceDet(String token ,String userRef, int appId, String deviceDet);
		@Query(value="SELECT t.* FROM push_notif.token t WHERE t.user_ref = ?1 and t.app_id = ?2  " , nativeQuery = true)
		List<TokenDevice> findTokenByUserRefAndAppId(String userRef,int appId);
}
