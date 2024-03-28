"""
Generate a random string of letters in [a-z0-9] of length 5.
This is used to tag old course books so that students can't access them
(without asking me for the random key).
"""

import string
from random import *

chars = string.ascii_lowercase + string.digits

s = ''.join(choice(chars) for i in range(5))

print(s)
