"""
Script to copy a markdown file into a template file and output it
as an HTML file.
"""

import sys, os

root              = sys.argv[1]
homedir           = os.getenv('HOME')
coursedir         = homedir + '/_/src/MCV/cs115_code'
templatedir       = coursedir + '/lectures/reveal'
content_marker    = '@@CONTENT@@'
css_marker        = '@@CSS@@'
template_filename = templatedir + '/template.html'
md_filename       = root + '.md'
css_filename      = templatedir + '/css/mcv.css'
output_filename   = root + '.html'

with open(output_filename, 'w') as outfile:
    with open(template_filename, 'r') as templatefile:
        for line in templatefile:
            if line.startswith(content_marker):
                with open(md_filename, 'r') as infile:
                   for iline in infile:
                       print(iline, file=outfile, end='')
                continue
            elif line.startswith(css_marker):
                with open(css_filename, 'r') as infile:
                   for iline in infile:
                       print(iline, file=outfile, end='')
                continue
            else:
                print(line, file=outfile, end='')


