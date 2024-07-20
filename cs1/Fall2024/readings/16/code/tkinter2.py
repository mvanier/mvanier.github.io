from tkinter import *

root = Tk()
root.geometry("800x600")
c = Canvas(root, width=800, height=600)
c.pack()

r = c.create_rectangle(0, 0, 50, 50, fill="red", outline="red")
r2 = c.create_rectangle(0, 50, 50, 100, fill="blue", outline="blue")
r3 = c.create_rectangle(50, 0, 100, 50, fill="green", outline="green")
r4 = c.create_rectangle(50, 50, 100, 100, fill="yellow", outline="yellow")

input("Press <return> to quit.")
