package tests;

import copy.CopyUtils;

import java.util.List;
import java.util.Objects;

import static tests.Assertions.assertCondition;

public class Test1 implements BaseTest {

    @Override
    public void test() {
        String className = this.getClass().getName();
        System.out.println("Start test " + className);

        Man source = new Test1.Man("Ivan", 12, List.of("Java", "C#"));
        Man copy = CopyUtils.deepCopy(source);
        assertCondition(source != copy);
        assertCondition(source.equals(copy));

        assertCondition(source != copy);
        assertCondition(source.name != copy.name);
        assertCondition(source.favoriteBooks != copy.favoriteBooks);
        for (int i = 0; i < source.favoriteBooks.size(); i++) {
            assertCondition(source.favoriteBooks.get(i) != copy.favoriteBooks.get(i));
        }
        assertCondition(source.equals(copy));

        System.out.println("Finish test " + className);
    }


    public static class Man {
        private String name;
        private int age;
        private List<String> favoriteBooks;

        public Man(String name, int age, List<String> favoriteBooks) {
            this.name = name;
            this.age = age;
            this.favoriteBooks = favoriteBooks;
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

        public List<String> getFavoriteBooks() {
            return favoriteBooks;
        }

        public void setFavoriteBooks(List<String> favoriteBooks) {
            this.favoriteBooks = favoriteBooks;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Man man = (Man) o;
            return age == man.age && Objects.equals(name, man.name) && Objects.equals(favoriteBooks, man.favoriteBooks);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age, favoriteBooks);
        }

        @Override
        public String toString() {
            return "Man{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", favoriteBooks=" + favoriteBooks +
                    '}';
        }
    }
}
