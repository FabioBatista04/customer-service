
print('Start :::::');

db = db.getSiblingDB('customer_service');
db.createCollection('customers');
db.customers.createIndex({ "email": 1 }, { unique: true });
db.createCollection('users');
db.users.createIndex({ "username": 1 }, { unique: true });

print('END ::::');