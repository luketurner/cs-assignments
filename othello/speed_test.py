import pycallgraph
from othello.player import CPUPlayer

board=[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,1,2,0,0,0],[0,0,0,2,1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]

a = CPUPlayer(1)

pycallgraph.start_trace()

a.take_turn(board)

pycallgraph.make_dot_graph('profile.png')