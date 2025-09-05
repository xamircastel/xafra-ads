package com.commons.help.job.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Encryption {

	private static final Logger Log = LoggerFactory.getLogger(Encryption.class);
	private static final String ENCRYPTION_ALGORITH = "AES/ECB/PKCS5Padding";

	public static String decrypt(String valueToDecrypt, String preSharedKey) throws Exception {
		if (valueToDecrypt != null && !valueToDecrypt.isEmpty()) {
			byte[] value = Base64.getDecoder().decode(valueToDecrypt);
			try {
				Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITH);
				SecretKeySpec k = new SecretKeySpec(preSharedKey.getBytes(), "AES");
				cipher.init(2, k);
				byte[] decrypted = cipher.doFinal(value);
				return new String(decrypted);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
					| BadPaddingException e) {
				Log.error("decrypt", e);
				throw new Exception(e.getMessage());
			}
		}
		return null;
	}

	public static String encrypt(String valueToEncrypt, String preSharedKey) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITH);
			SecretKeySpec key = new SecretKeySpec(preSharedKey.getBytes(StandardCharsets.UTF_8.toString()), "AES");
			cipher.init(1, key);
			byte[] crypted = cipher.doFinal(valueToEncrypt.getBytes(StandardCharsets.UTF_8.toString()));
			return Base64.getEncoder().encodeToString(crypted);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			Log.error("encrypt", e);
			throw new Exception(e.getMessage());
		}
	}
}
