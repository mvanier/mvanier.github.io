Part B: Operators
=================

In mathematics, an *associative* operator ``OP`` is one that has the
property that ``x OP (y OP z) == (x OP y) OP z``. In other words, in a
chained operator expression of the form ``x OP y OP z``, where you put
the parentheses doesn’t matter; you can interpret the expression as
either ``x OP (y OP z)`` or ``(x OP y) OP z`` and it will mean the same
thing.

Associativity in programming languages like Haskell doesn’t usually mean this
kind of associativity, because most programming languages (including Haskell)
aren’t powerful enough to express the concept of mathematical associativity.
Instead, associativity (more technically known as *fixity*) is just a syntactic
property of an operator which allows the parser to correctly interpret
expressions of the form ``x OP y OP z``.

If the operator ``OP`` is "left-associative",
this will be interpreted as ``(x OP y) OP z``
(put the parentheses around the leftmost operator subexpression).
If it’s "right-associative", it will be interpreted as ``x OP (y OP z)``
(put the parentheses around the rightmost operator subexpression).
If it’s "non-associative", simply disallow the expression altogether
(|ie| require the programmer to explicitly put the parentheses in
to indicate what the intended meaning is).

.. note::

   Some operators can only be non-associative,
   because they can’t be chained in this way due to their types.

Haskell allows you to define your own operators and to set both the
precedence of the operators (from 0 to 9) and their
associativities/fixities (``infix`` for non-associative, ``infixl`` for
left-associative, or ``infixr`` for right-associative). The default
associativity (in case you don’t set it) is left-associative.

Some operators have type signatures which require a particular associativity to
work in chained operator expressions, while with other operators, you have a
choice, though sometimes one choice is much better than another. For instance,
the "cons" operator (``:``) must be right-associative for expressions like ``1
: 2 : [3]`` to work, while the addition operator (``+``) can be either right-
or left-associative (it’s actually left-associative, but it would work
correctly either way). Some operators like the division operator (``/``)
could theoretically be either right- or left-associative but are
left-associative to conform to mathematical practice (because the ``/``
operator is not associative in the mathematical sense, so
right-associative and left-associative interpretations of chained
operators give different results). Some operators like the equality
operator (``==``) are non-associative; you might wish that an expression
like ``x == y == z`` means the same thing as ``(x == y) && (x == z)`` but
it doesn’t (in fact, it’s a syntax error). This makes sense, because ``x
== (y == z)`` requires that ``x`` but not ``y`` or ``z`` be a ``Bool``,
while ``(x == y) == z`` requires that ``z`` but not ``x`` or ``y`` be a
``Bool``. The point is, you sometimes have to think a bit in order to set
the associativity of a user-defined operator correctly.

Note also that Haskell allows you to set invalid associativities for
your operators! Any operator can be declared ``infix``, which simply
means that chained operator expressions using that operator become parse
errors and are rejected. Or an operator which should be declared
``infixr`` could be declared ``infixl`` instead, which means that
chained operator expressions will be interpreted incorrectly, leading to
type errors in some cases and other kinds of errors in other cases.

Also, operators in Haskell (including user-defined operators) can only
be made from "operator characters", which means symbolic characters (not
``A-Z`` or ``a-z`` or ``0-9`` or ``_``) and not including some
characters with special functions (like parentheses, square brackets
|etc|).


1. What is the associativity of this operator?
----------------------------------------------

[**5 marks**]

For the first part of this problem, we will show you some new operators
we’ve defined, and you should indicate what the associativity (fixity)
should be. Here are your choices:

* If neither ``infixr`` or ``infixl`` will work with chained operator
  expressions (|ie| both of them lead to type errors), declare it to be
  ``infix`` (non-associative).

* If the operator can be declared either ``infixr`` or ``infixl``
  without resulting in a type error for chained operator expressions,
  declare it ``infixl`` but add a comment stating that it could be
  ``infixr`` as well.

* If the operator can be declared as only one of ``infixr`` or
  ``infixl`` without resulting in a type error for chained operator
  expressions, declare it to be the correct associativity (either
  ``infixr`` or ``infixl``; whichever works).

When figuring out if a "chained operator expression" works for either
the ``infixr`` or ``infixl`` associativity, choose whichever values you
like as arguments to the operator; if any choice of arguments works,
it’s valid for that associativity. Give one example of a chained
operator expression in that case. No examples are needed if the correct
associativity is ``infix`` (|ie| non-associative).

Here are the operators (just a description, not a definition):

* The operator ``>#<`` compares two integer scores and tells you the
  winner. For example, ``51 >#< 40 = "First Player"``, and
  ``21 >#< 21 = "Tie"``. It has type
  ``Integer -> Integer -> String``.

* The operator ``+|`` adds two integers and takes the last digit of
  their sum. For example, ``7 +| 6 = 3``. It has type
  ``Integer -> Integer -> Integer``.

* The operator ``&<`` appends an integer to the end of a list. For
  example, ``[1, 2] &< 3 = [1, 2, 3]``. It has type
  ``[Integer] -> Integer -> [Integer]``, where ``[Integer]``
  means a list of integers.

* An operator ``>&&`` that "cons"es an integer twice to the beginning
  of a list. For example, ``1 >&& [2, 3] = [1, 1, 2, 3]``. It has type
  ``Integer -> [Integer] -> [Integer]``.

Write your answers in a Haskell comment,
along with a brief explanation of why that answer is correct,
plus examples (if needed).


2. A curious operator
---------------------

[**5 marks**]

Now consider an operator ``+#`` that adds two integers and tells you how
many digits long their sum is. For example, ``2 +# 800 = 3``, since
``802`` is three digits long. It has type
``Integer -> Integer -> Integer``. What *could* its associativity
be in order to allow chained operators to type check (give all answers
that work). What *should* its associativity be?

.. tip::

   Try writing out some examples.

Again, write your answers in a Haskell comment,
and explain why the answers are correct.

