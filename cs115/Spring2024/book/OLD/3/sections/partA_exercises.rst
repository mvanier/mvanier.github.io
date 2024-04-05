Part A: Basic exercises
=======================

This assignment is mainly concerned with algebraic data types and type
classes. Here we will do some exercises involving the ``Eq``, ``Ord``
and ``Num`` type classes. There is also a test script for this section
(see below), which you should run to see if your code works correctly
before submitting your assignment.


1. ``Eq`` and ``Show`` for ``Nat1``
-----------------------------------

[**1 mark**]

In class, we saw the definition of a simple algebraic data type for
natural numbers:

.. code-block:: haskell

   data Nat = Zero | Succ Nat

We'll be creating our own variants of this type to experiment with
type class instances.  First, we'll write the ``Nat1`` type:

.. code-block:: haskell

   data Nat1 = Zero1 | Succ1 Nat1

Write out manual definitions of instances of the ``Eq`` and ``Show``
type classes for ``Nat1``.
Make sure that ``show (Succ1 Zero1)`` returns ``"Succ1 Zero1"``
and not |eg| ``"Succ1 (Zero1)"``.
For larger numbers, put parentheses around all arguments to ``Succ1``
except for ``Zero1`` (for instance ``Succ1 (Succ1 Zero1)`` for 2.)

.. note::

   In this and all the problems
   involving ``Nat`` and ``Nat``-like types in this section,
   *do not* convert your ``Nat``\s (or related types)
   to integers (or to other ``Nat``-like types)
   and do the required operations on the other type
   unless we specifically say to do so.
   In fact, the only time we will allow this is when the problem is
   to convert a ``Nat`` (or related types) to integers.
   If you violate this rule, you will get no credit for those problems.


2. Deriving ``Eq`` and ``Show`` for ``Nat2``
--------------------------------------------

[**0.5 marks**]

Next, we'll define the ``Nat2`` type:

.. code-block:: haskell

   data Nat2 = Zero2 | Succ2 Nat2

Make Haskell derive the ``Eq`` and ``Show`` instances
for ``Nat2`` by adding a ``deriving`` annotation to this type.


3. ``Ord`` for ``Nat2``
-----------------------

[**0.5 marks**]

Write out an explicit instance definition
of the ``Ord`` type class for the ``Nat2`` type.
This can be done with a single method definition (for the ``<=`` operator)
since all other ``Ord`` methods can be defined in terms of ``<=``.

Also, note that if we wanted,
we could have Haskell derive the ``Ord`` instance for us.
Sometimes this will give us the definition we want, and sometimes it won’t.
Will it work in this case? Why or why not?
(Include your answer in a comment.)


4. ``Eq`` and ``Ord`` for ``SignedNat``
---------------------------------------

[**1 mark**]

.. note::

   For the rest of this section, we'll be using the ``Nat`` type defined as:

   .. code-block:: haskell

      data Nat = Zero | Succ Nat
        deriving (Eq, Show, Ord)

Let’s say we would like to represent negative numbers as well as natural
numbers, while keeping a unary representation. I’m not sure why we would
want this, but let’s say we do. We define this datatype:

.. code-block:: haskell

   data SignedNat =
     Neg Nat | Pos Nat
     deriving (Show)

The ``Eq`` and ``Ord`` instances for ``Nat`` exist
(since we're deriving them).
Write out the ``Eq`` and ``Ord`` instance definitions
for the ``SignedNat`` type.
Could you just use automatically-derived definitions
for the ``Eq`` and ``Ord`` instances?
Write the answer in a comment.


5. ``Num`` for ``SignedNat``
----------------------------

[**3 marks**]

Write out the ``Num`` instance definition for ``SignedNat``.

.. tip::

   * For details on the ``Num`` methods see
     https://hackage.haskell.org/package/base/docs/Prelude.html#t:Num.
     (It’s actually more detail than you need.)

   * Note that ``signum n`` should be ``-1`` if ``n < 0``, ``0`` if
     ``n == 0``, and ``+1`` if ``n > 0``, where ``-1``, ``0``, and
     ``1`` are expressed as their ``SignedNat`` equivalents.

   * A clean way to do this for longer method definitions is to
     implement the ``Num`` methods as separate stand-alone functions
     |eg| ``addSignedNat``, ``mulSignedNat`` |etc| and then write the
     instance definition by referring to those functions. Also note
     that you don’t have to implement all 7 ``Num`` methods;
     specifically, you only have to implement one of (``(-)``,
     ``negate``) because the other can be defined trivially in terms of
     it.

   * Beware of subtle problems involving zero.

   * You’ll probably find it useful to define some helper functions to
     do arithmetic operation on ``Nat``\s.

   * Watch out for compiler warnings about non-exhaustive pattern matches!
     It's easy to miss cases.


6. ``signedNatToInteger``
-------------------------

[**1 mark**]

Write a function called ``signedNatToInteger`` which converts a ``signedNat``
value to an ``Integer``, as the name suggests. This is used by the test suite.

Of course, here you are allowed (required) to convert ``Nat``\s and
``signedNat``\s to integers.


7. ``UnaryInteger``
-------------------

[**2 marks**]

Is there anything about the ``SignedNat`` datatype that strikes you as a bit
ugly and redundant? Come up with a different datatype to represent positive and
negative integers called ``UnaryInteger`` in the spirit of ``Nat`` (|ie| a
unary encoding) that doesn’t have this redundancy. Does it have any other
problems? Can you think of a way to fix even those problems, possibly at the
cost of increased complexity?

The purpose of this exercise is to illustrate the fact that designing datatypes
involves tradeoffs, and it’s not always possible to do a perfect job. In
particular, there are invariants that you may want Haskell to enforce that
Haskell can’t enforce.

For this problem, we’re not demanding that you come up with the exact datatypes
we did, just that you try some alternatives and think about the problems that
come up.

Also, note that you don’t have to implement the new datatype’s operations. Just
write up the ``data`` declarations and describe them in words in a comment.


8. Generic ``factorial``
------------------------

[**1 mark**]

Write a definition of the factorial function called ``factorial`` which will
work for any instance of ``Num`` and ``Ord``. Make sure that it reports an
error when given a negative number. Verify that it works for the ``SignedNat``
datatype by computing the result of ``factorial (Pos (Succ (Succ (Succ
Zero))))``. (Write the answer in a comment.)

.. note::

   Remember that integer literals can represent any type which is an
   instance of the ``Num`` type class.

