Part B. Puzzles and derivations
===============================

Solve the first three of the following puzzles by desugaring the monadic
code from the ``do`` notation to the ``return`` method and the ``>>=``
and ``>>`` operators. Then substitute the definitions of ``return``,
``>>=`` and ``>>`` and derive the result.

.. note::

   For the first two problems, you do not need to desugar a
   ``do`` expression using the complete desugaring to a ``case`` expression
   to handle possible pattern-match failures. Simply rewrite it in terms of
   the "simple" desugaring involving the ``return`` and ``>>=`` operators.
   For the third problem you will need to desugar the ``do`` expression
   using the full ``case`` expression desugaring (see lecture 11 for more
   on this).

One way to shorten your derivations a little is to use ``concatMap``
instead of ``concat (map ...)``. For these exercises, you may
consider the definition of ``concatMap`` to be:

.. code-block:: haskell

   concatMap :: (a -> [b]) -> [a] -> [b]
   concatMap f lst = concat (map f lst)

Note that ``concatMap <anything> []`` is the empty list, and
``concatMap (\n -> []) <anything>`` is also the empty list. You can
then write the definition of the ``>>=`` operator for the list monad as:

.. code-block:: haskell

   (>>=) :: [a] -> (a -> [b]) -> [b]
   mv >>= f = concatMap f mv

Write your solutions in Haskell comments. Note that the order of
evaluation is not important; you can reduce the expressions in whatever
order is most convenient.


1. ``[]`` in the list monad
---------------------------

[**2 marks**]

Why does this expression:

.. code-block:: haskell

   do n1 <- [1..6]
      n2 <- [1..6]
      []
      return (n1, n2)

evaluate to ``[]``?  Show all the steps in your derivation.


2. The case of the useless ``return``
-------------------------------------

[**2 marks**]

Why does this expression:

.. code-block:: haskell

   do n1 <- [1..6]
      n2 <- [1..6]
      return <anything>
      return (n1, n2)

return the same thing as this expression:

.. code-block:: haskell

   do n1 <- [1..6]
      n2 <- [1..6]
      return (n1, n2)

? Answer this by reducing both expressions to the same expression. Show
all the steps in your derivations.


3. A trivial pattern-matcher
----------------------------

[**3 marks**]

You can use the list monad to perform simple pattern-matching tasks.
Consider this code:

.. code-block:: haskell

   let s = ["aaxybb", "aazwbb", "foobar", "aaccbb", "baz"] in
     do ['a', 'a', c1, c2, 'b', 'b'] <- s
        return [c1, c2]

This returns this result:

.. code-block:: haskell

   ["xy", "zw", "cc"]

Explain why by deriving the result, using the full ``case``-style
desugaring of ``do`` expressions we covered in lecture 11. Note that the
``fail`` method of the ``MonadFail`` type class has this definition in
the list monad:

.. code-block:: haskell

  fail _ = []

Explain what would happen if instead ``fail`` for the list monad was
this instead:

.. code-block:: haskell

   fail s = error s

.. note::

  Don’t forget that the ``String`` datatype in Haskell is just a list
  of ``Char``\s, which is why the ``String`` type can also be written
  as ``[Char]``.


4. An equivalence
-----------------

[**3 marks**]

In the lecture on the list monad we mentioned that in GHC the real
definition of ``>>=`` for lists is:

.. code-block:: haskell

    m >>= k = foldr ((++) . k) [] m

and we claimed that this is the same as the definition we used:

.. code-block:: haskell

    m >>= k = concat (map k m)

(except that the first version may run faster, which you don’t need to
concern yourself with here). Show that the two expressions
``foldr ((++) . k) [] m`` and ``concat (map k m)`` do indeed compute the
same thing.  Write your answer in a comment, as usual.  

.. tip::

   Show that given ``m = [x1, x2, ...]``
   both expressions evaluate to the same thing.
   Also show this for ``m = []``.
   Expand ``(++) . k`` into an explicit lambda expression.

|hline|

