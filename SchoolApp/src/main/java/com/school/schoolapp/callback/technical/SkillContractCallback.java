package com.school.schoolapp.callback.technical;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.technical.SkillContractEntity;

public class SkillContractCallback extends BaseCallback {

	private List<SkillContractEntity> data;

	public List<SkillContractEntity> getData() {
		return data;
	}

	public void setData(List<SkillContractEntity> data) {
		this.data = data;
	}
	
	
}
