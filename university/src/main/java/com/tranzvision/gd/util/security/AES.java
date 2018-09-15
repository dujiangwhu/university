package com.tranzvision.gd.util.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {


	/**
	 * 加密
	 *
	 * @param src
	 *            byte[] 加密的数据源
	 * @param password
	 *            String 加密秘钥
	 * @return byte[] 加密后的数据
	 */
	public static String encrypt(String content, String key) {
		if(key == null || key.length() != 16)
		{
			System.err.println("AES key 的长度必须是16位！");
			return null;
		}
		try
		{
			Cipher cipher = Cipher.getInstance("AES/ECB/NOPadding");
			int blockSize = cipher.getBlockSize();
			byte[] dataBytes = content.getBytes();
			int plaintextLength = dataBytes.length;
			if (plaintextLength % blockSize != 0)
			{
				plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
			}
			byte[] plaintext = new byte[plaintextLength];
			System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, keyspec);
			byte[] encrypted = cipher.doFinal(plaintext);
			if (encrypted == null){
				return null;
			}
			char[] hexArray = "0123456789abcdef".toCharArray();
			char[] hexChars = new char[encrypted.length * 2];
			for (int j = 0; j < encrypted.length; j++) {
				int v = encrypted[j] & 0xFF;
				hexChars[j * 2] = hexArray[v >>> 4];
				hexChars[j * 2 + 1] = hexArray[v & 0x0F];
			}
			return new String(hexChars);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}



	public static void main(String[] args) {

		System.out.println(AES.encrypt("123456", "5NIWjlgmvqwbt494"));
	}
}
