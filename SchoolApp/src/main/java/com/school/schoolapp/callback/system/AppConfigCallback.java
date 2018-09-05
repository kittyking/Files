package com.school.schoolapp.callback.system;

import com.school.schoolapp.classes.BaseCallback;

public class AppConfigCallback extends BaseCallback {

	private AppVersion data;
	
	public AppVersion getData() {
		return data;
	}

	public void setData(AppVersion data) {
		this.data = data;
	}

	public class AppVersion{
		private String version;
		private String path;
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
	}
}
