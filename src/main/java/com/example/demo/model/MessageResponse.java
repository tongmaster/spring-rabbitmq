package com.example.demo.model;

import java.util.List;

public class MessageResponse<T> {
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<T> getDataList() {
		return dataList;
	}
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	private int  status;
	private String msg;
	private List<T> dataList;
	private T data;
	
	
}
