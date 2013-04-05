from copy import deepcopy
from multiprocessing import Process, Pipe
from sys import exit
import operator as op
import re

from board import BoardWindow
from player import CPUPlayer, Player

class AbstractGame(object):
	def __init__(self, players):
		self.colors = ["", "red", "white"]

		self.board = self.build_board()

		self.players = players

		self.setup_game()

	def build_board(self):
		return [[0 for i in range(8)] for j in range(8)]

	def setup_game(self):
		self.board[3][3] = 1
		self.board[3][4] = 2
		self.board[4][4] = 1
		self.board[4][3] = 2

	def is_valid_move(self, coords, player):
		if coords == [-1, -1]:
			return True

		if not self.board[coords[0]][coords[1]] == 0:
			return False

		if len(self.get_swaps_from_move(coords, player)) > 0:
			return True

		return False

	def to_location(self, coords):
		if coords == [-1, -1]:
			return "-1"
		else:
			return "ABCDEFGH"[coords[0]]+str(coords[1]+1)

	def from_location(self, loc):
		if loc == "-1":
			return [-1, -1]
		else:
			return ["ABCDEFGH".find(loc[0]), int(loc[1]) - 1]

	def get_scores(self):
		scores = [0, 0]
		for i in self.board:
			for j in i:
				if j == 1:
					scores[0] += 1
				if j == 2:
					scores[1] += 1
		return scores

	def square_location_map(self, coords, index):
		return {'loc': self.to_location(coords), 'color': self.colors[index]}

	def get_swaps_from_move(self, coords, index):
		enemy_index = 3 - index
		x, y = coords
		size = len(self.board)
		moves = []

		def valid(x,y):
			return (0 <= x < size) and (0 <= y < size)

		def check_direction(opx, opy):
			i = 1
			to_change = []
			while valid(opx(x,i),opy(y,i)) and self.board[opx(x,i)][opy(y,i)] == enemy_index:
				to_change.append([opx(x,i), opy(y,i)])
				i += 1
			if valid(opx(x,i),opy(y,i)) and self.board[opx(x,i)][opy(y,i)] == index:
				for square in to_change:
					moves.append(self.square_location_map(square, index))

		def null(x,y):
			return x

		#diagonals
		check_direction(op.add, op.add)
		check_direction(op.sub, op.add) 
		check_direction(op.add, op.sub)
		check_direction(op.sub, op.sub)

		#straights
		check_direction(op.add, null)
		check_direction(op.sub, null)
		check_direction(null, op.add)
		check_direction(null, op.sub)

		return moves

	def alter_game_state(self, coords, index):
		# ASSUMES VALID MOVE
		
		enemy_index = 3 - index

		# Check for special case of forfeit move.
		if coords == [-1, -1]:
			moves = []
			initial_move = {"loc": "-1", "color": self.colors[index - 1]} 
		else:
			moves = self.get_swaps_from_move(coords, index)
			initial_move = self.square_location_map(coords, index)
			moves.append(initial_move)
			# Make changes to internal board
			for move in moves:
				coords = self.from_location(move['loc'])
				self.board[coords[0]][coords[1]] = index

	def full_board(self):
		for x in range(len(self.board)):
			for y in range(len(self.board)):
				if self.is_valid_move([x,y],1) or self.is_valid_move([x,y],2):
					return False
		return True

	def end_game(self):
		print("Game is over~")
		self.board_pipe.close()
		exit(0)


class GuiGame(AbstractGame):
	def __init__(self):
		self.moves_made = 0
		self.board_history = {}
		self.colors = ["", "red", "white"]
		self.board_pipe, game_pipe = Pipe()
		self.board_thread = Process(target=BoardWindow, args=(game_pipe,self.colors))
		self.board_thread.start()

		order_selection = self.board_pipe.recv()

		if order_selection == u"yes":
			#player first
			super(GuiGame, self).__init__([Player(1), CPUPlayer(2)])
		else:
			super(GuiGame, self).__init__([CPUPlayer(1), Player(2)])

		moves = [
				{"loc": "D4", "color": self.colors[1]},
				{"loc": "E4", "color": self.colors[2]},
				{"loc": "D5", "color": self.colors[2]},
				{"loc": "E5", "color": self.colors[1]},
				]

		if self.board_pipe.recv() == "initial_state":
			self.board_pipe.send(moves)
		self.board_history[0] = {'board': deepcopy(self.board), 'moves': moves, 'initial_move': {"loc": "N/A", "color": "N/A"}}

	def alter_game_state(self, coords, index):
		# ASSUMES VALID MOVE
		
		enemy_index = 3 - index

		# Check for special case of forfeit move.
		if coords == [-1, -1]:
			moves = []
			initial_move = {"loc": "-1", "color": self.colors[index - 1]} 
		else:
			moves = self.get_swaps_from_move(coords, index)
			initial_move = self.square_location_map(coords, index)
			moves.append(initial_move)
			# Make changes to internal board
			for move in moves:
				coords = self.from_location(move['loc'])
				self.board[coords[0]][coords[1]] = index

		# Store board state in history list.
		self.board_history[self.moves_made] = {'board': deepcopy(self.board), 'moves': moves, 'initial_move': initial_move}

		# Collect game data to send to board
		return {"moves": moves, 
			"turn": enemy_index, 
			"valid": True, 
			"initial_move": initial_move, 
			"scores": self.get_scores(),
			"moves_made": self.moves_made}

	def safe_receive(self):
		recv = self.board_pipe.recv()
		if recv == "END_GAME":
			self.end_game()
		elif type(recv) == dict:
			if 'undo' in recv:
				self.undo_moves_since(recv['move_number'])
		else:
			return recv

	def undo_moves_since(self, move_number):

		data = self.board_history[move_number]
		self.board = deepcopy(data['board'])
		self.moves_made = move_number
		if move_number % 2 == 0:
			index = 1
		else:
			index = 2

		setup_board_moves = []
		for col in range(len(self.board)):
			for row in range(len(self.board)):
				setup_board_moves.append(self.square_location_map([col, row], self.board[col][row]))

		self.board_pipe.send(
			{"moves": setup_board_moves, 
			"turn": index, 
			"valid": True, 
			"initial_move": data['initial_move'], 
			"scores": self.get_scores(),
			"moves_made": self.moves_made,
			"undone": True})

		self.safe_receive()

		self.board_pipe.send(
			{"moves": data['moves'], 
			"turn": index, 
			"valid": True, 
			"initial_move": data['initial_move'], 
			"scores": self.get_scores(),
			"moves_made": self.moves_made})

		self.safe_receive()

		self.move(index)


	def move(self, i):
		player = self.players[i-1]
		color = self.colors[i]
		self.moves_made += 1
		
		player_pipe, recv_pipe = Pipe()
		player_process = Process(target=player.take_turn,args=(self.board,recv_pipe)).start()

		while not player_pipe.poll(0.1):
			if self.board_pipe.poll():
				coords = self.safe_receive()

		turn = player_pipe.recv()

		player_pipe.close()

		if turn:
			# CPU player returned turn value
			if not self.is_valid_move(turn, i):
				print "CPU INVALID TURN", self.to_location(turn), "FORFEITING"
				turn = [-1,-1]

			state_alteration = self.alter_game_state(turn, i)
			self.board_pipe.send(state_alteration)
			self.safe_receive()
		else:
			# Turn value is false; get move from board.
			coords = self.from_location(self.safe_receive())

			while not self.is_valid_move(coords, i):
				self.board_pipe.send({"valid": False})
				coords = self.from_location(self.safe_receive())

			self.board_pipe.send(self.alter_game_state(coords, i))


		if self.full_board():
			self.end_game()

		self.move(3-i)

	def play(self):
		self.move(1)


if __name__ == "__main__":
	game = GuiGame()
	game.play()