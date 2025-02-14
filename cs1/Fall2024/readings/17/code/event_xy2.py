from tkinter import *
import random

# Graphics commands.

def draw_square(canvas, color, size, location):
    x, y = location
    x1 = x - size/2
    y1 = y - size/2
    x2 = x + size/2
    y2 = y + size/2
    canvas.create_rectangle(x1, y1, x2, y2, fill=color, outline=color) 

def draw_random_square(canvas, x, y, min_size, max_size):
    color = '#{:06x}'.format(random.randrange(16**6))
    size = random.randint(min_size, max_size)
    draw_square(canvas, color, size, (x, y))
   

# Event handlers.

def exit_python(event):
    '''Exit Python.'''
    quit()

def button_handler(event):
    '''Handle left mouse button click events.'''
    draw_random_square(canvas, event.x, event.y, 50, 150)


if __name__ == '__main__':
    root = Tk()
    root.geometry('800x600')
    canvas = Canvas(root, width=800, height=600)
    canvas.pack()

    # Bind events to handlers.
    root.bind('<q>', exit_python)
    canvas.bind('<Button-1>', button_handler)

    # Start it up.
    root.mainloop()

