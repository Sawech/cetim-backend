package com.cetim.labs.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDTO {
    private Long orderID;
    private String echantillonID;
	private String echantillonType;
    private Integer delai;
    private List<String> tests;
    private List<TestDTO> testDTOs = new ArrayList<>();
    private Map<String, String> testNotes;
    
    public OrderDTO() {
    	
    }
    
	public Long getOrderID() {
		return orderID;
	}
	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}
	public String getEchantillonID() {
		return echantillonID;
	}
	public void setEchantillonID(String echantillonID) {
		this.echantillonID = echantillonID;
	}
	public String getEchantillonType() {
		return echantillonType;
	}
	public void setEchantillonType(String echantillonType) {
		this.echantillonType = echantillonType;
	}
	public Integer getDelai() {
		return delai;
	}
	public void setDelai(Integer delai) {
		this.delai = delai;
	}
	public List<String> getTests() {
		return tests;
	}
	public void setTests(List<String> testIds) {
		this.tests = testIds;
	}
	public List<TestDTO> getTestDTOs() {
		return testDTOs;
	}
	public void setTestDTOs(List<TestDTO> testDTOs) {
		this.testDTOs = testDTOs;
	}

	public void addTestDTO(TestDTO testDTO) {
		this.testDTOs.add(testDTO);
	}
	public Map<String, String> getTestNotes() {
		return testNotes;
	}
	public void setTestNotes(Map<String, String> testNotes) {
		this.testNotes = testNotes;
	}
}
