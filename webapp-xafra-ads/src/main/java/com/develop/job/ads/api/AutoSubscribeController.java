package com.develop.job.ads.api;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.develop.job.ads.impls.AutoSubscribe;
import com.develop.job.tools.LogManager;
import com.develop.job.tools.Utils;
import com.google.common.base.Stopwatch;

@RestController
@RequestMapping(value = { "/v1" })
public class AutoSubscribeController {

	protected final Logger log = LoggerFactory.getLogger(AutoSubscribeController.class);

	@Autowired
	private AutoSubscribe sub;

	@PostMapping(value = { "/auto/subscribe/{productId}", "/auto/subscribe/{productId}/{hour}",
			"/auto/subscribe/{productId}/ts/{time}", "/auto/subscribe/{productId}/{hour}/{time}",
			"/auto/subscribe/{productId}/ts/{time}/{source}", "/auto/subscribe/{productId}/{hour}/{time}/{source}",
			"/auto/subscribe/{productId}/s/{source}", "/auto/subscribe/{productId}/{hour}/s/{source}",
			"/auto/subscribe/{productId}/{hour}/{time}/{source}/{limit}", "/auto/subscribe/{productId}/l/{limit}",
			"/auto/subscribe/{productId}/ts/{time}/l/{limit}", "/auto/subscribe/{productId}/{hour}/l/{limit}",
			"/auto/subscribe/{productId}/ts/{time}/s/{source}/l/{limit}",
			"/auto/subscribe/{productId}/s/{source}/l/{limit}" })

	public ResponseEntity<?> encryp(@RequestHeader HttpHeaders headers, @PathVariable("productId") Long productId,
			@PathVariable(name = "hour", required = false) String hour,
			@PathVariable(name = "time", required = false) Integer timeSleep,
			@PathVariable(name = "source", required = false) String source,
			@PathVariable(name = "limit", required = false) Integer limit) {
		String uuid = Utils.getUUID();
		try {
			LogManager.setLogName("xafra-ads-auto");
			Stopwatch stopwatch = Stopwatch.createStarted();
			hour = (hour != null && !"".equals(hour)) ? hour : "23";
			timeSleep = (timeSleep != null) ? timeSleep : 10;
			source = (source != null) ? source : "AA230";
			String process = "Threads OK";
			sub.process(productId, hour, timeSleep, source, limit, uuid);

			log.info("[{}]| rest TimeTaken={} ms", uuid, String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
			return ResponseEntity.status(HttpStatus.OK).body(process);
		} catch (Exception e) {
			log.error("[{}]", uuid, e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

	}

	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}
}
