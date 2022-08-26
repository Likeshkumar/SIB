package test;

public class testsubstring {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String subrod="HMASNH009329F307108035436";
		String name="GOWTHAM";
		String test=subrod.substring(1);
		String respontestingse = subrod.substring(8);
		String response = subrod.substring(6,8);
		String clearpin = subrod.substring(8,12);
		
		System.out.println(name.substring(6,7));
		System.out.println(test);
		System.out.println(respontestingse);
		System.out.println(response);
		System.out.println(clearpin);

	}

}
