package com.blacknebula.scrumpoker.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

/**
 * @author hazem
 */
public class JsonSerializerTest {
    /**
     * @verifies generate Json String from object
     * @see JsonSerializer#serialize(Object)
     */
    @Test
    public void serialize_shouldGenerateJsonStringFromObject() throws Exception {
        // given
        final DummyPerson dummyPerson = new DummyPerson("Leo", 30, "male");
        // when
        final String json = JsonSerializer.serialize(dummyPerson);
        // then
        Assertions.assertThat(json).isEqualTo("{\"name\":\"Leo\",\"age\":30,\"sex\":\"male\"}");
    }

    /**
     * @verifies create object for json text
     * @see JsonSerializer#toObject(String, Class)
     */
    @Test
    public void toObject_shouldCreateObjectForJsonText() throws Exception {
        // given
        final String json = "{\"name\":\"Leo\",\"age\":30,\"sex\":\"male\"}";
        // when
        final DummyPerson dummyPerson = JsonSerializer.toObject(json, DummyPerson.class);
        // then
        Assertions.assertThat(dummyPerson).isNotNull();
        Assertions.assertThat(dummyPerson.getName()).isEqualTo("Leo");
        Assertions.assertThat(dummyPerson.getAge()).isEqualTo(30);
        Assertions.assertThat(dummyPerson.getSex()).isEqualTo("male");
    }

    /**
     * @verifies create List of objects for json text
     * @see JsonSerializer#toListObject(String, Class)
     */
    @Test
    public void toListObject_shouldCreateListOfObjectsForJsonText() throws Exception {
        // given
        final String json = "[{\"name\":\"Leo\",\"age\":30,\"sex\":\"male\"}, {\"name\":\"Leonidas\",\"age\":50,\"sex\":\"male\"}]";
        // when
        final List<DummyPerson> dummyPeople = JsonSerializer.toListObject(json, DummyPerson.class);
        // then
        Assertions.assertThat(dummyPeople).hasSize(2);
        dummyPeople.forEach(dummyPerson -> {
            Assertions.assertThat(dummyPerson.getName()).isNotNull();
            Assertions.assertThat(dummyPerson.getAge()).isNotNull();
            Assertions.assertThat(dummyPerson.getSex()).isNotNull();
        });
    }

    public static class DummyPerson {
        private String name;
        private Integer age;
        private String sex;

        public DummyPerson() {
        }

        public DummyPerson(String name, int age, String sex) {
            this.name = name;
            this.age = age;
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
