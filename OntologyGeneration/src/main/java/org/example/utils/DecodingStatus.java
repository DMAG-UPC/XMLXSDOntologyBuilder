package org.example.utils;

public class DecodingStatus {
    private boolean insideNamedIndividual;
    private boolean insideRequirement;
    private boolean insideRequirementName;
    private boolean hasWrittenCVEs;

    public DecodingStatus() {
        insideNamedIndividual = false;
        insideRequirement = false;
        insideRequirementName = false;
        hasWrittenCVEs = false;
    }

    public boolean isInsideNamedIndividual() {
        return insideNamedIndividual;
    }

    public void setInsideNamedIndividual(boolean insideNamedIndividual) {
        this.insideNamedIndividual = insideNamedIndividual;
    }

    public boolean isInsideRequirement() {
        return insideRequirement;
    }

    public void setInsideRequirement(boolean insideRequirement) {
        this.insideRequirement = insideRequirement;
    }

    public boolean isInsideRequirementName() {
        return insideRequirementName;
    }

    public void setInsideRequirementName(boolean insideRequirementName) {
        this.insideRequirementName = insideRequirementName;
    }

    public boolean isHasWrittenCVEs() {
        return hasWrittenCVEs;
    }

    public void setHasWrittenCVEs(boolean hasWrittenCVEs) {
        this.hasWrittenCVEs = hasWrittenCVEs;
    }
}