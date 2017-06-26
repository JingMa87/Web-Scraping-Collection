package com.wixsite.jingmacv;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestStream extends WebScraperCassandra {

	public static void main(String[] args) {
		// Uses a stream and filters a list of persons using a lambda.
		List<Person> list = Arrays.asList(new Person("Jing", 7), new Person("Rik", 25), new Person("Floris", 21));
		Person person = list.stream().filter(p -> p.name.equals("Jing") || p.age == 30).findAny().orElse(null);
		System.out.println(person.name + " is " + person.age + " years old.");
		
		// Uses a .map() function to get the name of a person.
		list.stream().map(a -> a.getAge()).collect(Collectors.toList()).forEach(x -> System.out.println(x));
		list.stream().map(p -> {
			NewPerson newPerson = new NewPerson();
			newPerson.name = p.getName();
			newPerson.age = p.getAge();
			if (newPerson.name.equals("Jing"))
				newPerson.awesomeness = 9001;
			else
				newPerson.awesomeness = 9000;
			return newPerson;
		}).collect(Collectors.toList()).forEach(newPerson -> System.out.println(newPerson));
		
		// Uses a .map() function to square numbers in a list.
		List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
		nums.stream().map(n -> n * n).collect(Collectors.toList()).forEach(n -> System.out.println(n));
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
	
	public static class NewPerson {
		
		private String name;
		private int age;
		private int awesomeness;
		
		public NewPerson() {
		}
		
		public NewPerson(String name, int age, int awesomeness) {
			this.name = name;
			this.age = age;
			this.awesomeness = awesomeness;
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

		public int getSalary() {
			return awesomeness;
		}

		public void setSalary(int salary) {
			this.awesomeness = salary;
		}
		
		public String toString() {
			return name + " is " + age + " years old and has an awesomeness of " + awesomeness + ".";
		}
	}
}
