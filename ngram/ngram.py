from __future__ import print_function

import random

from nltk.corpus import brown

# 
# LOCAL IMPORTS
#
from ngram import NgramModel, UnigramModel
from ngram.selectors import weighted_random as selector

def test_model(n, inits):
	ngram = NgramModel(n, brown.words(), selector)

	with open("out" + str(n), 'w') as outf:
		for i in inits:
			print(' '.join(ngram.generate_sentence(i)), file=outf)


def main():
	random.seed()

	# Unigram model is special case

	#unigram = UnigramModel(brown.words())
	#with open("out1", 'w') as outf:
	#	for i in range(10):
	#		print(' '.join(unigram.generate_sentence()), file=outf)

	#test_model(2, [("the",), ("a",), ("an",), ("what",), ("who",), 
	#				 ("there",), ("then",), ("later",), ("earlier",), ("today",), 
	#				 ("they",), ("i",), ("we",), ("you",), ("where",)])
	#test_model(3, [("they", "are"), ("they", "were"), ("he", "is"), ("it", "is"), ("we", "are"),
	#				 ("then", "he"), ("it", "was"), ("what", "is"), ("where", "was"), ("who", "did"),
	#				 ("when", "will"), ("we", "saw"), ("he", "took"), ("you", "are"), ("however", ",")])
	test_model(4, [("then", ",", "he"), ("then", ",", "she"), ("later", ",", "it"), ("where", "they", "are"), ("that", "was", "when"),
				("so", "it", "was"), ("not", "only", "that"), ("does", "not", "know"), ("as", "he", "said"), ("that", "is", ","),
				("however", ",", "he"), ("then", ",", "it"), ("now", ",", "he"), ("if", "not", ","), ("now", "there", "is")])

if __name__ == '__main__':
	main()
