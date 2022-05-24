"""
Script to convert overlay statements of this form:

  ---
  ## title

  [preamble]

  // OVERLAY

  [contents 1]

  // NEXT

  [contents 2]

  // NEXT

  ---

to:

  ---
  ## title

  [preamble]

  [contents 1]

  ---
  ## title

  [preamble]

  [contents 2]

  ---

with as many contents sections as there are "// NEXT" markers.

== MAYBE-TODO ==

Support `// END OVERLAY` to add stuff after the overlay.
This is _significantly_ harder because you have to parse the entire
slide first.

"""

import sys

# States:
#   TITLE   -- before first slide
#   NORMAL  -- outside of overlay
#   OVERLAY -- in an overlay
class State:
    TITLE   = 0
    NORMAL  = 1
    OVERLAY = 2

state = State.TITLE
lines = []
overlay_lines = []

def is_sep(line):
    return line.rstrip() == '---'

def is_overlay(line):
    return line.rstrip() == '// OVERLAY'

def is_next(line):
    return line.rstrip() == '// NEXT'

def print_sep():
    print('---')

def print_lines(end=True):
    global lines
    for line in lines:
        assert not is_sep(line)
        # print(f'{get_state()}[{len(line)}]', end=': ')
        print(line, end='')
    if end:
        print_sep()
    lines = []

def print_overlay(end=True):
    global overlay_lines
    for line in lines:
        print(line, end='')
    for line in overlay_lines:
        print(line, end='')
    if end:
        print_sep()
    overlay_lines = []

def get_state():
    match state:
        case State.TITLE:
            return 'TITLE'
        case State.NORMAL:
            return 'NORMAL'
        case State.OVERLAY:
            return 'OVERLAY'
        case _:
            raise Exception(f'Unknown state: {state}')

for line in sys.stdin:
    # print('%% ' + line, end='')
    if is_sep(line):
        # print('%% SEP; state = ' + get_state())
        match state:
            case State.TITLE:
                print_lines()
                state = State.NORMAL
            case State.NORMAL:
                print_lines()
                lines = []
            case State.OVERLAY:
                # Done with overlay.
                print_overlay()
                lines = []
                state = State.NORMAL
    elif is_overlay(line):
        # print('%% OVERLAY; state = ' + get_state())
        match state:
            case State.TITLE:
                raise Exception('Overlays not allowed on title slide')
            case State.NORMAL:
                state = State.OVERLAY
            case State.OVERLAY:
                raise Exception('Overlays can\'t nest')
    elif is_next(line):
        # print('%% NEXT; state = ' + get_state())
        match state:
            case State.TITLE:
                raise Exception('Overlays not allowed on title slide')
            case State.NORMAL:
                raise Exception('Overlays can\'t start with a NEXT')
            case State.OVERLAY:
                # Flush the overlay buffer.
                print_overlay()
    else:
        # print('%% OTHER; state = ' + get_state())
        match state:
            case State.TITLE:
                lines.append(line)
            case State.NORMAL:
                lines.append(line)
            case State.OVERLAY:
                overlay_lines.append(line)

# Print any remaining lines at the end.
if overlay_lines:
    print_overlay(False)
else:
    print_lines(False)

