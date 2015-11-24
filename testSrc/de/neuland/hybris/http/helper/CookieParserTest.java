package de.neuland.hybris.http.helper;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CookieParserTest {

    CookieParser cookieParser;

    @Before
    public void setUp() {
        cookieParser = CookieParser.getInstance();
    }

    @Test
    public void shouldReturnACookieByGivenName() {
        assertEquals("JSESSIONID=07067BCF03EB91C40061E665BADAC3EF", cookieParser.getSpecialCookie("JSESSIONID=07067BCF03EB91C40061E665BADAC3EF; Path=/", "JSESSIONID"));
    }

    @Test
    public void shouldReturnACookieFromTheEndOfTheCookieString() {
        assertEquals("Path=/", cookieParser.getSpecialCookie("JSESSIONID=07067BCF03EB91C40061E665BADAC3EF; Path=/", "Path"));
    }

    @Test
    public void shouldFailIfTheCookieIsNotFound() {
        assertNull(cookieParser.getSpecialCookie("JSESSIONID=07067BCF03EB91C40061E665BADAC3EF; Path=/","NoCookie"));
    }

}
