Preamble
========

Due date
--------

This assignment is due on |dueday|, |lab1due| at |duetime|.


Prerequisites
-------------

The only prerequisite for the course is having taken and passed CS 4. We
absolutely require you to know the OCaml programming language before
taking this course, because all of your submissions will be in OCaml and
we don’t have time to teach that language. We will not be using any
features of the language that weren’t taught in CS 4, though one or two
minor ones (like labelled arguments) will be in the code base in a few
places.

.. note::

   The CS 4 requirement doesn’t apply if you’re a grad student. In that
   case, though, you are responsible for getting up to speed in OCaml by
   yourself.

We will also assume that you have a rudimentary knowledge
of Unix-style terminal commands
(at roughly the level required for CS 4).
If not, please read |eg| a Linux tutorial,
because we will be using the terminal a lot in this course.

.. note::

   You are **not** required to use Linux in this course. The terminal in
   MacOS works pretty much the same way (both Linux and MacOS are Unix
   systems), so the commands should work fine assuming the software is
   installed correctly. Windows terminal commands are completely
   different. If you have Windows, you should install
   `Windows Subsystem for Linux`_ (WSL),
   which gives you a full version of Linux running inside of Windows. 
   (Use that instead of trying to run Haskell natively on Windows.)


Syllabus
--------

Please make sure you have read the
:doc:`Syllabus </content/admin/Syllabus>` page and the
:doc:`Collaboration policies </content/admin/Collaboration_policies>`
page before submitting any assignments,
so you know how the course is organized,
what the grading policies are,
what is and is not acceptable collaboration |etc|.


Software installation
---------------------

See the :doc:`Software </content/Software>` page for instructions on how
to install the course software.


External websites
-----------------

In addition to this book, we will be using the following websites:

* The course Canvas page, which is where lectures will be posted,
  as well as source code for assignments, papers of interest,
  and teaching assistant contact information.

* The course Piazza page, which is where most course-related announcements
  will be posted, as well as serving as a question-and-answer forum.

* The course CodePost_ page, which is where assignments will be submitted
  and graded.

Students will be enrolled in Piazza and CodePost by the course instructor.
If you added the course late, you may need to remind the instructor to add you.

In addition, some TAs and the instructor may choose to host office hours online
on Zoom.


Code base
---------

All assignments will have a code base, which includes test code
and sometimes template code for modules.
These are posted on the course Canvas site in the Modules section
as a single zip file.

The code base for the |imp| language contains two subdirectories:
``imp`` and ``sexpr``.
The ``sexpr`` subdirectory contains the code
to parse S-expressions from strings and files.
The ``imp`` subdirectory contains the base implementation
of the ``imp`` language.
You should unzip the file in a directory and do the following:

#. ``cd`` into the ``sexpr`` subdirectory and run ``make``.
#. Then ``cd`` into the ``imp`` subdirectory and run ``make``.

If all goes well, this should create the ``imp`` executable, which you
can run as follows:

.. code-block:: text

   $ ./imp
   >>>

Then you can type in |imp| expressions at the prompt. Control-D will
exit the interpreter.

The ``rlwrap`` program
~~~~~~~~~~~~~~~~~~~~~~

If you can, try to install the ``rlwrap`` program,
which will allow you to do line editing on your interpreters the same way
you do on |eg| ``utop`` with OCaml.
Most package managers will allow you to install ``rlwrap``.
On MacOS using Homebrew, the command is:

.. code-block:: text

   $ brew install rlwrap

On Ubuntu Linux, it's:

.. code-block:: text

   $ sudo apt install rlwrap

You can use it like this:

.. code-block:: text

   $ rlwrap ./imp

and then you can use line editing with your |imp| interpreter.
(The same applies to all of the interpreters in this course.)

Writing and testing your code
-----------------------------

You will write two new files of |imp| code:

* ``lab1a.imp``, which is all the |imp| code you will write for part A
* ``lab1c.imp``, which is all the (extended) |imp| code you will write
  for part C

You will also edit the file ``imp.ml`` in accordance with the
instructions given in part B.

The ``imp`` subdirectory of the code base has a ``tests`` subdirectory.
Copy your ``lab1a.imp`` and ``lab1c.imp`` files into that directory, go
back to the ``imp`` directory and type:

.. code-block:: text

   $ make test

This will run some tests on your code, all of which should pass. Of
course, passing the tests doesn’t mean that your code is perfect, but
failing a test means that something is definitely wrong.


What to hand in
---------------

For this assignment, you will submit three files to CodePost
as ``Assignment 1``:

#. ``lab1a.imp``
#. ``imp.ml`` (containing your additions as described in part B)
#. ``lab1c.imp``

