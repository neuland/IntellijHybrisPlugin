package de.neuland.hybris.util.application;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.impl.ContentImpl;
import icons.ApplicationIcon;

import javax.swing.*;
import java.awt.*;

public class ConsoleToolWindowUtil {

    private static ConsoleToolWindowUtil instance = new ConsoleToolWindowUtil();

    private ToolWindow toolWindow;

    private String[] consoleName;

    private String[] consoleDescription;

    private ConsoleToolWindowUtil() {
    }

    public static ConsoleToolWindowUtil getInstance() {
        return instance;
    }

    public void showConsoleToolWindow(Project project, ConsoleView outputConsole, ConsoleView resultConsole, ConsoleView stackConsole, String title) {
        Integer currentSelectedContentIndex = 0;
        if(toolWindow == null) {
            createNewToolWindow(project, title);
        } else if(!toolWindow.getTitle().equals(title)) {
            ToolWindow window = ToolWindowManager.getInstance(project).getToolWindow(title);
            if(window == null) {
                createNewToolWindow(project, title);
            } else {
                toolWindow = window;
                currentSelectedContentIndex = getCurrentSelectedTab();
                toolWindow.getContentManager().removeAllContents(false);
            }
        } else {
            currentSelectedContentIndex = getCurrentSelectedTab();
            toolWindow.getContentManager().removeAllContents(false);
        }
        setConsolesInToolWindow(outputConsole, resultConsole, stackConsole);
        toolWindow.activate(null);
        selectOldSelectedTab(currentSelectedContentIndex);
    }

    public void setConsoleName(String[] consoleName) {
        this.consoleName = consoleName;
    }

    public void setConsoleDescription(String[] consoleDescription) {
        this.consoleDescription = consoleDescription;
    }

    private void selectOldSelectedTab(Integer currentSelectedContentIndex) {
        Content contentToSelect = toolWindow.getContentManager().getContent(currentSelectedContentIndex);
        toolWindow.getContentManager().setSelectedContent(contentToSelect);
    }

    private void setConsolesInToolWindow(ConsoleView outputConsole, ConsoleView resultConsole, ConsoleView stackConsole) {
        Content resultConsoleContent = createConsoleContent(resultConsole, consoleName[0], consoleDescription[0]);
        Content outputConsoleContent = createConsoleContent(outputConsole, consoleName[1], consoleDescription[1]);
        Content stackConsoleContent = createConsoleContent(stackConsole, consoleName[2], consoleDescription[2]);

        toolWindow.getContentManager().addContent(resultConsoleContent);
        toolWindow.getContentManager().addContent(outputConsoleContent);
        toolWindow.getContentManager().addContent(stackConsoleContent);
    }

    private Integer getCurrentSelectedTab() {
        Integer currentSelectedContentIndex;
        Content currentSelectedContent = toolWindow.getContentManager().getSelectedContent();
        if(currentSelectedContent == null) {
            return 0;
        }
        currentSelectedContentIndex = toolWindow.getContentManager().getIndexOfContent(currentSelectedContent);
        return currentSelectedContentIndex;
    }

    private void createNewToolWindow(Project project, String title) {
        toolWindow = ToolWindowManager.getInstance(project).registerToolWindow(title, true, ToolWindowAnchor.BOTTOM);
        toolWindow.setTitle(title);
        toolWindow.setIcon(ApplicationIcon.HYBRIS_SMALL);
    }

    private Content createConsoleContent(ConsoleView console, String title, String description) {
        Content content = new ContentImpl(createConsolePanel(console), description, true);
        content.setTabName(title);
        content.setDisplayName(title);
        content.setIcon(ApplicationIcon.HYBRIS_CONSOLE);
        return content;
    }

    private JComponent createConsolePanel(ConsoleView view) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(view.getComponent(), BorderLayout.CENTER);
        return panel;
    }

}
