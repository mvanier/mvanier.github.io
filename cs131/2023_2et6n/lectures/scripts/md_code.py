"""
Script to convert code in Markdown documents
so as to allow emphasis.
"""

import sys

# Constants.
SMALLER    = 'smaller'

state      = 'normal'
code_size  = 'normalsize'
code_lines = []

def process_line(line):
    new_line = ''
    em_state = 0
    while line:
        #print('>>> ' + line, file=sys.stderr)
        if line.startswith('**'):
            line = line[2:]
            if em_state == 0:
                new_line += '<em>'
                em_state = 1
            else:
                new_line += '</em>'
                em_state = 0
            continue
        elif line.startswith('%|'):
            line = line[2:]
            new_line += '<span class="comment">'
            continue
        elif line.startswith('|%'):
            line = line[2:]
            new_line += '</span>'
            continue
        elif line[0] == '<':
            #new_line += '&lt;&ZeroWidthSpace;'
            new_line += '&lt;&#8203;'
        elif line[0] == '>':
            new_line += '&gt;'
        else:
            new_line += line[0]
        line = line[1:]
    print(new_line)

def process_code(lines, size):
    if size == 'smaller':
        print('<pre class="smaller"><code>', end='')
    else:
        print('<pre><code>', end='')
    for line in lines:
        process_line(line[:-1])
    print('</code></pre>')

for line in sys.stdin:
    match state:
        case 'normal':
            if line.startswith('// CODE SMALLER'):
                code_lines = []
                state = 'in_code'
                code_size = SMALLER
            elif line.startswith('// CODE'):
                code_lines = []
                state = 'in_code'
                code_size = 'normalsize'
            else:
                print(line, end='')
        case 'in_code':
            if line.startswith('// END CODE'):
                process_code(code_lines, code_size)
                state = 'normal'
                code_size = 'normalsize'
            else:
                code_lines.append(line)

