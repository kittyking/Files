package com.school.schoolapp.classes.workspace;

import com.school.schoolapp.classes.BaseCallback;

public class WorkspaceWorkStatusCallback extends BaseCallback {
	
	private WorkStatusVO data;
	
	
	
	public WorkStatusVO getData() {
		return data;
	}



	public void setData(WorkStatusVO data) {
		this.data = data;
	}



	public class WorkStatusVO{
		private String opened;
		
		private String date;
		
		private String week;
		
		private String diff;

		public String getOpened() {
			return opened;
		}

		public void setOpened(String opened) {
			this.opened = opened;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getWeek() {
			return week;
		}

		public void setWeek(String week) {
			this.week = week;
		}

		public String getDiff() {
			return diff;
		}

		public void setDiff(String diff) {
			this.diff = diff;
		}
		
	}

}
