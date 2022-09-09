import psycopg2
import json
import os

JSON_FILENAME = 'yelp_academic_dataset_business.json'
TABLE_NAME = 'full_location'
require_info = ['business_id', 'address', 'city', 'state', 'postal_code', 'latitude', 'longitude']

# Connect to 315 database
# Use an environment variable too access db password
conn = psycopg2.connect("host=csce-315-db.engr.tamu.edu dbname=team910_d10_db user=jlee232435 password=727000824")
cur = conn.cursor()

with open(JSON_FILENAME, 'r', encoding='utf-8') as f:
    count = 0
    for text in f:
        count += 1
        if count < 100:
            continue
        line = json.loads(text)
        data_collect = []
        missing_data = False
        for i in require_info:
            if i not in line:
                missing_data = True
            else:
                data_collect.append(line[i])
        if not missing_data:
            query = "INSERT INTO " + TABLE_NAME + " VALUES ("
            for i in data_collect:
                temp = '%s'
                query += temp + ', '
            query = query[:-2] + ') ON CONFLICT DO UPDATE;'
            cur.execute(query,tuple(data_collect))
        if count % 100 == 0:
            print(count)
            print(data_collect)
            conn.commit()
    conn.commit()
