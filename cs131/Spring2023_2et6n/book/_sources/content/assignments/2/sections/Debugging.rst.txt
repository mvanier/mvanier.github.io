Debugging
=========

Debugging a multi-file OCaml project can be challenging.  Here are some tips
and tricks to make it less painful.


About REPLs
-----------

The term "REPL" is an acronym meaning
:raw-html:`"<b>R</b>ead-<b>E</b>val-<b>P</b>rint-<b>L</b>oop"`.
It's a fancy name for an interactive interpreter.
Many languages have REPLs, such as Python, Ruby,
almost all Scheme and Lisp dialects, OCaml, and Haskell.
Other languages like C, C++ and Go do not.
(Java didn't originally, but now has the ``jshell`` REPL.)

The reason that REPLs are popular is that they are a big help
for debugging, for writing quick throwaway code to explore an
unfamiliar library, and as a way to interactively prototype a solution.
Here, we're going to concentrate on using REPLs for debugging.

Each project in CS 131 has *two* REPLs:
the OCaml REPL and the REPL for the language being implemented.
Each of them has different uses, which we'll describe below.


Using the |uscheme| REPL
------------------------

Running ``make test`` will work fine if all of the tests pass,
but if a test fails, you have to dig deeper.
Since each of the languages we will implement provides a REPL,
it makes sense to use it to help understand when a test fails.

The first thing to do when a test fails is to determine which test file
contained the failing test.
The output from ``make test`` will tell you this.

The second thing is to isolate which test in the file failed.
For most test failures
(where the output of some code isn't what was expected),
the output of ``make test`` will also tell you this.
However, there are cases when ``make test``
doesn't give you enough information.
An example would be when the code in a ``check-expect`` test
loops forever.
You will be told that a test in a particular file timed out,
but not which test timed out.
What do you do then?

Fundamentally, all that ``make test`` is doing is running the
|uscheme| interpreter on all of the test files,
and collecting and displaying the output if a test fails.
You can also manually run a test file.  For instance,
if a test in ``tests/lists.scm`` fails, you can type:

.. code-block:: text

   $ ./uscheme tests/lists.scm

and look at the output.  At this point, your main goal is to find
the test that failed.

The output from tests run interactively is more verbose than the output
from just running ``make test``.  For instance, passing tests will
also print the location of the test that passed.

.. note::

   This is a new feature!  If you really hate it, you can disable it
   by setting the ``verbose_tests`` variable in ``eval.ml`` to ``false``.

Let's define a simple test file called ``foo.scm`` and put it in the
``tests`` directory:

.. code-block:: text

   ;; foo.scm
   (check-expect (+ 1 1) 2)
   (define loop () (loop))
   (check-expect (loop) 0)
   (check-expect (* 2 3) 6)

Looking at this, you can see that the ``loop`` function will loop indefinitely.
If you run this file, here's what you see:

.. code-block:: text

   $ ./uscheme tests/foo.scm
   check-expect test passed (tests/foo.scm: 2:1-24)

and then it will hang (loop forever).
The test output at least tells you that the last passing test was at line 2.
So you should look for the test following the test on line 2,
which is the one that calls ``(loop)`` and thus loops forever.
(Of course, most examples will be more complicated than this.)

Once you have located the test that fails, try running it interactively
in the |uscheme| REPL:

.. code-block:: text

   $ ./uscheme
   >>> (define loop () (loop))
   >>> (loop)  ; runs forever

This allows you to confirm what the problem is.
Now you've narrowed down the problem significantly.


Using the OCaml REPL
--------------------

Often a bug won't be in the code you write in |uscheme|
but in the OCaml code you write to implement the interpreter.
Finding these bugs can be tricky.
For simple OCaml programs (like most of the ones in CS 4),
you can simply load files into the OCaml REPL (``utop``)
using the ``#use`` directive:

.. code-block:: ocaml

   # #use "lab3.ml";;

and then test the functions interactively
to see if they are doing the right thing.
But with a large multi-file program like the interpreters in this assignment
(and in the rest of the course), it's not as easy.

Starting with this assignment, we've added a ``Makefile`` target that will
allow you to open an OCaml REPL (using ``utop``) "pre-loaded" with the
code from the assignment.  The steps are:

#. Compile the code (type ``make``).
#. Type ``make repl``.  That will start up ``utop`` with the
   code for the assignment pre-loaded.
#. Inside ``utop``, experiment with the functions that you think
   aren't working correctly.

Here's an example to show how this works:

.. code-block:: text

   $ make
   $ make repl
   # open Env;;
   # string_of_value (IntVal 10);;
   - : string = "10"

Technically, we could have omitted the ``open Env;;`` line and typed
``Env.string_of_value (IntVal 10);;``, but it's nice not to have to
qualify all names from a module.

Using the REPL is useful when you think a specific function
is giving the wrong answer for a specific set of inputs.
It's not always easy to use, though,
since sometimes it's hard to create the necessary inputs.
But it will make debugging easier in many cases. 

The OCaml debugger
------------------

The most extreme approach is to use the `OCaml debugger`_.
This will allow you to step through your code with microscopic precision.

To run the OCaml debugger on your code, compile it normally and then
just call it with the ``ocamldebug`` program:

.. code-block:: text

   $ make
   $ ocamldebug ./uscheme
           OCaml Debugger version 4.14.1

   (ocd)

The ``(ocd)`` is the OCamldebug prompt. To run the interpreter, type ``run``:

.. code-block:: text

   (ocd) run
   >>> (+ 1 2)
   3

Now, if there is code that crashes the interpreter,
you can enter it and the debugger will halt on the crash.
You can set breakpoints, move back in time (!)
and do all the things you can usually do in debuggers.

.. warning::

   Line-by-line debugging, which you're probably used to
   from imperative languages,
   doesn't work in functional languages like OCaml.
   Instead, the debugger works on "events",
   which are "interesting" locations in the source code.
   This means that the learning curve for using the debugger
   is greater than that in an imperative language.
   This is one reason why few OCaml programmers use the debugger.
   (The others are the excellent OCaml REPL and the type checker,
   which catches most simple bugs.)
   Nevertheless, for really tricky bugs, it's good to have the
   debugger available.




