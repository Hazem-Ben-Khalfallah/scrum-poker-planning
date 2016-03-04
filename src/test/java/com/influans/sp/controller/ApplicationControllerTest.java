package com.influans.sp.controller;

import com.influans.sp.Application;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class ApplicationControllerTest {

    @Autowired
    private ApplicationController applicationController;

    @Test
    public void testConfigEndpoint() {
        final ResponseEntity<String> res = applicationController.getParams();
        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(res.getBody()).isEqualTo("scrum_poker");
    }

}
