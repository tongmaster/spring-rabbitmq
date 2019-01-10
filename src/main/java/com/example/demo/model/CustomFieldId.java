package com.example.demo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class CustomFieldId implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer msgId;

	private String keyName;

	private String keyValue;


}
