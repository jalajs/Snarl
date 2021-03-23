package Observer;

import java.util.List;

import GameState.GameState;

/**
 * This subject manages all the observers and notifies them. The game manager has a Subject, and can easily notify
 * all the observers of updates to the gamestate by using it.
 *
 */
public class Subject {
  private List<Observer> observers;

  /**
   * This method ‘attaches’ an observer to the subject. The given observer is added to the list of Observers
   * @param observer
   */
  public void attach(Observer observer){
    observers.add(observer);
  }

  /**
   * This method removes the given observer from the Subject’s list of observers
   * @param observer
   */
  public void detach(Observer observer) {
    observers.remove(observer);
  }

  /**
   * Iterate through all the observers and call observer.update()
   */
  public void notifyObservers(GameState gameState) {
    for(Observer observer: observers) {
      observer.update(gameState);
    }
  }

}
