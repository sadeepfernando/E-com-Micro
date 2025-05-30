package com.example.product_service;

import com.example.product_service.dto.ProductResponse;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;


@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
		RestAssured.defaultParser = io.restassured.parsing.Parser.JSON;
	}

	static {
		mongoDBContainer.start();
	}


	@Test
	void shouldCreateTheProduct() {

		String requestBody = """
				{
				    "name":"iphone 15",
				    "description":"iphone",
				    "price": 1400
				}
				""";
		RestAssured.given()
				.body(requestBody)
				.contentType("application/json")
				.when()
				.post("/api/product")
				.then()
				.statusCode(201);
	}

	@Test
	void shouldGetAllProducts(){

		 RestAssured.given()
				.accept("application/json")
				.when()
				.get("/api/product")
				.then()
				.statusCode(200)
				 .body("size()", Matchers.greaterThan(0));
	}

}
