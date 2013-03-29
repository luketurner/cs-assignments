from collections import defaultdict


class HiddenMarkovModel:
    """
    This implementation of a Hidden Markov Model is intended to be trained on already-tagged
    corpora, and has only decoding functionality as befits its use as a part of speech tagger.

    The model is designed to operate under a bigram assumption OR a trigram assumption
    based on arguments passed to the constructor.

    The training set requires the form provided by NLTK corpus' tagged_sents() function;
    for more details, see HiddenMarkovModel.train() documentation.

    The only way a client should interface with the model is by creating one with the constructor,
    training the model with train() or via the constructor, and then using the decode() function.

    This model probably shouldn't be accessed directly. Look at tagger.SentenceTagger() for a public
    interface.
    """
    def __init__(self, training_set, n=2):

        if n == 2 or n == 3:
            self.n = n
        else:
            self.n = 2

        self.train(training_set)

    def __normalize(self, freqs):
        """
        Utility function to normalize frequency distributions
        (i.e. change into probability distribution)
        """
        n = float(sum(freqs.values()))
        probs = {'NOTINCORPUS': (10000.0 / len(freqs))}
        for key in freqs:
            probs[key] = ((freqs[key] * 10000.0) / n) 
        return probs
        #return dict([k, f/n] for k, f in freqs.items())

    def __transition(self, *args):
        """
        Given arguments, returns transition probability P(args[-1]|args[:-1]).
        """
        try:
            trans = self.priors[tuple(args[:-1])][args[-1]]
        except KeyError:
            trans = 1
            #if tuple(args[:-1]) in self.priors:
            #    trans = 10000.0/len(self.priors[tuple(args[:-1])])
            #else:
            #    trans = 10000.0/len(self.priors)
        return trans

    def __emission(self, state, obs):
        """
        Returns emission probability P(obs|state).
        """
        try:
            emission = self.emissions[state][obs]
        except KeyError:
            emission = 1#10000.0/len(self.emissions[state])
        return emission

    def train(self, training_set):
        """
        Trains HMM on provided training set. Note training set should take the
        form of a list of lists of tuples, each tuple containing (observation, state).
        Each "inner list" (i.e. list containing tuples) should be considered a single
        observation string, and the training set consists of some number of these
        strings (ideally, a lot).
        NLTK's corpus function "tagged_sents()" returns data of this form.
        """
        
        emissions = {}
        priors = {}

        for sentence in training_set:

            #more convenient to add this in right at the beginning than in the iteration.
            sentence[0:0] = [('SSTART', 'SSTART')]
            #sentence.append(('SEND', 'SEND'))


            for observation, state in sentence:
                
                if not state in emissions:
                    emissions[state] = {}
                if not observation in emissions[state]:
                    emissions[state][observation] = 0

                emissions[state][observation] += 1


            for i in range(0,len(sentence) - self.n + 1):
                #TODO: generalize for quadrigrams+?
                # Still want bigrams for intiial transitions, even if n > 2
                if i < (self.n - 1) and self.n > 2:
                    prior_tags = tuple(sentence[0])
                    tag = sentence[1]
                else:
                    prior_tags = tuple(y for x, y in sentence[i:i+self.n-1])
                    tag = sentence[i+self.n-1][1]
                    
                if not prior_tags in priors:
                    priors[prior_tags] = {}
                if not tag in priors[prior_tags]:
                    priors[prior_tags][tag] = 0
                
                priors[prior_tags][tag] += 1

        states = set(emissions.keys())

        self.emissions = emissions
        self.priors = priors
        #self.emissions = {}
        #self.priors = {'NOTINCORPUS': 10000.0/len(priors)}
        #
        #for key, val in emissions.items():
        #    self.emissions[key] = self.__normalize(val)
        #    
        #for key, val in priors.items():
        #    self.priors[key] = self.__normalize(val)
            
        self.states = states

    def decode(self, observations):
        """
        Given a list of observations, returns the list of hidden states most
        likely to correspond to those observations. Uses the Viterbi algorithm.
        Returns a tuple containing the list of hidden states deemed to correspond
        to the observations provided, as well as the calculated probability associated
        with that sequence (which may be useful for determining relative certainty).
        """

        array = [{}]
        path = {}

        for state in self.states:
            array[0][state] = self.__transition("SSTART", state) * self.__emission(state, observations[0])
            path[state] = [state]

        for i in range(1,len(observations)):
            array.append(dict())
            newpath = {}

            for y in self.states:
                (prob, state) = max([(array[i-1][y0] * self.__transition(y0, y) * self.__emission(y, observations[i]), y0) for y0 in self.states])
                array[i][y] = prob
                newpath[y] = path[state] + [y]

            path = newpath

        (prob, state) = max([(array[len(observations) - 1][y], y) for y in self.states])
        return path[state], prob
