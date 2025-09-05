package com.develop.job.ads.impls;

import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.commons.help.job.utils.Encryption;
import com.develop.job.ads.call.CallProcessAutoSubscribe;
import com.develop.job.db.dao.bi.IBlackList;
import com.develop.job.db.entity.BlackList;
import com.develop.job.tools.RestClient;
import com.google.common.base.Stopwatch;

@Component
@PropertySource("classpath:application.properties")
public class AutoSubscribe {

	protected static final Logger log = LoggerFactory.getLogger(AutoSubscribe.class);

	@Autowired
	protected IBlackList blk;

	@Autowired
	protected RestClient rest;

	@Value("${file.name.topup}")
	private String fileName;

	@Value("${file.path.topup}")
	private String filePath;

	@Value("${limit.subscribe}")
	private String limit;

	private String uuid;
	private String hora;

	public void process(Long productId, String horaR, Integer timeSleep, String source, Integer limitR, String uuidR) {
		uuid = uuidR;
		hora = horaR;
		Stopwatch stopwatch = Stopwatch.createStarted();
		ExecutorService threadPool = Executors.newFixedThreadPool(2);
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		String fileNameS = fileName.replace("{date}", date);

		String filePathS = filePath + fileNameS;

		Integer limitAuto = limitR != null ? limitR : Integer.parseInt(limit);

		log.info("[{}]| START path={}, limit={}, hour={}, timeSleep={}, productId={}, source={}", uuid, filePathS,
				limitAuto, hora, timeSleep, productId, source);
		Stream<String> stream;
		Stream<String> streamFilter;

		Path path = Paths.get(filePathS);
		try {
			stream = Files.lines(path);
			streamFilter = stream.filter(l -> filters(l));
			List<String> msisdns = new ArrayList<String>();

			streamFilter.parallel().forEach(l -> {
				if (msisdns.size() < limitAuto) {
					StringTokenizer st = new StringTokenizer(l.trim(), ",");
					String msisdn = st.nextToken();
					BlackList blacklist = blk.getBlackListByMsisdn(msisdn);
					if (blacklist == null)
						msisdns.add(msisdn);
				}
			});
			log.info("[{}]|size subscribe = {}", uuid, msisdns.size());

			String auth = auth();
			CallProcessAutoSubscribe subscribe = new CallProcessAutoSubscribe();
			subscribe.setAuth(auth);
			subscribe.setMsisdns(msisdns);
			subscribe.setBlk(blk);
			subscribe.setRest(rest);
			subscribe.setProductId(productId);
			subscribe.setSource(source);
			subscribe.setTimeSleep(timeSleep);
			subscribe.setUuid(uuidR);
			threadPool.submit(subscribe);

		} catch (Exception e) {
			log.error("[{}]", uuid, e);
			threadPool.shutdown();
		} finally {
			threadPool.shutdown();
		}
		log.info("[{}]|END TimeTaken={} ms", uuid, String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
	}

	protected Boolean filters(String line) {
		StringTokenizer st = new StringTokenizer(line.trim(), ",");
		if (st.countTokens() == 3) {
			@SuppressWarnings("unused")
			String msisdn = st.nextToken();
			String amount = st.nextToken().trim();
			String date = st.nextToken();
			try {
				Integer amounti = Integer.parseInt(amount);
				String[] sp = date.split(" ");
				if (amounti >= 10 && hora.equals(sp[1].trim())) {
					return Boolean.TRUE;
				} else
					return Boolean.FALSE;
			} catch (Exception e) {
				return Boolean.FALSE;
			}
		} else
			return Boolean.FALSE;
	}

	private String auth() throws UnsupportedEncodingException {
		Long timemillis = Instant.now().toEpochMilli();
		String preSharedKey = "WGNm3QE2f7cwCwi7";
		String valueToEncrypt = "1923#" + timemillis;

		try {
			return Encryption.encrypt(valueToEncrypt, preSharedKey);
		} catch (Exception e) {
			log.error("Error en encriptación", e);
			return null;
		}
	}
}
