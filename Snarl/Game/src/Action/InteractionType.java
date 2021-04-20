package Action;

public enum InteractionType {

  EJECT ("eject"), // todo: ATTACK
  EXIT ("exit"),
  OK ("ok"),
  KEY ("key"),
  ATTACK ("attack"),
  INVALID ("invalid"),
  NONE ("none");

  public String type;
  InteractionType(String type) {
    this.type = type;
  }

}
