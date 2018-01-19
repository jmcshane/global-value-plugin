package org.jenkinsci.plugins.sample;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import org.jenkinsci.plugins.workflow.cps.CpsScript;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.SynchronousStepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.Collections;
import java.util.Set;

public class GlobalValueStep extends Step {
    private final CpsScript scriptContext;
    private final String name;
    private final Object value;

    @DataBoundConstructor
    public GlobalValueStep(CpsScript scriptContext, String name, Object value) {
        this.scriptContext = scriptContext;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public CpsScript getScriptContext() {
        return scriptContext;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new Execution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public String getFunctionName() {
            return "globals";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return Collections.emptySet();
        }
    }

    public static class Execution extends SynchronousStepExecution<Void> {

        @SuppressFBWarnings(value="SE_TRANSIENT_FIELD_NOT_RESTORED", justification="Only used when starting.")
        private transient final GlobalValueStep step;

        Execution(GlobalValueStep step, StepContext context) {
            super(context);
            this.step = step;
        }

        @Override protected Void run() throws Exception {
            step.scriptContext.getBinding().setVariable(step.name, step.value);
            return null;
        }

        private static final long serialVersionUID = 1L;

    }

}
