"""
This script converts:

    // SMALLER

to:

    <div class="smaller">

and

    // END SMALLER

to

    </div>

It's pretty trivial, and could be replaced by a sed script.
"""

import sys, re

for line in sys.stdin:
    if re.search(r'// SMALLER', line):
        line = re.sub(r'// SMALLER', '<div class="smaller">\n', line)
    elif re.search(r'// END SMALLER', line):
        line = re.sub(r'// END SMALLER', '\n</div>', line)
    print(line, end='')

