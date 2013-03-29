from tagger import SentenceTagger
from nltk.corpus import brown
import timeit

print "Generating tagger; this may take a while..."

tagger = SentenceTagger(corpus=brown.tagged_sents()[:50000])

print "Done. Loading sentences..."

sentences = brown.tagged_sents()[57000:57100]

print "Done. Tagging..."

#test loading

total = 0.0

for sentence in sentences:
    result = tagger.models[2].decode([x for x, y in sentence])[0]
    correct = list(y for x, y in sentence)
    print result, correct
    if result == correct:
        total += 1

print "Percent correct:", (total / 100.0)
