from . import AbstractGame

class HeadlessGame(AbstractGame):
	def move(self, index):
		player = self.players[i-1]
		color = self.colors[i]

		turn = player.take_turn(self.board)
		if turn:
			# CPU player returned turn value
			if not self.is_valid_move(turn, i):
				turn = [-1,-1]

			self.alter_game_state(turn, i)
		else:
			# Turn value is false; invalid player used.
			raise Exception("GUI player-type used for headless game.")

		if self.full_board():
			self.end_game()

		self.move(3-i)

	def __end_game(self):
		p1, p2 = self.get_scores()
		print "GAME OVER. P1:", p1, ",P2:", p2