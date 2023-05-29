Part C: Extending the S-expression parser
=========================================

In lecture 21 we created a very simple S-expression parser for a
Scheme-like language. In this section you will extend the S-expression
parser in several ways to get some practice working with parser
combinators.

While doing this, you should be aware that there is nothing about an
S-expression parser that necessarily connects it to the Scheme
programming language. Scheme does use S-expressions as the basis for its
syntax (though with lots of extensions, such as the dot notation for
improper lists), but you could use S-expressions in other contexts as
well, and people do.  [1]_


Template code and testing
-------------------------

The code used in the lecture is available in the file ``Sexpr.hs``.
You will be modifying various parts of this code in this section.
There is also a file of sample Scheme-like code called ``test.scm``.
Use this to test your parser.
You can test your parser interactively
by starting ``ghci`` and typing these lines:

.. code-block:: text

   ghci> :set -package parsec
   ghci> :load Sexpr.hs
   ghci> runPpSexpr "test.scm"

This will load your code and run the ``runPpSexpr`` function
on the ``test.scm`` file.
This function will parse all the code and output a "pretty-printed"
version of the code representation.
(You may not think it’s particularly pretty,
but it will display the data structures very explicitly.)
If the parser gives errors on any part of this code,
it isn’t working properly.
Do *not* change the pretty-printer code itself
unless specifically indicated below.
(You will have to modify other parts of the code, of course.)
Note that you can also create your own file of S-expressions
and test it this way.

.. note::
   
   If you create your own test file,
   please call the file something other than ``test.scm``
   or you may break the tests in the ``Makefile``. 

As described in the preamble,
you can also test the code from the terminal by typing:

.. code-block:: text

   $ make sexpr
   $ make sexpr_test

This tests that the output of running the parser on ``test.scm``
is what was expected.  It does this by comparing the output
to an expected output file.


1. Simplifying the ``Sexpr`` datatype
-------------------------------------

[**2 marks**]

The parser given to you in the original version of the file ``Sexpr.hs``
has these datatypes as the basic S-expression datatypes:

.. code-block:: haskell

   data Atom =
       BoolA  Bool
     | IntA   Integer
     | FloatA Double
     | IdA    String
     deriving (Show)

   data Sexpr =
       AtomS Atom
     | ListS [Sexpr]
     | QuoteS Sexpr
     deriving (Show)

The ``Atom`` type is fairly standard (though we’ll be extending it
below), but the ``Sexpr`` type itself is a bit odd. There is actually no
need for the ``QuoteS`` constructor for quoted expressions. If you know
Scheme, you’ll know that expression like these ones:

.. code-block:: text

   'a
   '(foo bar baz)

are actually syntactic sugar for these expressions:

.. code-block:: text

   (quote a)
   (quote (foo bar baz))

These S-expressions can be represented as ``Sexpr`` expressions without
the ``QuoteS`` constructor as follows:

.. code-block:: text

   ListS [AtomS (IdA "quote"), AtomS (IdA "a")]
   ListS [AtomS (IdA "quote"),
          ListS [AtomS (IdA "foo"),
                 AtomS (IdA "bar"),
                 AtomS (IdA "baz")]

Therefore, your first exercise is to remove all the code dealing with
the ``QuoteS`` constructor (including the code in the pretty-printer
|ie| the ``ppSexpr`` function).  Nevertheless, your parser must be
able to parse quoted expressions, converting them into S-expressions of
the form given above. The ``Sexpr`` datatype will become simply:

.. code-block:: haskell

   data Sexpr =
       AtomS Atom
     | ListS [Sexpr]
     deriving (Show)

This exercise is mostly to get you familiar with the code base. You
won’t be changing the way parsing is done, but you will have to modify
the function ``parseSexpr`` and change the way quoted expressions are
converted into ``Sexpr``\s.

.. note::

   You don't have to modify ``parseQuote``, and you shouldn't.


2. Generalized parentheses
--------------------------

[**2 marks**]

Many (if not most) Schemes allow you to use square brackets or curly
braces to delimit S-expressions instead of parentheses. This is
convenient in deeply-nested expressions; you can easily tell visually
where an expression starts and ends. A closing square bracket can only
match an opening square bracket, and not an opening parenthesis (and
similarly for curly braces). In this exercise, you will extend the
S-expression parser to allow it to parse S-expressions using square
brackets or curly braces. Some examples:

.. code-block:: text

   (this is an expression using parentheses)
   [this is an expresssion using square brackets]
   {this uses curly braces}
   (this [weird expression] {uses all [kinds (of matching delimiters)]})

.. note::

   The delimiters must match, so if a particular list uses (say)
   an open square bracket as the starting delimiter,
   it must use a close square bracket as the ending delimiter.
   (The same goes for parentheses and curly braces.)

Modify the ``parseList`` function so that it can parse S-expressions
with any of the three types of delimiters. The best way to do this is to
define a helper function which takes the two delimiter characters as
arguments and outputs a parser, and then use three such parsers in the
body of ``parseList``. Use the ``<|>`` (alternation) operator to combine
the three parsers into one big parser.

Note that the fact that a given S-expression uses parentheses or square
brackets or curly braces is *not* encoded into the datatype; the
datatype doesn’t care about which delimiters were used for a given
S-expression.


3. Why not ``try``?
-------------------

[**2 marks**]

Note that you don’t need to use the ``try`` combinator in the
``parseList`` function (in the event that you tried to parse one kind of
delimiter and failed). Why not? (Write the answer in a comment.)


4. Better floats
----------------

[**2 marks**]

The floating-point number parser in the template code is limited; it
can’t handle floating-point numbers with exponents. So these are valid
floating-point numbers according to the parser:

.. code-block:: text

   1.2
   -3.4
   42.12334

but these aren’t:

.. code-block:: text

   1.2e10
   -3.4e-10
   42.12334E+10

Modify the function ``parseFloat`` to make it parse floating-point
numbers with exponents. Exponents are optional, but if present they have
this syntax: exponent letter (``e`` or ``E``), optional sign (``+`` or
``-`` (or no sign)), and one or more digits (``0`` to ``9``).

There are a couple of things you should know about the floating-point
number parser in the template code:

* Floating-point numbers are required to have a decimal point, and at
  least one digit after the decimal point. Most computer languages will
  accept floating-point numbers without decimal points if |eg| an
  exponent is present, but requiring the decimal point simplifies the
  parsing. Requiring a digit after the decimal point is my personal
  preference; I think that floating-point numbers without digits after
  the decimal point look ugly |smile|. You don't need to change any of
  this; it’s just for your information.

* The parser works by creating a string version of the floating-point
  number and then using the Haskell ``read`` function to convert the
  string to a floating-point number. This approach will still work with
  exponents, but in this case the string version of the floating-point
  number may have an exponent (represented as a string concatenated to
  the end of the rest of the floating-point number’s string
  representation).


5. Adding strings
-----------------

[**2 marks**]

Scheme includes strings as a basic data type, but the ``Atom`` datatype
in the parser doesn’t support strings. Change it to this type:

.. code-block:: haskell

  data Atom =
      BoolA   Bool
    | IntA    Integer
    | FloatA  Double
    | IdA     String
    | StringA String
    deriving (Show)

As you can see, now strings are a special kind of atom which use the
``StringA`` constructor. Extend the atom parser ``parseAtom`` so that it
can parse strings as well. Do this by defining a parser called
``parseString`` which parses strings, and then call that inside of
``parseAtom``.

.. hint::

   Inside ``parseAtom``, handle strings after integers but before identifiers.

The string syntax we’ll use is extremely simple:
a string is a sequence of characters
between two double-quote (``"``) characters.
The double-quote character cannot occur in a string
(there is no backslash-escaping, for instance),
but you can put newline characters in a string directly
(you don’t have to write |eg| ``\n`` for a newline,
and in fact that will give you two characters, not one).
Here are some example strings the parser can recognize:

.. code-block:: text

   "I am a string"
   "This string
   spans
   multiple lines"
   "I can have 'single quotes' inside but not double quotes"

It isn’t too hard to improve the string parser to handle backslash
escapes, but it isn’t required.

----

.. rubric:: Footnotes

.. [1] Notably, there are popular OCaml libraries
   which use S-expressions as a serialization format.

