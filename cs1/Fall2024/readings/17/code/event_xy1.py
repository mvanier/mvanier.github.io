from tkinter import *
import random


# Event handlers.

def exit_python(event):
    '''Exit Python.'''
    quit()

def button_handler(event):
    '''Handle left mouse button click events.'''
    print('x = {}, y = {}'.format(event.x, event.y))


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

