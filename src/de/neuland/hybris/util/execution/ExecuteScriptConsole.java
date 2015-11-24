package de.neuland.hybris.util.execution;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import de.neuland.hybris.util.application.ConsoleToolWindowUtil;
import de.neuland.hybris.http.HybrisHTTPRequest;
import de.neuland.hybris.http.ServerAnwserTypes;

import java.util.Arrays;

public abstract class ExecuteScriptConsole extends ExecuteConsole {
    protected static final String[] CONSOLE_NAMES = new String[]{"Result", "Output", "Stacktrace"};

    private ConsoleView outputConsole;
    private ConsoleView resultConsole;
    private ConsoleView stackConsole;

    public ExecuteScriptConsole() {
        super();
    }

    public void initConsoleWindow(Project project) {
        outputConsole = createConsole(project);
        resultConsole = createConsole(project);
        stackConsole = createConsole(project);
    }

    protected void fillConsolesWithTheResult(String jsonResult) {
        clearConsole();
        HybrisHTTPRequest hybrisHttpRequest = HybrisHTTPRequest.getInstance();
        updateConsoleWindow(hybrisHttpRequest.getHybrisConsoleOutput(jsonResult, ServerAnwserTypes.EXECUTION_RESULT), ConsoleViewContentType.NORMAL_OUTPUT, resultConsole);
        updateConsoleWindow(hybrisHttpRequest.getHybrisConsoleOutput(jsonResult, ServerAnwserTypes.OUTPUT_TEXT), ConsoleViewContentType.NORMAL_OUTPUT, outputConsole);
        updateConsoleWindow(hybrisHttpRequest.getHybrisConsoleOutput(jsonResult, ServerAnwserTypes.STACKTRACE_TEXT), ConsoleViewContentType.ERROR_OUTPUT, resultConsole);
    }

    protected void fillExceptionConsole(Exception e, Project project) {
        clearConsole();
        Messages.showMessageDialog(project, "Their was am error by executing the script", "Error", Messages.getErrorIcon());
        updateConsoleWindow("The reason could be:\n-Request timeout\n-Wrong userdata\n-No connection to the server", ConsoleViewContentType.ERROR_OUTPUT);
        updateConsoleWindow(e.getLocalizedMessage(), ConsoleViewContentType.ERROR_OUTPUT);
        updateConsoleWindow(Arrays.toString(e.getStackTrace()), ConsoleViewContentType.ERROR_OUTPUT);

        updateConsoleWindow("The reason could be:\n-Wrong userdata\n-No connection to the server\n", ConsoleViewContentType.ERROR_OUTPUT, resultConsole);
        updateConsoleWindow(e.getLocalizedMessage(), ConsoleViewContentType.ERROR_OUTPUT, stackConsole);
        updateConsoleWindow(Arrays.toString(e.getStackTrace()), ConsoleViewContentType.ERROR_OUTPUT, stackConsole);
    }

    protected void showTabs(Project project, String tabHeadline, String[] consoleDescriptions) {
        ConsoleToolWindowUtil.getInstance().setConsoleName(CONSOLE_NAMES);
        ConsoleToolWindowUtil.getInstance().setConsoleDescription(consoleDescriptions);
        ConsoleToolWindowUtil.getInstance().showConsoleToolWindow(project, outputConsole, resultConsole, stackConsole, tabHeadline);
    }

}
