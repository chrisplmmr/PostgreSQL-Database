import psycopg2
import json
import os

# Hyperparameters: we adjust these to tell the program what to import
JSON_FILENAME = 'yelp_academic_dataset_business.json'
TABLE_NAME = 'full_location'
require_info = ['business_id', 'address', 'city', 'state', 'postal_code', 'latitude', 'longitude']

# Connect to 315 database using the credentials provided:
conn = psycopg2.connect("host=csce-315-db.engr.tamu.edu dbname=team910_d10_db user=cwp684 password=427002602")
cur = conn.cursor()

# open the file (with unicode encoding to avoid errors):
with open(JSON_FILENAME, 'r', encoding='utf-8') as f:
    # keep track of the number of entries we've made:
    count = 0

    # for each line (potential entry) in the file:
    for text in f:
        count += 1

        # parse the line as a JSON object, store in a dictionary
        line = json.loads(text)

        # store the data parsed from this line, and whether all the data is present:
        data_collect = []
        missing_data = False

        # make sure all required information is present in this entry.
        # if so, parse the data into data_collect:
        for i in require_info:
            if i not in line:
                missing_data = True
                break
            else:
                data_collect.append(line[i])

        if not missing_data:
            # form the postgres query string using the data collected:
            #query = "UPDATE " + TABLE_NAME + " VALUES ("
            #for i in data_collect:
            #    temp = '%s'
            #    query += temp + ', '
            #query = query[:-2] + ') ON CONFLICT DO NOTHING;'
            query = "UPDATE full_location SET latitude = " + "%s" + ", longitude = " + "%s" + " WHERE business_id = " + "%s" + ";"
            #print(query)
            # execute the query, replacing the placeholders with parsed
            # information from data_collect:
            #cur.execute(query, tuple(data_collect))
            latData = []
            latData.append(data_collect[5])
            latData.append(data_collect[6])
            latData.append(data_collect[0])
            cur.execute(query, tuple(latData))

        # every 100 queries, print some data as an indicator that everything
        # is working properly, and commit the changes to the database:
        if count % 100 == 0:
            print(data_collect)
            conn.commit()

    # commit the remaining changes to the database:
    conn.commit()
