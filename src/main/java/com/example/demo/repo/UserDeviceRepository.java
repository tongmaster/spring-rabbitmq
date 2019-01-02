package com.example.demo.repo;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.UserDevice;


@Repository
public interface UserDeviceRepository extends CrudRepository<UserDevice, Integer>  {

}
