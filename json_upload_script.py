"""
a modified version of the script given in class that (should be able to)
parse & upload JSON files to the database server

- ori :)

- Chris test comment
"""

import psycopg2
import json
import os

JSON_FILENAME = 'yelp_academic_dataset_business.json'
TABLE_NAME = 'test'
info = ['business_id', 'name', 'stars']

# Connect to 315 database
# Use an environment variable too access db password
conn = psycopg2.connect("host=csce-315-db.engr.tamu.edu dbname=team910_d10_db user=oyonay12 password=MYPASSWORDHERE")
cur = conn.cursor()

# Read in json data
with open(JSON_FILENAME, 'r') as f:
  data = json.load(f)

  # Iterate over each line of the data, whcih in this case is a seperate json data entry
  for line in data:
    # Messy, but don't attempt to insert if a data field isn't read properly
    missing_data = False
    # Dictionary access this lines values for the keys that match our schema
    for i in info:
        if i not in line:
            missing_data = True

    # Only insert the values to test if they are all present
    if not missing_data:
      # WARNING: Will halt operation if there is a duplicate PRIMARY_KEY
      # formulate the query:
      query = "INSERT INTO " + TABLE_NAME + " VALUES ("
      for i in info:
          query += str(data[i]) + ", "
      query = query[:-2] + ");"

      cur.execute(query)

# Commit everything to the database
conn.commit()
