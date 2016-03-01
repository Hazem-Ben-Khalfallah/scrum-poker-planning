package com.influans.sp.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ApplicationService {

	@Value("${project.name}")
	private String projectName;

	@Value("${project.author}")
	private String projectAuthor;

	@Value("${project.website}")
	private String projectWebsite;

	public ApplicationService() { }

	public String getProjectName() {
		return projectName;
	}

	public String getProjectAuthor() {
		return projectAuthor;
	}

	public String getProjectWebsite() {
		return projectWebsite;
	}

}
