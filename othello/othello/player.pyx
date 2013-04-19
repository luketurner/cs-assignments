
import collections
import functools

import operator as op
from itertools import product
from copy import deepcopy
from time import sleep
import re

class Player:
	def __init__(self, id):
		pass

	def take_turn(self, board, out):
		out.send(False) # let turn be selected by game
		out.close()


class Infinity(object):
	def __cmp__(self,a):
		return 1
class NegativeInfinity(object):
	def __cmp__(self,a):
		return -1

class memoized(object):
	'''Decorator. Caches a function's return value each time it is called.
	If called later with the same arguments, the cached value is returned
	(not reevaluated).
	'''
	def __init__(self, func):
		self.func = func
		self.cache = {}

	def __call__(self, *args):
		if not isinstance(args, collections.Hashable):
			# uncacheable. a list, for instance.
			# better to not cache than blow up.
			return self.func(*args)

		if args[0:2] in self.cache:
			return self.cache[args[0:2]]

		else:
			value = self.func(*args)
			self.cache[args[0:2]] = value
			return value

	def __repr__(self):
		'''Return the function's docstring.'''
		return self.func.__doc__

	def __get__(self, obj, objtype):
		'''Support instance methods.'''
		return functools.partial(self.__call__, obj)

		
class BoardNode(object):
	
	def __init__(self, board, move, player):
		self.board = board
		self.move = move
		self.player = player

	def has_children(self):
		for x,y in product(range(8), repeat=2):
			if self.board[x][y] == 0:
				moves = self.get_swaps_from_move(x,y)
				if len(moves) > 0:
					return True

	def children(self):
		for board, move in self.next_moves():
			yield BoardNode(board, move, 3 - self.player)
	
	def next_moves(self):
		for x,y in product(range(8), repeat=2):
			if self.board[x][y] == 0:
				moves = self.get_swaps_from_move(x,y)
				if len(moves) > 0:
					moves.append([x,y])
					yield self.alter_state(moves), [x,y]

	def alter_state(self, moves):

		board = deepcopy(self.board)

		# Make changes to internal board
		for x,y in moves:
			board[x][y] = self.player

		return board

	def get_scores(self):
		scores = [0, 0]
		for i in self.board:
			for j in i:
				if j == 1:
					scores[0] += 1
				if j == 2:
					scores[1] += 1
		return scores
	
	def get_swaps_from_move(self, x, y):
		enemy_index = 3 - self.player
		moves = []
		size = 8

		def valid(x,y):
			return (0 <= x < size) and (0 <= y < size)

		def check_direction(opx, opy):
			i = 1
			to_change = []
			while valid(opx(x,i),opy(y,i)) and self.board[opx(x,i)][opy(y,i)] == enemy_index:
				to_change.append([opx(x,i), opy(y,i)])
				i += 1
			if valid(opx(x,i),opy(y,i)) and self.board[opx(x,i)][opy(y,i)] == self.player:
				for square in to_change:
					moves.append(square)

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

cdef int fitness(self, node):
	scores = node.get_scores()
	if self.us == 1:
		return scores[0]-scores[1]
	else:
		return scores[1]-scores[0]

cdef int ab_prune(self, node, int depth, int alpha, int beta, int player):

	if depth == 0 or not node.has_children():
		return fitness(node)

	if node.player == player:
		for child in node.children():
			alpha = max(alpha, ab_prune(child, depth-1, alpha, beta, player))
			if beta <= alpha:
				break
		return alpha
	else:
		for child in node.children():
			beta = min(beta, ab_prune(child, depth-1, alpha, beta, player))
			if beta <= alpha:
				break
		return beta

class CPUPlayer:
	def __init__(self, id):
		self.us = id
		self.them = 3 - id

	def take_turn(self, board, out):
		self.board = board

		root = BoardNode(self.board, [], self.us)

		def prune(child):
			a = ab_prune(child, 8, -1000, 1000, self.us)
			print(child.move, a)
			return a

		coords = max(root.children(), key=prune).move
		out.send(coords)
		out.close()
