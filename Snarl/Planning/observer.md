The goal of the Observer component is to provide a rendered view of the game to a third party. In order to accomplish this goal, the component will need three entities. An Observer abstract class, and extending View class, and a Subject class. The Subject class has two fields, a list of Observers and a GameManger. The GameManager represents the GameManager object running the game. Please see the game-manager.md for any clarifications about that object’s design. Additionally, the Subject has the following methods:
<br>
- Void attach(Observer observer)
   - This method ‘attaches’ an observer to the subject. The given observer is added to the list of Observers
- Void detach(Observer observer)
   - This method removes the given observer from the Subject’s list of observers
- Void notifyObservers()
   - Iterate through all the observers and call observer.update()
<br>
The Observer will have the following methods:
- Void update()
  - This method updates all the information needed to render the view to the 3rd party.
  - This method may be expanded to take in parameters relevant to rendering. It will be called by the Subject in notifyObservers().
<br>
The View class, as it extends the Observer abstract class, will be privy to the Observer’s implementation of update. The View has a single String field representation that represents the rendered view of the game in ASCII form. This is what the 3rd party will be privy to in order to view the current state of the game. This design is easily extendable, as we could replace the string view with a more complex UI and still maintain the other functionality of the Observer.
<br>
Please consult the below UML diagram for any questions about this components structure.
<br>
<img src="Milestone5UML.png" alt="UML Diagram" />
