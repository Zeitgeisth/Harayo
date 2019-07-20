from gensim.models import FastText
from app import db
import warnings
warnings.filterwarnings("ignore")


def get_similar_items(item):
    model = FastText.load("lf.model")
    similar = model.most_similar(item, topn = 5)
    item = [item]
    model.build_vocab([item], update=True)
    print(model.wv.vocab)
    model.train([item], total_examples=len([item]), epochs=model.epochs)
    model.save('lf.model')
    return similar

if __name__ == "__main__":
    print(get_similar_items("Samsung S9"))
    input()
