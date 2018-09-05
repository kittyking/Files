package com.school.schoolapp.callback.technical;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.technical.SkillInfoEntity;

public class SkillListCallback extends BaseCallback {

	private List<SkillInfoEntity>  data;

	public List<SkillInfoEntity> getData() {
		return data;
	}

	public void setData(List<SkillInfoEntity> data) {
		this.data = data;
	}
	
}
