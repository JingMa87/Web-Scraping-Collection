package com.wixsite.jingmacv;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

public class TestStream extends WebScraper {
	
	private final static Logger log = Logger.getLogger(TestStream.class);

	public static void main(String[] args) throws IOException {
		// Uses a stream and filters a list of persons.
		List<Person> list = Arrays.asList(new Person("Jing", 7), new Person("Rik", 25), new Person("Floris", 21));
		Person person = list.stream().filter(p -> p.name.equals("Jason") || p.age == 20).findAny().orElse(new Person("John", 40));
		System.out.println(person.name + " is " + person.age + " years old.");
		
		// Streams 2 List<Integer> and returns a Stream<Integer>.
		System.out.println();
		List<Integer> list1 = Arrays.asList(1, 2, 3);
		List<Integer> list2 = Arrays.asList(4, 5, 6);
		Stream<Integer> stream = Stream.of(list1, list2).map(l -> l.stream().map(i -> i * i).collect(Collectors.toList())).flatMap(l -> l.stream());
		stream.forEach(i -> System.out.print(i + " "));
		System.out.println();
		
		log.info("INFO LOG! THIS IS TO FILL THE DOCUMENT UNTIL ITS FULL AHAHA");
		log.info("INFO LOG! THIS IS TO FILL THE DOCUMENT UNTIL ITS FULL AHAHA");
		log.info("INFO LOG! THIS IS TO FILL THE DOCUMENT UNTIL ITS FULL AHAHA");
		log.info("INFO LOG! THIS IS TO FILL THE DOCUMENT UNTIL ITS FULL AHAHA");
		
	}
	
	public static class Person {
		
		private String name;
		private int age;
		
		public Person(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}
}
