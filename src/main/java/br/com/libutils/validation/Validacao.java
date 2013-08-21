package br.com.libutils.validation;

import br.com.libutils.exception.CnpjValidationException;
import br.com.libutils.exception.CpfValidationException;
import br.com.libutils.exception.EmailValidationException;

public class Validacao {

	public static void validaEmail(String strEmail)
			throws EmailValidationException {

		// TODO: (Refatorar) Utilizar Expressao regular para validar

		int indexArroba = strEmail.indexOf("@");

		if (indexArroba < 1) {
			throw new EmailValidationException(strEmail);
		}

		int indexPonto = strEmail.indexOf(".", indexArroba);

		if (indexPonto <= (indexArroba + 1)) {
			throw new EmailValidationException(strEmail);
		}

	}

	public static void validaCPF(final String strCpf)
			throws CpfValidationException {

		// TODO: (Refatorar) Utilizar Expressao regular para validar
		String validateCpf = strCpf.replace(".", "").replace("-", "");

		if (validateCpf == "") {
			throw new CpfValidationException(strCpf);
		}

		// Elimina CPFs invalidos conhecidos
		if (validateCpf.length() != 11 || validateCpf == "00000000000"
				|| validateCpf == "11111111111" || validateCpf == "22222222222"
				|| validateCpf == "33333333333" || validateCpf == "44444444444"
				|| validateCpf == "55555555555" || validateCpf == "66666666666"
				|| validateCpf == "77777777777" || validateCpf == "88888888888"
				|| validateCpf == "99999999999") {
			throw new CpfValidationException(strCpf);
		}

		// Valida 1o digito
		int add = 0;
		for (int i = 0; i < 9; i++) {
			add += Integer.parseInt(String.valueOf(validateCpf.charAt(i)))
					* (10 - i);
		}

		int rev = 11 - (add % 11);
		if (rev == 10 || rev == 11) {
			rev = 0;
		}

		if (rev != Integer.parseInt(String.valueOf(validateCpf.charAt(9)))) {
			throw new CpfValidationException(strCpf);
		}

		// Valida 2o digito
		add = 0;
		for (int i = 0; i < 10; i++) {
			add += Integer.parseInt(String.valueOf(validateCpf.charAt(i)))
					* (11 - i);
		}

		rev = 11 - (add % 11);
		if (rev == 10 || rev == 11) {
			rev = 0;
		}

		if (rev != Integer.parseInt(String.valueOf(validateCpf.charAt(10)))) {
			throw new CpfValidationException(strCpf);
		}

	}

	public static void validaCNPJ(final String str_cnpj)
			throws CnpjValidationException {

		// TODO: (Refatorar) Utilizar Expressao regular para validar

		int soma = 0;
		int dig;

		if (str_cnpj.length() != 14) {
			throw new CnpjValidationException();
		}

		String cnpj_calc = str_cnpj.substring(0, 12);

		final char[] chr_cnpj = str_cnpj.toCharArray();

		/* Primeira parte */
		for (int i = 0; i < 4; i++) {
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
			}
		}
		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
			}
		}
		dig = 11 - soma % 11;

		cnpj_calc += dig == 10 || dig == 11 ? "0" : Integer.toString(dig);

		/* Segunda parte */
		soma = 0;
		for (int i = 0; i < 5; i++) {
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
			}
		}

		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
			}
		}

		dig = 11 - soma % 11;
		cnpj_calc += dig == 10 || dig == 11 ? "0" : Integer.toString(dig);

		if (!str_cnpj.equals(cnpj_calc)) {
			throw new CnpjValidationException(str_cnpj);
		}
	}

}
