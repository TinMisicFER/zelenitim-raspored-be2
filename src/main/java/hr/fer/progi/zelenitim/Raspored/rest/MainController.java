package hr.fer.progi.zelenitim.Raspored.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") //!!!!!!!!!!!!!!!!!!!!!!!!!!!!
public class MainController {

	@RequestMapping("/test_auth")
	public String firstPage() {
		return "success";
	}
}
