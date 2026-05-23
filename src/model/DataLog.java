package model;

public class DataLog {
	private String wrDate;
	private String allData;

	public DataLog(String wrDate) {
		this.wrDate = wrDate;
	}

	public DataLog(String wrDate, String allData) {
		this.wrDate = wrDate;
		this.allData = allData;
	}


	public String getWrDate() {
		return wrDate;
	}

	public String getAllData() {
		return allData;
	}



}
