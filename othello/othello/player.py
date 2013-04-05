import operator as op
from time import sleep
import re

class Player:
	def __init__(self, id):
		pass

	def take_turn(self, board, out):
		out.send(False) # let turn be selected by game
		out.close()

class CPUPlayer:
	def __init__(self, id):
		self.us = id
		self.them = 3 - id

	def __get_swaps_from_move(self, x, y):

		index = self.us
		enemy_index = self.them
		size = len(self.board) # 8
		moves = []

		def valid_move(x,y):
			return (0 <= x < size) and (0 <= y < size)

                #TODO 1,7 and 5,7 invalid even when valid
		def check_direction(opx, opy):
			i = 1
			to_change = []
			while valid_move(opx(x,i),opy(y,i)) and self.board[opx(x,i)][opy(y,i)] == enemy_index:
				to_change.append([opx(x,i), opy(y,i)])
				i += 1
			if valid_move(opx(x,i),opy(y,i)) and self.board[opx(x,i)][opy(y,i)] == index:
				for square in to_change:
					moves.append(square)

		def null(x,y):
			return x

		check_direction(op.add, op.add)
		check_direction(op.sub, op.add)
		check_direction(op.add, op.sub)
		check_direction(op.sub, op.sub)

		check_direction(op.add, null)
		check_direction(op.sub, null)
		check_direction(null, op.add)
		check_direction(null, op.sub)

		return moves

	def take_turn(self, board, out):
		maxs = [-1,-1,0]
		self.board = board
		#sleep(10) #TODO
		for col in range(len(board)):
			for row in range(len(board)):
                                if board[col][row] == 0:
                                        swaps = self.__get_swaps_from_move(col, row)
                                        if len(swaps) > maxs[2]:
                                                maxs = [col,row,len(swaps)]
		out.send(maxs[:-1])
		out.close()
