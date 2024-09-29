from tkinter import *

def exit_python(event):
    """Exit python when the event 'event' occurs."""
    quit()  # no arguments to quit

root = Tk()
root.geometry('800x600')
c = Canvas(root, width=800, height=600)
c.pack()
a = c.create_rectangle(0, 0, 50, 50, fill='red', outline='red')
root.bind('<q>', exit_python)
root.mainloop()

