package com.school.schoolapp.callback.technical;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.technical.SkillDetailEntity;

public class SkillDetailCallback extends BaseCallback {

	private SkillDetailEntity  data;

	public SkillDetailEntity getData() {
		return data;
	}

	public void setData(SkillDetailEntity data) {
		this.data = data;
	}
	
}
