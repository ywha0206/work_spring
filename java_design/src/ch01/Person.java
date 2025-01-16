package ch01;

// 생성, 구조, 행위
// 롬북 <-- 빌더 패턴
// 사전 기반 지식
// this. 사용 , this() 자기자신 생성자 호출
public class Person {
	// 멤버 변수 선언
	private String name;
	private int age;

	
	// 외부 클래스 생성자
	private Person(PersonBuilder builder) {
		this.name = builder.name;
		this.age = builder.age;
	}
	
	// Builder 클래스 정의
	// static 내부 클래스로 정의된다
	// user.builder() 
	public static class PersonBuilder{
		private String name;
		private int age;

		
		// 초기화 해줘야 합니다 - 필수속성 (final) 초기화하는 생성자
		public PersonBuilder(String name, int age) {
			this.name = name;
			this.age = age;
		}
		
		// 선택적 속성을 설정하는 메서드
		public PersonBuilder name(String name) {
			this.name = name;
			return this; // 메서드 체이닝을 위해 this를 반환
		}
		public PersonBuilder age(int age) {
			this.age = age;
			return this; // 메서드 체이닝을 위해 this를 반환
		}
		
		// 빌더 패턴의 핵심
		public Person build() {
			return new Person(this);
		}
	}
	
	public static void main(String[] args) {
//		Person p1 = new Person.PersonBuilder("홍길동", 10).build();
//		Person p1 = new Person.PersonBuilder.age(10).name("홍길동").build();	
	}
}
