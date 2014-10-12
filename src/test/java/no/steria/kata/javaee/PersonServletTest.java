package no.steria.kata.javaee;

import org.dom4j.DocumentHelper;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PersonServletTest {

    private final PersonServlet personServlet = new PersonServlet();
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse resp = mock(HttpServletResponse.class);
    private final PersonDao personDao = mock(PersonDao.class);
    private final StringWriter htmlSource = new StringWriter();

    @Test
    public void shouldShowCreateForm() throws Exception {

        when(req.getMethod()).thenReturn("GET");


        personServlet.service(req, resp);

        verify(resp).setContentType("text/html");

        assertThat(htmlSource.toString())
                .contains("<form method='POST' action='createPerson'")
                .contains("<input type='text' name='full_name' value=''")
                .contains("<input type='submit' name='createPerson' value='Create person'")
                ;

        DocumentHelper.parseText(htmlSource.toString());
    }

    @Before
    public void setUp() throws Exception {
        personServlet.setPersonDao(personDao);
        when(resp.getWriter()).thenReturn(new PrintWriter(htmlSource));
    }

    @Test
    public void shouldCreatePerson() throws Exception {
        when(req.getMethod()).thenReturn("POST");
        when(req.getParameter("full_name")).thenReturn("Obi-Wan Kenobi");

        personServlet.service(req,resp);

        verify(personDao).createPerson(Person.withName("Obi-Wan Kenobi"));
        verify(resp).sendRedirect("/");
    }

    @Test
    public void shouldNotAllowEmptyName() throws Exception {
        when(req.getMethod()).thenReturn("POST");
        when(req.getParameter("full_name")).thenReturn(null);

        personServlet.service(req, resp);

        verify(personDao,never()).createPerson(any(Person.class));

        assertThat(htmlSource.toString())
                .contains("<p style='color=red;'>Empty name is not allowed</p>")
                .contains("<form method='POST' action='createPerson'")
                .contains("<input type='text' name='full_name' value=''")
                .contains("<input type='submit' name='createPerson' value='Create person'")
        ;

        DocumentHelper.parseText(htmlSource.toString());
    }

    @Test
    public void shouldNotAllowIllegalCharactersInName() throws Exception {
        when(req.getMethod()).thenReturn("POST");
        when(req.getParameter("full_name")).thenReturn("Dar<&>th");

        personServlet.service(req, resp);

        verify(personDao,never()).createPerson(any(Person.class));

        assertThat(htmlSource.toString())
                .contains("<p style='color=red;'>Illegal characters in name</p>")
                .contains("<input type='text' name='full_name' value='Dar&lt;&amp;&gt;th'")
        ;

        DocumentHelper.parseText(htmlSource.toString());

    }
}
