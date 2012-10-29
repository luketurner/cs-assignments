from random import choice

def weighted_random(f):
	if len(f) == 0:
		return '.'

	selectlist = []
	for key, val in f.items():
		for i in range(val):
			selectlist.append(key)
	return choice(selectlist)

def maximum(f):
	if len(f) != 0:
		return max(f.items(), key=lambda x: x[1])[0]
	else:
		return '.'