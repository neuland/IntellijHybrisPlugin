package de.neuland.hybris.run.action.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public class HybrisRunProfileState implements RunProfileState {
    public HybrisRunProfileState(Project project, ExecutionEnvironment environment, Executor executor, String serverUrl, String hacUser, String hacPassword, String maxCount, String localISOCode) {
    }

    @Nullable
    @Override
    public ExecutionResult execute(Executor executor, ProgramRunner programRunner) throws ExecutionException {
        return null;
    }
}
