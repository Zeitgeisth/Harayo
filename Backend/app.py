from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow
import os

from gensim.models import FastText
import warnings
warnings.filterwarnings("ignore")
# from api import account_api
# from models import model_app
# from models import User, UserSchema, Item,ItemSchema

#init app here
app = Flask(__name__)
basedir = os.path.abspath(os.path.dirname(__file__))
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + os.path.join(basedir,'db.sqlite')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config["DEBUG"] = True
# app.register_blueprint(account_api)
# app.register_blueprint(model_app)


#Database
db = SQLAlchemy(app)
ma = Marshmallow(app)

class User(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    name = db.Column (db.String(80), nullable = False)
    password = db.Column (db.String(80), nullable = False)
    location = db.Column(db.LargeBinary, nullable = False)
    def __init__(self, name, password, location):
        self.name = name
        self.password = password
        self.location = location

class Item(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    name = db.Column(db.String(80))
    location = db.Column (db.LargeBinary, nullable =True)
    description = db.Column (db.String(100))
    catagory= db.Column (db.Integer)
    image = db.Column(db.LargeBinary, nullable = True) 
    status = db.Column(db.Integer)
    user = db.Column (db.Integer, db.ForeignKey('user.id'),nullable = False)
    def __init__(self,name,location, description,catagory,image,status,user):
        self.name = name
        self.location = location
        self.description = description
        self.catagory = catagory
        self.image = image
        self.status = status
        self.user  =user

class UserSchema(ma.Schema):
    class Meta:
        fields = ('id','name','password','location')

class ItemSchema(ma.Schema):
    class Meta:
        fields = ('id','name', 'catagory','location','description','catagory','image','status','user')



user_schema = UserSchema(strict = True)
users_schema = UserSchema(many = True, strict =True)

item_schema = ItemSchema(strict = True)
items_schema = ItemSchema (many =True, strict = True)

def get_similar_items(item, location):
    model = FastText.load("lf.model")
    similar = model.most_similar(item, topn = 5)
    items = []
    for it, _ in similar:
        items.append(it)
    items = tuple(items)
    item = [item]
    model.build_vocab([item], update=True)
    #print(model.wv.vocab)
    result = db.engine.execute("SELECT name, location FROM Item WHERE name IN {0} AND location={1} AND catagory=2".format(items, location))
    result_data = [{column: value for column, value in row.items()} for row in result ]
    print(result_data)
    model.train([item], total_examples=len([item]), epochs=model.epochs)
    model.save('lf.model')
    return result_data


@app.route('/get_lost_items', methods = ['GET'])
def lost_items():
    lost_items = Item.query.filter_by(status=1)
    result = items_schema.dump(lost_items)
    return jsonify(result.data)
@app.route('/get_found_items',methods = ['GET'])
def found_items():
    found_items = Item.query.filter_by(found_items)
    result = items_schema.dump (found_items)
    return jsonify(result.data)


@app.route ('/add_lost_item', methods = ['POST'])
def add_lost_items():
    name = request.json['name']
    location = request.json['location']
    description = request.json['description']
    catagory = request.json['catagory']
    image = request.json['image']
    status = request.json ['status']
    user = request.json ['user']
    existing_item= get_similar_items(name,location)
    print(existing_item)

    new_item = Item(name, location,description, int(catagory), image, int(status), int(user))
    db.session.add(new_item)
    db.session.commit()
    return existing_item

@app.route ('/add_found_item', methods = ['POST'])
def add_found_items():
    name = request.json['name']
    location = request.json['location']
    description = request.json['description']
    catagory = request.json['catagory']
    image = request.json['image']
    status = request.json ['status']
    user = request.json ['user']
    
    new_item = Item(name, location,description, catagory, image, status, user)
    db.session.add(new_item)
    db.session.commit()
    return item_schema.jsonify(new_item)


#server
if __name__=='__main__':
    app.run(host='0.0.0.0')