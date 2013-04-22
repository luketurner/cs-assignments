import timeit

boards=[
"[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,1,2,0,0,0],[0,0,0,2,1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]"]

players=[1,2]

for i in boards:
	for j in players:
		setup="from multiprocessing import Process,Pipe;from othello.player import CPUPlayer;p1,p2=Pipe();b={};p=CPUPlayer({})".format(i, j)
		print "Board: {}\nPlayer: {}".format(i,j)
		print "Time: "
		print timeit.timeit(stmt="p.take_turn(b)", setup=setup, number=1)
		print '-------------'