package com.example.demo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CustomField;
import com.example.demo.model.CustomFieldId;
@Repository
public interface CustomFieldRepository extends CrudRepository<CustomField, CustomFieldId>   {

}
