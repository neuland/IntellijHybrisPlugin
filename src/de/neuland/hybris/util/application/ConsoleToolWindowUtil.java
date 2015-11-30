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

    public void showConsoleToolWindow(Project project, String title, ConsoleView... consoles) {
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
        setConsolesInToolWindow(consoles);
        toolWindow.activate(null);
        selectTab(currentSelectedContentIndex);
    }

    public void setConsoleName(String[] consoleName) {
        this.consoleName = consoleName;
    }

    public void setConsoleDescription(String[] consoleDescription) {
        this.consoleDescription = consoleDescription;
    }

    public void selectTab(Integer currentSelectedContentIndex) {
        Content contentToSelect = toolWindow.getContentManager().getContent(currentSelectedContentIndex);
        toolWindow.getContentManager().setSelectedContent(contentToSelect);
    }

    private void setConsolesInToolWindow(ConsoleView... consoles) {
        int i = 0;
        for (ConsoleView consoleView : consoles) {
            Content consoleContent = createConsoleContent(consoleView, consoleName[i], consoleDescription[i]);
            toolWindow.getContentManager().addContent(consoleContent);
            i++;
        }
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
