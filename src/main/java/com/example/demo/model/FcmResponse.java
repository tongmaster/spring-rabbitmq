package com.example.demo.model;

import java.util.List;

public class FcmResponse {

	private Long multicast_id;
	private int success;
	private int failure ;
	private Long canonical_ids;
	private List<FcmResult> results;
	public Long getMulticast_id() {
		return multicast_id;
	}
	public void setMulticast_id(Long multicast_id) {
		this.multicast_id = multicast_id;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getFailure() {
		return failure;
	}
	public void setFailure(int failure) {
		this.failure = failure;
	}
	public Long getCanonical_ids() {
		return canonical_ids;
	}
	public void setCanonical_ids(Long canonical_ids) {
		this.canonical_ids = canonical_ids;
	}
	public List<FcmResult> getResults() {
		return results;
	}
	public void setResults(List<FcmResult> results) {
		this.results = results;
	}
	
}
