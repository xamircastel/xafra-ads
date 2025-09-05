package com.develop.job.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.develop.job.bo.impl.BuyCache;
import com.develop.job.model.BuyModel;


@RestController
@RequestMapping(value="/path")
public class Buy {
	@Autowired
	private BuyCache cache;
	
	private static final Logger log = LoggerFactory.getLogger(Buy.class);	
	
	@PostMapping("/ping")
	public String ping() {
		return "pong";
	}

	@PostMapping(value="/buy", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buy() {
		BuyModel buy =cache.getByKey(1);
//		buy.setNombre("compra 1");
//		buy.setId(1);
		return ResponseEntity.status(HttpStatus.OK).body(buy);
	}
	
	@PostMapping(value="/newbuy", produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> newBuy(@RequestBody BuyModel newBuy) {
		log.info("data received {}",newBuy);
		return ResponseEntity.status(HttpStatus.OK).body("create");
	}
}
