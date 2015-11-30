package de.neuland.hybris.util.execution;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import de.neuland.hybris.util.application.ConsoleToolWindowUtil;
import de.neuland.hybris.http.HybrisHTTPRequest;
import de.neuland.hybris.http.ServerAnwserTypes;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class ExecuteFlexsearchConsole extends ExecuteConsole {

    private static final String[] CONSOLE_NAMES = new String[]{"Search Result", "Execution statistics", "History", "Stacktrace"};
    private static final String[] CONSOLE_DESCRIPTIONS = new String[]{"Flexsearch Results", "Flexsearch execution statistics", "Flexsearch history", "Flexsearch Erros"};

    private ConsoleView executionStatisticsConsole;
    private ConsoleView searchResultConsole;
    private ConsoleView historyConsole;
    private ConsoleView stacktraceConsoleView;

    private static ExecuteFlexsearchConsole ourInstance = new ExecuteFlexsearchConsole();

    public static ExecuteFlexsearchConsole getInstance() {
        return ourInstance;
    }

    private ExecuteFlexsearchConsole() {
    }

    public void execute(final String documentContent, final Project project) {
        final HybrisHTTPRequest hybrisHttpRequest = HybrisHTTPRequest.getInstance();
        final String jSessionID = executePrepare(project);
        try {
            startSendRequestAnimationInAllConsoles();

            ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
                @Override
                public void run() {
                    String jsonResult = hybrisHttpRequest.executeFlexsearchScript(documentContent, jSessionID);
                    markRequestAsFinish();
                    try {
                        JSONObject responseJson = new JSONObject(jsonResult);
                        if(!responseJson.has("exception") || responseJson.isNull("exception")) {
                            updateConsoleWindow(hybrisHttpRequest.getHybrisFlexsearchConsoleOutput(jsonResult, ServerAnwserTypes.SEARCH_RESULT), ConsoleViewContentType.NORMAL_OUTPUT, searchResultConsole);
                            updateConsoleWindow(hybrisHttpRequest.getHybrisFlexsearchConsoleOutput(jsonResult, ServerAnwserTypes.EXECUTION_STATISTICS), ConsoleViewContentType.NORMAL_OUTPUT, executionStatisticsConsole);
                            updateConsoleWindow(hybrisHttpRequest.getHybrisFlexsearchConsoleOutput(jsonResult, ServerAnwserTypes.HISTORY), ConsoleViewContentType.NORMAL_OUTPUT, historyConsole);
                            ConsoleToolWindowUtil.getInstance().selectTab(0);
                        } else {
                            StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
                            JBPopupFactory.getInstance().createHtmlTextBalloonBuilder("Error inside flexible search", MessageType.ERROR, null).setFadeoutTime(7500)
                                    .createBalloon().show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
                            updateConsoleWindow(hybrisHttpRequest.getHybrisFlexsearchConsoleOutput(jsonResult, ServerAnwserTypes.FLEXSEARCH_EXCEPTION), ConsoleViewContentType.ERROR_OUTPUT, stacktraceConsoleView);
                            ConsoleToolWindowUtil.getInstance().selectTab(3);
                        }
                    } catch (JSONException e) {
                        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
                        JBPopupFactory.getInstance().createHtmlTextBalloonBuilder("Error while parsing FlexibleSearch response", MessageType.ERROR, null).setFadeoutTime(7500)
                                .createBalloon().show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Messages.showMessageDialog(project, "Their was am error by executing the script", "Error", Messages.getErrorIcon());
            updateConsoleWindow("The reason could be:\n-Request timeout\n-Wrong userdata\n-No connection to the server", ConsoleViewContentType.ERROR_OUTPUT);
            updateConsoleWindow(e.getLocalizedMessage(), ConsoleViewContentType.ERROR_OUTPUT);
            updateConsoleWindow(Arrays.toString(e.getStackTrace()), ConsoleViewContentType.ERROR_OUTPUT);

            updateConsoleWindow("The reason could be:\n-Wrong userdata\n-No connection to the server\n", ConsoleViewContentType.ERROR_OUTPUT, searchResultConsole);
            updateConsoleWindow(e.getLocalizedMessage(), ConsoleViewContentType.ERROR_OUTPUT, historyConsole);
            updateConsoleWindow(Arrays.toString(e.getStackTrace()), ConsoleViewContentType.ERROR_OUTPUT, historyConsole);
        }
        ConsoleToolWindowUtil.getInstance().setConsoleName(CONSOLE_NAMES);
        ConsoleToolWindowUtil.getInstance().setConsoleDescription(CONSOLE_DESCRIPTIONS);
        ConsoleToolWindowUtil.getInstance().showConsoleToolWindow(project, "Hybris Flexsearch Console", searchResultConsole, executionStatisticsConsole, historyConsole, stacktraceConsoleView);
    }

    public void initConsoleWindow(Project project) {
        executionStatisticsConsole = createConsole(project);
        searchResultConsole = createConsole(project);
        historyConsole = createConsole(project);
        stacktraceConsoleView = createConsole(project);
    }
}
