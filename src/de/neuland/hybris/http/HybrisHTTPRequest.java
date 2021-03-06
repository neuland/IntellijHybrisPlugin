package de.neuland.hybris.http;

import com.bethecoder.ascii_table.ASCIITable;
import de.neuland.hybris.http.helper.CookieParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HybrisHTTPRequest {
    private static HybrisHTTPRequest ourInstance = new HybrisHTTPRequest();
    private final static String FLEXSEARCH_CONSOLE_EXECUTE_URL = "/console/flexsearch/execute";
    private final static String GROOVY_CONSOLE_EXECUTE_URL = "/console/groovy/execute";
    private final static String BEANSHELL_CONSOLE_EXECUTE_URL = "/console/beanshell/execute";
    private final static String HYBRIS_5_CONSOLE_EXECUTE_URL = "/console/scripting/execute";
    private final static String HYBRIS_LOGIN_URL = "/j_spring_security_check";
    private String serverURL;
    //Variablen für Flexsearch
    private String username;
    private String maxCount;
    private String localeISOCode;
    private boolean hybrisVersion5OrAbove;

    public static HybrisHTTPRequest getInstance() {
        return ourInstance;
    }

    private HybrisHTTPRequest() {

    }

    public boolean isHybrisServerURLSet() {
        return serverURL != null;
    }

    public void setLocaleISOCode(String localeISOCode) {
        this.localeISOCode = localeISOCode;
    }

    public void setMaxCount(String maxCount) {
        this.maxCount = maxCount;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isHybrisVersion5OrAbove() {
        return hybrisVersion5OrAbove;
    }

    public void setHybrisVersion5OrAbove(boolean hybrisVersion5OrAbove) {
        this.hybrisVersion5OrAbove = hybrisVersion5OrAbove;
    }

    public boolean isUserdataSet() {
        HTTPRequestManager httpRequestManager = HTTPRequestManager.getInstance();
        return  httpRequestManager.isUserDataSet();
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String executeFlexsearchScript(String script, String jSessionID) {
        HTTPRequestManager httpRequestManager = HTTPRequestManager.getInstance();
        List<NameValuePair> scriptParameter = new ArrayList<NameValuePair>();
        if(isHybrisVersion5OrAbove()) {
            scriptParameter.add(new BasicNameValuePair("scriptType", "flexibleSearch"));
            scriptParameter.add(new BasicNameValuePair("commit", "false"));
        }
        scriptParameter.add(new BasicNameValuePair("flexibleSearchQuery", script));
        scriptParameter.add(new BasicNameValuePair("user", username));
        scriptParameter.add(new BasicNameValuePair("locale", localeISOCode));
        scriptParameter.add(new BasicNameValuePair("maxCount", maxCount));
        scriptParameter.add(new BasicNameValuePair("sqlQuery", ""));
        return httpRequestManager.doPostRequestWithCookie(serverURL + (isHybrisVersion5OrAbove() ? HYBRIS_5_CONSOLE_EXECUTE_URL : FLEXSEARCH_CONSOLE_EXECUTE_URL), jSessionID, scriptParameter);
    }

    public String executeGroovyScript(String script, String jSessionID) {
        HTTPRequestManager httpRequestManager = HTTPRequestManager.getInstance();
        List<NameValuePair> scriptParameter = new ArrayList<NameValuePair>();
        if(isHybrisVersion5OrAbove()) {
            scriptParameter.add(new BasicNameValuePair("scriptType", "groovy"));
            scriptParameter.add(new BasicNameValuePair("commit", "false"));
        }
        scriptParameter.add(new BasicNameValuePair("script", script));
        return httpRequestManager.doPostRequestWithCookie(serverURL + (isHybrisVersion5OrAbove() ? HYBRIS_5_CONSOLE_EXECUTE_URL : GROOVY_CONSOLE_EXECUTE_URL), jSessionID, scriptParameter);
    }

    public String executeBeanshellScript(String script, String jSessionID) {
        HTTPRequestManager httpRequestManager = HTTPRequestManager.getInstance();
        List<NameValuePair> scriptParameter = new ArrayList<NameValuePair>();
        if(isHybrisVersion5OrAbove()) {
            scriptParameter.add(new BasicNameValuePair("scriptType", "beanshell"));
            scriptParameter.add(new BasicNameValuePair("commit", "false"));
        }
        scriptParameter.add(new BasicNameValuePair("script", script));
        return httpRequestManager.doPostRequestWithCookie(serverURL + (isHybrisVersion5OrAbove() ? HYBRIS_5_CONSOLE_EXECUTE_URL : BEANSHELL_CONSOLE_EXECUTE_URL), jSessionID, scriptParameter);
    }

    public String getJSessionID(String username, String password) {
        HTTPRequestManager httpRequestManager = HTTPRequestManager.getInstance();
        httpRequestManager.setUsername(username);
        httpRequestManager.setPassword(password);
        return getJSessionID();
    }

    public String getJSessionID() {
        HTTPRequestManager httpRequestManager = HTTPRequestManager.getInstance();
        List<NameValuePair> loginParameterPair = httpRequestManager.createLoginDataPair("j_username","j_password");
        String cookieString = httpRequestManager.doLoginForCookie(serverURL + HYBRIS_LOGIN_URL, loginParameterPair);
        return CookieParser.getInstance().getSpecialCookie(cookieString, "JSESSIONID");
    }

    public String getHybrisConsoleOutput(String json, ServerAnwserTypes type) {
        if(type == ServerAnwserTypes.EXECUTION_RESULT) {
            return returnFromJSON(json, "executionResult");
        }
        if(type == ServerAnwserTypes.OUTPUT_TEXT) {
            return returnFromJSON(json, "outputText");
        }
        if(type== ServerAnwserTypes.STACKTRACE_TEXT) {
            return returnFromJSON(json, "stacktraceText");
        }
        return null;
    }

    public String getHybrisFlexsearchConsoleOutput(String json, ServerAnwserTypes type) {
        if(type == ServerAnwserTypes.SEARCH_RESULT) {
            String ascii = returnAsciiTableFromJSON(json, "headers", "resultList");
            System.out.println(ascii);
            return ascii;
        }
        if(type == ServerAnwserTypes.EXECUTION_STATISTICS) {
            return "Katalogversion:" + returnFromJSON(json, "catalogVersionsAsString") +
                    "\nAusführungszeit: " + returnFromJSON(json, "executionTime") + "ms" +
                    "\nErsetzter Parameter" + returnFromJSON(json, "parametersAsString");
        }
        if(type == ServerAnwserTypes.HISTORY) {
            return returnFromJSON(json, "query");
        }
        if(type == ServerAnwserTypes.FLEXSEARCH_EXCEPTION) {
            return returnFlexsearchStacktraceFromJSON(json);
        }
        return null;
    }

    public boolean isHybrisServerURLEqual(String otherServerURL) {
        return serverURL.equals(otherServerURL);
    }

    private String returnAsciiTableFromJSON(String json, String headers, String resultList) {
        JSONObject jsonObject = new JSONObject(json);
        List<Object> headersList = jsonObject.getJSONArray(headers).getMyArrayList();

        JSONArray results = jsonObject.getJSONArray(resultList);
        Integer resultLength = results.length();
        String[][] values = new String[resultLength][headersList.size()];

        Integer i = 0;
        for(Object o : results.getMyArrayList()) {
            JSONArray current = (JSONArray)o;
            Integer j = 0;
            for(Object o2 : current.getMyArrayList()) {
                if(o2 instanceof String) {
                    String jsonO = (String)o2;
                    values[i][j] = jsonO;
                } else if(o2 instanceof JSONObject) {
                    JSONObject jsonO = (JSONObject)o2;
                    values[i][j] = jsonO.toString();
                } else {
                    values[i][j] = "";
                }
                j++;
            }
            i++;
        }

        String[] headerRow = new String[headersList.size()];
        i = 0;
        for(Object o : headersList) {
            if(o instanceof String) {
                String current = (String)o;
                headerRow[i] = current;
            } else {
                o.getClass().toString();
                headerRow[i] = "";
            }
            i++;
        }

        return ASCIITable.getInstance().getTable(headerRow, values);
    }

    private String returnFlexsearchStacktraceFromJSON(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject exceptionJson = jsonObject.getJSONObject("exception");
        StringBuilder sb = new StringBuilder();
        sb.append("Exception message: ").append(exceptionJson.getString("message")).append("\n\nStacktrace:\n");

        JSONArray stackTraceJson = exceptionJson.getJSONArray("stackTrace");
        for(int i = 0; i < stackTraceJson.length(); i++) {
            JSONObject currentStackTraceEntry = stackTraceJson.getJSONObject(i);
            Object className = currentStackTraceEntry.get("className");
            Object methodName = currentStackTraceEntry.get("methodName");
            Object fileName = currentStackTraceEntry.get("fileName");
            int lineNumber = currentStackTraceEntry.getInt("lineNumber");
            sb.append((className instanceof String) ? (String) className : className.toString()).append(".")
                    .append((methodName instanceof String) ? (String) methodName : methodName.toString()).append("(")
                    .append((fileName instanceof String) ? (String) fileName : fileName.toString()).append(":")
                    .append(lineNumber).append(")").append("\n");
        }
        return sb.toString();
    }

    private String returnFromJSON(String json, String type) {
        JSONObject jsonObject = new JSONObject(json);
        if(jsonObject.get(type) instanceof String) {
            return jsonObject.getString(type);
        }
        return jsonObject.get(type) + "";
    }


}
