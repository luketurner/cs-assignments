from tagger import SentenceTagger
from nltk.corpus import brown

#print "Generating tagger; this may take a while..."

tagger = SentenceTagger(corpus=brown.tagged_sents()[:50000])

#print "Completed generation. Saving..."

tagger.save_to_disk()

#print "Done."

#test loading

tagger2 = SentenceTagger(filename="saved_tagger.dump")

print tagger2.tag("This is a little odd")
