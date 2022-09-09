import psycopg2
import json
import os

JSON_FILENAME = 'yelp_academic_dataset_review.json'
TABLE_NAME = 'review'
require_info = ['review_id', 'user_id', 'business_id', 'stars', 'text', 'date']

# Connect to 315 database
# Use an environment variable too access db password
conn = psycopg2.connect("host=csce-315-db.engr.tamu.edu dbname=team910_d10_db user=hoangnghiaanh04 password=228000790")
cur = conn.cursor()

with open(JSON_FILENAME, 'r', encoding='utf-8') as f:
    count = 0
    for text in f:
        count += 1
        if count < 1204900:
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
            query = query[:-2] + ') ON CONFLICT DO NOTHING;'
            cur.execute(query, tuple(data_collect))
        if count % 1000 == 0:
            pid = os.fork()
            if pid>0:
                continue
            else:
                conn.commit()
                print(count)
                print(data_collect)
                os._exit(0)
    conn.commit()
