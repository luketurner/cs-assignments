import timeit

board="\
[\
[0,1,0,0,2,0,0,0],\
[0,0,1,1,1,1,0,0],\
[0,0,2,1,2,1,0,0],\
[0,0,2,2,1,1,2,0],\
[0,1,2,2,2,2,2,2],\
[0,1,1,2,2,2,1,0],\
[0,1,1,2,2,0,0,0],\
[0,0,0,2,0,0,0,0]]"

players=[1,2]

for j in players:
	setup="from multiprocessing import Process,Pipe;from othello.player import CPUPlayer;p1,p2=Pipe();b={};p=CPUPlayer({})".format(board, j)
	print "Board: {}\nPlayer: {}".format(board,j)
	print "Time: "
	print timeit.timeit(stmt="a=Process(target=p.take_turn,args=(b,p2));a.start();a.join();", setup=setup, number=1)
	print '-------------'
