#
# Python Part-Of-Speech Tagger
#

from models import HiddenMarkovModel
import pickle
import re

class SentenceTagger:
    """
    Provides a straightforward object which can contain multiple HMMs
    which use different n-gram assumptions. Also allows saving and loading
    HMMs. Primarily intended for use in a web-server, which would create a
    SentenceTagger once and then call tag() for HTTP requests.
    """
    def __init__(self, corpus=None, filename=None, grams=[2]):
        if corpus:
            self.models = {}
            for ngram in grams:
                self.models[ngram] = HiddenMarkovModel(corpus, ngram)
        if filename:
            self.load_from_disk(filename)
                
    def tag(self, sentence, n=2):
        words = re.findall('[\w\']+', sentence.lower())
        return self.models[n].decode(words)[0]

    def save_to_disk(self, filename="saved_tagger.dump", protocol=2):
        with open(filename, 'wb') as f:
            pickle.dump(self.models, f, protocol)

    def load_from_disk(self, filename):
        with open(filename, 'rb') as f:
            self.models = pickle.load(f)
