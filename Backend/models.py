from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow
import os
from flask import Blueprint

model_app = Blueprint('model_app', __name__)

db = SQLAlchemy (model_app)

ma = Marshmallow (model_app)

class User(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    name = db.Column (db.String(80), nullable = False)
    password = db.Column (db.String(80), nullable = False)
    location = db.Column(db.LargeBinary, nullable = False)


class Item(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    name = db.Column(db.String(80))
    location = db.Column (db.LargeBinary, nullable =True)
    description = db.Column (db.String(100))
    catagory= db.Column (db.Integer)
    image = db.Column(db.LargeBinary, nullable = True) 
    status = db.Column(db.Integer)
    user = db.Column (db.Integer, db.ForeignKey('user.id'),nullable = False)


class UserSchema(ma.Schema):
    class Meta:
        fields = ('id','name','password','location')

class ItemSchema(ma.Schema):
    class Meta:
        fields = ('id','name', 'catagory','location','description','catagory','image','status','user')
