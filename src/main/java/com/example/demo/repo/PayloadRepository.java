package com.example.demo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.PayloadLog;
@Repository
public interface PayloadRepository extends CrudRepository<PayloadLog, Integer> {}
