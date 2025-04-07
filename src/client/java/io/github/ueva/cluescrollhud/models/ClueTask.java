package io.github.ueva.cluescrollhud.models;

public class ClueTask {


    private final String objective;
    private final int amount;
    private int completed;

    public ClueTask(String objective, int amount, int completed) {
        this.objective = objective;
        this.amount = amount;
        this.completed = completed;
    }

    public ClueTask(String objective, int amount) {
        this(objective, amount, 0);
    }

    public String getObjective() {
        return objective;
    }

    public int getAmount() {
        return amount;
    }

    public int getCompleted() {
        return completed;
    }

    public int getPercentCompleted() {
        return (int) Math.floor((double) completed / amount * 100);
    }

    public boolean isCompleted() {
        return completed >= amount;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public String getFormattedObjective() {
        return objective.replace("%amount%", String.valueOf(amount));
    }

}
