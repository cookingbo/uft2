package model;

public class DataLog {
	private String wrDate;
	private int allData;

	public DataLog(String wrDate) {
		this.wrDate = wrDate;
	}

	public DataLog(String wrDate, int allData) {
		this.wrDate = wrDate;
		this.allData = allData;
	}

	public String getWrDate() {
		return wrDate;
	}

	public int getAllData() {
		return allData;
	}



}
