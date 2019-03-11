package com.app.ratelimiter.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class EmployeeController {
	@RequestMapping("/test")
	public String getEmployees() {
		System.out.println("oge...");
		return "ok";
	}
}