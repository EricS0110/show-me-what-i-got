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
    mongo_collection = mongo_db_input[str(collection)]
    cursor = mongo_collection.find({})
    document_results = []
    fields = []
    for document in cursor:
        document_results.append(document)
    for entry in document_results:
        for key in entry.keys():
            if key != "_id":
                if key not in fields:
                    fields.append(key)
    return fields


def get_items(mongo_db_input, collection, field, criteria):
    mongo_collection = mongo_db_input[str(collection)]
    item_query = {f"{field}": {"$regex": criteria, "$options": "i"}}
    cursor = mongo_collection.find(item_query)
    item_results = []
    item_result_strings = []
    for item in cursor:
        item_results.append(item)
    for entry in item_results:
        text_string = ""
        for key, value in entry.items():
            if type(value) == str and value != "":
                text_string += f"{key}: {value}\n"
            elif type(value) == int or type(value) == float:
                text_string += f"{key}: {value}\n"
        item_result_strings.append(text_string)

    return item_result_strings


def hello_chaquopy(input):
    test_string = f"Hello Chaquopy! Author: {input}"
    return test_string
