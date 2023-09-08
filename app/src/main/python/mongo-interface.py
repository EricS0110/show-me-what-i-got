import dns.resolver

dns.resolver.default_resolver = dns.resolver.Resolver(configure=False)
dns.resolver.default_resolver.nameservers = ['8.8.8.8']
import pymongo


def get_mongo_connection(input_username, input_password, input_cluster, input_database, input_uri):
    username = str(input_username)
    password = str(input_password)
    cluster = str(input_cluster)
    database = str(input_database)
    uri = str(input_uri)
    mongo_client = pymongo.MongoClient(f'mongodb+srv://{username}:{password}'
                                       f'@{cluster}.{uri}.mongodb.net')
    mongo_db = mongo_client[f'{database}']
    return mongo_db


def test_mongo_connection(mongo_db_input):
    found_collection = [col for col in mongo_db_input.list_collections()]
    return len(found_collection)


def get_collection_list(mongo_db_input):
    found_collections = [col['name'] for col in mongo_db_input.list_collections()]
    return found_collections


def get_field_list(mongo_db_input, collection):
    mongo_collection = mongo_db_input.db[collection]
    cursor = mongo_col.find({})
    field_set = set()
    for document in cursor:
        for key in document.keys():
            field_set.add(key)
    field_set.discard('_id')
    return field_set


def hello_chaquopy(input):
    test_string = f"Hello Chaquopy! Author: {input}"
    return test_string
