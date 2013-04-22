cdef get_swaps_from_move(board, int x, int y):
	cint enemy_index = 3 - self.player
	moves = []
	cint size = 8

	cdef boolean valid(int x,int y):
		return (0 <= x < size) and (0 <= y < size)

	cdef check_direction(opx, opy):
		cint i = 1
		to_change = []
		while valid(opx(x,i),opy(y,i)) and self.board[opx(x,i)][opy(y,i)] == enemy_index:
			to_change.append([opx(x,i), opy(y,i)])
			i += 1
		if valid(opx(x,i),opy(y,i)) and self.board[opx(x,i)][opy(y,i)] == self.player:
			moves.extend(to_change)

	cdef null(int x,int y):
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