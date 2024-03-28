"""
Preprocessor from `.md.in` files to `.md` files.

This preprocessor implements several shortcuts:

In normal text:

  %%              --> comment to end of line
  @br@            --> <br/>
  @sp@            --> &nbsp;
  @hsp@           --> &hairsp;
  @eg@            --> _e.g._
  @ie@            --> _i.e._
  @nb@            --> _N.B._
  @etc@           --> _etc._
  @--@            --> &ndash;
  @---@           --> &mdash;
  @smiley@        --> &#9786;
  @vsp1@          --> $$\n\\\n$$ (single line of vertical space)
  @zw@            --> &#8203;  (zero width space)
  @eqtag FOO@     --> $$\n\label{}\tag{FOO}\n$$

  // IMG NAME --> <img src="images/NAME" alt="NAME" />
  // IMG NAME xxx --> 
     <img src="images/NAME" alt="NAME" width="xxx" />
  // IMG NAME xxx yyy --> 
     <img src="images/NAME" alt="NAME" width="xxx" height="yyy"/>

In math mode:
  \OP      --> \operatorname

In code blocks:
  **       --> toggles emphasis

"""

import re, sys, copy

state      = 'normal'  # 'normal' or 'in_code'
code_size  = 'normal'  # 'normal' or 'smaller'
code_lines = []

# Substitutions.
subs = [(r'{br}',            r'<br/>'),
        (r'@sp@',            r'&nbsp;'),
        (r'@hsp@',           r'&hairsp;'),
        (r'@eg@',            r'_e.g._'),
        (r'@ie@',            r'_i.e._'),
        (r'@nb@',            r'_N.B._'),
        (r'@etc@',           r'_etc._'),
        (r'@--@',            r'&ndash;'),
        (r'@---@',           r'&mdash;'),
        (r'@star@',          r'*'),
        (r'@3star@',         r'***'),
        (r'@smiley@',        r'&#9786;'),
        (r'@vsp1@',          '$$\n\\\\\\\\\n$$'),
        (r'@zw@',            r'&#8203;'),   # zero width space
        (r'\\OP',            r'\\operatorname'),
        (r'@eqtag ([^)]*)@', '$$\\n\\\\label{}\\\\tag{\\1}\\n$$'),
       ]

def text_sub_img(line):
    """
    Convert image specification to HTML.
    """
    line2 = re.sub(r'// IMG ([A-Za-z0-9_\-.]+)\s*$',
                   r'<img src="images/\1" alt="\1" />\n',
                   line) 
    if line2 != line:
        return line2

    line3 = re.sub(r'// IMG ([A-Za-z0-9_\-.]+)\s+([0-9]+)\s*$',
                   r'<img src="images/\1" alt="\1" width="\2" />\n',
                   line) 
    if line3 != line:
        return line3

    line4 = re.sub(r'// IMG ([A-Za-z0-9_\-.]+)\s+([0-9]+)\s+([0-9]+)\s*$',
                   r'<img src="images/\1" alt="\1" width="\2" height="\3" />\n',
                   line) 
    return line4

def text_sub(line):
    """
    Apply text substitutions to a non-code line.
    """
    if line.startswith('// IMG'):
        return text_sub_img(line)

    for (init, final) in subs:
        line = re.sub(init, final, line)
    return line

def process_code_line(line):
    new_line = ''
    state = 0
    stars_patt = re.compile(r'@@stars ([1-9])@@')
    while line:
        if line.startswith('**'):
            line = line[2:]
            if state == 0:
                new_line += '<em>'
                state = 1
            else:
                new_line += '</em>'
                state = 0
            continue
        elif line.startswith('*|'):
            line = line[2:]
            new_line += '<span class="comment">'
            continue
        elif line.startswith('|*'):
            line = line[2:]
            new_line += '</span>'
            continue
        elif m := stars_patt.match(line):
            # Get the number of stars.
            nstars = int(m.group(1))
            # Delete the pattern.
            line = stars_patt.sub('', line)
            # Add the asterisks to the new line.
            new_line += '*' * nstars
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
        process_code_line(line[:-1])
    print('</code></pre>')

for line in sys.stdin:
    match state:
        case 'normal':
            if line.startswith('// CODE SMALLER'):
                code_lines = []
                state = 'in_code'
                code_size = 'smaller'
            elif line.startswith('// CODE'):
                code_lines = []
                state = 'in_code'
                code_size = 'normal'
            else:
                # Not in code.
                # Skip comments.
                if line.startswith('%%'):
                    continue
                # Apply text substitutions.
                print(text_sub(line), end='')
        case 'in_code':
            if line.startswith('// END CODE'):
                process_code(code_lines, code_size)
                state = 'normal'
                code_size = 'normal'
            else:
                code_lines.append(line)

