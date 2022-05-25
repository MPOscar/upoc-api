package rondanet.upoc.core.utils;

public class GtinValidador {

	public static boolean isValid(String gtin){
		try{
			long checkIfIsValidNumber = Long.parseLong(gtin);
			char[] numbers = gtin.toCharArray();
			if(numbers.length >= 8 && numbers.length <= 18){
				int checkDigit = Integer.parseInt(Character.toString(numbers[numbers.length - 1]));
				int suma = 0;
				int paraSumar = 3;
				for (int i = numbers.length - 2; i >= 0; i--) {
					String numberStr = Character.toString(numbers[i]);
					int cifra = Integer.parseInt(numberStr);
					suma += cifra * paraSumar;
					if(paraSumar == 1){
						paraSumar = 3;
					} else {
						paraSumar = 1;
					}
				}
				int multiploMasCercano = ((suma / 10) + 1) * 10;
				return suma % 10 == checkDigit || (multiploMasCercano - suma) == checkDigit;
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}
}
