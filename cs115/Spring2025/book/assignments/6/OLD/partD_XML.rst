Part D: Writing a simple XML parser
===================================

In this section,
you will write an extremely simplified XML parser using parser combinators.
In case you don't know, XML is a structured data format
which is a generalization of the HTML (HyperText Markup Language) format
commonly used to write web pages.
Unlike HTML, XML is extremely regular
and is used for much more than just formatting documents.
It is a general-purpose data format which is heavily used in the real world.
XML is not terribly complex,
but writing a full XML parser would take too long,
so the parser you will write here will be missing many XML features.
Some of these missing features include:

* XML comments, which consist of text
  between the delimiters ``<!--`` and ``-->``.

* Initial ``DOCTYPE`` declarations and similar declarations.

* Forms without specific end tags _e.g._ ``<hr />``.

* Forms with attributes _e.g._
  ``<a href="https://cms.caltech.edu/">Caltech CMS</a>``.

If none of this means anything to you, don't worry;
we'll explain exactly what syntax your parser will have to handle below.


Template code and testing
-------------------------

As in part C, we are supplying you with a file of template code
called ``XML.hs``.
You should modify it according to the instructions given below.
The template code defines the XML data structures and the pretty-printer,
so you can concentrate on writing the parser.
There is also a test file called ``test.xml``
which you should use to test the parser;
it contains all the forms that your parser should be able to parse.
You can test your parser interactively
by starting ``ghci`` and typing these lines:

.. code-block:: text

   ghci> :set -package parsec
   ghci> :load XML.hs
   ghci> runPpSexpr "test.xml"

As in part C, this will attempt to load the test file,
parse its contents and output a "pretty-printed" version of the XML.
Again, if the parser gives errors on any part of this code,
it isn't working properly.
Do *not* change the pretty-printer code at all for this section.

As described in the preamble,
you can also test the code from the terminal by typing:

.. code-block:: text

   $ make xml
   $ make xml_test

As in part C, this works by running the parser on the ``test.xml`` file
and comparing it to an expected output file.

The template code as given doesn't work;
you need to fill in the parts labeled ``{- TODO -}`` with real code,
and please remove the ``{- TODO -}`` comments as you fill in the code.
Note that there are lines containing the ``<?>`` operator
following the ``{- TODO -}`` comments;
these should be left in,
as they will affect the kind of error messages your code generates.
(The ``<?>`` operator is defined in the ``Parsec`` libraries.)

Also, there exist some Haskell modules that contain XML parsing code.
You are not allowed to use these for this assignment,
for what should be obvious reasons.
(If you do, you will get no credit for this section.)


Simplified XML syntax
---------------------

The XML parser you will write will handle only two kinds of forms:

* Tagged forms, which look like this: ``<TAGNAME>TEXT...</TAGNAME>``.
  The tag name (in the place marked ``TAGNAME``)
  consists of one or more characters,
  all of which are either letters
  (upper- or lowercase, from ``a`` (or ``A``) to ``z`` (or ``Z``))
  or digits from ``0`` to ``9`` (but no symbolic characters).
  Tags can contain a mixture of uppercase characters,
  lowercase characters, and digits.
  The ``TEXT`` part can consist of any number of characters
  (or no characters at all)
  except for the characters ``<``, ``>``, or ``&``.
  Notice that the end tag is distinct from the start tag
  because it contains the forward-slash character (``/``) before the tag name.

* Special character entities, which look like this: ``&NAME;``.
  Entities always start with the ``&`` character,
  followed by the entity name
  (which consists of lower-case letter characters only),
  followed by a semicolon.
  In fact, only a few character entities will be recognized by the parser;
  anything else that has the correct entity syntax
  but isn't one of the specified entities is an error.

.. warning::

   We require that start and end tags match *exactly*,
   which means that ``</foo>`` will only match ``<foo>``
   but not ``<FOO>`` or ``<Foo>``.

Here are some examples of tagged forms:

.. code-block:: text

   <p>This is a test.</p>

   <a><b><c>Forms</c> can be <b>nested</b> as long as their <emphasis>start tags match
   their end tags!</emphasis></b></a>

Here are the character entities that the parser will recognize:

.. code-block:: text

   &lt;  (means the < (less-than) character)
   &gt;  (means the > (greater-than) character)
   &amp; (means the & (ampersand) character)

Note that the limitation on text
(that it can't contain the literal characters ``<``, ``>``, and ``&``)
is not a real limitation
because we could just use the character entities
``&lt;``, ``&gt;``, and ``&amp;`` instead.


Datatypes
---------

Here are the datatypes you will use when writing your XML parser:

.. code-block:: haskell

   type Tag = String

   data Entity = LT_E | GT_E | AMP_E
      deriving (Show)

   data Elem =
       TextE String     -- raw text
     | EntE Entity      -- entity
     | FormE Tag [Elem] -- tagged data
     deriving (Show)

We use the type name ``Tag`` as an abbreviation for ``String``
in cases where we intend the string to be used as a tag.
``Entity`` refers to the character entities, which are either
``&lt;`` (less-than sign),
``&gt;`` (greater-than sign),
or ``&amp;`` (ampersand symbol),
as described above.
They are represented by the constructors
``LT_E``, ``GT_E``, and ``AMP_E`` respectively.
The ``Elem`` type is the type of all XML elements;
these are either a text string, an entity or a tagged form.
The contents of a tagged form are a list of XML elements,
which is a bit like the lists in S-expressions
(this is what allows tags to be nested).


Code to write
-------------

[**10 marks**]

Modify the template file ``XML.hs``.
Remove the ``{- TODO -}`` comments and replace them with your own code.
You should test the sub-parsers individually using ``parseTest``
as described above and make sure they each work before continuing.
When you are done, run ``test`` at the ``ghci`` prompt to test the parser.
You will need to have downloaded the ``test.xml`` file
into the current directory before you do this.


Hints and suggestions
---------------------

* There is not a lot of code to write for this section (about 30 lines),
  so if you find yourself writing a large amount of code,
  you are doing something wrong
  (or at least not doing it in the most efficient way).

* Hoogle_ is your friend!
  Before you write a parser,
  check to see if there is already
  a parser defined in the Parsec libraries that does what you need
  (or most of what you need).
  Some of the sub-parsers you will write
  can be defined in one line in terms of parsers in the Parsec libraries.
  Check out the documentation for the following modules:

  .. code-block:: text

     Text.Parsec
     Text.Parsec.Combinator
     Text.Parsec.Char
     Text.Parsec.Prim

* Somewhere in the code you are likely to run into a situation
  where you want a parser to backtrack when it fails.
  In that case, you need to use the ``try`` combinator.
  We only needed to use ``try`` once in all of our code,
  but it was critical in that location, so watch out for it.

