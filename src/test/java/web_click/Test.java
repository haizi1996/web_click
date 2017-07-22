package web_click;

public class Test {
	
	public static void main(String[] args) {
		String s = "192.168.56.102";
		String s1 = s.replaceAll("\\.", "=");
		System.out.println(s1);
		System.out.println(s1.replaceAll("=", "\\."));
	}

}
