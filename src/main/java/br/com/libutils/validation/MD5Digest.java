package br.com.libutils.validation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe utilit�ria para criptografar mensagens utilizando MD5. No escopo do
 * autorizador, � utilizado para criptografar as senhas digitadas pelo usu�rio.
 * 
 * @author Luis Sergio Faria Carneiro
 */
public class MD5Digest {

	// objeto que efetivamente faz a criptogafia
	private MessageDigest digest;

	private static MD5Digest singleton = null;

	/**
	 * Converte array de bytes para sua representa��o hexadecimal.
	 */
	private static String toHex(final byte[] contents) {
		final StringBuffer sb = new StringBuffer(255);
		for (final byte content : contents) {
			final int intValue = content & 0xFF;
			final String hex = Integer.toHexString(intValue);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}

		return sb.toString();
	}

	private MD5Digest() {
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (final NoSuchAlgorithmException impossible) {
		}
	}

	/**
	 * @return Uma (�nica) inst�ncia da classe respons�vel pela criptografia
	 */
	public static MD5Digest getInstance() {
		if (singleton == null) {
			singleton = new MD5Digest();
		}
		return singleton;
	}

	/**
	 * Gera o padr�o MD5 para um texto qualquer.
	 * 
	 * @param contents
	 *            Texto que se deseja criptografar
	 * @return C�digo MD5 gerado para o texto em quest�o.
	 */
	public String generateDigest(final String contents) {
		if (contents == null) {
			return null;
		}
		return generateDigest(contents.getBytes());
	}

	/**
	 * Gera o padr�o MD5 para um texto qualquer.
	 * 
	 * @param contents
	 *            Texto em bytes que se deseja criptografar
	 * @return C�digo MD5 gerado para o texto em quest�o.
	 */
	public String generateDigest(final byte[] contents) {
		if (contents == null) {
			return null;
		}
		final byte[] result = digest.digest(contents);
		return toHex(result);
	}

}
