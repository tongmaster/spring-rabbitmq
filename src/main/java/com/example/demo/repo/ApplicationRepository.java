package com.example.demo.repo;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Application;


@Repository
public interface ApplicationRepository extends CrudRepository<Application, Integer>  {

}
