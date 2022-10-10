# Configuration file for the Sphinx documentation builder.
#
# For the full list of built-in configuration values, see the documentation:
# https://www.sphinx-doc.org/en/master/usage/configuration.html

# -- Project information -----------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#project-information

#############
# JR added:
import os
import sys

sys.path.insert(0, os.path.abspath('../../'))
extensions = [
   'sphinx.ext.autodoc',
   'sphinx.ext.githubpages',
   'sphinx_rtd_theme',
   'recommonmark'
]

html_theme = 'sphinx_rtd_theme'
##############

project = 'phenopacket-tools'
copyright = '2022, Peter Robinson'
author = 'Peter Robinson'

# -- General configuration ---------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#general-configuration

extensions = []

templates_path = ['_templates']
exclude_patterns = ['_build', 'Thumbs.db', '.DS_Store']
source_suffix = ['.rst', '.md']


# -- Options for HTML output -------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#options-for-html-output

# html_theme = 'alabaster'
# html_static_path = ['_static']
