package de.neuland.hybris.http;

import de.neuland.hybris.http.helper.CookieParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class HybrisHTTPRequestManagerTest {

    HTTPRequestManager httpRequestManager;

    @Before
    public void setUp() {
        httpRequestManager = HTTPRequestManager.getInstance();
        httpRequestManager.setUsername("admin");
        httpRequestManager.setPassword("1234");
    }

    @Test
    public void shouldCreateALoginParameterPair() {
        List<NameValuePair> pair = httpRequestManager.createLoginDataPair("j_username","j_password");
        assertEquals(pair.get(0).getName(), "j_username");
        assertEquals(pair.get(0).getValue(), "admin");
        assertEquals(pair.get(1).getName(), "j_password");
        assertEquals(pair.get(1).getValue(), "1234");
    }

    @Ignore
    @Test
    public void shouldCreateACookie() {
        List<NameValuePair> pair = httpRequestManager.createLoginDataPair("j_username","j_password");
        String result = httpRequestManager.doLoginForCookie("http://localhost:9001/j_spring_security_check", pair);
        assertNotNull(result);
        assertTrue(result.matches(".*JSESSIONID=.*"));
    }

    @Test
    public void shouldSaysIfAllUserdatesAreSet(){
        assertTrue(httpRequestManager.isUserDataSet());
    }

    @Test
    public void shouldSaysThatNotAllUserdatasAreSetWhenNameIsNull() {
        httpRequestManager.setUsername(null);
        assertFalse(httpRequestManager.isUserDataSet());
    }

    @Test
    public void shouldSaysThatNotAllUserdatasAreSetWhenPasswordIsNull() {
        httpRequestManager.setPassword(null);
        assertFalse(httpRequestManager.isUserDataSet());
    }

    @Test
    public void shouldSaysThatNotAllUserdatasAreSetWhenUsernameAndPasswordIsNull() {
        httpRequestManager.setUsername(null);
        httpRequestManager.setPassword(null);
        assertFalse(httpRequestManager.isUserDataSet());
    }

    @Ignore
    @Test
    public void shouldDoAPostRequestWithAGivenCookie() {
        List<NameValuePair> pair = httpRequestManager.createLoginDataPair("j_username","j_password");
        String result = httpRequestManager.doLoginForCookie("http://localhost:9001/j_spring_security_check", pair);
        String jSessionID = CookieParser.getInstance().getSpecialCookie(result, "JSESSIONID");
        List<NameValuePair> script = new ArrayList<NameValuePair>();
        script.add(new BasicNameValuePair("script", "return \"Groovy Rocks!\""));
        assertEquals(httpRequestManager.doPostRequestWithCookie("http://localhost:9001/console/groovy/execute", jSessionID, script), "{\"executionResult\":\"Groovy Rocks!\",\"outputText\":\"\",\"stacktraceText\":\"\"}");
    }
}
