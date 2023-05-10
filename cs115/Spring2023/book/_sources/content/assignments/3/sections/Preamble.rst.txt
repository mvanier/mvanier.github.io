Preamble
========

Due date
--------

This assignment is due on |dueday|, |lab3due| at |duetime|.


Coverage
--------

This assignment covers the material up to lecture 8.


Grading update
--------------

.. note::

   For this and future assignments,
   point values of each problem will be indicated.
   Each section will still be worth 10 points,
   and the overall grade will still be the average of the section scores.
   This will make it easier to see which problems are worth how much.


What to hand in
---------------

Write all of your code for the first two sections in a single text file
called ``Lab3ab.hs``. The sparse matrix code should be a separate file
called ``SparseMatrix.hs``. These files should be submitted to CodePost
as ``Assignment 3``.


Writing and testing your code
-----------------------------

In the code base for the assignment (available on Canvas)
we are supplying automated test scripts for part A and part C.
The test script for part A is ``SignedNatTests.hs``
and the one for part C is ``SparseMatrixTests.hs``.
In addition, we are supplying a ``Makefile``.
You can run the tests by putting all three files
in the same directory as your code
(which must have the correct module names
|ie| ``Lab3ab.hs`` for part A and ``SparseMatrix.hs`` for part C).
Then type:

.. code-block:: text

   $ make test_partA
   
to test part A only, or

.. code-block:: text

   $ make test_partC

to test part C only, or

.. code-block:: text

   $ make test

to test both parts A and C.

As in assignment 2,
the test code relies on the ``HUnit`` and ``QuickCheck`` modules
for unit testing and randomized testing, respectively.
The test script cannot test for everything,
but it should catch the most serious problems.

One trick you might want to try
in case you have a test failure which is difficult to debug
is to use the ``trace`` function from the ``Debug.Trace`` module.
This function takes a string and a value,
prints the string and returns the value.
The trick is that it does this without being in the ``IO`` monad,
so it uses the dreaded ``unsafePerformIO`` function.
This is OK since all it does is print output before returning a value.
For a particular value in a computation, you can do this:

.. code-block:: haskell

   -- Somewhere inside an expression, you want to print the value of x.
   -- Instead of just `x`, write:
   (trace ("x = " ++ show x) x)

Then, when this code is run,
you will get a printout indicating what the value of ``x`` was
at that point in the program.
This is the Haskell way of emulating
the common style of debugging used in other languages using print statements.


Haskell note: Infinite loops in tests
-------------------------------------

One thing to be wary of with the randomized tests in parts A and C
is infinite loops.  The test framework can't detect this,
so it will seem that the tests have just stopped producing output.

It turns out that reporting failures on infinite loops is very hard to do
reliably in GHC Haskell.  There is a ``timeout`` function,
but it doesn't work when the infinite loop happens in a tight inner loop
of a function that doesn't internally allocate memory.
If you're curious, there is a discussion at `this link
<https://stackoverflow.com/questions/55336948/how-can-i-use-the-haskell-timeout-function-in-system-timeout-to-halt-runaway-c>`_
and also `here
<https://downloads.haskell.org/ghc/latest/docs/users_guide/bugs.html#bugs-in-ghc>`_.

The bottom line is that if you see your tests hanging,
you probably have an infinite loop in the code being tested,
and you need to look at it carefully.


Haskell note: The ``ScopedTypeVariables`` pragma
------------------------------------------------

.. note::

   You can skip this section for now,
   but if you get a weird error message involving "rigid type variables",
   please read it for an explanation of what's going on.

For part C (the sparse matrix implementation), there will be one
function (sparse matrix multiplication) which is naturally done by
dividing it up into helper functions. If you do this, you may well
encounter a strange kind of type error that you haven’t seen before.

The problem arises when a function is polymorphic
|ie| its type depends on a type variable, say ``a``.
Let’s assume that this function has an explicit type signature,
as most top-level functions do in Haskell.
Let's also assume that there is a helper function,
probably defined in a ``where`` clause,
which is also polymorphic
(its type depends on a type variable ``a``)
and which also has an explicit type signature.
So the function looks something like this:

.. code-block:: haskell

   foo :: a -> Foo a
   foo x = ...
     where
       bar :: a -> Int -> Foo a
       bar y n = ...

The issue here is that Haskell can’t assume
that the ``a`` type variable in the type declaration of the ``bar`` function
(defined in the ``where`` clause)
is the same type variable as the ``a`` type variable
in the type declaration of the ``foo`` function
(defined at the top level).

.. code-block:: haskell

   foo :: a -> Foo a   -- type variable `a` (top level)
   foo x = ...
     where
       bar :: a -> Int -> Foo a  -- `a` is not necessarily the same type variable!
       bar y n = ...

In some cases, this doesn’t cause any problems.
Specifically, if you can move the function ``bar``
to the top level with no errors,
then everything should still work.
On the other hand,
if ``bar`` is using anything defined in ``foo``
that has a type which depends on ``a``,
you’re in trouble.
That’s this scenario:

.. code-block:: haskell

   foo :: a -> Foo a
   foo x = ...  -- N.B. `x` has type `a`
     where
       bar :: a -> Int -> Foo a
       bar y n =
         ... x ... -- `bar` uses `x` (from the outer scope)
                   -- but `x` was not an argument to `bar`

What happens here is that ``x`` has the type ``a`` (the outer ``a``) but
inside ``bar``, you probably expect that ``x`` and ``y`` both have the
same type (``a``) when in fact Haskell considers them to be different.
If you try to use ``x`` inside ``bar`` as if it has the same type as
``y``, your code won’t compile and you’ll see errors describing
something called ``rigid type variables``, which isn’t very helpful.

There are three different ways to fix this.

First, you can remove the type signature of ``bar``, leading to this:

.. code-block:: haskell

   foo :: a -> Foo a
   foo x = ...
     where
       bar y n = ... [bar uses x but x was not an argument to bar] ...

Here you are depending on the type inference engine to sort things out,
and it usually does what you want. We don’t like this, because we like
having explicit type signatures on all of our functions.

The second way is to add extra arguments instead of using arguments in
an outer scope:

.. code-block:: haskell

   foo :: a -> Foo a
   foo x = ... bar x ... -- `bar` called with `x` as its first argument
     where
       bar :: a -> a -> Int -> Foo a
       bar x y n = ...

This will work fine, but the code can get significantly messier.
Basically, you are rewriting ``bar`` so that it could be a top-level
function.

The third way is the slickest. First, at the top of the entire file,
before the ``module`` line, add a *language pragma* that looks like
this:

.. code-block:: haskell

   {-# LANGUAGE ScopedTypeVariables #-}

Language pragmas are optional opt-in features of GHC. This turns on one
such feature called ``ScopedTypeVariables``.

Then, rewrite the type signature of the ``foo`` function as follows:

.. code-block:: haskell
   :emphasize-lines: 1

   foo :: forall a . a -> Foo a  -- note the `forall` keyword
   foo x = ...
     where
       bar :: a -> Int -> Foo a
       bar y n = ...

All you’ve done is add the ``forall a .`` before the type signature of ``foo``.
What this does (along with the ``ScopedTypeVariables`` pragma)
is say "in any helper function of ``foo`` that uses the type variable ``a``,
the ``a`` in the helper function must refer to the *same* ``a``
as the ``a`` in ``foo``'s type signature".
In other words, the *type variable* ``a`` is *scoped*
which means that the outer declaration which includes ``a``
applies to the entire function.
This is exactly what you want, and this fixes the compilation error.

.. note::

   If you have a type class qualifier,
   it has to come *after* the ``forall``:

   .. code-block:: haskell
      :emphasize-lines: 1

      foo :: forall a . Eq a => a -> Foo a
      foo x = ...
        where
          bar :: a -> Int -> Foo a
          bar y n = ...

You are allowed to use the ``ScopedTypeVariables`` pragma in this
assignment, and in all assignments from now on. It’s never necessary but
it can be convenient.

