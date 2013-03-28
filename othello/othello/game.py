from copy import deepcopy
from multiprocessing import Process, Pipe
from sys import exit
import operator as op
import re

from board import BoardWindow
from player import CPUPlayer, Player

class OthelloGame:
	def __init__(self):
		self.board_pipe, game_pipe = Pipe()
		self.colors = ["", "red", "white"]
		self.board_thread = Process(target=BoardWindow, args=(game_pipe,self.colors))
		self.board_thread.start()
		self.moves_made = 0
		self.board_history = {}

		self.board = self.__build_board()

		order_selection = self.board_pipe.recv()

		if order_selection == u"yes":
			#player first
			self.players = [Player(1), CPUPlayer(2)]
		else:
			self.players = [CPUPlayer(1), Player(2)]

		self.__setup_game()


	def __setup_game(self):
		self.board[3][3] = 1
		self.board[3][4] = 2
		self.board[4][4] = 1
		self.board[4][3] = 2
		moves = [
				{"loc": "D4", "color": self.colors[1]},
				{"loc": "E4", "color": self.colors[2]},
				{"loc": "D5", "color": self.colors[2]},
				{"loc": "E5", "color": self.colors[1]},
				]
		self.board_history[0] = {'board': deepcopy(self.board), 'moves': moves, 'initial_move': {"loc": "N/A", "color": "N/A"}}
		if self.board_pipe.recv() == "initial_state":
			self.board_pipe.send(moves)


	def __build_board(self):
		return [[0 for i in range(8)] for j in range(8)]

	def __is_valid_move(self, coords, player):
		if coords == [-1, -1]:
			return True

		if not self.board[coords[0]][coords[1]] == 0:
			return False

		if len(self.__get_swaps_from_move(coords, player)) > 0:
			return True

		return False

	def __square_location_map(self, coords, index):
		return {'loc': self.__to_location(coords), 'color': self.colors[index]}


	def __to_location(self, coords):
		if coords == [-1, -1]:
			return "-1"
		else:
			return "ABCDEFGH"[coords[0]]+str(coords[1]+1)

	def __from_location(self, loc):
		if loc == "-1":
			return [-1, -1]
		else:
			return ["ABCDEFGH".find(loc[0]), int(loc[1]) - 1]

	def __get_scores(self):
		scores = [0, 0]
		for i in self.board:
			for j in i:
				if j == 1:
					scores[0] += 1
				if j == 2:
					scores[1] += 1
		return scores

	def __get_swaps_from_move(self, coords, index):
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
					moves.append(self.__square_location_map(square, index))

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

	def __alter_game_state(self, coords, index):
		# ASSUMES VALID MOVE
		
		enemy_index = 3 - index

		# Check for special case of forfeit move.
		if coords == [-1, -1]:
			moves = []
			initial_move = {"loc": "-1", "color": self.colors[index - 1]} 
		else:
			moves = self.__get_swaps_from_move(coords, index)
			initial_move = self.__square_location_map(coords, index)
			moves.append(initial_move)
			# Make changes to internal board
			for move in moves:
				coords = self.__from_location(move['loc'])
				self.board[coords[0]][coords[1]] = index

		# Store board state in history list.
		self.board_history[self.moves_made] = {'board': deepcopy(self.board), 'moves': moves, 'initial_move': initial_move}

		# Collect game data to send to board
		return {"moves": moves, 
			"turn": enemy_index, 
			"valid": True, 
			"initial_move": initial_move, 
			"scores": self.__get_scores(),
			"moves_made": self.moves_made}

	def __safe_receive(self):
		recv = self.board_pipe.recv()
		if recv == "END_GAME":
			self.__end_game()
		elif type(recv) == dict:
			if 'undo' in recv:
				self.__undo_moves_since(recv['move_number'])
		else:
			return recv

	def __undo_moves_since(self, move_number):

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
				setup_board_moves.append(self.__square_location_map([col, row], self.board[col][row]))

		self.board_pipe.send(
			{"moves": setup_board_moves, 
			"turn": index, 
			"valid": True, 
			"initial_move": data['initial_move'], 
			"scores": self.__get_scores(),
			"moves_made": self.moves_made,
			"undone": True})

		self.__safe_receive()

		self.board_pipe.send(
			{"moves": data['moves'], 
			"turn": index, 
			"valid": True, 
			"initial_move": data['initial_move'], 
			"scores": self.__get_scores(),
			"moves_made": self.moves_made})

		self.__safe_receive()

		self.__move(index)


	def __move(self, i):
		player = self.players[i-1]
		color = self.colors[i]
		self.moves_made += 1
		
		turn = player.take_turn(self.board)
		if turn:
			# CPU player returned turn value
			if not self.__is_valid_move(turn, i):
				print "CPU INVALID TURN", self.__to_location(turn), "FORFEITING"
				turn = [-1,-1]

			state_alteration = self.__alter_game_state(turn, i)
			self.board_pipe.send(state_alteration)
			self.__safe_receive()
		else:
			# Turn value is false; get move from board.
			coords = self.__from_location(self.__safe_receive())

			while not self.__is_valid_move(coords, i):
				self.board_pipe.send({"valid": False})
				coords = self.__from_location(self.__safe_receive())

			self.board_pipe.send(self.__alter_game_state(coords, i))


		if self.__full_board():
			self.__end_game()

		self.__move(3-i)


	def play(self):
		self.__move(1)

	def __full_board(self):
		for x in range(len(self.board)):
			for y in range(len(self.board)):
				if self.__is_valid_move([x,y],1) or self.__is_valid_move([x,y],2):
					return False
		return True

	def __end_game(self):
		print("Game is over~")
		exit(0)

if __name__ == "__main__":
	game = OthelloGame()
	game.play()
