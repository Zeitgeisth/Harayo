from models import *
from flask import Blueprint

account_api = Blueprint('account_api', __name__)


user_schema = UserSchema(strict = True)
users_schema = UserSchema(many = True, strict =True)

item_schema = ItemSchema (strict = True)
items_schema = ItemSchema (many =True, strict = True)

@account_api.route('/get_lost_items', method = ['GET'])
def lost_items():
    lost_items = Item.query.filter_by(status=1)
    result = items_schema.dump(lost_items)
    return jsonify(result.data)

@account_api.route ('/add_lost_item', method = ['POST'])
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
    return product_schema.jsonify(new_item)