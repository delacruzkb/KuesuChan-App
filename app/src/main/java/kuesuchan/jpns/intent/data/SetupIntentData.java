package kuesuchan.jpns.intent.data;

import java.util.List;

public class SetupIntentData {

    private List<String> writingStyles;
    private List<String> sections;
    private List<String> activities;
    private String source;
    private boolean allSections;
    private boolean graded;
    private int amount;

    public List<String> getWritingStyles() {
        return writingStyles;
    }

    public void setWritingStyles(List<String> writingStyles) {
        this.writingStyles = writingStyles;
    }

    public List<String> getSections() {
        return sections;
    }

    public void setSections(List<String> sections) {
        this.sections = sections;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isAllSections() {
        return allSections;
    }

    public void setAllSections(boolean allSections) {
        this.allSections = allSections;
    }

    public boolean isGraded() {
        return graded;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
