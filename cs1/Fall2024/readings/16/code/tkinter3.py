from tkinter import *

root = Tk()
root.geometry("800x600")

c = Canvas(root, width=800, height=600)
c.pack()
dim = 50  # side length of squares in pixels


def square(x, y, color):
    """Draw a colored square."""
    c.create_rectangle(x * dim, y * dim, (x + 1) * dim, (y + 1) * dim,
                       fill=color, outline=color)


def big_square(row, col):
    """Draw a big square made out of little squares."""
    row *= 2
    col *= 2
    square(row, col, 'red')
    square(row + 1, col, 'blue')
    square(row, col + 1, 'green')
    square(row + 1, col + 1, 'yellow')


def draw():
    """Draw the entire drawing."""
    for row in range(8):
        for col in range(6):
            big_square(row, col)


draw()
input("Press <return> to quit.")
