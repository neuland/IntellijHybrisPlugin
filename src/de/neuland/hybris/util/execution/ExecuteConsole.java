package de.neuland.hybris.util.execution;

import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import de.neuland.hybris.http.HybrisHTTPRequest;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ExecuteConsole {

    private List<ConsoleView> consoleViewList;
    private boolean processComplete = false;

    public ExecuteConsole() {
        super();
        consoleViewList = new ArrayList<ConsoleView>();
    }

    public abstract void execute(String documentContent, Project project);

    public abstract void initConsoleWindow(Project project);

    public void clearConsole() {
        for(ConsoleView consoleView : consoleViewList) {
            consoleView.clear();
        }
    }

    public void updateConsoleWindow(String text, ConsoleViewContentType type, ConsoleView currentConsole) {
        currentConsole.print(text, type);
    }

    public void updateConsoleWindow(String text, ConsoleViewContentType type) {
        consoleViewList.get(0).print(text, type);
    }

    public void updateConsoleWindow(String text) {
        updateConsoleWindow(text, ConsoleViewContentType.NORMAL_OUTPUT);
    }

    protected void startSendRequestAnimationInAllConsoles() {
        processComplete = false;
        clearAllConsoles();
        ApplicationManager.getApplication().executeOnPooledThread(new SendRequestAnimation());
    }

    protected void markRequestAsFinish() {
        processComplete = true;
        clearAllConsoles();
    }

    protected void fillAllConsolesWithTheSameContent(String content) {
        for (ConsoleView consoleView : consoleViewList) {
            consoleView.print(content, ConsoleViewContentType.NORMAL_OUTPUT);
        }
    }

    protected void clearAllConsoles() {
        for (ConsoleView consoleView : consoleViewList) {
            consoleView.clear();
        }
    }

    protected String executePrepare(Project project) {
        HybrisHTTPRequest hybrisHttpRequest = HybrisHTTPRequest.getInstance();
        if(!hybrisHttpRequest.isHybrisServerURLSet()) {
            String hybrisURL = Messages.showInputDialog(project, "Hybris URL, e.g. http://localhost:9001", "Input the Hybris URL", Messages.getQuestionIcon());
            if(hybrisURL != null) {
                hybrisHttpRequest.setServerURL(hybrisURL);
            } else {
                Messages.showMessageDialog(project, "Server request aborted. Caused by an invalid hybris URL", "Error", Messages.getErrorIcon());
                return null;
            }
        }
        String jSessionID;
        if(!hybrisHttpRequest.isUserdataSet()) {
            String hacUsername = Messages.showInputDialog(project, "HAC Username", "HAC Username", Messages.getQuestionIcon());
            String hacPassword = Messages.showPasswordDialog(project, "HAC Password", "HAC Password", Messages.getQuestionIcon());
            if(hacUsername != null && hacPassword != null) {
                jSessionID = hybrisHttpRequest.getJSessionID(hacUsername, hacPassword);
            } else {
                Messages.showMessageDialog(project, "Server request aborted. Caused by an invalid login data", "Error", Messages.getErrorIcon());
                return null;
            }
        } else {
            jSessionID = hybrisHttpRequest.getJSessionID();
        }

        if(consoleViewList.isEmpty()) {
            initConsoleWindow(project);
        }

        clearConsole();
        return jSessionID;
    }

    protected ConsoleView createConsole(Project project) {
        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        ConsoleView thisConsole = consoleBuilder.getConsole();
        consoleViewList.add(thisConsole);
        return thisConsole;
    }

    private class SendRequestAnimation implements Runnable {
        @Override
        public void run() {
            int i = 0;
            while(!processComplete) {
                if(i > 5) {
                    i = 0;
                    clearAllConsoles();
                    fillAllConsolesWithTheSameContent("Send request");
                } else {
                    String dots = "";
                    for(int j = 0; j < i; j++) {
                        dots += ".";
                    }
                    clearAllConsoles();
                    fillAllConsolesWithTheSameContent("Send request" + dots);
                }
                i++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
