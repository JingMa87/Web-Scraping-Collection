package com.wixsite.jingmacv;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test extends WebScraperCassandra {

	public static void main(String[] args) {
		// Uses a stream and filters a list of persons using a lambda.
		List<Person> list = Arrays.asList(new Person("Jing", 27), new Person("Jing", 30), new Person("Floris", 28));
		Person person = list.stream().filter(p -> p.name.equals("Jing") || p.age == 30).findAny().orElse(null);
		System.out.println(person.name + " is " + person.age + " years old.");
		
		// Uses a .map() function to get the name of a person.
		list.stream().map(Person::getAge).collect(Collectors.toList()).forEach(System.out::println);
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
