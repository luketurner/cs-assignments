
import collections
import functools

import operator as op
from itertools import product
from threading import Timer, Event
import re

import numpy

import cfunc

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
		
class BoardNode(object):
	
	def __init__(self, board, move, player):
		self.board = board
		self.move = move
		self.player = player

	def children(self):
		for board, move in self.next_moves():
			yield BoardNode(board, move, 3 - self.player)
	
	def next_moves(self):
		for x,y in product(xrange(8), repeat=2):
			if self.board[x,y] == 0:
				moves = cfunc.get_swaps_from_move(self.board, x, y, self.player)
				if len(moves) > 0:
					moves.append([x,y])
					yield self.alter_state(moves), [x,y]

	def alter_state(self, moves):

		board = numpy.empty_like(self.board)
		board[:] = self.board

		# Make changes to internal board
		for x,y in moves:
			board[x,y] = self.player

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
			while valid(opx(x,i),opy(y,i)) and self.board[opx(x,i),opy(y,i)] == enemy_index:
				to_change.append([opx(x,i), opy(y,i)])
				i += 1
			if valid(opx(x,i),opy(y,i)) and self.board[opx(x,i),opy(y,i)] == self.player:
				moves.extend(to_change)

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

class CPUPlayer:
	def __init__(self, id):
		self.us = id
		self.them = 3 - id

	def fitness(self, node):
		scores = node.get_scores()
		if self.us == 1:
			return scores[0]-scores[1]
		else:
			return scores[1]-scores[0]

	def ab_prune(self, node, depth, alpha, beta, done):

		if depth == 0 or done.is_set() or not any(node.children()):
			return self.fitness(node)

		if node.player == self.us:
			for child in node.children():
				alpha = max(alpha, self.ab_prune(child, depth-1, alpha, beta, done))
				if beta <= alpha:
					break
			return alpha
		else:
			for child in node.children():
				beta = min(beta, self.ab_prune(child, depth-1, alpha, beta, done))
				if beta <= alpha:
					break
			return beta


	def take_turn(self, board):
		self.board = board

		root = BoardNode(numpy.array(self.board), [], self.us)

		done = Event()
		def done_timer(event):
			event.set()

		def prune(child):
			a = self.ab_prune(child, 7, -1000, 1000, done)
			print(child.move, a)
			return a

		timer = Timer(85.0, done_timer, [done])
		timer.start()


		coords = max(root.children(), key=prune).move
		#out.send(coords)
		#out.close()