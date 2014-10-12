package no.steria.kata.javaee;

import org.fest.assertions.Assertions;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class PersonTest {
    @Test
    public void shouldCreatePersonWithGivenName() throws Exception {
        assertThat(Person.withName("Darth").getName()).isEqualTo("Darth");
    }

    @Test
    public void peopleWithSameNameShouldBeEqual() throws Exception {
        assertThat(Person.withName("Darth"))
                .isEqualTo(Person.withName("Darth"))
                .isNotEqualTo(Person.withName("Anakin"))
                .isNotEqualTo(Person.withName(null))
                .isNotEqualTo(new Object())
                .isNotEqualTo(null)
         ;

        assertThat(Person.withName(null))
                .isNotEqualTo(Person.withName("Darth"))
                .isEqualTo(Person.withName(null))
                ;

    }
}
