Diffator
========

Diffator is Java two-way diff library developed for purposes of service <http://www.nalezen.cz>. It has following attributes.

Universal
---------

Does diff operation on two arrays of strings. This strings may be words, lines of file, blocks of text, etc. You may simply create own output handlers reacting to events from library. Simple output formater can be created - see SimpleHtmlDiffWriter for reference.

Library also provides content similarity ratio in range from 0.0 to 1.0. This number means "how big portion of longer content is covered by shorter one". So 0.0 means no match, 1.0 means complete match.

Diff algorithm
--------------

Used algorithm is simple:

1.  find [longest common substring](<http://en.wikibooks.org/w/index.php?title=Algorithm_Implementation/Strings/Longest_common_substring&stable=1>) of contents
2.  this splits each content into 3 parts: 'before-match-after'
3.  call 1. for 'before' parts of contents and then for 'after' parts of contents recursively

This algoriths belongs to the category of simplest, but efficient. One of problems of it is scattered contents detection - in some cases this algorithm fails.

Lightweight
-----------

Because used in web crawler (<http://www.nalezen.cz/about-crawler>) it must be memory efficient. For this reason it was optimized to minimize memory allocation during diff operation. It reports diff events to callback handler in SAX-like manner so no output collection is created.

Fast
----

Again, this library is used in web crawler therefore speed is important. During development 3 strategies had been developed before we found suitable one for us. You may pick from (M is count of items in left content, N is count of items in right content):
*  M*N memory - All possible substrings of inputs are calculated before diff. This array is reused for longest common string of subparts during recursion.
*  2*M memory - Memory efficient but CPU intensive. Only longest substring is calculated. This must be repeated for every subpart during recursion.
*  Sparse memory - Great for similar contents - only non-zero cells in M*N matrix are stored. Memory very efficient if contents are really similar.

It is hard to decide what algorithm will be most suitable for you. It depends on size of your data, similarity of data, memory usage you are willing to accept, ... Maybe best way is to test all 3 algorithms in your environment.

Sample
============

Simple HTML output
------------------

    String[] wordsA = ...;
    String[] wordsB = ...;
    
    Content ca = new Content(wordsA);
    Content cb = new Content(wordsB);
    		
    FileOutputStream fstr = new FileOutputStream("test.html");
    		
    SimpleHtmlDiffWriter writer = new SimpleHtmlDiffWriter(fstr);
    writer.setTitle("My sample diff");
    writer.setNewLinesStrategy(NewLinesStrategy.perBlock);
    		
    ContentComparator.compareStatic(ca, cb, writer);
    
Which outputs something like:
![Sample output](/sample.png "Sample output")

Own handler
-----------

Just prepare object of class having cz.nalezen.diffator.DiffEventsHandler interface. Then pass it to :

`test()`
