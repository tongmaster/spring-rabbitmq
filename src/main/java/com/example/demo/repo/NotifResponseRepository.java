package com.example.demo.repo;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Application;
import com.example.demo.model.NotifResponse;


@Repository
public interface NotifResponseRepository extends CrudRepository<NotifResponse, Integer>  {

}
