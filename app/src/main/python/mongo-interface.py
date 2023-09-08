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


def get_book_by_author(mongo_db_input, author):
    book_collection = mongo_db_input['books']
    book_author_query = {"Author": {"$regex": author, "$options": 'i'}}
    book_results_cursor = book_collection.find(book_author_query).sort([("Author", pymongo.DESCENDING),
                                                                        ("Series", pymongo.DESCENDING),
                                                                        ("Series_Number", pymongo.ASCENDING)])
    book_results = []
    book_results_strings = []
    for book in book_results_cursor:
        book_results.append(book)
    for book in book_results:
        text_string = ""
        for key, value in book.items():
            if type(value) == str and value != '':
                text_string += f"{key}: {value}\n"
            elif type(value) == int or type(value) == float:
                text_string += f"{key}: {value}\n"
        book_results_strings.append(text_string)

    return book_results_strings


def get_book_by_title(mongo_db_input, title):
    book_collection = mongo_db_input['books']
    book_title_query = {"Title": {"$regex": title, "$options": 'i'}}
    book_results_cursor = book_collection.find(book_title_query).sort([("Author", pymongo.DESCENDING),
                                                                       ("Series", pymongo.DESCENDING),
                                                                       ("Series_Number", pymongo.ASCENDING)])
    book_results = []
    book_results_strings = []
    for book in book_results_cursor:
        book_results.append(book)
    for book in book_results:
        text_string = ""
        for key, value in book.items():
            if type(value) == str and value != '':
                text_string += f"{key}: {value}\n"
            elif type(value) == int or type(value) == float:
                text_string += f"{key}: {value}\n"
        book_results_strings.append(text_string)

    return book_results_strings


def get_book_by_isbn(mongo_db_input, isbn):
    book_collection = mongo_db_input['books']
    book_isbn_query = {"ISBN": {"$regex": isbn, "$options": 'i'}}
    book_results_cursor = book_collection.find(book_isbn_query).sort([("Author", pymongo.DESCENDING),
                                                                      ("Series", pymongo.DESCENDING),
                                                                      ("Series_Number", pymongo.ASCENDING)])
    book_results = []
    book_results_strings = []
    for book in book_results_cursor:
        book_results.append(book)
    for book in book_results:
        text_string = ""
        for key, value in book.items():
            if type(value) == str and value != '':
                text_string += f"{key}: {value}\n"
            elif type(value) == int or type(value) == float:
                text_string += f"{key}: {value}\n"
        book_results_strings.append(text_string)

    return book_results_strings


def get_comics(mongo_db_input, title):
    comic_collection = mongo_db_input['comics']
    comic_query = {"Title": {"$regex": title, "$options": 'i'}}
    comic_results_cursor = comic_collection.find(comic_query).sort([("Title", pymongo.DESCENDING),
                                                                    ("Issue_Year", pymongo.ASCENDING),
                                                                    ("Issue_Month", pymongo.ASCENDING)])
    comic_results = []
    comic_results_strings = []
    for comic in comic_results_cursor:
        comic_results.append(comic)
    for comic in comic_results:
        text_string = ""
        for key, value in comic.items():
            if type(value) == str and value != '':
                text_string += f"{key}: {value}\n"
            elif type(value) == int or type(value) == float:
                text_string += f"{key}: {value}\n"
        comic_results_strings.append(text_string)

    return comic_results_strings


def get_comics_with_issue(mongo_db_input, title, issue):
    comic_collection = mongo_db_input['comics']
    comic_query = {"Title": {"$regex": title, "$options": 'i'}, "Issue": issue}
    comic_results_cursor = comic_collection.find(comic_query).sort([("Title", pymongo.DESCENDING),
                                                                    ("Issue_Year", pymongo.ASCENDING),
                                                                    ("Issue_Month", pymongo.ASCENDING)])
    comic_results = []
    comic_results_strings = []
    for comic in comic_results_cursor:
        comic_results.append(comic)
    for comic in comic_results:
        text_string = ""
        for key, value in comic.items():
            if type(value) == str and value != '':
                text_string += f"{key}: {value}\n"
            elif type(value) == int or type(value) == float:
                text_string += f"{key}: {value}\n"
        comic_results_strings.append(text_string)

    return comic_results_strings


def get_movies(mongo_db_input, title):
    movie_collection = mongo_db_input['movies']
    movie_query = {"Title": {"$regex": title, "$options": 'i'}}
    movie_results_cursor = movie_collection.find(movie_query).sort([("Title", pymongo.DESCENDING),
                                                                    ("Series", pymongo.DESCENDING),
                                                                    ("Series_Number", pymongo.ASCENDING)])
    movie_results = []
    movie_results_strings = []
    for movie in movie_results_cursor:
        movie_results.append(movie)
    for movie in movie_results:
        text_string = ""
        for key, value in movie.items():
            if type(value) == str and value != '':
                text_string += f"{key}: {value}\n"
            elif type(value) == int or type(value) == float:
                text_string += f"{key}: {value}\n"
        movie_results_strings.append(text_string)

    return movie_results_strings


def get_music_by_album(mongo_db_input, album):
    music_collection = mongo_db_input['music']
    music_query = {"Album": {"$regex": album, "$options": 'i'}}
    music_results_cursor = music_collection.find(music_query).sort([("Artist", pymongo.DESCENDING),
                                                                    ("Year", pymongo.ASCENDING),
                                                                    ("Album", pymongo.DESCENDING)])
    music_results = []
    music_results_strings = []
    for music in music_results_cursor:
        music_results.append(music)
    for music in music_results:
        text_string = ""
        for key, value in music.items():
            if type(value) == str and value != '':
                text_string += f"{key}: {value}\n"
            elif type(value) == int or type(value) == float:
                text_string += f"{key}: {value}\n"
        music_results_strings.append(text_string)

    return music_results_strings


def get_music_by_artist(mongo_db_input, artist):
    music_collection = mongo_db_input['music']
    music_query = {"Artist": {"$regex": artist, "$options": 'i'}}
    music_results_cursor = music_collection.find(music_query).sort([("Artist", pymongo.DESCENDING),
                                                                    ("Year", pymongo.ASCENDING),
                                                                    ("Album", pymongo.DESCENDING)])
    music_results = []
    music_results_strings = []
    for music in music_results_cursor:
        music_results.append(music)
    for music in music_results:
        text_string = ""
        for key, value in music.items():
            if type(value) == str and value != '':
                text_string += f"{key}: {value}\n"
            elif type(value) == int or type(value) == float:
                text_string += f"{key}: {value}\n"
        music_results_strings.append(text_string)

    return music_results_strings


def get_television(mongo_db_input, title):
    tv_collection = mongo_db_input['television']
    tv_query = {"Title": {"$regex": title, "$options": 'i'}}
    tv_results_cursor = tv_collection.find(tv_query).sort([("Title", pymongo.DESCENDING),
                                                           ("Year", pymongo.ASCENDING),
                                                           ("Season", pymongo.ASCENDING),
                                                           ("Disc_Number", pymongo.ASCENDING)])
    tv_results = []
    tv_results_strings = []
    for tv in tv_results_cursor:
        tv_results.append(tv)
    for tv in tv_results:
        text_string = ""
        for key, value in tv.items():
            if type(value) == str and value != '':
                text_string += f"{key}: {value}\n"
            elif type(value) == int or type(value) == float:
                text_string += f"{key}: {value}\n"
        tv_results_strings.append(text_string)

    return tv_results_strings


def hello_chaquopy(input):
    test_string = f"Hello Chaquopy! Author: {input}"
    return test_string
