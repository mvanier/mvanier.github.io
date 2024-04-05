Part A: Exercises
=================

In order to work with the ``IO`` monad (or with any monad), you need to
understand how to desugar monadic code using the ``do`` notation to code
using the monad operators (``>>=`` and ``>>``). As we discussed in
class, there are two ways to do this, and we will explore both of them
here.


1. Desugaring ``do``
--------------------

[**1 mark**]

Consider the following function, which is a manual implementation of the
``putStrLn`` function:

.. code-block:: haskell

   myPutStrLn :: String -> IO ()
   myPutStrLn "" = putChar '\n'
   myPutStrLn (c:cs) =
     do putChar c
        myPutStrLn cs

Desugar this into a function that does not use the ``do`` notation.
Either kind of desugaring will give the same answer.

.. note::

   A very common way to write this function would be as follows:

   .. code-block:: haskell

      myPutStrLn :: String -> IO ()
      myPutStrLn "" = putChar '\n'
      myPutStrLn (c:cs) = do
        putChar c
        myPutStrLn cs

   This is perfectly acceptable, even though the ``do`` is situated far
   to the right of the lines inside the ``do``. Haskell’s indentation
   rules only require that these lines be indented relative to the
   *line* in which ``do`` is located, not relative to ``do`` itself.
   Feel free to use this style when writing your own code, though you
   don’t have to. You can also write it in a non-indentation-dependent
   style as follows:

   .. code-block:: haskell

      myPutStrLn :: String -> IO ()
      myPutStrLn "" = putChar '\n'
      myPutStrLn (c:cs) = do {
        ; putChar c
        ; myPutStrLn cs
        }

   I rarely use this style myself, but some programmers prefer it.


2. A style mistake
------------------

[**1 mark**]

Ben Bitfiddle wrote this code as a way to get familiar with the ``IO``
monad:

.. code-block:: haskell

   greet :: String -> IO ()
   greet name = do putStrLn ("Hello, " ++ name ++ "!")

Although this code works fine, it exhibits poor style because some of it
is redundant. Can you simplify it a little without changing its
behavior?

.. hint::

   We don’t mean replacing the parentheses with a ``$`` operator,
   though you can do that too.


3. Two desugarings
------------------

[**4 marks**]

Here is a more complicated version of the ``greet`` function:

.. code-block:: haskell

   -- Ask the user for his/her name, then print a greeting.
   greet2 :: IO ()
   greet2 = do
     putStr "Enter your name: "
     name <- getLine
     putStr "Hello, "
     putStr name
     putStrLn "!"

Desugar this function in two ways, both described in lecture 12: the
simple desugaring, and the more complicated desugaring involving an
explicit ``case`` statement. In both cases, preserve the binding of the
name to the identifier ``name`` (in other words, don’t try to write it
in a point-free manner; that’s not what we’re after here). Does the
complex desugaring behave differently than the simple desugaring here?
Write your answer in a comment. If there is a pattern match failure for
the complex desugaring, use the error message
``"Pattern match failure in do expression"``, which is close to what
would actually be printed. Recall that we use the ``fail`` method from
the ``MonadFail`` type class to handle pattern match failures in ``do``
expressions for monads that support it (which includes ``IO``).

Call the function desugared in the first way ``greet2a``, and the
function desugared in the second way ``greet2b``.

.. note::

   *  For the desugarings (this problem and the next) don’t just write
      the desugarings in a comment; write new versions of the functions
      and rename them. This will allow you to test them to make sure
      they behave correctly.

   *  *Coding style:* Using indentation and formatting in a judicious
      manner will make the desugared code much easier to read. For
      instance, don’t write the entire definition on one line, even if
      you can.

   *  Some desugarings may give rise to warnings which you can ignore.
      (This is an exception to our usual rule that warnings are errors.)
      On the other hand, if your code generates warnings that are not
      the result of a correct desugaring you will lose marks.


4. Two desugarings, part 2
--------------------------

[**4 marks**]

Let’s say we want to change the ``greet`` function so that it will
capitalize the name given to it (in case the user entered his/her name
without bothering to capitalize it correctly). That might lead to a
function like this:

.. code-block:: haskell

   -- Need to import this to get the definition of toUpper:
   import Data.Char

   -- Ask the user for his/her name, then print a greeting.
   -- Capitalize the first letter of the name.
   greet3 :: IO ()
   greet3 = do
     putStr "Enter your name: "
     (n:ns) <- getLine
     let name = toUpper n : ns
     putStr "Hello, "
     putStr name
     putStrLn "!"

Again, desugar this in the two different ways (also making sure to
handle the ``let`` expression correctly in both cases). Does the more
complex desugaring have any effects here? Write your answer in a
comment. *Hint:* What kind of user input could be used to check which
desugaring was being used? If there is a pattern match failure for the
complex desugaring, again use the error message
``"Pattern match failure in do expression"``.

Call the function desugared in the first way ``greet3a``, and the
function desugared in the second way ``greet3b``.

