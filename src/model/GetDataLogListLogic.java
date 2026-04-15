package model;

import java.util.List;

import dao.DataLogDAO;

public class GetDataLogListLogic {
	public List<DataLog> execute() {
		DataLogDAO dao = new DataLogDAO();
		List<DataLog> dataLogList = dao.findAll();
		return dataLogList;
	}

}
