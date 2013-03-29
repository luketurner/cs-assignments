As-yet-incomplete Othello-playing A.I.

**Requires:** Python 2.7 w/tkinter

## To Run GUI Game

	python othello/game.py

## Architecture 

Has this general construction:

Game logic is programmed in the game.AbstractGame class, including initialization, making changes, calculating scores, determining swaps from a certain tile placement, etc.

Special stuff for the GUI is in game.GuiGame, which overwrites some methods of AbstractGame to work with a GUI and adds "undo" functionality.

Actual GUI creation and handling is in board.BoardWindow, which subclasses tk.Frame. It's intended to be called in a separate process via multiprocessing, which is how GuiGame calls it.

Another way of playing is the headless game client class headless.HeadlessGame. HeadlessGame init takes two paramters which designate the Player-subclassed classes to be playing each other. Headless games cannot use the plain old Player class.

## Write-Up

Our game and gui are defined by a few key characteristics. The GUI subclass is executed and runs in a separate process from the game logic process. Communication between the two is facilitated by a so-called interprocess "pipe." In general, the GUI recieves user input and alerts the game process with relevant information about what the user requested. The game process takes action to respond to this input, and also controls the flow of the game. Communication from the game process to the board process almost always consists of dictionaries representing board configurations & game states. A typical state dictionary sent from the game process contains information about the turn, the score, the player's move, the resulting changes in the board (from captures), and the current move index (used for undo).

In short, the game class is aware of the game state, and spends most of its time waiting for input from the GUI board. When it receives such input, it considers it in the context of the game state, takes actions to update its local game information, and sends a data map containing relevant information to the GUI board, which takes the data and updates its interface accordingly.

Game state stored in the game class includes not only the current board state, move index, and player turn, but also all previous board states and move data. This data is used to undo moves that may have resulted from a mistake. When the GUI receives an undo request, it sends it to the board, which resets its current game states to the "undone" states, and sends the reset data back to the board for display. There is no undo tree: once a move is undone, it is gone forever.

In order to allow the game to continue to receive information from the board while the CPU player is "thinking" (for instance, information about undo or board window close), it spools off the player's turn into a separate process as well, and then synchronizes with it when it is finished.

Player classes implement a very simple interface: their constructor accepts an argument telling them their player ID, and they have a take_turn(board,out) function that the game calls. The player class can send False, which passes the move to the physical user, or can send a board index which represents their choice of move ([-1, -1] represents a forfeit move). Thus, the player AI doesn't have to concern itself with game mechanics at all.