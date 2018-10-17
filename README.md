# Orbital4
## Connect-4 in four directions around a single point.

For a .jar you can run right away, [click this link](https://github.com/dog-eared/Orbital4/raw/master/dist/Orbital4.jar).

![Screenshot of Orbital4](https://raw.githubusercontent.com/dog-eared/Orbital4/master/src/style/Orbital4.png)

Orbital4 is a variation on Connect4 wherein players can drop pieces in four directions onto a single point
in the middle of the screen.

Orbital4 was written in Java for COMP10062 (Programming in Java) at Mohawk College. The project was open ended, but
the specifications were:
- the view can respond to button presses to call methods in the model, but otherwise not implement any other logic 
- uses at least 3 different JavaFX component types (label, button, textfield, etc)
- the styling of the default GUI must be modified 

=======
## HOW TO PLAY:

Players take turns dropping pieces by clicking the buttons around the main game board.

Pieces travel in a straight line from the button to the other side. They must connect to another piece to either the left, right,
above or below to be valid.

The first player to get four in a row in any direction (including diagonals) is the winner.

If you get 4 in a row in multiple directions, you get multiple points (and the other player feels even dumber). 

## THINGS I'D LIKE TO DO:
- implement a computer controlled player
- add visual display of winning combinations
- add customizable player colours
- revamp GUI
- neverending mode, where the game isn't over when someone gets 4 in a row
- pieces smoothly sliding in from off screen

**This project is complete for the time being.**
