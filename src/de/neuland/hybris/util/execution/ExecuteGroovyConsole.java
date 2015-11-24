package de.neuland.hybris.util.execution;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import de.neuland.hybris.http.HybrisHTTPRequest;

public class ExecuteGroovyConsole extends ExecuteScriptConsole {

    private static final String[] CONSOLE_DESCRIPTIONS = new String[]{"Hybris Groovy result", "Hybris Groovy output", "Hybris Groovy stacktrace"};

    private static ExecuteGroovyConsole instance = new ExecuteGroovyConsole();

    private ExecuteGroovyConsole() {
    }

    public static ExecuteGroovyConsole getInstance() {
        return instance;
    }

    public void execute(final String documentContent, final Project project) {
        final HybrisHTTPRequest hybrisHttpRequest = HybrisHTTPRequest.getInstance();
        final String jSessionID = executePrepare(project);
        if (jSessionID == null) {
            return;
        }
        try {
            startSendRequestAnimationInAllConsoles();
            ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
                @Override
                public void run() {
                    String jsonResult = hybrisHttpRequest.executeGroovyScript(documentContent, jSessionID);
                    markRequestAsFinish();
                    fillConsolesWithTheResult(jsonResult);
                }
            });
        } catch (Exception e) {
            fillExceptionConsole(e, project);
        }
        showTabs(project, "Hybris Groovy Console", CONSOLE_DESCRIPTIONS);
    }

}
