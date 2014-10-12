package no.steria.kata.javaee;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.fest.assertions.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.net.URI;

import static org.fest.assertions.Assertions.assertThat;

public class PersonWebTest {
    @Test
    @Ignore
    public void shouldCreateAndFindPerson() throws Exception {
        Server server = new Server(0);
        server.setHandler(new WebAppContext("src/main/webapp", ""));
        server.start();

        URI uri = server.getURI();

        WebDriver browser = createBrowser();
        browser.get(uri.toString());

        browser.findElement(By.linkText("Create person")).click();
        browser.findElement(By.name("full_name")).sendKeys("Darth Vader");
        browser.findElement(By.name("createPerson")).click();

        browser.findElement(By.linkText("Find people")).click();
        browser.findElement(By.name("query")).sendKeys("th vad");
        browser.findElement(By.name("findPeople")).click();

        assertThat(browser.getPageSource()).contains("<li>Darth Vader</li>");
    }

    private HtmlUnitDriver createBrowser() {
        return new HtmlUnitDriver() {
            @Override
            public WebElement findElement(By by) {
                try {
                    return super.findElement(by);
                } catch (NoSuchElementException e) {
                    throw new NoSuchElementException("Did not find " + by + " in " + super.getPageSource());
                }
            }
        };
    }
}
