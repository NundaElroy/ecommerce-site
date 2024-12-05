package com.ecommerce.nunda;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class ApplicationTests {

	Calculator underTest = new Calculator();
	@Test
	void contextLoads() {
		//given
		int a = 20;
		int b = 10;
		//when
		int result = underTest.add(a, b);
		//then
		int expected = 30;
		assertThat(result).isEqualTo(expected);

	}
	class Calculator {
		int add (int  a , int b){
			return a + b;
		}
	}
}
