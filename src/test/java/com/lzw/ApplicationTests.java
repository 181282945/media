package com.lzw;

import com.lzw.core.util.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testMethod(){
		Map<String,Object> claims = new HashMap<>();
		claims.put("username","peter");
		String token = Jwts.builder().setClaims(claims).setExpiration(DateUtil.addDays(DateUtil.newDate(),5)).signWith(SignatureAlgorithm.HS384,"123456").compact();
		System.out.println(token);

		Claims claims2;
		try {
			claims2 = Jwts.parser()
					.setSigningKey("123456")
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			claims2 = null;
		}
		System.out.println(claims2);
	}

}
