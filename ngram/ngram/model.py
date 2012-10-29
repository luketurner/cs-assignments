from __future__ import print_function

from collections import defaultdict
from random import choice

from selectors import maximum

class NgramModel:
    def __init__(self, n, words, selector=maximum):
        """Parameters:
            n: number of "grams" (i.e. n=2 for bigrams)
            words: Training set, in the form of a list of strings.
            selector (optional): Function for use in selecting a word
                given a frequency distribution of words. 
                By default, selects maximum.

        """
        self.n = n
        self.grams = self.train_from_words(words)
        self.selector = selector

    def train_from_words(self, words):
        n = self.n
        w = len(words)
        grams = defaultdict(lambda: defaultdict(int))

        for i in range(0,w-n+1):
            grams[tuple(x.lower() for x in words[i:i+n-1])][words[i+n-1].lower()] += 1
            
        return grams

    def select_given_prefix(self, prefix):
        # Note: Undefined behavior for unseen prefixes.
        # Such situations can be handled by selector function.
        return self.selector(self.grams[prefix])

    def generate_sentence(self, prefix, sentence=()):

        if sentence == ():
            sentence += prefix

        if '.' not in prefix and '?' not in prefix and '!' not in prefix:
            next = (self.select_given_prefix(prefix),)
            return self.generate_sentence(prefix[1:] + next, sentence + next)
        else:
            return sentence

    def select_word(self):
        return self.selector(self.grams)

class UnigramModel:
    #Really just a wrapper around a frenquency distribution.
    # Exists merely to give a relatively unified interface
    def __init__(self, words):
        self.words = words

    def generate_sentence(self):
        token = choice(self.words)
        sentence = []
        while token not in '!.?':
            token = choice(self.words)
            sentence.append(token)
        return sentence