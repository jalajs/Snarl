package RuleChecker;

public enum GameCondition {
  WIN("Win"),
  LOSS("Loss"),
  ONGOING("Ongoing");

  public String condition;
  GameCondition(String condition) {
    this.condition = condition;
  }
}
