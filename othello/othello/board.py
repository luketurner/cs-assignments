from multiprocessing import Process
from ScrolledText import ScrolledText
import Tkinter as tk
import tkMessageBox

class BoardWindow(tk.Frame):

	square_side = 45
	def __init__(self, pipe, colors, master=None):
		#instance variables
		self.game_pipe = pipe
		self.colors = colors
		self.player = 1
		self.turn = 1
		self.moves_made = 0

		#initialize gui
		tk.Frame.__init__(self, master)
		self.bind("<Destroy>", self.__window_close)
		self.grid()
		self.__create_widgets()
		self.game_pipe.send(self.__order_selection_dialog())
		self.__draw_turns()
		self.__get_initial_state()
		self.mainloop()

	def __get_initial_state(self):
		self.game_pipe.send("initial_state")
		moves = self.game_pipe.recv()
		self.__make_moves(moves)

	def __create_widgets(self):
		tk.Label(self, text="Othellobot", font=('Arial', -17, 'italic')).grid(column=0, row=0)
		tk.Label(self, text="by Luke Turner and Antonio Ledesma", font=('Arial', -12)).grid(column=0, row=1)

		self.forfeit_move_button = tk.Button(self, text="Forfeit Move", command=self.__board_doubleclick)
		self.forfeit_move_button.grid(column=1,row=1)

		self.turns = tk.Canvas(self, width=410, height=65)
		self.turns.grid(column=1,row=0)

		self.undo_frame = tk.LabelFrame(self, text="Undo Moves", padx=5, pady=5)
		self.__fill_undo_frame()
		self.undo_frame.grid(column=2,row=0)

		self.board = tk.Canvas(self, width=370, height=370)
		self.board.grid(column=0, row=2)

		self.logbox = ScrolledText(self)
		self.logbox.grid(column=1, row=2, columnspan=2)
		
		self.__draw_board()

	def __window_close(self, e):
		self.game_pipe.send("END_GAME")
		self.game_pipe.close()

	def __fill_undo_frame(self):
		self.undo_button = tk.Button(self.undo_frame, text="Undo", command=self.__undo_moves)
		self.undo_button.grid(column=2,row=1)
		self.undo_move_string = tk.StringVar()
		self.undo_move_string.set('-1')
		tk.Entry(self.undo_frame, textvariable=self.undo_move_string, state=tk.NORMAL).grid(column=2,row=0)

	def __order_selection_dialog(self):
		choice = tkMessageBox.askquestion("Order Selector", "Do you want to be Player 1 (Red)?")
		if choice == u"yes":
			self.log("You are Player 1. The CPU is Player 2.\n")
			self.player = 1
		else:
			self.log("You are Player 2. The CPU is Player 1.\n")
			self.player = 2
		self.focus_force()
		return choice

	def __invalid_move_alert(self):
		tkMessageBox.showwarning("Invalid Move Choice", "Your choice of move was invalid. Please try again.")
		self.focus_force()

	def __invalid_undo_alert(self):
		tkMessageBox.showwarning("Invalid Undo", "The move index to undo to is invalid.")
		self.focus_force()

	def log(self, text):
		self.logbox.insert(tk.INSERT, text)

	def __draw_board(self):

		for col in range(1,9):
			for row in range(1,9):

				coords = [self.square_side*(col-1)+5, self.square_side*(row-1)+5, 
				(self.square_side*col)+5, (self.square_side*row)+5]
				tag = "ABCDEFGH"[col - 1] + str(row)

				self.board.create_rectangle(*coords, tags=tag+"R")
				self.board.create_oval(*coords, tags=tag+"C", outline="", activeoutline="black")
				self.board.create_text((coords[0]+coords[2])/2, 
					(coords[1]+coords[3])/2, 
					tags=tag+"T", text=tag)

		self.board.bind("<Double-Button-1>", self.__board_doubleclick)
		self.log("CONTROLS: Double-click on square during your turn to place your tile.\n" +
			"          Double-click anywhere during CPU turn to give it permission to move.\n" +
			"Beginning Game. Initial turn goes to player 1.\n")

	def __draw_turns(self):
		self.turns.create_rectangle(5, 5, 200, 55, tags=("1", "R"))
		self.turns.create_rectangle(200, 5, 400, 55, tags=("2", "R"))
		if self.player == 1:
			player_loc = 100
			cpu_loc = 300
		else:
			player_loc = 300
			cpu_loc = 100

		self.turns.create_text(player_loc, 23, text="Player", font=("Arial", -14, "bold"))
		self.turns.create_text(cpu_loc, 23, text="CPU", font=("Arial", -14, "bold"))
		self.turns.create_text(100, 40, text="2", tags="1T", font=("Arial", -14, "bold"))
		self.turns.create_text(300, 40, text="2", tags="2T", font=("Arial", -14, "bold"))
		self.__set_turn(1)

	def __set_turn(self, turn):
		self.turn = turn
		self.turns.itemconfigure("R", fill="")
		self.turns.itemconfigure(str(turn), fill="red")


	def __alter_game_state(self, game_data):
		if game_data["valid"]:
			logstring = "[MOVE " + str(game_data['moves_made']) + "] " + game_data["initial_move"]["color"]
			if game_data["initial_move"]["loc"] == "-1":
				# forfeit the move.
				logstring += " forfeits their move.\n"
			else:
				logstring += (" moves to " + str(game_data["initial_move"]["loc"]) + ".\n")
				self.__make_moves(game_data["moves"])

			if "undone" in game_data:
				logstring = "[UNDO] We're undoing back to move " + str(game_data['moves_made']) + ".\n"

			self.log(logstring)
			self.moves_made = game_data['moves_made']
			self.__set_turn(game_data["turn"])
			self.__update_scores(game_data["scores"])
		else:
			self.__invalid_move_alert()

	def __update_scores(self, scores):
		self.turns.itemconfigure("1T", text=scores[0])
		self.turns.itemconfigure("2T", text=scores[1])

	def __undo_moves(self):
		move = self.undo_move_string.get()
		try:
			move_number = int(move)
			if move_number < 0:
				move_number = self.moves_made + move_number
			if move_number < 0 or move_number >= self.moves_made:
				raise IndexError()

			self.game_pipe.send({'undo': True, 'move_number': move_number})
			board_setup = self.game_pipe.recv()
			while not 'undone' in board_setup: 
				board_setup = self.game_pipe.recv()
			self.__alter_game_state(board_setup)
			self.game_pipe.send(True)
			self.__alter_game_state(self.game_pipe.recv())
			self.game_pipe.send(True)
		except (ValueError, IndexError):
			self.__invalid_undo_alert()

	def __board_doubleclick(self, event=False):
		if not event:
			loc = "-1"
		else:
			obj = self.board.find_closest(event.x, event.y)[0]
			loc = self.board.gettags(obj)[0][:-1]

		if self.turn == self.player:
			# Player turn
			self.game_pipe.send(loc)
			if self.game_pipe.poll(0.25):
				game_data = self.game_pipe.recv()
				self.__alter_game_state(game_data)
		else:
			self.configure(cursor='wait')
			game_data = self.game_pipe.recv()
			self.__alter_game_state(game_data)
			self.game_pipe.send(True)
			self.configure(cursor='arrow')

	def __make_moves(self, moves):
		self.board.itemconfigure("H", fill="")
		self.board.dtag("H", "H")
		for move in moves:
			if move['color'] == '':
				color = '' #self.cget('background')
				border = ''
			else:
				color = move['color']
				border = 'black'
			self.board.itemconfigure(move['loc']+"C", fill=color, outline=border)
			self.board.addtag_withtag("red", move['loc']+"C")
			self.board.itemconfigure(move['loc']+"R", fill="yellow", outline="black")
			self.board.addtag_withtag("H", move['loc']+"R")
