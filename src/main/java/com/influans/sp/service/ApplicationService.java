package com.influans.sp.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ApplicationService {

	@Value("${project.name}")
	private String projectName;

	public String getProjectName() {
		return projectName;
	}

}
