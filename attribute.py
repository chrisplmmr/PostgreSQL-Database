import psycopg2
import json
import os

JSON_FILENAME = 'yelp_academic_dataset_business.json'
TABLE_NAME = 'attributes'
require_info = ['BusinessAcceptsCreditCards', 'BikeParking', 'GoodForKids', 'ByAppointmentOnly', 'NoiseLevel']

# Connect to 315 database
# Use an environment variable too access db password
conn = psycopg2.connect("host=csce-315-db.engr.tamu.edu dbname=team910_d10_db user=hoangnghiaanh04 password=228000790")
cur = conn.cursor()

with open(JSON_FILENAME, 'r', encoding='utf-8') as f:
    count = 0
    for text in f:
        count += 1
        line = json.loads(text)
        data_collect = []

        # append ID
        data_collect.append('atr' + str(count))

        data_collect.append(line['business_id'])
        if line['is_open'] == 1:
            data_collect.append(True)
        else:
            data_collect.append(False)
        temp = line[TABLE_NAME]

        for i in require_info:
            missing_data = True
            if temp is not None:
                if i in temp:
                    missing_data = False
            if missing_data:
                data_collect.append(None)
            else:
                data_collect.append(temp[i])
        query = "INSERT INTO " + TABLE_NAME + " VALUES ("
        for i in data_collect:
            temp = '%s'
            query += temp + ', '
        query = query[:-2] + ') ON CONFLICT DO NOTHING;'
        cur.execute(query, tuple(data_collect))
        if count % 100 == 0:
            conn.commit()
            print(count)
            print(data_collect)
    conn.commit()
