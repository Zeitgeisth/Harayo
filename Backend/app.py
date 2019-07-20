from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow
import os
# from api import account_api
# from models import model_app
# from models import User, UserSchema, Item,ItemSchema

#init app here
app = Flask(__name__)
basedir = os.path.abspath(os.path.dirname(__file__))
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + os.path.join(basedir,'db.sqlite')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

# app.register_blueprint(account_api)
# app.register_blueprint(model_app)


#Database
db = SQLAlchemy (app)
ma = Marshmallow (app)

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

    new_item = Item(name, location,description, catagory, image, status, user)
    db.session.add(new_item)
    db.session.commit()
    return item_schema.jsonify(new_item)

@app.route ('/add_found_item', methods = ['POST'])
def add_lost_items():
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
    app.run(debug=True)