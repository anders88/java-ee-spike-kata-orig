package no.steria.kata.javaee;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PersonServlet extends HttpServlet {
    private PersonDao personDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        displayCreatePage(resp, null);
    }

    private void displayCreatePage(HttpServletResponse resp, String errormessage) throws IOException {
        resp.setContentType("text/html");

        PrintWriter writer = resp.getWriter();
        writer.append("<html><body>");

        if (errormessage != null) {
            writer.append("<p style='color=red;'>" + errormessage + "</p>");
        }

        writer
                .append("<form method='POST' action='createPerson'>")
                .append("<input type='text' name='full_name' value=''/>")
                .append("<input type='submit' name='createPerson' value='Create person'/>")
                .append("</form>")
        ;

        writer.append("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("full_name");
        String errormessage = validateName(name);
        if (errormessage == null) {
            personDao.createPerson(Person.withName(name));
            resp.sendRedirect("/");
        } else {
            displayCreatePage(resp, errormessage);
        }
    }

    private String validateName(String name) {
        if (name == null || name.isEmpty()) {
            return "Empty name is not allowed";
        }
        for (char c : name.toCharArray()) {
            if (Character.isLetterOrDigit(c) || c == '-' || c == ' ') {
                return "Illegal characters in name";
            }
        }
        return null;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
