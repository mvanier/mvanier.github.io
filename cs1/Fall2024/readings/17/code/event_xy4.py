from tkinter import *
import random

# Graphics commands.

def draw_square(canvas, color, size, location):
    x, y = location
    x1 = x - size/2
    y1 = y - size/2
    x2 = x + size/2
    y2 = y + size/2
    square = canvas.create_rectangle(x1, y1, x2, y2, fill=color, outline=color) 
    return square

def draw_random_square(canvas, x, y, min_size, max_size):
    color = '#{:06x}'.format(random.randrange(16**6))
    size = random.randint(min_size, max_size)
    square = draw_square(canvas, color, size, (x, y))
    return square


# Event handlers.

def button_handler(event):
    '''Handle left mouse button click events.'''
    square = draw_random_square(canvas, event.x, event.y, 50, 150)
    squares.append(square)

def key_handler(event):
    '''Handle key press events.'''
    global squares
    key = event.keysym
    if key == 'q':
        quit()
    elif key == 'c':
        # Clear all squares from the canvas.
        for square in squares:
            canvas.delete(square)
        squares = []

if __name__ == '__main__':
    root = Tk()
    root.geometry('800x600')
    canvas = Canvas(root, width=800, height=600)
    canvas.pack()
    squares = []

    # Bind events to handlers.
    root.bind('<Key>', key_handler)
    canvas.bind('<Button-1>', button_handler)

    # Start it up.
    root.mainloop()

