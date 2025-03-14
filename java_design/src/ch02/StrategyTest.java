package ch02;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class StrategyTest {

	public static void main(String[] args) {
		// 추상화를 높이는 게 좋은 코드가 될 수 있다.
		EncodingStrategy base64 = new Base64Strategy();
		EncodingStrategy normal = new NormalStrategy();
		EncodingStrategy append = new AppendStrategy();
		EncodingStrategy url = new URLStrategy();
		
		String message = "1234 한글 입력";
		
		Encoder encoder = new Encoder();
		
		// base64로 인코딩해주세요
		encoder.setEncodingStrategy(base64);
		System.out.println("base64 : " + encoder.getMessage(message));
		
		// 일반텍스트로 인코딩해주세요
		encoder.setEncodingStrategy(normal);
		System.out.println("normal : " + encoder.getMessage(message));
		
		// append 텍스트로 인코딩해주세요
		encoder.setEncodingStrategy(append);
		System.out.println("append : " + encoder.getMessage(message));
		
		// 도전 과제 - 
		// URL 인코딩 만들기
		encoder.setEncodingStrategy(url);
		System.out.println("url : " + encoder.getMessage(message));
		
	}
	
	
} // end of class


// 인코딩 전략
interface EncodingStrategy{
	String encode(String text);
}

// 바이트 010101 ---> new File();
// 서버측 데이터를 API -- json -- 문자열
// Base64 인코딩
class Base64Strategy implements EncodingStrategy {

	@Override
	public String encode(String text) {
		return Base64.getEncoder().encodeToString(text.getBytes());
	}
	
}

class NormalStrategy implements EncodingStrategy {

	@Override
	public String encode(String text) {
		return text;
	}
	
}

class AppendStrategy implements EncodingStrategy {

	@Override
	public String encode(String text) {
		return "ABC" + text;
	}
	
}


class URLStrategy implements EncodingStrategy {

	@Override
	public String encode(String text) {
		try {
			// URLEncoder 사용해서 UTF-8 형식으로 인코딩
			// 공백, 특수문자 등을 % 형식으로 변환해 전송할 수 있도록 한다.
			return URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
//			return URLEncoder.encode(text, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("url 인코딩 메서드 catch");
		}
		return null;
	}
	
}

// 클라이언트 클래스
class Encoder {
	
	// DI - 생성자 주입
	// DI - 메서드 주입 setter
	
	// 행동을 할 멤버
	EncodingStrategy encodingStrategy;
	
	// 전략에 따라서 실행할 메서드
	public String getMessage(String message) {
		return this.encodingStrategy.encode(message);
	}
	
	// 전략에 따라서 멤버 변경할 수 있는 메서드
	public void setEncodingStrategy(EncodingStrategy encodingStrategy) {
		this.encodingStrategy = encodingStrategy;
	}
	
}