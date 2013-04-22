import numpy
cimport numpy
DTYPE = numpy.int
ctypedef numpy.int_t DTYPE_T

def get_swaps_from_move(numpy.ndarray board, int x, int y, int player):
	cdef int enemy_index = 3 - player
	moves = []

	i = 1
	to_change = []
	while (0 <= x+i < 8) and (0 <= y+i < 8) and board[x+i,y+i] == (3 - player):
		to_change.append([x+i, y+i])
		i += 1
	if (0 <= x+i < 8) and (0 <= y+i < 8) and board[x+i,y+i] == player:
		moves.extend(to_change)

	i = 1
	to_change = []
	while (0 <= x+i < 8) and (0 <= y-i < 8) and board[x+i,y-i] == enemy_index:
		to_change.append([x+i, y-i])
		i += 1
	if (0 <= x+i < 8) and (0 <= y-i < 8) and board[x+i,y-i] == player:
		moves.extend(to_change)

	i = 1
	to_change = []
	while (0 <= x-i < 8) and (0 <= y+i < 8) and board[x-i,y+i] == enemy_index:
		to_change.append([x-i, y+i])
		i += 1
	if (0 <= x-i < 8) and (0 <= y+i < 8) and board[x-i,y+i] == player:
		moves.extend(to_change)

	i = 1
	to_change = []
	while (0 <= x-i < 8) and (0 <= y-i < 8) and board[x-i,y-i] == enemy_index:
		to_change.append([x-i, y-i])
		i += 1
	if (0 <= x-i < 8) and (0 <= y-i < 8) and board[x-i,y-i] == player:
		moves.extend(to_change)

	i = 1
	to_change = []
	while (0 <= x-i < 8) and (0 <= y < 8) and board[x-i,y] == enemy_index:
		to_change.append([x-i, y])
		i += 1
	if (0 <= x-i < 8) and (0 <= y < 8) and board[x-i,y] == player:
		moves.extend(to_change)

	i = 1
	to_change = []
	while (0 <= x+i < 8) and (0 <= y < 8) and board[x+i,y] == enemy_index:
		to_change.append([x+i, y])
		i += 1
	if (0 <= x+i < 8) and (0 <= y < 8) and board[x+i,y] == player:
		moves.extend(to_change)

	i = 1
	to_change = []
	while (0 <= x < 8) and (0 <= y-i < 8) and board[x,y-i] == enemy_index:
		to_change.append([x, y-i])
		i += 1
	if (0 <= x < 8) and (0 <= y-i < 8) and board[x,y-i] == player:
		moves.extend(to_change)

	i = 1
	to_change = []
	while (0 <= x < 8) and (0 <= y+i < 8) and board[x,y+i] == enemy_index:
		to_change.append([x, y+i])
		i += 1
	if (0 <= x < 8) and (0 <= y+i < 8) and board[x,y+i] == player:
		moves.extend(to_change)

	return moves