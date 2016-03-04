package com.influans.sp.service;

import com.influans.sp.Application;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class ApplicationServiceTest {

	@Autowired
	private ApplicationService applicationService;
	
	@Test
	public void testConfigParams() {
		Assertions.assertThat(applicationService.getProjectName()).isEqualTo("scrum_poker");
	}
	
}
