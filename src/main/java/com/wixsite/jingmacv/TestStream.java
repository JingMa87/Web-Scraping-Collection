package com.wixsite.jingmacv;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TestStream extends WebScraperCassandra {

	public static void main(String[] args) {
		// Uses a stream and filters a list of persons using a lambda.
		List<Person> list = Arrays.asList(new Person("Jing", 7), new Person("Rik", 25), new Person("Floris", 21));
		Person person = list.stream().filter(p -> p.name.equals("Jason") || p.age == 20).findAny().orElse(new Person("John", 40));
		System.out.println(person.name + " is " + person.age + " years old.");
		
		List<Integer> list1 = Arrays.asList(1, 2, 3);
		List<Integer> list2 = Arrays.asList(4, 5, 6);
		Stream.of(list1, list2).forEach(l -> {
			l.stream().map(i -> i * i).forEach(i -> System.out.print(i + " "));
		});
		
		System.out.println();
		Stream<String> strm = list1.stream().map(i -> new Person("Test", i).getName());
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
