Part A: ``IORef``\s and state monads
=====================================

In this section, you will be writing functions in a stateful style
using either the ``IO`` monad or a state monad.
Here are the definitions of while loops for each monad,
which you should include in your assignment submission:

.. code-block:: haskell

   -- While loop in the IO monad.
   whileIO :: IO Bool -> IO () -> IO ()
   whileIO test block =
     do b <- test
        when b (do block
                   whileIO test block)

   -- While loop in a state monad.
   whileState :: (s -> Bool) -> State s () -> State s ()
   whileState test body =
     do s0 <- get
        when (test s0)
             (do body
                 whileState test body)

Note that somewhere in your code you will need to import the modules
``Control.Monad``, ``Control.Monad.State``, and ``Data.IORef`` for this
code to work.

.. note::

   As mentioned previously, if you want to test your code in ``ghci``,
   you have to type this line before loading in your code:

   .. code-block:: text

      ghci> :set -package mtl

   Otherwise, ``ghci`` won’t be able to find the ``Control.Monad.State``
   module.

You should use either ``whileIO`` or ``whileState`` in all of the
functions in this section. Do not use recursion!
(If you do, you will get no credit.)
Obviously, use ``whileIO`` for the functions using the ``IO``
monad and ``whileState`` for the functions using the ``State`` monad.


1. ``factIO``
-------------

[**2 marks**]

Write a function called ``factIO`` which computes factorials
and works in the ``IO`` monad.

The type signature of this function will be:

.. code-block:: haskell

   factIO :: Integer -> IO Integer

Use ``IORef``\s to store all the local variables of the function.
There should be two local variables,
one representing a counter and the other the running total.
The counter is initialized with the input to ``factIO``
and is decremented by 1 at each iteration until it hits zero.
The running total is initialized with 1
and is multiplied by the counter value at each iteration.
At the end, the running total will have the desired factorial.
This is returned (using the ``return`` method of the ``IO`` monad)
as the result.

Signal an error if the input is invalid (|ie| less than zero).
This applies to all of the functions in this section.


2. ``factState``
----------------

[**3 marks**]

Write a function called ``factState`` that computes factorials and uses
a state monad.

The type signature of the function will be:

.. code-block:: haskell

   factState :: Integer -> Integer

The state type will be ``(Integer, Integer)``;
the first ``Integer`` will represent the counter
and the second the running total.
This function is actually shorter (less than 10 lines)
and easier to write than the ``factIO`` function.

.. hint::

   You will need to define a state transformer "helper value"
   (with type ``State (Integer, Integer) Integer``)
   which will actually embody the computation.


3. ``fibIO``
------------

[**2 marks**]

Write a function called ``fibIO`` that computes fibonacci numbers
and uses the ``IO`` monad.
This fibonacci function should have
``fib(0) == 0`` and ``fib(1) == 1``
and so on.

The type signature of the function will be:

.. code-block:: haskell

   fibIO :: Integer -> IO Integer

Again, use ``IORef``\s to hold all the local variables for the function.
This time, there are three local variables;
one represents the count,
and two represent the last two fibonacci numbers computed.
Initialize the count with the input to the ``fibIO`` function
and decrement it by 1 at each iteration;
when it hits zero, the desired fibonacci number
is in the smaller of the other two variables.
The non-counter variables get increased on each iteration
according to the usual fibonacci rule.
Don’t forget to check the input for errors.


4. ``fibState``
---------------

[**3 marks**]

Write a function called ``fibState`` that computes fibonacci numbers and
uses a state monad. The fibonacci function is as described above.

The type signature of the function will be:

.. code-block:: haskell

   fibState :: Integer -> Integer

The state type will be ``(Integer, Integer, Integer)``;
the first two ``Integer``\s represent the last two fibonacci numbers counted
and the final ``Integer`` represents a counter
which starts equal to the input to ``fibState``
and is decremented by 1 at each iteration until it hits zero.
The desired fibonacci number
will then be in the smaller of the other two ``Integer`` values in the state.
Again, this function is quite short (less than 10 lines).


.. hint::

   Again, you will need to define a state transformer "helper value"
   (this time, with type ``State (Integer, Integer, Integer) Integer``)
   which will actually embody the computation.

