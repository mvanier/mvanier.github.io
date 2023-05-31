Part B: Deriving the reader monad
=================================

We have discussed state monads in class and derived them from first
principles. There is another kind of state-like monad called the
"reader" monad; this is used in stateful computations that only require
read-only access to the state. In this section you will derive the
reader monad from first principles. In fact, the datatypes we will use
are simpler than the ones actually found in the GHC libraries, because
they are less general. Nevertheless, the principles are the same.

.. note::

   The reader monad is actually a very practical monad which is used in
   many real-world applications. Whenever there is some read-only state
   (like, for instance, configuration settings) that has to be shared
   among many functions but which will never be modified after it is
   initially set, a reader monad is often a good choice.

   You might be wondering why we would use a reader monad instead of
   just defining global constants. Typically, the values passed in a
   reader monad are read once (|eg| from a file), put into the monad and
   then never modified. Since they may come from an external source
   (|eg| a file), they can’t be global constants.

The reader monad represents computations that can access read-only data.
Represented conceptually, functions in this monad would look like:

.. code-block:: text

   a -------[read from a stored value]------> b

By analogy to state monads, we can write these functions as:

.. code-block:: haskell

   (a, r) -> b

for some state type ``r``.

.. note::

   The state type ``r`` goes into the computation but doesn’t come out,
   so even if it was locally modified,
   nothing outside of this function would ever see the modifications.
   That’s what we mean by "read-only".

We can curry this to get:

.. code-block:: haskell

   a -> r -> b

which is effectively equivalent to the uncurried version. We can
parenthesize it as follows:

.. code-block:: haskell

   a -> (r -> b)

We therefore define the ``Reader`` datatype as follows:

.. code-block:: haskell

   data Reader r b = Reader (r -> b)

so we can write the characteristic monadic functions in the reader monad
as:

.. code-block:: haskell

   a -> Reader r b

and the only difference is that values of the type ``(r -> b)`` are now
values of the type ``(Reader r b)`` which is just a ``Reader`` wrapper
around the same function type.

This is equivalent to the monadic function form:

.. code-block:: haskell

   a -> m b

with ``(Reader r)`` as the ``Monad`` instance.

We will also define this helper function:

.. code-block:: haskell

   runReader :: Reader r a -> r -> a
   runReader (Reader f) = f

We will use this definition of the ``Monad`` instance for the ``Reader``
monad:

.. code-block:: haskell

   instance Monad (Reader r) where
     return x = Reader (\r -> x)

     mx >>= f = Reader (\r ->
                  let x = runReader mx r in
                    runReader (f x) r)

The ``>>=`` operator can also be written without ``runReader`` as:

.. code-block:: haskell

   mx >>= f = Reader (\r ->
                let (Reader g) = mx
                    x = g r
                    (Reader h) = f x
                in h r)


Deriving the ``>>=`` operator and the ``return`` function
---------------------------------------------------------

[**10 marks**]

Derive the definitions of ``return`` and ``>>=`` that we have given above
from first principles.
Recall that ``return`` must conceptually be
the identity function in the monad
and the ``>>=`` operator must be defined
so that monadic function composition
(the ``>=>`` operator from the ``Control.Monad`` module) works correctly.
Use the derivation of the ``Monad`` instance for state monads
as a model for your derivation.
(The reader monad is basically a simplified state monad.)

Note that you do *not* have to prove that the ``Monad`` instance for the
reader monad satisfies the monad laws.

.. note::

   Please write your answer in a multi-line Haskell comment
   (between ``{-`` and ``-}`` symbols).

.. hint::

   Start by deriving the ``>>=`` operator and then derive the ``return`` method.
   Recall that the ``>>=`` operator has to be defined
   to make monadic function composition work correctly,
   and the ``return`` operator is a monadic identity function.
   Again, the derivation will proceed much like the state monad derivation,
   though it will be simpler.

