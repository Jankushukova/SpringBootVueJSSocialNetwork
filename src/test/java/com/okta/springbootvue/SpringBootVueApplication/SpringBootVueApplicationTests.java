package com.okta.springbootvue.SpringBootVueApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootVueApplicationTests {

	@Test
	public void contextLoads() {
	}
	@Test
	public void testConfigureGlobal() throws Exception {
		String auth = "dXNlcjpwYXNzd29yZA==";
		System.out.println(new String(Base64.getDecoder().decode(auth)));
	}

}
