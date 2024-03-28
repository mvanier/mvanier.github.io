Part C: Programming in extended |uscheme|
=========================================

Whew! If you got through parts A and B, relax. This part will be a breeze.
|smile|

In this section you will write a number of |uscheme| functions to test
your implementation and also to get some practice writing idiomatic
Scheme functions. You should use the file ``uscheme/tests/lab2c.scm`` as
a template file. Just delete the lines that say

.. code-block:: text

   ; TODO

and replace them with your code.
The unit tests are already in the file, so once you've compiled |uscheme|,
if you type ``make test`` at the terminal prompt,
it will run these tests (as well as all the other ones).
Alternatively, to run only the tests in this file, type:

.. code-block:: text

   $ ./uscheme tests/lab2c.scm

The template file also has the type of each function in a comment before
the function, as documentation.

All of these functions should be written in a functional style, without
``set``, ``begin`` or ``while`` expressions (this is a requirement).

.. tip::

   Look at the primitive |uscheme| functions defined in ``basis.ml``
   and make sure you understand what each one does. That will make this
   section much easier.

Here are the functions you have to write:


1. ``map``
----------

This function takes these arguments:

* a function of one argument ``f``
* a list ``lst``

It returns a list obtained by applying the function ``f`` to each
element of ``lst`` (like the OCaml ``List.map`` function).


2. ``filter``
-------------

This function takes these arguments:

* a function of one argument ``f`` that returns a boolean
* a list ``lst``

It returns a list which consists of all the elements of ``lst`` for
which the function ``f`` returns ``#t`` (in their original order). (This
is like the OCaml ``List.filter`` function.) Use a ``cond`` expression
to write the only conditional in the function (this is a requirement).
Note that an "else" case in a ``cond`` can be simulated by having the
test expression be simply ``#t``.


3. ``exists?``
--------------

This function takes these arguments:

* a function of one argument ``f`` that returns a boolean
* a list ``lst``

It returns ``#t`` if there is at least one element in ``lst`` for which the
function ``f`` returns ``#t``. Again, use a ``cond`` to write the conditional.


4. ``all?``
-----------

This function is the same as ``exists?`` except that it only returns ``#t`` if
the function ``f`` returns ``#t`` for *all* of the elements of ``lst``.
Again, use ``cond``.


5. ``takewhile``
----------------

This function takes these arguments:

* a function of one argument ``f`` that returns a boolean
* a list ``lst``

It returns all of the elements of ``lst`` up to but not including the first one
for which the function ``f`` does not return ``#t``. In other words, you "take"
elements from the list "while" ``f`` returns ``#t``, and then stop once it
doesn’t. This is *not* the same as ``filter``!

Using ``cond`` is not required (here or in the rest of the functions in this
section), though it can certainly be done that way.


6. ``dropwhile``
----------------

This is like ``takewhile`` except that it drops elements from the list
argument until it finds one for which the function ``f`` returns ``#f``
(|ie| it "drops" elements "while" ``f`` returns ``#t``), and then it
returns the entire list starting from that element.


7. ``foldl``
------------

This function takes these arguments:

* a function of two arguments ``f``
* an initial value ``init``
* a list ``lst``

It computes the result of applying the function ``f`` to ``init`` and
the first element of ``lst``, then applying ``f`` to that value and the
next element of ``lst``, and so on until all elements of ``lst`` have
been processed. If ``lst`` is empty, ``foldl`` returns ``init``. In
other words, it’s like the OCaml function ``List.fold_left``.


8. ``foldr``
------------

This function takes the same arguments as ``foldl`` but "folds from the
right". If ``lst`` is empty, again return ``init``. Otherwise, apply the
function ``f`` to the *last* element of ``lst`` and ``init``, then apply
``f`` to the second-last element of ``lst`` and the result of the first
application of ``f``, {etc} until you get to the front of the list. In
other words, it’s like the OCaml function ``List.fold_right``.


9. ``curry``
------------

This function takes one argument: a function of two arguments ``f``. It
returns a *curried* version of this function, |ie| a function that takes
a single argument which has the same type as the first argument of
``f``, and returns a function of one argument which has the same type as
the second argument of ``f``. Applying either the original function or
the curried function to the same two arguments gives the same result,
though you need more parentheses if you’re using the curried version.


10. ``uncurry``
---------------

This function takes a curried version of a function of two arguments and
returns the uncurried version.


11. ``list``
------------

This function takes an arbitrary number of arguments and returns a list
of the arguments. Use the special syntax for variadic functions you
implemented in part A of this assignment. (This function’s definition is
therefore completely trivial.)

Since this function is used in the test script ``tests/lists``, once it
is working, also add it to the basis functions in ``basis.ml``.


12. ``extremum``
----------------

We want to implement variadic ``min`` and ``max``, that is ``min`` and
``max`` that can take an arbitrary number of arguments. Since the code
for those two functions is so similar, we can factor it out by defining
a function called ``extremum`` which takes two arguments: an operator
``op`` and a list of numbers ``lst``. From this, ``min`` and ``max``
have the following definitions:

.. code-block:: text

   ;; min : x1 x2 ... -> min(x1, x2, ...)
   (define min lst (extremum < lst))

   ;; max : x1 x2 ... -> max(x1, x2, ...)
   (define max lst (extremum > lst))

Define ``extremum``. Note that if ``min`` or ``max`` are applied to zero
arguments, call the ``error`` function with the argument
``'not-enough-arguments``. If they are applied to a single argument,
return that argument. Otherwise, compute the minimum/maximum of the
arguments.


13. ``o``
---------

This function takes a list of one-argument functions and composes all of
them together. Specifically:

* ``(o)`` is just the identity function
* ``(o f)`` is the same as ``f`` for any function ``f``
* ``(o f g)`` is the composition of the functions ``f`` and ``g`` |ie|
  the function that given an argument ``x`` computes ``(f (g x))``
* ``(o f g h ...)`` composes functions ``f``, ``g``, ``h`` …​ in the
  same way

.. tip::

   Define a two-argument function ``compose`` that composes exactly two
   functions, and use it and ``foldr`` to define ``o``. The solution is
   quite short.

By the way, we use the name ``o`` because it looks like the small circle
usually used in math books to represent function composition.

