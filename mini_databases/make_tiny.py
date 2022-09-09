# writes the first NUM_LINES from BIG_FILE to TINY_FILE
BIG_FILE = 'yelp_academic_dataset_user.json'
TINY_FILE = 'tiny.json'
NUM_LINES = 10

f = open(BIG_FILE)
g = open(TINY_FILE, 'w')
for i in range(NUM_LINES):
    g.write(f.readline())

g.close()
